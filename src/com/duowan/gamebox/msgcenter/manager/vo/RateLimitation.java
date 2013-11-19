/**
 * 
 */
package com.duowan.gamebox.msgcenter.manager.vo;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangtao.robin
 * 
 */
public class RateLimitation {

	private static final Pattern PATTERN_RATE_LIMITATION = Pattern.compile("(\\d+)/(\\d*)([smhd])");

	private int rate;

	private int durationSeconds;

	/**
	 * @return the rate
	 */
	public int getRate() {
		return rate;
	}

	/**
	 * @param rate
	 *            the rate to set
	 */
	public void setRate(int rate) {
		this.rate = rate;
	}

	/**
	 * @return the durationSeconds
	 */
	public int getDurationSeconds() {
		return durationSeconds;
	}

	/**
	 * @param durationSeconds
	 *            the durationSeconds to set
	 */
	public void setDurationSeconds(int durationSeconds) {
		this.durationSeconds = durationSeconds;
	}

	public void setDurationUnit(int duration, TimeUnit unit) {
		this.durationSeconds = (int) TimeUnit.SECONDS.convert(duration, unit);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RateLimitation [rate=").append(rate).append(", durationSeconds=")
				.append(durationSeconds).append("]");
		return builder.toString();
	}

	public static RateLimitation parseRateLimitation(String rateStr) {
		if (rateStr == null) {
			return null;
		}
		// format: <rate-number>/<duration-number><s|m|h|d>
		Matcher matcher = PATTERN_RATE_LIMITATION.matcher(rateStr);
		if (matcher.matches()) {
			RateLimitation limitation = new RateLimitation();
			limitation.setRate(Integer.parseInt(matcher.group(1)));
			int duration = 1;
			if (matcher.group(2).length() != 0) {
				duration = Integer.parseInt(matcher.group(2));
			}
			TimeUnit unit = null;
			switch (matcher.group(3).charAt(0)) {
			case 's':
				unit = TimeUnit.SECONDS;
				break;
			case 'm':
				unit = TimeUnit.MINUTES;
				break;
			case 'h':
				unit = TimeUnit.HOURS;
				break;
			case 'd':
				unit = TimeUnit.DAYS;
				break;
			default:
				throw new RuntimeException("Unknown time unit: " + matcher.group(3));
			}
			limitation.setDurationUnit(duration, unit);
			return limitation;
		}
		return null;
	}

	public static void main(String[] args) {
		RateLimitation limitation = parseRateLimitation("10/3h");
		System.out.println(limitation);
		limitation = parseRateLimitation("8/d");
		System.out.println(limitation);
		limitation = parseRateLimitation("123/m");
		System.out.println(limitation);
		System.out.println(parseRateLimitation(null));
		System.out.println(parseRateLimitation("dad"));
	}
}
