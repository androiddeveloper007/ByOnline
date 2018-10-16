package com.bowen.commonlib.http.ssl;

import com.bowen.commonlib.CommonLibApplication;
import com.bowen.commonlib.R;
import com.bowen.commonlib.utils.LogUtil;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by AwenZeng on 2017/4/11.
 */

public class SslContextFactory {
    private static final String CLIENT_TRUST_PASSWORD = "bowen861idb";//证书密码
    private static final String CLIENT_AGREEMENT = "TLS";//使用协议
    private static final String CLIENT_TRUST_MANAGER = "X509";
    private static final String CLIENT_TRUST_KEYSTORE = "BKS";
    private SSLContext sslContext = null;

    public SSLSocketFactory getSslSocketFactory() {
        try {
            try {
                //取得SSL的SSLContext实例
                sslContext = SSLContext.getInstance(CLIENT_AGREEMENT);
               //取得TrustManagerFactory的X509密钥管理器实例
                TrustManagerFactory trustManager = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                InputStream is = CommonLibApplication.getCommonLibContext().getResources().openRawResource(R.raw.bowen_https_boyizaixian);
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null);
                keyStore.setCertificateEntry("0", certificateFactory.generateCertificate(is));
                trustManager.init(keyStore);
                if (is != null)
                    is.close();
                sslContext.init(null, trustManager.getTrustManagers(), new SecureRandom());
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("SslContextFactory", e.getMessage());
        }
        return sslContext.getSocketFactory();
    }

}
