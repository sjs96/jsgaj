package fixed.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES对称加密
 */
public class AESUtils {

	public static final String SECURITY = "klsoi12ijsmdeytp";

	private AESUtils() {
	}

	/**
	 * 加密
	 * 
	 * @param secret
	 *            密钥
	 * @param value
	 *            待加密的字符串
	 * @return 加密后的字符串
	 */
	public static String encrypt(String secret, String value) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(secret.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			byte[] byteContent = value.getBytes("UTF-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(byteContent);
			return parseByte2HexStr(result); // 加密
		} catch (NoSuchAlgorithmException e) {
			Util.SOP(e);
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			Util.SOP(e);
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			Util.SOP(e);
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			Util.SOP(e);
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			Util.SOP(e);
			e.printStackTrace();
		} catch (BadPaddingException e) {
			Util.SOP(e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param secret
	 *            密钥
	 * @param value
	 *            待解密字符串
	 * @return 解密后的字符串
	 */
	public static String decrypt(String secret, String value) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(secret.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(parseHexStr2Byte(value));
			return new String(result, "UTF-8"); // 加密
		} catch (NoSuchAlgorithmException e) {
			Util.SOP(e);
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			Util.SOP(e);
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			Util.SOP(e);
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			Util.SOP(e);
			e.printStackTrace();
		} catch (BadPaddingException e) {
			Util.SOP(e);
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			Util.SOP(e);
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**将二进制转换成16进制 
	 * @param buf 
	 * @return 
	 */  
	public static String parseByte2HexStr(byte buf[]) {  
	        StringBuffer sb = new StringBuffer();  
	        for (int i = 0; i < buf.length; i++) {  
	                String hex = Integer.toHexString(buf[i] & 0xFF);  
	                if (hex.length() == 1) {  
	                        hex = '0' + hex;  
	                }  
	                sb.append(hex.toUpperCase());  
	        }  
	        return sb.toString();  
	}
	/**将16进制转换为二进制 
	 * @param hexStr 
	 * @return 
	 */  
	public static byte[] parseHexStr2Byte(String hexStr) {  
	        if (hexStr.length() < 1)  
	                return null;  
	        byte[] result = new byte[hexStr.length()/2];  
	        for (int i = 0;i< hexStr.length()/2; i++) {  
	                int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
	                int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
	                result[i] = (byte) (high * 16 + low);  
	        }  
	        return result;  
	}
}