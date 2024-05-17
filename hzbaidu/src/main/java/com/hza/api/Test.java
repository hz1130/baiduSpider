package com.hza.api;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Random;
import java.util.Scanner;

public class Test {

    private static final String strs = "abcdefghijklmnopqrstuvwxyz0123456789";

    public static String randomStrings(int len) {
        Random random1 = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(strs.charAt(random1.nextInt(strs.length())));
        }
        return sb.toString();
    }

    private static class TrustAllManagerBxl implements X509TrustManager {

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
        }

    }

    public static void main(String[] args) throws Exception {


    }

}
