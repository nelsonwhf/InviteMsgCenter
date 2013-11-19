package com.duowan.gamebox.msgcenter.serialize.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.duowan.gamebox.msgcenter.manager.vo.Invitation;
import com.duowan.gamebox.msgcenter.manager.vo.InvitationDetail;
import com.duowan.gamebox.msgcenter.serialize.util.InvitationAvro;
import com.duowan.gamebox.msgcenter.serialize.util.InvitationDetailAvro;

public class AvroSerializer {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	public byte[] invitationSerialize(Invitation invitation,String serverUrl)
	{
		InvitationAvro invitationavro=invitation2avro(invitation, serverUrl);
		byte[] bytes=invitationByteArray(invitationavro);
		return bytes;
	}
	
	public byte[] invitationDetailSerialize(InvitationDetail invitationdetail,String serverUrl) {
		InvitationDetailAvro invitationdetailavro=invitationdetail2avro(invitationdetail, serverUrl);
		byte[] bytes=invitationdetailByteArray(invitationdetailavro);
		return bytes;
	}
	
	private byte[] invitationByteArray(InvitationAvro invitationavro){
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		DatumWriter<InvitationAvro> writer=new SpecificDatumWriter<InvitationAvro>(InvitationAvro.class);  
        Encoder encoder= EncoderFactory.get().binaryEncoder(out,null);  
		try {
			writer.write(invitationavro, encoder);
			encoder.flush();
		} catch (IOException e) {
			log.error("Error in Invitation ByteArray:"+e.getMessage());
			e.printStackTrace();
		}
	    byte[] bytearray= out.toByteArray();
		try {
			out.close();
		} catch (IOException e) {
			log.error("Error in Invitation ByteArray Stream close:"+e.getMessage());
			e.printStackTrace();
		}
		return bytearray;
	}
	
	private byte[] invitationdetailByteArray(InvitationDetailAvro invitationdetailavro){
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		DatumWriter<InvitationDetailAvro> writer=new SpecificDatumWriter<InvitationDetailAvro>(InvitationDetailAvro.class);  
        Encoder encoder= EncoderFactory.get().binaryEncoder(out,null);  
		try {
			writer.write(invitationdetailavro, encoder);
			encoder.flush();
		} catch (IOException e) {
			log.error("Error in InvitationDetail ByteArray:"+e.getMessage());
			e.printStackTrace();
		}
	    byte[] bytearray= out.toByteArray();
		try {
			out.close();
		} catch (IOException e) {
			log.error("Error in InvitationDetail ByteArray Stream close:"+e.getMessage());
			e.printStackTrace();
		}
		return bytearray;
	}
	
	private InvitationAvro invitation2avro(Invitation invitation,String serverUrl){
		InvitationAvro invitationavro=new InvitationAvro();
		invitationavro.setServerUrl(serverUrl);
		try {
			BeanUtils.copyProperties(invitationavro, invitation);
		} catch (IllegalAccessException e) {
			log.error("Invitation to InvitationAvro Fail:"+invitation);
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			log.error("Invitation to InvitationAvro Fail:"+invitation);
			e.printStackTrace();
		}
		Set<InvitationDetail> details=invitation.getDetails();
		setListValue(details, invitationavro);
		return invitationavro;
	}
	
	private void setListValue(Set<InvitationDetail> details,InvitationAvro invitationavro)
	{
		java.util.List<java.lang.CharSequence> toUserIds = new java.util.ArrayList<java.lang.CharSequence>();
		java.util.List<java.lang.CharSequence> toUserTypes=new java.util.ArrayList<java.lang.CharSequence>();
		java.util.List<java.lang.CharSequence> toDisplayNames=new java.util.ArrayList<java.lang.CharSequence>();
		for(InvitationDetail detail:details)
		{
			toUserIds.add(detail.getToUserId());
			toUserTypes.add(detail.getToUserType());
			toDisplayNames.add(detail.getToDisplayName());
		}
		invitationavro.setToUserIds(toUserIds);
		invitationavro.setToUserTypes(toUserTypes);
		invitationavro.setToDisplayNames(toDisplayNames);
	}
	
	public InvitationDetailAvro invitationdetail2avro(InvitationDetail invitationdetail,String serverUrl){
		InvitationDetailAvro invitationdetailavro=new InvitationDetailAvro();
		invitationdetailavro.setServerUrl(serverUrl);
		try {
			BeanUtils.copyProperties(invitationdetailavro,invitationdetail);
		} catch (IllegalAccessException e) {
			log.error("Invitation to InvitationAvro Fail:"+invitationdetail);
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			log.error("Invitation to InvitationAvro Fail:"+invitationdetail);
			e.printStackTrace();
		}
		return invitationdetailavro;
	}
}
