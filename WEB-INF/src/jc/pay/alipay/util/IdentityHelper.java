package jc.pay.alipay.util;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 封装各种生成唯一性ID算法的工具类.
 * 
 * @version 1.0.0
 */
public class IdentityHelper {

	private static SecureRandom random  = new SecureRandom();

	/**
	 * 生成指定区间[min-max)之间的随机数
	 * 
	 * @param min
	 *            最小值，包含
	 * @param max
	 *            最大值，不包含
	 * @return 最小值和最大值之间的随机数
	 */
	public static int randomInt(int min, int max) {
		return random.nextInt(max) % (max - min + 1) + min;
	}

	/**
	 * 使用SecureRandom随机生成长整型.
	 */
	public static long randomLong() {
		long r = random.nextLong();
		return r == Integer.MIN_VALUE ? Integer.MAX_VALUE : r;
	}

	/**
	 * 生成数字大小写字母组合随机数
	 * 
	 * @param length
	 * @return
	 */
	public static String randomString(int length) {
		String strBase = "0123456789ABCDEFGHIGKLMNOPQRSTUVWXYZabcdefghigklmnopkrstuvwxyz";
		return randomString(length, strBase);
	}

	/**
	 * 使用SecureRandom生成指定长度和范围的随机字符串
	 * 
	 * @param length
	 *            随机字符串长度
	 * @param base
	 *            随机字符串范围
	 * @return 指定长度和范围的随机字符串
	 */
	public static String randomString(int length, String base) {
		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sbf.append(base.charAt(number));
		}
		return sbf.toString();
	}

	/**
	 * 生成订单号
	 * 
	 * @return
	 */
	public static String SerializeNo() {
		String strBase = "0123456789";
		return "HXQ" + System.currentTimeMillis() + randomString(4, strBase);
	}
	
	public static String SerializeNoaa() {
		String strBase = "0123456789abcdefghqoiuoqpomnbxvc";
		return randomString(16, strBase);
	}

	/**
	 * 生成流水号
	 */
	public static String generateSerialNo() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+(random.nextInt(10000)+10000);
	}
	
	/**
	 * 生成length位的随机数字
	 * @return
	 */
	public static String SerializeNo(int length) {
		String strBase = "0123456789";
		return  randomString(length, strBase);
	}


	/**
	 * 获得下一个流水号
	 * 
	 * @param current
	 *            当前流水号
	 * @param beginIndex
	 *            流水号变化部分的起始位置，默认从0开始
	 * @param endIndex
	 *            流水号变化部分的截止位置(不包括)，默认最后
	 * @param hex
	 *            是否十六进制：true是，false否
	 * @return 下一个流水号
	 */
	public static String nextSerialNo(String current, Integer beginIndex, Integer endIndex, Boolean hex) {
		if (current == null)
			return null;
		int length = current.length();
		if (beginIndex == null || beginIndex.intValue() < 1 || beginIndex.intValue() >= length)
			beginIndex = 0;
		if (endIndex == null || endIndex.intValue() > length)
			endIndex = length;
		if (endIndex.intValue() <= beginIndex.intValue())
			endIndex = beginIndex.intValue() + 1;

		String before = current.substring(beginIndex, endIndex);
		int ii = (hex != null && hex.booleanValue() == true ? Integer.parseInt(before, 16) : Integer.parseInt(before))
				+ 1;
		String after = hex != null && hex.booleanValue() == true ? Integer.toHexString(ii).toUpperCase()
				: String.valueOf(ii);

		StringBuffer sbf = new StringBuffer(50);
		if ((endIndex.intValue() - beginIndex.intValue()) < after.length()) {
			sbf.append("SerialNo ").append(after).append(" out of length : ")
					.append(endIndex.intValue() - beginIndex.intValue());
			throw new RuntimeException(sbf.toString());
		}

		sbf.append(current.substring(0, beginIndex));
		length = endIndex - beginIndex - after.length();
		for (int i = 0; i < length; i++) {
			sbf.append("0");
		}
		return sbf.append(after).append(current.substring(endIndex)).toString();
	}
}
