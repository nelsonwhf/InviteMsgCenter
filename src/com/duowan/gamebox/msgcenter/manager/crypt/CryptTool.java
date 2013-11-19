/**
 * 
 */
package com.duowan.gamebox.msgcenter.manager.crypt;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.duowan.gamebox.msgcenter.util.HexUtils;

/**
 * 通过DES加密解密.
 * 
 * 
 */
public class CryptTool {

	public static final String DEFAULT_ALGORITHM = "AES";
	public static final String DEFAULT_PADDING = "NoPadding";
	public static final String DEFAULT_MODE = "ECB";

	private final String algorithm;
	private final String mode;
	private final String padding;
	private final String transformation;

	private final boolean isNoPadding;
	private final int blockSize;

	public CryptTool() {
		this(DEFAULT_ALGORITHM, DEFAULT_MODE, DEFAULT_PADDING);
	}

	public CryptTool(String algorithm, String mode, String padding) {
		this.algorithm = algorithm;
		this.mode = mode;
		this.padding = padding;
		this.transformation = new StringBuilder().append(this.algorithm).append('/')
				.append(this.mode).append('/').append(this.padding).toString();
		this.isNoPadding = DEFAULT_PADDING.equals(this.padding);
		this.blockSize = this.algorithm.indexOf("AES") != -1 ? 16 : 8;
		// System.out.println(isNoPadding);
		// System.out.println(blockSize);
	}

	public static void main(String[] args) throws GeneralSecurityException {

		CryptTool endec = new CryptTool();

		// 1.1 >>> 首先要创建一个密匙

		// 3DES
		// byte rawKeyData[] = HexUtils
		// .convertBytes("6de992549102317cc19b1cf4ef6702194334b36776761ad0");

		// AES
		byte rawKeyData[] = HexUtils
				.convertBytes("c9228b7c617693b5eecd67750eb14aa3");

		// byte rawKeyData[] = "1234567812345678".getBytes();
		// byte rawKeyData[] = endec.createKey();
		System.out.println("密匙===>" + new String(rawKeyData));
		System.out.println("密匙 hex===>" + HexUtils.convertHexString(rawKeyData));
		System.out.println("密匙 size===>" + rawKeyData.length);

		String str = "efgh"; // 待加密数据
		// 2.1 >>> 调用加密方法
		byte[] encryptedData = endec.encrypt(rawKeyData, str.getBytes());
		System.out.println("加密 hex===>" + HexUtils.convertHexString(encryptedData));
		// 3.1 >>> 调用解密方法
		// encryptedData =
		// HexUtils.convertBytes("c9cb348142996031df178110dcd2d70a");
		byte[] data = endec.decrypt(rawKeyData, encryptedData);
		System.out.println("解密===>" + new String(data));
	}

	public byte[] createKey() throws NoSuchAlgorithmException {
		SecureRandom sr = new SecureRandom();
		KeyGenerator kg = KeyGenerator.getInstance(algorithm);
		kg.init(sr);
		SecretKey key = kg.generateKey();
		byte[] rawKeyData = key.getEncoded();
		return rawKeyData;
	}

	/**
	 * 加密方法
	 * 
	 * @param rawKeyData
	 * @param str
	 * @return
	 * @throws GeneralSecurityException
	 */
	public byte[] encrypt(byte[] rawKeyData, byte[] data)
			throws GeneralSecurityException {
		SecretKey key = new SecretKeySpec(rawKeyData, algorithm);
		Cipher cipher = Cipher.getInstance(transformation);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encryptedData = null;
		if (isNoPadding && data.length % blockSize != 0) {
			// add 0x00
			byte[] dataAppend = new byte[data.length + (blockSize - data.length % blockSize)];
			System.arraycopy(data, 0, dataAppend, 0, data.length);
			encryptedData = cipher.doFinal(dataAppend);
		} else {
			encryptedData = cipher.doFinal(data);
		}
		return encryptedData;
	}

	/**
	 * 解密方法
	 * 
	 * @param rawKeyData
	 * @param encryptedData
	 * @return
	 * @throws GeneralSecurityException
	 */
	public byte[] decrypt(byte[] rawKeyData, byte[] encryptedData) throws GeneralSecurityException {
		SecretKey key = new SecretKeySpec(rawKeyData, algorithm);
		Cipher cipher = Cipher.getInstance(transformation);
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] decryptedData = cipher.doFinal(encryptedData);
		if (isNoPadding) {
			// remove 0x00
			int i = decryptedData.length - 1;
			int j = i;
			for (; i >= 0; i--) {
				if (decryptedData[i] != 0x00) {
					break;
				}
			}
			if (i != j) {
				byte[] data = new byte[i + 1];
				System.arraycopy(decryptedData, 0, data, 0, data.length);
				return data;
			}
		}
		return decryptedData;
	}
}
