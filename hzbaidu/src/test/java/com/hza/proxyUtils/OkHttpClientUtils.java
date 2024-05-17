package com.hza.proxyUtils;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class OkHttpClientUtils {
    private static int readTimeout = 6000; //读取超时时间毫秒
    private static int writeTimeout = 6000; //写数据超时时间毫秒
    private static int connectTimeout = 6000; //连接超时时间

    private static volatile OkHttpClient client = null;
    private static SSLSocketFactory sslSocketFactory = null;

    private static int maxIdleConnection = 10;
    private static long keepAliveDuration = 10;
    private final static Gson gson = new Gson();

    private final static String ip = "tps267.kdlapi.com";
    private final static String port = "15818";
    private final static String username = "t15096838352379";
    private final static String password = "0nm781nk";

    private OkHttpClientUtils() {
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


    static {
        X509TrustManager trustManager = new TrustAllManagerBxl();
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }


        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                //代理服务器的IP和端口号
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, Integer.valueOf(port))))
//        代理的鉴权账号密码
                .proxyAuthenticator(new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        //设置代理服务器账号密码
                        String credential = Credentials.basic(username, password);
                        return response.request().newBuilder()
                                .header("Proxy-Authorization", credential)
                                .build();
                    }
                })
                .sslSocketFactory(sslSocketFactory, trustManager)
                .build();
    }

    /**
     * @description: 获取单例的okhttpclient对象，并配置通用信息
     * @date: 2018/3/19 9:54
     * @return: okhttp3.OkHttpClient
     */
    public static OkHttpClient getInstance() {

        if (client == null) {
            synchronized (OkHttpClientUtils.class) {
                if (client == null)
                    client = new OkHttpClient().newBuilder()
                            .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                            .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                            .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                            .connectionPool(new ConnectionPool(maxIdleConnection, keepAliveDuration, TimeUnit.MINUTES))
                            .build();
            }
        }
        return client;
    }


    /**
     * @description: 获取删除的通用Call
     * @date: 2018/3/19 9:55
     * @param: url 访问URL
     * @param: headerMap header键值对
     * @return: okhttp3.Call
     */
    private static Call baseDeleteCall(String url, Map<String, String> headerMap) {
        Request.Builder requestBuilder = requestBuilderAddHeader(headerMap, url);
        requestBuilder.delete();
        Request request = requestBuilder.build();
        Call call = getInstance().newCall(request);
        return call;
    }

    /**
     * @description: 获取通用的GET请求Call
     * @date: 2018/3/19 9:56
     * @param: url 访问URL
     * @param: headerMap header键值对
     * @return: okhttp3.Call
     */
    private static Call baseGetCall(String url, Map<String, String> headerMap) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = builder.build();
        Call call = getInstance().newCall(request);
        return call;
    }


    /**
     * @description: 获取POST发送请求参数的call
     * @date: 2018/3/19 9:57
     * @param: url 访问URL
     * @param: headerMap header键值对
     * @param: mapParams 请求参数键值对
     * @return: okhttp3.Call
     */
    private static Call basePostCall1(String url, Map<String, String> headerMap, Map<String, String> mapParams) {
        FormBody.Builder builder = new FormBody.Builder();
        if (mapParams != null) {
            for (String key : mapParams.keySet()) {
                builder.add(key, mapParams.get(key));
            }
        }
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        requestBuilder.post(builder.build());
        Request request = requestBuilder.build();
        Call call = getInstance().newCall(request);
        return call;
    }

    /**
     * @description: 获取post请求发送json串的call
     * @date: 2018/3/19 9:58
     * @param: url 请求URL
     * @param: headerMap header键值对
     * @param: jsonParams json请求串
     * @return: okhttp3.Call
     */
    private static Call basePostCall2(String url, Map<String, String> headerMap, String jsonParams) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8")
                , jsonParams);
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        requestBuilder.post(body);
        Request request = requestBuilder.build();
        Call call = getInstance().newCall(request);
        return call;
    }

    /**
     * @description: 获取file上传请求的call
     * @date: 2018/3/19 10:00
     * @param: url 请求URL
     * @param: headerMap header请求键值对
     * @param: fileMap 多个文件的Map,key为String类型(文件路径)或者byte[]类型(文件字节数组)，value为文件名称
     * @param: params 长传文件时附带请求参数键值对
     * @return: okhttp3.Call
     */
    private static Call baseFileCall(String url, Map<String, String> headerMap, Map<? extends Object, String> fileMap,
                                     Map<String, String> params) {
        //创建文件参数
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        if (fileMap != null) {
            for (Map.Entry<? extends Object, String> entry : fileMap.entrySet()) {
                //判断文件类型
                if (entry.getKey() instanceof String) {
                    //文件类型
                    MediaType MEDIA_TYPE = MediaType.parse(judgeType((String) entry.getKey()));
                    builder.addFormDataPart((String) entry.getKey(), entry.getValue(),
                            RequestBody.create(MEDIA_TYPE, new File((String) entry.getKey())));
                } else if (entry.getKey() instanceof byte[]) {
                    //文件字节流
                    MediaType MEDIA_TYPE = MediaType.parse("application/octet-stream");
                    builder.addFormDataPart(UUID.randomUUID().toString(), entry.getValue(),
                            RequestBody.create(MEDIA_TYPE, (byte[]) entry.getKey()));
                } else {
                    throw new IllegalArgumentException("the key of fileMap must be String or byte[]!");
                }
            }
        }
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        //发出请求参数
        Request.Builder builder1 = new Request.Builder();
        builder1.url(url);
        builder1.post(builder.build());
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                builder1.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = builder1.build();
        Call call = getInstance().newCall(request);
        return call;
    }

    /**
     * @description: 获取PUT发送请求参数的call
     * @date: 2018/3/19 9:57
     * @param: url 访问URL
     * @param: headerMap header键值对
     * @param: mapParams 请求参数键值对
     * @return: okhttp3.Call
     */
    private static Call basePutCall1(String url, Map<String, String> headerMap, Map<String, String> mapParams) {
        FormBody.Builder builder = new FormBody.Builder();
        if (mapParams != null) {
            for (String key : mapParams.keySet()) {
                builder.add(key, mapParams.get(key));
            }
        }
        Request.Builder requestBuilder = requestBuilderAddHeader(headerMap, url);
        requestBuilder.put(builder.build());
        Request request = requestBuilder.build();
        Call call = getInstance().newCall(request);
        return call;
    }

    /**
     * @description: 获取put请求发送json串的call
     * @date: 2018/3/19 9:58
     * @param: url 请求URL
     * @param: headerMap header键值对
     * @param: jsonParams json请求串
     * @return: okhttp3.Call
     */
    private static Call basePutCall2(String url, Map<String, String> headerMap, String jsonParams) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8")
                , jsonParams);
        Request.Builder requestBuilder = requestBuilderAddHeader(headerMap, url);
        requestBuilder.put(body);
        Request request = requestBuilder.build();
        Call call = getInstance().newCall(request);
        return call;
    }

    /**
     * @description: 转化JSON为对象
     * @date: 2018/3/19 10:05
     * @param: result JSON字符串
     * @param: fromJsonHandler JSON字符串处理接口
     * @return: java.lang.Object
     */
    public static Object fromJson(String result, FromJsonHandler fromJsonHandler) {
        return fromJsonHandler.fromJson(result);
    }

    /**
     * @description: 转化JSON为对象
     * @date: 2018/3/19 10:06
     * @param: result JSON字符串
     * @param: tClass 返回对象类型
     * @return: T 返回对象T
     */
    public static <T> T fromJson(String result, Class<T> tClass) {
        return gson.fromJson(result, tClass);
    }

    /**
     * @description: 转化JSON为对象
     * @date: 2018/3/19 10:07
     * @param: result 返回对象类型
     * @param: type Typetoken定义的typerefrence
     * @return: T 返回对象T
     */
    public static <T> T fromJson(String result, TypeToken<T> type) {
        return gson.fromJson(result, type.getType());
    }


    /**
     * @Author: zhuquanwen
     * @description: 同步GET请求
     * @date: 2018/3/19 10:17
     * @param: url 访问URL
     * @param: headerMap header键值对
     * @exception: IOException IO异常
     * @return: java.lang.String
     */
    public static String doGet(String url, Map<String, String> headerMap) throws IOException {
        Call call = baseGetCall(url, headerMap);
        return call.execute().body().string();
    }

    /**
     * @author: zhuquanwen
     * @description: 同步GET请求
     * @date: 2018/3/19 10:40
     * @param: url
     * @exception: IOException IO异常
     * @return: java.lang.String
     */
    public static String doGet(String url) throws IOException {
        return doGet(url, (Map<String, String>) null);
    }


    /**
     * @author: zhuquanwen
     * @description: 异步GET请求
     * @date: 2018/3/19 10:41
     * @param: url 请求URL
     * @param: headerMap header键值对
     * @param: callback okhttp异步回调
     * @exception: IOException IO异常
     * @return: void
     */
    public static void doGetAsyn(String url, Map<String, String> headerMap, Callback callback) throws IOException {
        Call call = baseGetCall(url, headerMap);
        call.enqueue(callback);
        ;
    }

    /**
     * @author: zhuquanwen
     * @description: 异步GET请求
     * @date: 2018/3/19 10:41
     * @param: url 请求URL
     * @param: callback okhttp异步回调
     * @exception: IOException IO异常
     * @return: void
     */
    public static void doGetAsyn(String url, Callback callback) throws IOException {
        doGetAsyn(url, (Map<String, String>) null, callback);
    }

    /**
     * @param url       请求URL
     * @param headerMap header键值对
     * @param mapParams 请求参数键值对
     * @author: zhuquanwen
     * @description: 同步POST请求
     * @date: 2018/3/19 10:45
     * @exception: IOException IO异常
     * @return: java.lang.String
     */
    public static String doPost(String url, Map<String, String> headerMap, Map<String, String> mapParams) throws IOException {
        Call call = basePostCall1(url, headerMap, mapParams);
        return call.execute().body().string();
    }

    /**
     * @param url       请求URL
     * @param mapParams 请求参数键值对
     * @author: zhuquanwen
     * @description: 同步POST请求
     * @date: 2018/3/19 10:45
     * @exception: IOException IO异常
     * @return: java.lang.String
     */
    public static String doPost(String url, Map<String, String> mapParams) throws IOException {
        return doPost(url, (Map<String, String>) null, mapParams);
    }

    /**
     * @param url        请求URL
     * @param headerMap  header键值对
     * @param jsonParams 请求JSON串
     * @author: zhuquanwen
     * @description: 同步POST请求
     * @date: 2018/3/19 10:45
     * @exception: IOException IO异常
     * @return: java.lang.String
     */
    public static String doPost(String url, Map<String, String> headerMap, String jsonParams) throws IOException {
        Call call = basePostCall2(url, headerMap, jsonParams);
        return call.execute().body().string();
    }

    /**
     * @param url        请求URL
     * @param jsonParams 请求JSON串
     * @author: zhuquanwen
     * @description: 同步POST请求
     * @date: 2018/3/19 10:45
     * @exception: IOException IO异常
     * @return: java.lang.String
     */
    public static String doPost(String url, String jsonParams) throws IOException {
        return doPost(url, (Map<String, String>) null, jsonParams);
    }

    /**
     * @param url       请求URL
     * @param headerMap header键值对
     * @param mapParams 请求键值对
     * @param callback  okhttp异步回调
     * @author: zhuquanwen
     * @description: 异步POST请求
     * @date: 2018/3/19 10:47
     * @exception: IOException IO异常
     * @return: void
     */
    public static void doPostAsyn(String url, Map<String, String> headerMap, Map<String, String> mapParams,
                                  Callback callback) throws IOException {
        Call call = basePostCall1(url, headerMap, mapParams);
        call.enqueue(callback);
    }

    /**
     * @param url       请求URL
     * @param mapParams 请求键值对
     * @param callback  okhttp异步回调
     * @author: zhuquanwen
     * @description: 异步POST请求
     * @date: 2018/3/19 10:47
     * @exception: IOException IO异常
     * @return: void
     */
    public static void doPostAsyn(String url, Map<String, String> mapParams, Callback callback) throws IOException {
        doPostAsyn(url, (Map<String, String>) null, mapParams, callback);
    }

    /**
     * @param url        请求URL
     * @param headerMap  header键值对
     * @param jsonParams 请求JSON串
     * @param callback   okhttp异步回调
     * @author: zhuquanwen
     * @description: 异步POST请求
     * @date: 2018/3/19 10:47
     * @exception: IOException IO异常
     * @return: void
     */
    public static void doPostAsyn(String url, Map<String, String> headerMap, String jsonParams, Callback callback) throws IOException {
        Call call = basePostCall2(url, headerMap, jsonParams);
        call.enqueue(callback);
    }

    /**
     * @param url        请求URL
     * @param jsonParams 请求JSON串
     * @param callback   okhttp异步回调
     * @author: zhuquanwen
     * @description: 异步POST请求
     * @date: 2018/3/19 10:47
     * @exception: IOException IO异常
     * @return: void
     */
    public static void doPostAsyn(String url, String jsonParams, Callback callback) throws IOException {
        doPostAsyn(url, (Map<String, String>) null, jsonParams, callback);
    }

    /**
     * @param url       URL请求
     * @param headerMap header请求键值对
     * @param fileMap   多个文件的Map,key为String类型(文件路径)或者byte[]类型(文件字节数组)，value为文件名称
     * @param params    文件上传附带参数键值对
     * @author: zhuquanwen
     * @description: 同步文件上传
     * @date: 2018/3/19 10:49
     * @exception: IOException IO异常
     * @exception: IllegalArgumentException 参数异常
     * @return: java.lang.String
     */
    public static String doFile(String url, Map<String, String> headerMap, Map<? extends Object, String> fileMap, Map<String, String> params)
            throws IOException, IllegalArgumentException {
        Call call = baseFileCall(url, headerMap, fileMap, params);
        return call.execute().body().string();

    }

    /**
     * @param url     URL请求
     * @param fileMap 多个文件的Map,key为String类型(文件路径)或者byte[]类型(文件字节数组)，value为文件名称
     * @param params  文件上传附带参数键值对
     * @author: zhuquanwen
     * @description: 同步文件上传
     * @date: 2018/3/19 10:49
     * @exception: IOException IO异常
     * @exception: IllegalArgumentException 参数异常
     * @return: java.lang.String
     */
    public static String doFile(String url, Map<? extends Object, String> fileMap, Map<String, String> params) throws IOException {
        return doFile(url, (Map<String, String>) null, fileMap, params);
    }

    /**
     * @param url       URL请求
     * @param headerMap header请求键值对
     * @param fileMap   多个文件的Map,key为String类型(文件路径)或者byte[]类型(文件字节数组)，value为文件名称
     * @param params    文件上传附带参数键值对
     * @param callback  okhttp异步回调
     * @author: zhuquanwen
     * @description: 异步文件上传
     * @date: 2018/3/19 10:49
     * @exception: IOException IO异常
     * @exception: IllegalArgumentException 参数异常
     * @return: void
     */
    public static void doFileAsyn(String url, Map<String, String> headerMap, Map<? extends Object, String> fileMap,
                                  Map<String, String> params, Callback callback) throws IOException, IllegalArgumentException {
        Call call = baseFileCall(url, headerMap, fileMap, params);
        call.enqueue(callback);
    }

    /**
     * @param url      URL请求
     * @param fileMap  多个文件的Map,key为String类型(文件路径)或者byte[]类型(文件字节数组)，value为文件名称
     * @param params   文件上传附带参数键值对
     * @param callback okhttp异步回调
     * @author: zhuquanwen
     * @description: 异步文件上传
     * @date: 2018/3/19 10:49
     * @exception: IOException IO异常
     * @exception: IllegalArgumentException 参数异常
     * @return: void
     */
    public static void doFileAsyn(String url, Map<? extends Object, String> fileMap, Map<String, String> params
            , Callback callback) throws IOException {
        doFileAsyn(url, (Map<String, String>) null, fileMap, params, callback);
    }

    /**
     * @param url      URL请求
     * @param fileDir  下载文件夹路径
     * @param fileName 下载文件名称
     * @author: zhuquanwen
     * @description: 同步文件下载
     * @date: 2018/3/19 10:53
     * @exception: InterruptedException 线程打断异常
     * @return: boolean
     */
    public static boolean downFile(String url, final String fileDir, final String fileName) throws InterruptedException {
        return downFile(url, null, fileDir, fileName);
    }

    /**
     * @param url       URL请求
     * @param headerMap header键值对
     * @param fileDir   下载文件夹路径
     * @param fileName  下载文件名称
     * @author: zhuquanwen
     * @description: 同步文件下载
     * @date: 2018/3/19 10:53
     * @exception: InterruptedException 线程打断异常
     * @return: boolean
     */
    public static boolean downFile(String url, Map<String, String> headerMap,
                                   final String fileDir, final String fileName) throws InterruptedException {
        Boolean[] result = new Boolean[1];
        result[0] = null;
        Request.Builder requestBuilder = requestBuilderAddHeader(headerMap, url);
        Request request = requestBuilder.build();
        Call call = getInstance().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result[0] = false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] buf = new byte[2048];
                int len = 0;
                File file = new File(fileDir, fileName);
                try (
                        InputStream is = response.body().byteStream();
                        FileOutputStream fos = new FileOutputStream(file);
                ) {
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    result[0] = true;
                }
            }
        });
        while (true) {
            if (result[0] != null) {
                break;
            } else {
                TimeUnit.MILLISECONDS.sleep(50);
            }
        }
        return result[0];
    }

    /**
     * @author: zhuquanwen
     * @description: 同步文件下载
     * @date: 2018/3/19 10:55
     * @param: url 请求URL
     * @exception: InterruptedException 线程打断异常
     * @return: java.io.InputStream
     */
    public static InputStream downFile(String url) throws InterruptedException {
        return downFile(url, null);
    }

    /**
     * @param url       请求URL
     * @param headerMap header 键值对
     * @author: zhuquanwen
     * @description: 同步文件下载
     * @date: 2018/3/19 10:55
     * @exception: InterruptedException 线程打断异常
     * @return: java.io.InputStream
     */
    public static InputStream downFile(String url, Map<String, String> headerMap) throws InterruptedException {
        Boolean[] flag = new Boolean[1];
        flag[0] = null;
        InputStream[] iss = new InputStream[1];
        iss[0] = null;
        Request.Builder requestBuilder = requestBuilderAddHeader(headerMap, url);
        Request request = requestBuilder.build();
        Call call = getInstance().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                flag[0] = false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                is = response.body().byteStream();
                iss[0] = is;
                flag[0] = true;
            }
        });
        while (true) {
            if (flag[0] != null) {
                break;
            } else {
                TimeUnit.MILLISECONDS.sleep(50);
            }
        }
        return iss[0];
    }


    /**
     * @param url       请求URL
     * @param headerMap header 键值对
     * @param callback  okhttp异步回调
     * @author: zhuquanwen
     * @description: 异步文件下载
     * @date: 2018/3/19 10:56
     * @return: void
     */
    public static void downFileAsyn(String url, Map<String, String> headerMap, Callback callback) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = getInstance().newCall(request);
        call.enqueue(callback);
    }

    /**
     * @param url      请求URL
     * @param callback okhttp异步回调
     * @author: zhuquanwen
     * @description: 异步文件下载
     * @date: 2018/3/19 10:56
     * @return: void
     */
    public static void downFileAsyn(String url, Callback callback) {
        downFileAsyn(url, null, callback);
    }

    /**
     * @param url       请求URL
     * @param headerMap header键值对
     * @param mapParams 请求参数键值对
     * @author: zhuquanwen
     * @description: 同步PUT请求
     * @date: 2018/3/19 10:57
     * @exception: IOException IO异常
     * @return: java.lang.String
     */
    public static String doPut(String url, Map<String, String> headerMap, Map<String, String> mapParams) throws IOException {
        Call call = basePutCall1(url, headerMap, mapParams);
        return call.execute().body().string();
    }

    /**
     * @param url       请求URL
     * @param mapParams 请求参数键值对
     * @author: zhuquanwen
     * @description: 同步PUT请求
     * @date: 2018/3/19 10:57
     * @exception: IOException IO异常
     * @return: java.lang.String
     */
    public static String doPut(String url, Map<String, String> mapParams) throws IOException {
        return doPut(url, (Map<String, String>) null, mapParams);
    }

    /**
     * @param url        请求URL
     * @param headerMap  header键值对
     * @param jsonParams 请求json字符串
     * @author: zhuquanwen
     * @description: 同步PUT请求
     * @date: 2018/3/19 10:57
     * @exception: IOException IO异常
     * @return: java.lang.String
     */
    public static String doPut(String url, Map<String, String> headerMap, String jsonParams) throws IOException {
        Call call = basePutCall2(url, headerMap, jsonParams);
        return call.execute().body().string();
    }

    /**
     * @param url        请求URL
     * @param jsonParams 请求json字符串
     * @author: zhuquanwen
     * @description: 同步PUT请求
     * @date: 2018/3/19 10:57
     * @exception: IOException IO异常
     * @return: java.lang.String
     */
    public static String doPut(String url, String jsonParams) throws IOException {
        return doPut(url, (Map<String, String>) null, jsonParams);
    }

    /**
     * @param url       请求URL
     * @param headerMap header键值对
     * @param mapParams 请求参数键值对
     * @param callback  okhttp异步请求回调
     * @author: zhuquanwen
     * @description: 异步put请求
     * @date: 2018/3/19 10:59
     * @exception: IOException IO异常
     * @return: void
     */
    public static void doPutAsyn(String url, Map<String, String> headerMap, Map<String, String> mapParams, Callback callback) throws IOException {
        Call call = basePutCall1(url, headerMap, mapParams);
        call.enqueue(callback);
    }

    /**
     * @param url       请求URL
     * @param mapParams 请求参数键值对
     * @param callback  okhttp异步请求回调
     * @author: zhuquanwen
     * @description: 异步put请求
     * @date: 2018/3/19 10:59
     * @exception: IOException IO异常
     * @return: void
     */
    public static void doPutAsyn(String url, Map<String, String> mapParams, Callback callback) throws IOException {
        doPutAsyn(url, (Map<String, String>) null, mapParams, callback);
    }

    /**
     * @param url        请求URL
     * @param headerMap  header键值对
     * @param jsonParams 请求json字符串
     * @param callback   okhttp异步请求回调
     * @author: zhuquanwen
     * @description: 异步put请求
     * @date: 2018/3/19 10:59
     * @exception: IOException IO异常
     * @return: void
     */
    public static void doPutAsyn(String url, Map<String, String> headerMap, String jsonParams, Callback callback) throws IOException {
        Call call = basePutCall2(url, headerMap, jsonParams);
        call.enqueue(callback);
    }

    /**
     * @param url        请求URL
     * @param jsonParams 请求JSON字符串
     * @param callback   okhttp异步请求回调
     * @author: zhuquanwen
     * @description: 异步put请求
     * @date: 2018/3/19 10:59
     * @exception: IOException IO异常
     * @return: void
     */
    public static void doPutAsyn(String url, String jsonParams, Callback callback) throws IOException {
        doPutAsyn(url, (Map<String, String>) null, jsonParams, callback);
    }


    /**
     * @param url       请求URL
     * @param headerMap header 请求键值对
     * @author: zhuquanwen
     * @description: 同步delete请求
     * @date: 2018/3/19 11:01
     * @exception: IOException IO异常
     * @return: java.lang.String
     */
    public static String doDelete(String url, Map<String, String> headerMap) throws IOException {

        Call call = baseDeleteCall(url, headerMap);
        return call.execute().body().string();
    }

    /**
     * @param url 请求URL
     * @author: zhuquanwen
     * @description: 同步delete请求
     * @date: 2018/3/19 11:01
     * @exception: IOException IO异常
     * @return: java.lang.String
     */
    public static String doDelete(String url) throws IOException {
        return doDelete(url, (Map<String, String>) null);
    }

    /**
     * @param url       请求URL
     * @param headerMap header键值对
     * @param callback  okhttp异步回调
     * @author: zhuquanwen
     * @description: 异步DELETE请求
     * @date: 2018/3/19 11:03
     * @exception: IOException IO异常
     * @return: java.lang.String
     */
    public static String doDeleteAsyn(String url, Map<String, String> headerMap, Callback callback) throws IOException {

        Call call = baseDeleteCall(url, headerMap);
        return call.execute().body().string();
    }

    /**
     * @param url      请求URL
     * @param callback okhttp异步回调
     * @author: zhuquanwen
     * @description: 异步DELETE请求
     * @date: 2018/3/19 11:03
     * @exception: IOException IO异常
     * @return: java.lang.String
     */
    public static String doDeleteAsyn(String url, Callback callback) throws IOException {
        return doDeleteAsyn(url, (Map<String, String>) null, callback);
    }


    private static Request.Builder requestBuilderAddHeader(Map<String, String> headerMap, String url) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return requestBuilder;
    }

    private static String judgeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    @FunctionalInterface
    public interface FromJsonHandler {
        Object fromJson(String json);
    }

    public static void main(String[] args) throws IOException {
        OkHttpClientUtils okHttpClientUtils = new OkHttpClientUtils();
        Map<String, String> map = new HashMap<>();
        map.put("sec-ch-ua", "\".Not/A)Brand\";v=\"99\", \"Google Chrome\";v=\"103\", \"Chromium\";v=\"103\"");
        map.put("sec-ch-ua-mobile", "?0");
        map.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
        map.put("sec-ch-ua-platform", "\"macOS\"");
        map.put("Accept", "*/*");
        map.put("Sec-Fetch-Site", "same-origin");
        map.put("Sec-Fetch-Mode", "cors");
        map.put("Sec-Fetch-Dest", "empty");
        map.put("Referer", "https://fanyi.baidu.com/?aldtype=16047");
        map.put("Accept-Language", "zh-CN,zh;q=0.9");
        map.put("Cookie", "BIDUPSID=9136F2D6EF296FB00658A18AAF3D3686; PSTM=1646633752; REALTIME_TRANS_SWITCH=1; FANYI_WORD_SWITCH=1; HISTORY_SWITCH=1; SOUND_SPD_SWITCH=1; SOUND_PREFER_SWITCH=1; APPGUIDE_10_0_2=1; BAIDUID=0FA83D94DFFFE959982A4B3BBF280D2C:FG=1; H_WISE_SIDS=110085_127969_131861_180636_188740_189755_190621_194085_194530_196427_197711_199567_203517_204904_205168_205859_206124_208721_209568_210299_210321_210443_211292_211312_212295_212726_212786_212873_212912_213040_213059_213079_213094_213271_213359_213440_213729_213940_214086_214094_214205_214379_214596_214641_214782_215070_215126_215728_215730_215825_215856_215957_216253_216332_216354_216569_216570_216596_216634_216646_216696_216853_216884_216922_216941_216963_217087_217149_217184_217320_217343_217658_217723_217836_218031_218234_218248_218330_218394_218453_218540; H_WISE_SIDS_BFESS=110085_127969_131861_180636_188740_189755_190621_194085_194530_196427_197711_199567_203517_204904_205168_205859_206124_208721_209568_210299_210321_210443_211292_211312_212295_212726_212786_212873_212912_213040_213059_213079_213094_213271_213359_213440_213729_213940_214086_214094_214205_214379_214596_214641_214782_215070_215126_215728_215730_215825_215856_215957_216253_216332_216354_216569_216570_216596_216634_216646_216696_216853_216884_216922_216941_216963_217087_217149_217184_217320_217343_217658_217723_217836_218031_218234_218248_218330_218394_218453_218540; Hm_lvt_64ecd82404c51e03dc91cb9e8c025574=1657721889,1658220372,1658603806,1658808382; delPer=0; PSINO=1; BAIDUID_BFESS=0FA83D94DFFFE959982A4B3BBF280D2C:FG=1; ZFY=AKIxEEIYvM6INI:BE0zm8yvjzBlSo1J:AWWmF:B33tZgFU:C; BCLID=7344506492623721271; BDSFRCVID=hM-OJeC62wAln9ODIX8Oqa0uuuTFtSTTH6aovyo5UobT_49KwqIDEG0PLx8g0KuMKjf8ogKK0mOTHUkF_2uxOjjg8UtVJeC6EG0Ptf8g0f5; H_BDCLCKID_SF=Jn4q_ItKJKI3fP36qRQEh4-3MfTMaR8XKKOLVbod3p7keq8CDRJNh-6-XPOktt3K-CCjL45aQpQMVUb2y5jHhnK-hNoP2-jnXbP8a-3hWlbpsIJMXbAWbT8U5tciatKOaKviaKJEBMb1SJvDBT5h2M4qMxtOLR3pWDTm_q5TtUJMeCnTD-Dhe6jBjGD8qT-ef5vfL5rh55r_et-reMvNXUI8LNDHtp5H2HCfKUIK5nOaHDJG5JoxQR093bO7ttoyJmJMWhcF5RovqMj9KM6r3fL1Db0thTvMtg3t3JbIfPToepvoD-cc3MkBe-jdJJQOBKQB0KnGbUQkeq8CQft20b0EeMtjKjLEtbADoCKhtKD3fP36q4JB-tFtqxby26n3a239aJ5nJDoSjqPzyM6UWq8IKpQKJnQt-jcd2tj-QpP-HJAwhhbZj4kEjxjCL5j35mcLKl0MLpRlbb0xyUQD04khqfnMBMPjamOnaU5o3fAKftnOM46JehL3346-35543bRTLnLy5KJtMDF4j6D-ejvWDNRf-b-X-CkXWJ52f-_ajp7_bf--D4AYMfbXtM3AygjDQRDMb45hSbQ-jtTxy5K_hPKqX6bO5IowWK5_ypvEMfTHQT3mDxrbbN3i-4jb3Jb8Wb3cWKJV8UbS3tRPBTD02-nBat-OQ6npaJ5nJq5nhMJmb67JD-50eG8jtj0jJJk8V-35b5rajJrT5-r_bPFQqxby26n3B4j9aJ5nJDobEJRkefnUyqvbXpQKJnQtbGnlLUOmQpP-HJ7C2tPh3t4V3bPJqxnf-m5RKl0MLpRWbb0xynoYyPIEeMnMBMPjamOnaU5o3fAKftnOM46JehL3346-35543bRTLnLy5KJtMDcnK4-Xj5JXeHoP; BCLID_BFESS=7344506492623721271; BDSFRCVID_BFESS=hM-OJeC62wAln9ODIX8Oqa0uuuTFtSTTH6aovyo5UobT_49KwqIDEG0PLx8g0KuMKjf8ogKK0mOTHUkF_2uxOjjg8UtVJeC6EG0Ptf8g0f5; H_BDCLCKID_SF_BFESS=Jn4q_ItKJKI3fP36qRQEh4-3MfTMaR8XKKOLVbod3p7keq8CDRJNh-6-XPOktt3K-CCjL45aQpQMVUb2y5jHhnK-hNoP2-jnXbP8a-3hWlbpsIJMXbAWbT8U5tciatKOaKviaKJEBMb1SJvDBT5h2M4qMxtOLR3pWDTm_q5TtUJMeCnTD-Dhe6jBjGD8qT-ef5vfL5rh55r_et-reMvNXUI8LNDHtp5H2HCfKUIK5nOaHDJG5JoxQR093bO7ttoyJmJMWhcF5RovqMj9KM6r3fL1Db0thTvMtg3t3JbIfPToepvoD-cc3MkBe-jdJJQOBKQB0KnGbUQkeq8CQft20b0EeMtjKjLEtbADoCKhtKD3fP36q4JB-tFtqxby26n3a239aJ5nJDoSjqPzyM6UWq8IKpQKJnQt-jcd2tj-QpP-HJAwhhbZj4kEjxjCL5j35mcLKl0MLpRlbb0xyUQD04khqfnMBMPjamOnaU5o3fAKftnOM46JehL3346-35543bRTLnLy5KJtMDF4j6D-ejvWDNRf-b-X-CkXWJ52f-_ajp7_bf--D4AYMfbXtM3AygjDQRDMb45hSbQ-jtTxy5K_hPKqX6bO5IowWK5_ypvEMfTHQT3mDxrbbN3i-4jb3Jb8Wb3cWKJV8UbS3tRPBTD02-nBat-OQ6npaJ5nJq5nhMJmb67JD-50eG8jtj0jJJk8V-35b5rajJrT5-r_bPFQqxby26n3B4j9aJ5nJDobEJRkefnUyqvbXpQKJnQtbGnlLUOmQpP-HJ7C2tPh3t4V3bPJqxnf-m5RKl0MLpRWbb0xynoYyPIEeMnMBMPjamOnaU5o3fAKftnOM46JehL3346-35543bRTLnLy5KJtMDcnK4-Xj5JXeHoP; BDRCVFR[feWj1Vr5u3D]=mk3SLVN4HKm; BA_HECTOR=8ha52haka0ak0h810l26sda81heetd417; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598; ab_sr=1.0.1_MTZjZTM3MDc4MmRlOWRmMzVmYzFlZDkzZGUxM2Y3OTQ4ZjUzM2Y5ZjEzMzJjMWZlYTE5MTc5Y2IzMTFjNGE3NWJhZDc3NGU5NzE2ZTBhMWQ1YTBjZTYzODg3NjcxNDBjMDU5OGVjMjdkMWY5ZGJhZWUyODIzNWIxNTZlOWJkYzlkMDg5ZDYxYjk2NmJiMmYyMzM4NzE1MTcwOWU1NWYwYQ==; H_PS_PSSID=36838_36546_36464_36255_36726_36413_36955_36167_36917_36919_36570_36776_36745_26350_36680_36934; Hm_lpvt_64ecd82404c51e03dc91cb9e8c025574=1659341261");

        Response execute = baseGetCall("https://fanyi.baidu.com/mtpe/user/getInfo?_=1659341261477", map).execute();
        System.out.println(execute.body().string());
    }

}