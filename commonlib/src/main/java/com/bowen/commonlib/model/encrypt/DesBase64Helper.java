package com.bowen.commonlib.model.encrypt;


import com.bowen.commonlib.utils.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Description:Des+Base64混合加密工具类
 * @author huangjx
 * @see DesBase64Helper
 * @since 1.0
 * @date 2016年4月6日
 */
public class DesBase64Helper {

    private static final String TAG = "DesBase64Helper";
    /**
     * 默认密钥
     */
    private final static String secretKey = "bwkj_app";
    private final static String secretKey1 = "_bowen_bwkj_app_2017_888";
    /**
     * 默认向量
     */
    private final static String iv = "01234567";
    /**
     * 设置私钥字节数组向量
     */
    private final static byte[] mIv = {1, 2, 3, 4, 5, 6, 7, 8};

    /**
     * 加解密统一使用的编码方式
     */
    private final static String encoding = "utf-8";

    static class SingletonHolder {
        static DesBase64Helper instance = new DesBase64Helper();
    }

    public static DesBase64Helper getInstance() {
        return SingletonHolder.instance;
    }

    private DesBase64Helper(){
    }

    /**
     * 3DES加密（使用默认密钥和默认向量）
     * @param plainText 普通文本
     * @return
     * @throws Exception
     */
    public static String encode(String plainText) {
        try {
            return encode(plainText, secretKey, iv);
        } catch (Exception e) {
            LogUtil.e(TAG, "des base64 encode exception:" + e.toString());
        }
        return plainText;
    }

    /**
     * 3DES加密（指定密钥和指定向量）
     *
     * @param plainText
     * @param sKey
     * @param iv
     * @return
     * @throws Exception
     */
    public static String encode(String plainText, String sKey, String iv) {
        try {
            DESedeKeySpec spec = new DESedeKeySpec(sKey.getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
            Key deskey = keyfactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
            byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
            return Base64.encode(encryptData);
        } catch (Exception e) {
            LogUtil.e(TAG, "des base64 encode exception:" + e.toString());
        }
        return plainText;
    }

    /**
     * 3DES解密（使用默认密钥和默认向量）
     * <p>
     * 然后在解密的时候首先把这首尾的字符串截取掉后再进行解密
     *
     * @param encryptText 加密文本
     * @return
     * @throws Exception
     */
    public String decode(String encryptText) throws Exception {
        try {
            return decode(encryptText, secretKey, iv);
        } catch (Exception e) {
            LogUtil.e(TAG, "des base64 encode exception:" + e.toString());
        }
        return encryptText;
    }

    /**
     * 3DES解密（指定密钥和指定向量）
     *
     * @param encryptText 加密文本
     * @param sKey
     * @param iv
     * @return
     * @throws Exception
     */
    public String decode(String encryptText, String sKey, String iv) {
        try {
            DESedeKeySpec spec = new DESedeKeySpec(sKey.getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
            Key deskey = keyfactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
            byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));
            return new String(decryptData, encoding);
        } catch (Exception e) {
            LogUtil.e(TAG, "des base64 encode exception:" + e.toString());
        }
        return encryptText;
    }

    /**
     * 获取密码
     *
     * @param key
     * @param mode
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     */
    public Cipher getCipher(Key key, int mode) {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(mode, key);
            return cipher;
        } catch (Exception e) {
            LogUtil.e(TAG, "des base64 encode exception:" + e.toString());
        }
        return null;
    }

    /**
     * 小型文件解密
     *
     * @param is
     * @param os
     * @param key
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    public void decrypt(InputStream is, OutputStream os, Key key) {
        try {
            byte[] buff = new byte[1024];
            int count = 0;
            CipherInputStream cis = new CipherInputStream(is, getCipher(key, Cipher.DECRYPT_MODE));
            while ((count = cis.read(buff)) >= 0) {
                os.write(buff, 0, count);
            }
            cis.close();
            os.flush();
        } catch (Exception e) {
            LogUtil.e(TAG, "des base64 encode exception:" + e.toString());
        }

    }

    /**
     * 小型文件加密
     *
     * @param is
     * @param os
     * @param key
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    public void encrypt(InputStream is, OutputStream os, Key key) {
        try {
            byte[] buff = new byte[1024];
            int count = 0;
            CipherOutputStream cos = new CipherOutputStream(os, getCipher(key, Cipher.ENCRYPT_MODE));
            while ((count = is.read(buff)) >= 0) {
                cos.write(buff, 0, count);
            }
            cos.flush();
            cos.close();
        } catch (Exception e) {
            LogUtil.e(TAG, "des base64 encode exception:" + e.toString());
        }
    }

    /**
     * 3DES加密（指定密钥）
     *
     * @param encryptString 加密文本
     * @param encryptKey
     * @return
     * @throws Exception
     */
    public String encryptDES(String encryptString, String encryptKey) {
        try {
            IvParameterSpec zeroIv = new IvParameterSpec(mIv);
            SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
            byte[] encryptedData = cipher.doFinal(encryptString.getBytes(encoding));
            return Base64.encode(encryptedData);
        } catch (Exception e) {
            LogUtil.e(TAG, "des base64 encode exception:" + e.toString());
        }
      return encryptString;
    }

    /**
     * 3DES加密（使用默认密钥）
     *
     * @param encryptString
     * @return
     */
    public String encryptDES(String encryptString) {
        return encryptDES(encryptString, secretKey);
    }

    /**
     * 3DES解密（指定密钥）
     *
     * @param decryptString 加密文本
     * @param decryptKey
     * @return
     * @throws Exception
     */
    public String decryptDES(String decryptString, String decryptKey) {
        try {
            byte[] byteMi = Base64.decode(decryptString);
            IvParameterSpec zeroIv = new IvParameterSpec(mIv);
            SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
            byte decryptedData[] = cipher.doFinal(byteMi);
            return new String(decryptedData, encoding);
        } catch (Exception e) {
            LogUtil.e(TAG, "des base64 encode exception:" + e.toString());
        }
        return decryptString;
    }

    /**
     * 3DES解密（使用默认密钥）
     *
     * @param decryptString
     * @return
     */
    public String decryptDES(String decryptString) {
        return decryptDES(decryptString, secretKey);
    }

//    public static void main(String[] args) {
//        try {
//
//            System.err.println(DesBase64Helper.getInstance().encode("今晚打老虎", secretKey1, iv));
//
//            System.err.println(DesBase64Helper.getInstance().decode("JbralyVwmg0eVHU36JdMjw==", secretKey1, iv));
//
//            System.err.println(DesBase64Helper.getInstance().encryptDES("今晚打老虎", secretKey));
//
//            System.err.println(DesBase64Helper.getInstance().decryptDES("1+VFvf5yLGvqCLuMb2IP0g===", secretKey));
//            System.err.println(DesBase64Helper.getInstance().decryptDES("3+YQovDmilw=", secretKey));
//            System.err.println(DesBase64Helper.getInstance().decryptDES("hvDGuZvPg9Kso9cgH7G/QQ==", secretKey));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}

