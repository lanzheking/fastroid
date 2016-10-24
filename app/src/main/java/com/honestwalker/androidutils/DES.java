package com.honestwalker.androidutils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import com.honestwalker.androidutils.exception.ExceptionUtil;

/**
 * 使用前必须先设置appkey
 * @author Administrator
 *
 */
public class DES {
	
	private static String key = AndroidUtilsContext.getDesKey();
	
	public static String getKey() {
		return key;
	}
	
	/**
	 * 设置appkey 如果大于8为自动修正为8位
	 * @param key
	 * @return
	 */
	public static String setKey(String key) {
		if(key.length() > 8) {
			key = key.substring(0,8);
		}
		DES.key = key;
		return key;
	}
	
    /**
     * 字符串加密
     * @param message 要加密的字符串，尽可能使用UTF-8
     * @return	加密后的字符串
     * @throws Exception
     */
    public static String encrypt(String message)
            throws Exception {   
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");   
  
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));   
  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");   
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);   
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));   
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);   
  
        String result = toHexString(cipher.doFinal(message.getBytes("UTF-8")));
        return result; 
    }  
    
    /**
	 * 解密
	 * @param message 要揭秘的字符串 
	 * @return 解密后的字符串
	 * @throws Exception
	 */
    public static String decrypt(String message) {   
            try{
	            byte[] bytesrc =convertHexString(message);      
	            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");       
	            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));      
	            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");      
	            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);      
	            IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));   
	                   
	            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);         
	             
	            byte[] retByte = cipher.doFinal(bytesrc);        
	            return new String(retByte);    
            } catch (Exception e) {
            	ExceptionUtil.showException(e);
            	return "";
			}
    }   
       
    private static byte[] convertHexString(String ss)    
    {    
	    byte digest[] = new byte[ss.length() / 2];    
	    for(int i = 0; i < digest.length; i++)    
	    {    
	    String byteString = ss.substring(2 * i, 2 * i + 2);    
	    int byteValue = Integer.parseInt(byteString, 16);    
	    digest[i] = (byte)byteValue;    
	    }    
	  
	    return digest;    
    }    
  
  
    private static String toHexString(byte b[]) {   
        StringBuffer hexString = new StringBuffer();   
        for (int i = 0; i < b.length; i++) {   
            String plainText = Integer.toHexString(0xff & b[i]);   
            if (plainText.length() < 2)   
                plainText = "0" + plainText;   
            hexString.append(plainText);   
        }   
           
        return hexString.toString();   
    }   
}
