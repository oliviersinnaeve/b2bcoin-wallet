package com.b2beyond.wallet.http;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class InvalidCertificateHostVerifier implements HostnameVerifier {

    @Override
    public boolean verify(String paramString, SSLSession paramSSLSession) {
        return true;
    }

}
