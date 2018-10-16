package com.bowen.commonlib.model.encrypt;

import android.text.TextUtils;

import com.bowen.commonlib.CommonLibApplication;
import com.bowen.commonlib.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * RSA+BASE64混合加密
 * Created by AwenZeng on 2017/3/31.
 */

public class RSAUtil {

    private static RSAUtil instance = null;

    public static RSAUtil getInstance(){
        if(instance == null){
            synchronized (RSAUtil.class){
                if(instance == null){
                    instance = new RSAUtil();
                }
            }
        }
        return instance;
    }

    private  PublicKey getPublicKey(){
        PublicKey publicKey = null;
        try {
            InputStream in = CommonLibApplication.getCommonLibContext().getResources().openRawResource(R.raw.rsa_public_key);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                } else {
                    sb.append(readLine);
                }
            }
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] buffer=Base64.decode(sb.toString());
            EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (Exception e) {
        }
        return publicKey;
    }

    public  String encode(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
            byte[] enBytes = cipher.doFinal(str.getBytes());
            return Base64.encode(enBytes);
        } catch (Exception e) {
        }
        return str;
    }

    public  String encode(String str,String key) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] buffer=Base64.decode(key);
            EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] enBytes = cipher.doFinal(str.getBytes());
            return Base64.encode(enBytes);
        } catch (Exception e) {
        }
        return str;
    }

}
