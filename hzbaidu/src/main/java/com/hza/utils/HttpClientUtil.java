package com.hza.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.pool.PoolStats;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;


/**
 * @author hza
 * HttpUtils Over !
 */

public class HttpClientUtil {
    private static CloseableHttpClient closeableHttpClient;
    private static PoolingHttpClientConnectionManager cm;

    private final static String ip = "tps267.kdlapi.com";
    private final static String port = "15818";
    private final static String username = "t15096838352379";
    private final static String password = "0nm781nk";

//    private final static String ip = "tunnel3.qg.net";
//    private final static String port = "18361";
//    private final static String username = "3E42F9A1";
//    private final static String password = "E06200A8D657";

    /**
     * 带有账号密码的代理
     * 非账号密码的直接在 static下 使用 setproxy() 直接构造一个代理就好
     *
     * @return
     */
    public static HttpClient getHttpClient() {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String proxyHost = ip;
        int proxyPort = Integer.valueOf(port);
        httpClient.getCredentialsProvider().setCredentials(
                new AuthScope(proxyHost, proxyPort),
                new UsernamePasswordCredentials(username, password));
        HttpHost proxy = new HttpHost(proxyHost, proxyPort);
        httpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
        return httpClient;
    }

    static {
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        /*
         一、绕过不安全的https请求的证书验证
         */
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", trustHttpsCertificates())
                .build();
        /*
          二、创建连接池管理对象
         */
        cm = new PoolingHttpClientConnectionManager(registry);
        cm.setMaxTotal(50); // 连接池最大有50个连接,<=20
        /*
            roadjava.com域名/ip+port 就是一个路由，
            http://www.roadjava.com/s/spsb/beanvalidation/
            http://www.roadjava.com/s/1.html
            https://www.baidu.com/一个域名，又是一个新的路由
         */
        cm.setDefaultMaxPerRoute(50); // 每个路由默认有多少连接,<=2
        httpClientBuilder.setConnectionManager(cm);
        /*
        三、设置请求默认配置
         */
        //设置代理地址、代理端口号、协议类型

/*        String ip = "112.194.91.200";
        String port = "2324";
        //代理主机
        HttpHost proxy = new HttpHost(ip, Integer.parseInt(port));*/

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(3000)
                .setSocketTimeout(3000)
                .setConnectionRequestTimeout(3000)
                // 设置代理
//                .setProxy(proxy)
                .build();
        httpClientBuilder.setDefaultRequestConfig(requestConfig);


        /*
        四、设置默认的一些header
         */
        List<Header> defaultHeaders = new ArrayList<>();
//        BasicHeader userAgentHeader = new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36");
//        defaultHeaders.add(userAgentHeader);
        httpClientBuilder.setDefaultHeaders(defaultHeaders);

        // 线程安全,此处初始化一次即可,通过上面的配置来生成一个用于管理多个连接的连接池closeableHttpClient
        closeableHttpClient = httpClientBuilder.build();
//        closeableHttpClient = (CloseableHttpClient) getHttpClient();
    }


    /**
     * 构造安全连接工厂
     *
     * @return SSLConnectionSocketFactory
     */
    private static ConnectionSocketFactory trustHttpsCertificates() {
        SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
        try {
            sslContextBuilder.loadTrustMaterial(null, new TrustStrategy() {
                // 判断是否信任url
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            });
            SSLContext sslContext = sslContextBuilder.build();
            return new SSLConnectionSocketFactory(sslContext,
                    new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"}
                    , null, NoopHostnameVerifier.INSTANCE);
        } catch (Exception e) {
            System.out.println("构造安全连接工厂失败" + e);
            throw new RuntimeException("构造安全连接工厂失败");
        }
    }

    /**
     * 发送get请求
     *
     * @param url     请求url,参数需经过URLEncode编码处理
     * @param headers 自定义请求头
     * @return 返回结果
     */
    public static String executeGet(String url, Map<String, String> headers) {
        // 构造httpGet请求对象
        HttpGet httpGet = new HttpGet(url);
        // 自定义请求头设置
        if (headers != null) {
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                httpGet.addHeader(new BasicHeader(entry.getKey(), entry.getValue()));
            }
        }
        // 可关闭的响应
        CloseableHttpResponse response = null;
        try {
//            System.out.println("prepare to execute url:{}", url);
            System.out.println("prepare to execute url:{}" + url);
            response = closeableHttpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            if (HttpStatus.SC_OK == statusLine.getStatusCode()) {
                HttpEntity entity = response.getEntity();
                String responseData = EntityUtils.toString(entity, StandardCharsets.UTF_8);
//                System.out.println(responseData);
                return responseData;
            } else {
                System.out.println("响应失败,响应码:" + statusLine.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("executeGet error,url:{}" + url + e);
        } finally {
            consumeRes(response);
        }
        return null;
    }

    /**
     * 发送表单类型的post请求
     *
     * @param url     要请求的url
     * @param list    参数列表
     * @param headers 自定义头
     * @return 返回结果
     */
    public synchronized static String postForm(String url, List<NameValuePair> list, Map<String, String> headers) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        if (headers != null) {
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                httpPost.addHeader(new BasicHeader(entry.getKey(), entry.getValue()));
            }
        }
        // 确保请求头一定是form类型
//        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        // 给post对象设置参数
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(list, Consts.UTF_8);
        httpPost.setEntity(formEntity);
        // 响应处理
        CloseableHttpResponse response = null;
        try {
            System.out.println("prepare to execute url:{}" + httpPost.getRequestLine());
            response = closeableHttpClient.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            if (HttpStatus.SC_OK == statusLine.getStatusCode()) {
                HttpEntity entity = response.getEntity();
//                System.out.println(EntityUtils.toString(entity, StandardCharsets.UTF_8));
                String responseData = EntityUtils.toString(entity, StandardCharsets.UTF_8);
//                System.out.println(responseData);
                return responseData;
            } else {
                System.out.println("响应失败,响应码:" + statusLine.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("executeGet error,url:{}" + url);
            e.printStackTrace();
        } finally {
            consumeRes(response);
        }
        return null;
    }


    public static String fromPost(String url, Map<String, String> headers) throws Exception {
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        String urlStr = url;
        // 创建httppost对象
        HttpPost httpPost = new HttpPost(urlStr);
        // 设置请求头
        if (headers != null) {
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                httpPost.addHeader(new BasicHeader(entry.getKey(), entry.getValue()));
            }
        }
        // 给post对象设置参数
        /*
         NameValuePair： <input id="user-name-label" type="text" name="userName"/>
         的name(userName)和input标签里面输入的值就构成了一个NameValuePair对象
         */
//        List<NameValuePair> list = new ArrayList<>();
//        list.add(new BasicNameValuePair("responseData", "{\"data\":\"aae414d1cd3b75f468e3550226ed89578960b0e9b95ca7e56de7599e3e6e453932d82041888666984dba503d1e7da17992d6fef0a37cdd89eb4c1f7bfc2a8f5609746ad065b23d0c092d9a881e0373aa\",\"key_id\":\"23\",\"sign\":\"bfeca04b\"}"));

        CloseableHttpResponse response = null;
        try {
            response = closeableHttpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String toStringResult = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            System.out.println(toStringResult);
            EntityUtils.consume(entity);
            return toStringResult;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (closeableHttpClient != null) {
                try {
                    closeableHttpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * form表单格式 post不定义type时默认形式
     * utils类需请求post 需要构建 字符串url
     * List<NameValuePair> list 为 body
     * map 为请求头 一般使用HashMap来构造
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String urlStr = "https://graph.baidu.com/api/antispam?sign=021f572315d4700f7e06d11658602157&srcp=home_icon&tn=bdbox_half&idctag=tc&sids=10006_10521_10966_10974_11031_17850_17071_18100_17201_17202_18312_19199_19162_19215_19268_19280_19670_19807_20005_20011_20051_20060_20070_20090_20134_20144_20162_20172_20180_20193_20236_20243_20263&gsid=1015_1021_1040_1060_1080_1100&logid=3734213723&entrance=question&pageFrom=bdquestion";
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("responseData", "{\"data\":\"aae414d1cd3b75f468e3550226ed89578960b0e9b95ca7e56de7599e3e6e453932d82041888666984dba503d1e7da17992d6fef0a37cdd89eb4c1f7bfc2a8f5609746ad065b23d0c092d9a881e0373aa\",\"key_id\":\"23\",\"sign\":\"bfeca04b\"}"));

        HashMap<String, String> map = new HashMap();

        map.put("Host", "graph.baidu.com");
        map.put("Sec-Fetch-Mode", "cors");
        map.put("User-Agent", "Mozilla/5.0 (Linux; Android 10; Pixel 3 Build/QQ3A.200805.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/76.0.3809.89 Mobile Safari/537.36 T7/13.6 SP-engine/2.38.0 matrixstyle/0 lite baiduboxapp/5.28.0.10 (Baidu; P1 10) imagesearch/1.0 main/1.0");
        map.put("Accept", "*/*");
        map.put("Origin", "https://graph.baidu.com");
        map.put("X-Requested-With", "com.baidu.searchbox.lite");
        map.put("Sec-Fetch-Site", "same-origin");
        map.put("Referer", "https://graph.baidu.com/s?f=question&tn=bdbox_half&sign=021f572315d4700f7e06d11658602157&sids=10006_10521_10966_10974_11031_17850_17071_18100_17201_17202_18312_19199_19162_19215_19268_19280_19670_19807_20005_20011_20051_20060_20070_20090_20134_20144_20162_20172_20180_20193_20236_20243_20263&logid=3734213723&idctag=tc&pageFrom=bdquestion&is_halfwap=1&more_question=1&jsup=&srcp=home_icon&entrance=QUESTION&question_verify=0&skts=d1%3A18%2Cd2%3A19%2Cd3%3A4&webviewType=half&kts=de,b0:1658602161036,b1:,b2:1627,b6:26&service=bdbox&uid=ga2etYiFvag_iS8Oga2zuluPH8giu28glu2yil8h2a8-9QP14iSMuSHoA&from=1023973h&ua=_a-qiyaO2ijBNE6lI5me6NNJ2I_cPX8WoavjhSGHNqqqB&ut=pJGgz9Rovh_cuDiEjNS-qkypmoq0C&osname=baiduboxapp&osbranch=a2&branchname=baiduboxlite&pkgname=com.baidu.searchbox.lite&cfrom=1023973h&ctv=2&cen=ua_uid_ut&typeid=0&puid=_avjiguKviGedqqqB&c3_aid=A00-5GXVXBKFJ76Y7RANVGY3DELZHDWBTYSF-C4Y4RX5F&zid=ZRyIbZQFt4Cai2va-Wz1QjqY4KuufezvfXej-8tPLqKD1QE9EUcE5yIC0btzJbVhmm10ziENzov3VwIUWhIbU2w&matrixstyle=0&fv=13.6.0.10&ds_stc=0.4327&ds_lv=4&sdk_version=13.6.0&network=1_0&graphCode=22005&image_source=TOP_GALLERY&cam_pos=BACK");
        map.put("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
        map.put("Cookie", "BAIDUCUID=ga2etYiFvag_iS8Oga2zuluPH8giu28glu2yil8h2a8-9QP14iSMuSHoA; matrixstyle=0; SP_FW_VER=0.0.0; SG_FW_VER=0.0.0; BAIDUID=E9F745BDE3F7A499CE1366146B273EAA:FG=1; iadlist=1073741825; fontsize=1.0; GCONF_PARAMS=NNw1ojug2kfUNwokYavKf_RJmC914-iPgO2j9NIyxofhO2Ir_PXl8oaS29N-Ixt7_RAUCfJ_AjjAaviggN2HiguYL8_Cavhl_a2li4aq2fg4Nv8qALqqC; SAMPLE_SIDS=; antispam_key_id=23; ZFY=FodqH2e3W9uyZWWQ0tTTmF6MA57KC3Meo12w3Kekb3c:C; H_WISE_SIDS=110086_196427_197471_197711_199571_204901_208721_209202_209239_209568_209594_210303_210314_210316_210321_210326_210339_212296_212798_213030_213352_214109_214129_214138_214143_214188_214804_214898_215730_215957_216207_216326_216447_216517_216614_216838_216859_216883_216941_217185_217659_218445_218482_218537_218549_218597_218648_218652_219028_219066_219138_219155_219245_219252_219329_219363_219447_219452_219581_219666_219711_219733_219737_219823_219845_219862_219907_219943_219946_219948_220002_220016_220068_220071_220236_220270_220315_220336_220556_220561_220607_220663_220818_220976_221016_221088_221107_221117_221119_221121_221352_221365_221370_221371_221502_221570_1131719; H_WISE_SIDS_BFESS=110086_196427_197471_197711_199571_204901_208721_209202_209239_209568_209594_210303_210314_210316_210321_210326_210339_212296_212798_213030_213352_214109_214129_214138_214143_214188_214804_214898_215730_215957_216207_216326_216447_216517_216614_216838_216859_216883_216941_217185_217659_218445_218482_218537_218549_218597_218648_218652_219028_219066_219138_219155_219245_219252_219329_219363_219447_219452_219581_219666_219711_219733_219737_219823_219845_219862_219907_219943_219946_219948_220002_220016_220068_220071_220236_220270_220315_220336_220556_220561_220607_220663_220818_220976_221016_221088_221107_221117_221119_221121_221352_221365_221370_221371_221502_221570_1131719; BCLID_BFESS=8399002765623425107; BDSFRCVID_BFESS=6tPOJeCin9GKCwcDdeFzqa0uugKX8jRTH60oYu-2HjJu3Fx5LOX9EG0PLU8g0KuMHXs7ogKK0mOTHUkF_2uxOjjg8UtVJeC6EG0Ptf8g0f5; H_BDCLCKID_SF_BFESS=tbIf_D0hfCt3ePov5J0_5PLqMfQXhtvKato2WbCQJtbm8pcNLPr_ef-ULpJ20P3-3jQD3CJabR3voKnF0lO1j68eWq_LJfvBWjIeKtPaBp7ibq5jDh3d3jksD-RtWxvv-bvy0hvctn6cShnaMUjrDRLbXU6BK5vPbNcZ0l8K3l02V-bIe-t2b6Qh-p52f6L8JnCD3j; BDUSS=GhKUnd6bU9QbmxGTWllV0p-SHJVeW1OVU53OUpweHIxUXlPMlJPWW5kckV-d0ZqRVFBQUFBJCQAAAAAAAAAAAEAAACqpEazvdSzvl8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMRy2mLEctpie; STOKEN=2d347221a05529c694cfe45c50a393149fb2db31ed3f1f4f0c1dfeddd34fcf32; WISE_HIS_PM=0; BDUSS_BFESS=GhKUnd6bU9QbmxGTWllV0p-SHJVeW1OVU53OUpweHIxUXlPMlJPWW5kckV-d0ZqRVFBQUFBJCQAAAAAAAAAAAEAAACqpEazvdSzvl8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMRy2mLEctpie; BAIDULOCNEW=12959220_4825334_100000_131_1658601818611_1; ab_sr=1.0.1_ZWZiOTc1MjI0ODVkZWJjMWY3OTFjYTFmNGVjNTFlZTcwNTU5OGE1YzdmYTQxNTY5YmZmMDhkMzgyM2Q5ODQ0ZDdmMDU5M2I3YzNmODQwMzVhMTc4NWEyODE4NjJkMWQyODQ2OGJjYmIwOGQzYTMwMmExNDYzMzdjMmUyOTdkNGQ5MDBmNmU5ZWZmNGJjZjQyY2FkODg1MTllZGUxODZmNg==; antispam_data=aae414d1cd3b75f468e3550226ed89578960b0e9b95ca7e56de7599e3e6e453932d82041888666984dba503d1e7da17992d6fef0a37cdd89eb4c1f7bfc2a8f5609746ad065b23d0c092d9a881e0373aa");
        map.put("Content-Type", "application/x-www-form-urlencoded");


        postForm(urlStr, list, map);
//        executeGet("https://www.baidu.com", null);
    }

    /**
     * 发送json类型的post请求
     *
     * @param url     请求url
     * @param body    json字符串
     * @param headers 自定义header
     * @return 返回结果
     */
    public static String postJson(String url, String body, Map<String, String> headers) {
        HttpPost httpPost = new HttpPost(url);
        // 设置请求头
        if (headers != null) {
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                httpPost.addHeader(new BasicHeader(entry.getKey(), entry.getValue()));
            }
        }
        // 确保请求头是json类型
        httpPost.addHeader("Content-Type", "application/json; charset=utf-8");
        /*
        设置请求体
         */
        StringEntity jsonEntity = new StringEntity(body, Consts.UTF_8);
        jsonEntity.setContentType("application/json; charset=utf-8");
        jsonEntity.setContentEncoding(Consts.UTF_8.name());
        httpPost.setEntity(jsonEntity);

        CloseableHttpResponse response = null;
        try {
            response = closeableHttpClient.execute(httpPost);
            printStat();
            StatusLine statusLine = response.getStatusLine();
            if (HttpStatus.SC_OK == statusLine.getStatusCode()) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, StandardCharsets.UTF_8);
            } else {
                System.out.println("响应失败,响应码:" + statusLine.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("executeGet error,url:{}" + url + e);
        } finally {
            consumeRes(response);
        }
        return null;
    }

    private static void printStat() {
        // 连接池的最大连接数 50
//        System.out.println("cm.getMaxTotal():{}",cm.getMaxTotal());
        // 每一个路由的最大连接数 50
//        System.out.println("cm.getDefaultMaxPerRoute():{}",cm.getDefaultMaxPerRoute());
        PoolStats totalStats = cm.getTotalStats();
        // 连接池的最大连接数 50
//        System.out.println("totalStats.getMax():{}",totalStats.getMax());
        // 连接池里面有多少连接是被占用了
        System.out.println("totalStats.getLeased():{}" + totalStats.getLeased());
        // 连接池里面有多少连接是可用的
        System.out.println("totalStats.getAvailable():{}" + totalStats.getAvailable());
    }

    private static void consumeRes(CloseableHttpResponse response) {
        // response.close();是关闭连接,不是归还连接到连接池
        if (response != null) {
            try {
                EntityUtils.consume(response.getEntity());
            } catch (IOException e) {
                System.out.println("consume出错" + e);
            }
        }
    }
}
