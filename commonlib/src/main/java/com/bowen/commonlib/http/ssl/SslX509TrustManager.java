package com.bowen.commonlib.http.ssl;

import com.bowen.commonlib.CommonLibApplication;
import com.bowen.commonlib.R;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * Created by AwenZeng on 2017/7/3.
 */

public class SslX509TrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream is = CommonLibApplication.getCommonLibContext().getResources().openRawResource(R.raw.bowen_https_boyizaixian);
            final Certificate ca;
            try {
                ca = cf.generateCertificate(is);
            } finally {
                is.close();
            }
            for (X509Certificate cert : chain) {
                // Make sure that it hasn't expired.
                cert.checkValidity();
                // Verify the certificate's public key chain.
                try {
                    cert.verify(((X509Certificate) ca).getPublicKey());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                } catch (SignatureException e) {
                    e.printStackTrace();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream is = CommonLibApplication.getCommonLibContext().getResources().openRawResource(R.raw.bowen_https_boyizaixian);
            final Certificate ca;
            try {
                ca = cf.generateCertificate(is);
            } finally {
                is.close();
            }
            for (X509Certificate cert : chain) {
                // Make sure that it hasn't expired.
                cert.checkValidity();
                // Verify the certificate's public key chain.
                try {
                    cert.verify(((X509Certificate) ca).getPublicKey());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                } catch (SignatureException e) {
                    e.printStackTrace();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
