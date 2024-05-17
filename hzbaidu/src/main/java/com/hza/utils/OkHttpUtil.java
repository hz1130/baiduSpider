package com.hza.utils;

import okhttp3.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OkHttpUtil {
    static com.hza.utils.OkHttpUtil okHttpUtil;
    private Request.Builder requestBuilder;
    private OkHttpClient okHttpClient;
    private OkHttpClient okHttpClientSmid;

    private static class TrustAllManager implements X509TrustManager {

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
        }

    }

    public static void main(String[] args) {

    }

    private OkHttpUtil() throws Exception {
        X509TrustManager trustManager = new TrustAllManager();
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{trustManager}, null);
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();


        okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .sslSocketFactory(sslSocketFactory, trustManager)
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
        okHttpClientSmid = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .sslSocketFactory(sslSocketFactory, trustManager)
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
        requestBuilder = new Request.Builder();
    }

    public static com.hza.utils.OkHttpUtil getInstance() {
        if (null == okHttpUtil) {
            synchronized (com.hza.utils.OkHttpUtil.class) {
                if (null == okHttpUtil) {
                    try {
                        okHttpUtil = new com.hza.utils.OkHttpUtil();
                    } catch (Exception ignore) {
                        ignore.printStackTrace();
                    }
                }
            }
        }
        return okHttpUtil;
    }

    public void doGetSmid(String url) {
        try {
            Request req = requestBuilder.url(url)
                    .get()
                    .build();
            Response response = okHttpClientSmid.newCall(req).execute();
            if (response.code() == 200) {
                Objects.requireNonNull(response.body()).string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] doGet(String url, boolean isProxy) {
        try {
            OkHttpClient client = null;
            if (isProxy) {
                client = null;
            } else {
                client = okHttpClient;
            }
            Request req = requestBuilder.url(url)
                    .get()
                    .build();
            Response response = client.newCall(req).execute();
            if (response.code() == 200) {
                byte[] bytes = Objects.requireNonNull(response.body()).bytes();
                response.close();
                return bytes;
            }
            response.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String doGet(String url) {
        try {
            Request req = requestBuilder.url(url)
                    .get()
                    .build();
            Response response = okHttpClient.newCall(req).execute();
            if (response.code() == 200) {
                return Objects.requireNonNull(response.body()).string();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    MediaType JSON = MediaType.parse("application/json;charset=utf-8");

    public String doPost(String url, String body) {
        try {
            RequestBody requestBody = RequestBody.create(JSON, String.valueOf(body));
            Request req = requestBuilder.url(url).post(requestBody).build();

            Response response = okHttpClient.newCall(req).execute();
            if (response.code() == 200) {
                return Objects.requireNonNull(response.body()).string();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String doPost(String url, Map<String, String> body) {
        try {
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : body.keySet()) {
                builder.add(key, body.get(key));
            }
            Request req = requestBuilder.url(url).post(builder.build()).build();
            Response response = okHttpClient.newCall(req).execute();
            if (response.code() == 200) {
                return Objects.requireNonNull(response.body()).string();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
