package net.joycool.wap.util;

import java.security.MessageDigest;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


/**
 * @author zhouj
 * @explain： 支付
 * @datetime:1007-10-24
 */
public class PayUtil {
//	 md5之后再des加密，用于校验
	public static String desEncryptMd5(String src, String key2) throws Exception {
		javax.crypto.SecretKey key = genDESKey(key2.getBytes());
		javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DES");
		cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key);
		return byteArr2HexStr(cipher.doFinal(getMD5Raw(src)));
	}

	
	public static String desEncrypt(String src, String key2) throws Exception {
		javax.crypto.SecretKey key = genDESKey(key2.getBytes());
		javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DES");
		cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key);
		return byteArr2HexStr(cipher.doFinal(src.getBytes()));
	}

	public static String desDecrypt(String crypt, String key2) throws Exception {
		javax.crypto.SecretKey key = genDESKey(key2.getBytes());
		javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DES");
		cipher.init(javax.crypto.Cipher.DECRYPT_MODE, key);
		return new String(cipher.doFinal(hexStr2ByteArr(crypt)));
	}
	
	public static javax.crypto.SecretKey genDESKey(byte[] key_byte) throws Exception {
		DESKeySpec dks = new DESKeySpec(key_byte);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		return keyFactory.generateSecret(dks);
	}
	
	
	private static boolean isInited = false;
	private static MessageDigest digest = null;
	
	public static synchronized String getMD5(String input) throws Exception {
		// please note that we dont use digest, because if we
		// cannot get digest, then the second time we have to call it
		// again, which will fail again
		if (isInited == false) {
			isInited = true;
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (digest == null)
			return input;

		// now everything is ok, go ahead
		try {
			digest.update(input.getBytes("UTF-8"));
		} catch (java.io.UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		byte[] rawData = digest.digest();
		return byteArr2HexStr(rawData);
	}
	public static synchronized byte[] getMD5Raw(String input) throws Exception {
		// please note that we dont use digest, because if we
		// cannot get digest, then the second time we have to call it
		// again, which will fail again
		if (isInited == false) {
			isInited = true;
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (digest == null)
			return null;

		// now everything is ok, go ahead
		try {
			digest.update(input.getBytes("UTF-8"));
		} catch (java.io.UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		byte[] rawData = digest.digest();
		return rawData;
	}
	
	public static synchronized String getMD5_Base64(String input) {
		// please note that we dont use digest, because if we
		// cannot get digest, then the second time we have to call it
		// again, which will fail again
		if (isInited == false) {
			isInited = true;
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (digest == null)
			return input;

		// now everything is ok, go ahead
		try {
			digest.update(input.getBytes("UTF-8"));
		} catch (java.io.UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		byte[] rawData = digest.digest();
		byte[] encoded = Base64.encode(rawData);
		String retValue = new String(encoded);
		return retValue;
	}
	
	/**
	 * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
	 * hexStr2ByteArr(String strIn) 互为可逆的转换过程
	 * 
	 * @param arrB
	 *            需要转换的byte数组
	 * @return 转换后的字符串
	 * @throws Exception
	 *             本方法不处理任何异常，所有异常全部抛出
	 */
	public static String byteArr2HexStr(byte[] arrB) throws Exception {
		int iLen = arrB.length;
		// 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];
			// 把负数转换为正数
			while (intTmp < 0) {
				intTmp = intTmp + 256;
			}
			// 小于0F的数需要在前面补0
			if (intTmp < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}
		return sb.toString();
	}

	/**
	 * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)
	 * 互为可逆的转换过程
	 * 
	 * @param strIn
	 *            需要转换的字符串
	 * @return 转换后的byte数组
	 * @throws Exception
	 *             本方法不处理任何异常，所有异常全部抛出
	 * @author LiGuoQing
	 */
	public static byte[] hexStr2ByteArr(String strIn) throws Exception {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;

		// 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}
	
}
