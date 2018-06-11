package com.kitchee.app.helpeo.network;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * Created by kitchee on 2018/6/11.
 * desc : 信任管理器
 */

public class MyX509TrustManager implements X509TrustManager {
    // 检查客户端证书
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }
    // 检查服务端证书
    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }
    // 返回受信任的X509证书数组
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
