package com.hza.api;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hza.entity.SignJson;
import com.hza.utils.Base64forRSA;
import com.hza.utils.HttpClientUtil;
import okhttp3.*;
import okhttp3.Authenticator;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import sun.nio.cs.ext.MacArabic;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

import static com.hza.utils.BaiduImgUtil.*;
import static com.hza.utils.HttpClientUtil.fromPost;
import static com.hza.utils.HttpClientUtil.postForm;
import static com.hza.utils.ImgUtil.*;

public class BaiduApi {

    private final static SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmss");
    private static final Gson gson = new Gson();

    private static final String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static HttpClientUtil httpClientUtil = new HttpClientUtil();

    public static String Buid = "ku2xjfJ5xq97a2fW8lEkoIrNbj_syAzJI4xFoN5Sxq8bkLPp5kWsM9MwA";

    private static String Bua = "_a-qiyaO2ijBNE6lI5me6NNJ2I_cPX8WoavjhSGHZqqqB";

    private final static String ip = "tps267.kdlapi.com";
    private final static String port = "15818";
    private final static String username = "t15096838352379";
    private final static String password = "0nm781nk";


    public static String[] BuidArrays = {
            "9uE4if4fwffkp2_6t5FN8YaIGj9z0w80NyAMifyUmj8G9QMnJfQ9MrHmA",
            "gaGbzf4oE89vJmiQja2Cj_5C26toJ26o9rwi6Ypqw68YkQa1Jk10RarwA",
            "9aGqg9ugvz_CymgjtuvNzgrCAi8SOEil84vnYg0Qwj8y9QPnJk1mO01uA",
            "Iux_iNuzwqt6pEjkf5xDotOHBfgO52qZk5xTjg4O-f8R9SMF59SUOD8aA",
            "gux9Y94eG89Ouw_CtpF4iNOHEYgaawqK9abbqguGvY8-kLMfJ8DaCyFwA",
            "NyvKY9pcmokQaAYzIJbD6iOzvjk1pwqsfaFFztpemq8jk3Ph08HURFEaA",
            "_aw76tuNvo_IpxqdI5mA_g5bmzNelwi6_JEC8_ODvq8R91MtliQYMMImA",
            "fyA2z_y-xit94m6w8rF1zkJxwYNC4EqlN5wgqkaDwo8Y9QMpliW_u87uA",
            "gJx9zk5rE_tuyA8FgaBz6kuSGzID4biu9rmKztaV2i889HP1p83-PVHuA",
            "fuFf8gpNE6traxoDfuAM8_r4vik75xiWI5muqfuxm68i9DMxlk3BRwFuA",
            "I5whjI0QEjg6uFi_kpACjg5rwjgVrbYQf4Eoj_a92Y8LkHapJI1iaaymA",
            "gpmpj_rsxi9ep2YjjJFH_8uf26NDrvzGi0Fiitpow68L91P15iWjODbmA",
            "fJEP8Yp_E_I3JmzvfamE_frZvYItyEza9Pmij9uPAo8i9QMx4IWoMbWuA",
            "9uxuo9aYE8tSlx6wfp2qfk5sw6t-uxq19lFxzf5-Az8L9Sh1JtSuO8HaA",
            "9rvkjtr52zknpFjJfO-o6942mo9XuAoJkJ2ZqNOo2i849SPnyiHKR67uA",
            "gr-o8g5AE6g9u2qF_uEVg_uQEj_2JmoN9rxqffy_x88YkHMfyt3UMaJmA",
            "gJAfz9pjE8tuywjDfrAKq9PS26gVJEiM8lxi8_ueAi8b9WhFJiHnaWEuA",
            "frAv8iPox6_MuA8nfaAl8I48Fj_m0w8F9uEIYtrWAz8b9HPnpfSzMFamA",
            "kpxdY8yGmYfF5wY__5E_Y_5rEqNwrFqKtJAvYtyPGz8BkQatJN3iMM8wA",
            "grvE_I0gwifVJ-zlf5vPqfrTw6kQ5wYFkJAno8ram_8j9QaCptSACXDaA",
            "f4mb6frNv6_Z0Af6Nrv0_949vz9n4EY6Ip2tijuXw88LkQaFr8HZa9buA",
            "_Pv4zjrF2q9gavq1NJ-o8N42FjNeOEizkym5q9rAxf8i9Wa2JfSouzJwA",
            "9J2b8I4uwjIJu2YbNlxOqIpgx_j44wgEt5FSqk4OEo8-k1aC09DoCE7mA",
            "8pES8_JlBfYulBql9r2TiN4WEq9y4FiL9yvc8jl_vz8D9WPv08H7Rw7aA",
            "grmFjfJxxzf6rbje9rx_8_PQ2Ytpavog9rEltIu6Ai8Y9Qag4iLDPb_uA",
            "tpmKg_u_A8gWpG8uIJvZitu6vjfpumYt9p-LYkp8wz8-9QP2pt17urzwA",
            "g5x4ofJS26tnrm6uI0xOof5gFjIWpwjm8pA56k4GE68LkSP_rfWmuz1mA",
            "YPwCYk44Eq_mp2q_gpmn6IJJAqiC4xYN9r2Vf9JDv_8j93Mn5iLqa8UuA",
            "_4wS6f5Xxz_95wqB9rbmqguOvijJ42qAjuwefiJuw88RkWh1y81FPbDmA",
            "Il2Ag85Ibz96OFzGjrxE69Pobf9CpE8pf4mKjirqbi8L91aFpfWKhH1wA",
            "Nrv1qkyp26gOPA6dgyxwiIa3E69xJEjHfO2kqIJ4vo8UkLPClI1qC1FuA",
            "tOwqf_0Ww_jkamYH85w5qkJkbfk5av8__5mlq_4ivo8MkWM_48LAuHJuA"

    };

    // 用来记录setSign 连续为空次数
    private static int errorCount = 0;

    public static String TIMES = null;

    // 全局LogMap 参数 用来存放写日志需要记录的信息
    static HashMap<String, String> logMap = new HashMap<>();

    static HashMap<String, String> imageMap = new HashMap<>();

    static ArrayList<String> uidList = new ArrayList<>();

    static String img_base64;


    public synchronized static boolean imgInfo() {
        try {
            // uid 赋值
//            setUidList();
            img_base64 = getImgUrlToBase64(getImgUrl());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取网络图片
     *
     * @return 网络图片url
     * @throws IOException
     */
/*    public static String getImgUrl() throws IOException {

        String getImgUrl = "http://sdc.zhenguanyu.com/papi?r=sdc-query/get-querys&ptype=bond&limit=1&type=online123";
        String getImgResponseData = httpClientUtil.executeGet(getImgUrl, null);
        JSONObject parseObject = JSONObject.parseObject(getImgResponseData);
        String imgUrl = parseObject.getJSONArray("data").getJSONObject(0).get("url").toString();
        String imageId = parseObject.getJSONArray("data").getJSONObject(0).get("imageId").toString();
        String solarId = parseObject.getJSONArray("data").getJSONObject(0).get("solarId").toString();
        imageMap.put("imageId", imageId);
        imageMap.put("solarId", solarId);

        return imgUrl;
    }*/
    public static String getImgUrl() throws IOException {

        URL url = new URL("http://sdc.zhenguanyu.com/papi?r=sdc-query/get-querys&ptype=bond&limit=1&type=online123");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("GET");
        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
        JSONObject parseObject = JSONObject.parseObject(response);
        String imgUrl = parseObject.getJSONArray("data").getJSONObject(0).get("url").toString();
        String imageId = parseObject.getJSONArray("data").getJSONObject(0).get("imageId").toString();
        String solarId = parseObject.getJSONArray("data").getJSONObject(0).get("solarId").toString();
        imageMap.put("imageId", imageId);
        imageMap.put("solarId", solarId);

        if (imgUrl == null || "".equals(imgUrl)) {
            System.err.println("query 图片为null");
        }
        System.out.println(imgUrl);
        return imgUrl;
    }

    public static void setUidList() {
        for (String forUid : BuidArrays) {
            uidList.add(forUid);
        }
    }


    public static String getTokenInfo() throws Exception {
        String img = extractImgBase64Feature(img_base64);
        String buff1 = "3082024f308201b8a003020102020453db2e22300d06092a864886f70d0101050500306b310b300906035504061302434e3110300e060355040813074265696a696e673110300e060355040713074265696a696e6731133011060355040a130a426169647520496e632e31133011060355040b130a426169647520496e632e310e300c0603550403130542616964753020170d3134303830313036303532325a180f32303634303731393036303532325a306b310b300906035504061302434e3110300e060355040813074265696a696e673110300e060355040713074265696a696e6731133011060355040a130a426169647520496e632e31133011060355040b130a426169647520496e632e310e300c06035504031305426169647530819f300d06092a864886f70d010101050003818d0030818902818100a8184a7716f3c8ce7ee12205e8b3de138c66b30066d3e78191e5df8103c9b704a95d16bfa99e9850fecc288f4fd29e0110c906b8f7986dd944a7816d1a0981f762e486eee919d75efe9f8d7addc0d81d1a34133ebea9fd54d67f21fb6e8b9adb9e3ebfc638e966fec3096cb316fb7882e8b0b0954f883377b6ead78bb06eec0f0203010001300d06092a864886f70d01010505000381810039a42addcace5a57a3a2703199c784fd16b68e0e0096ff5ed1ca52e84ab5e81ac87e13b6ad05c9b01964b12aad570215e587a8ed7753d4c87f224a332f546c8641e843d5cb6a595459cb8795ccc1e30f582f26c1e32e7ecb8c758baab5f85f3c6dbc56bad35bd9ac436b9513657730b39b5e99cfbbe92f9a33007bc46417ee5a" + Buid;
        String buff2 = "Mozilla/5.0 (Linux; Android 8.1.0; Pixel Build/OPM4.171019.021.P1; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/103.0.5060.129 Mobile Safari/537.36 SP-engine/2.38.0 matrixstyle/0 lite baiduboxapp/5.34.0.10 (Baidu; P1 8.1.0) graphplugin/1.0.10";

        StringBuilder builder = new StringBuilder();
        builder.append(img);
        builder.append(buff1);
        builder.append(buff2);
        TIMES = System.currentTimeMillis() + "000";
        builder.append(TIMES);
        String sBuilder = builder.toString();

        // 调用md5封装一层
        String md5Builder = getMd5(sBuilder);

        //RSA - So 传入 byte[]对象
        byte[] rsaBytes = encryptByte(md5Builder.getBytes());

        // getBase64
        String tokenForm = Base64forRSA.encode(rsaBytes);

        // URLEncoder 格式化
        String encoderToken = URLEncoder.encode(tokenForm);
        String result = encoderToken + "-v2";

        return result;
    }

  /*  public synchronized static String postToSign() throws Exception {
        StringBuilder builder = new StringBuilder();
        // fromImg 为 post 传递的 body参数



        String imgBody = createJSONObject().toString();
        System.out.println(imgBody);
        String fromImg = fromImg(imgBody);
        builder.append("https://graph.baidu.com/api/bdbox?path=bdboxn/2/qptranslate_sole&service=bdbox&uid=");
        builder.append(Buid);
        builder.append("&from=1023973h&ua=_a-qiyaO2ijBNE6lI5me6NNJ2I_");
        builder.append(randomString(18));
        builder.append("&ut=pJGgz9Rovh_cuDiEjNS-qkypmoq0C&osname=baiduboxapp&osbranch=a2&branchname=baiduboxlite&pkgname=com.baidu.searchbox.lite&cfrom=1023973h&ctv=2&cen=ua_uid_ut&typeid=0&puid=_avjiguKviGedqqqB&c3_aid=A00-5GXVXBKFJ76Y7RANVGY3DELZHDWBTYSF-C4Y4RX5F&zid=ZRyIbZQFt4Cai2va-Wz1QjqY4KuufezvfXej-8tPLqKBH_0Sm4UqlgZx92_o0u0mSJA9N5qwDV710kHXhZrA0Ng&matrixstyle=0&fv=13.6.0.10&ds_stc=0.4327&ds_lv=4&sdk_version=13.6.0&network=1_0&graphCode=22005&plugin_version=com.baidu.searchbox.godeye&enter_search_time=1658819756110&private_mode=1&entrance=QUESTION&image_source=TOP_GALLERY&srcp=home_icon&cam_pos=BACK&gsid=1015_1021_1040_1060_1080_1100&question_verify=1&kts=de,b0:1658822350048,b1:&token=");
        builder.append(getTokenInfo());
        builder.append("_" + TIMES);
        System.out.println(builder.toString());
        String urls = builder.toString();

        String urlStr = urls;
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("data", imgBody));

        HashMap<String, String> map = new HashMap();
        map.put("Host", "graph.baidu.com");
        map.put("Cache-Control", "max-age=0");
        map.put("User-Agent", "Mozilla/5.0 (Linux; Android 8.1.0; Pixel Build/OPM4.171019.021.P1; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/76.0.3809.89 Mobile Safari/537.36 T7/13.6 SP-engine/2.38.0 matrixstyle/0 lite baiduboxapp/5.28.0.10 (Baidu; P1 8.1.0) imagesearch/1.0 main/1.0 graphplugin/1.0.10");
        map.put("Cookie", "BAIDUCUID=" + Buid + "; BAIDUID=E9F745BDE3F7A499CE1366146B273EAA:FG=1; iadlist=1073741825; fontsize=1.0; GCONF_PARAMS=NNw1ojug2kfUNwokYavKf_RJmC914-iPgO2j9NIyxofhO2Ir_PXl8oaS29N-Ixt7_RAUCfJ_AjjAaviggN2HiguYL8_Cavhl_a2li4aq2fg4Nv8qALqqC; antispam_key_id=23; ZFY=FodqH2e3W9uyZWWQ0tTTmF6MA57KC3Meo12w3Kekb3c:C; BCLID_BFESS=8399002765623425107; BDUSS=GhKUnd6bU9QbmxGTWllV0p-SHJVeW1OVU53OUpweHIxUXlPMlJPWW5kckV-d0ZqRVFBQUFBJCQAAAAAAAAAAAEAAACqpEazvdSzvl8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMRy2mLEctpie; STOKEN=2d347221a05529c694cfe45c50a393149fb2db31ed3f1f4f0c1dfeddd34fcf32; WISE_HIS_PM=0; BDUSS_BFESS=GhKUnd6bU9QbmxGTWllV0p-SHJVeW1OVU53OUpweHIxUXlPMlJPWW5kckV-d0ZqRVFBQUFBJCQAAAAAAAAAAAEAAACqpEazvdSzvl8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMRy2mLEctpie; BAIDULOCNEW=12959220_4825334_100000_131_1658754343308_1; ab_sr=1.0.1_NmEyMzEzYzk3N2IyMjNjYjA2YjZjOWFkZTBmYTFiN2ZiNWFmZGQ1MGNlNjUyMDRiOWExZjZiODYxYzM5NjE1Yjk2MDlkZjUxNWEyNzg2NTg0ZDgxNjc1MTNlYTdlODI0YzhlMzFhMDI0OTFjNDRlM2JlNzVlMjA2NzIyMTVhNmQzMjU0MmRhNzRkODRmNTdlZmJlNzQxZGY3YWRkZGYwYw==; antispam_data=aae414d1cd3b75f468e3550226ed89572ffe1088d4179532ded49cf067bf096b5efdaf3197617dbbe37b2c15df34f4ec74806c1a064f6fb9030c590f8cdf3f99194af483471c147373e42853e60b1106");
//        map.put("X-BD-TraceId", "d2cf26d8d9d048a0aff69d2d95742ef1");
        map.put("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
//        map.put("X-Forwarded-For", "120.244.143.165");
        map.put("Content-Type", "application/x-www-form-urlencoded");


        String responseData = null;
        try {
            responseData = postForm(urlStr, list, map);
        } catch (Exception e) {
            System.err.println("postToSign" + e.toString());
        }
        System.err.println("responseData : " + responseData);
        if (responseData == null || "".equals("")) {
            errorCount++;
            if (errorCount > 20) {
                System.out.println(" xi xi xi xi xi  >>>>>> " + errorCount);
                invokeUid();
            }

        }
        return responseData;

    }*/


    public static String postToSign() throws Exception {
        String responseData = null;
        try {
            X509TrustManager trustManager = new TrustAllManagerBxl();
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            imgInfo();
            StringBuilder builder = new StringBuilder();
            // fromImg 为 post 传递的 body参数
            System.err.println("Buid ===" + Buid);
            String imgBody = createJSONObject().toString();
            System.out.println(imgBody);
            String fromImg = fromImg(imgBody);
            builder.append("https://graph.baidu.com/api/bdbox?path=bdboxn/2/qptranslate_sole&service=bdbox&uid=" + Buid + "&from=1023973q&ua=_a-qi4ujvfg4NE6pI5me6NNJ2I_naXiWoavjhSQHNqqqB&ut=pJGgzkIl-I_UhvClguL-akpBEqorA&osname=baiduboxapp&osbranch=a2&branchname=baiduboxlite&pkgname=com.baidu.searchbox.lite&cfrom=1023973q&ctv=2&cen=ua_uid_ut&typeid=0&c3_aid=A00-5XSVZHZKS4FKVFOJK6JRJOHY7C6AT27R-FIJHVAF5&zid=-9F5EMDUnxooe_Ek9Ze1znLO4gh1PQzUJmoTGCO0kAeDJD5lSu4Z8-avpnr9ABcsn_xykyweNFfOYHGXTmOIV9A&matrixstyle=0&fv=13.11.0.11&ds_stc=0.3919&ds_lv=4&sdk_version=13.6.0&network=1_0&graphCode=22006&plugin_version=com.baidu.searchbox.godeye&enter_search_time=1659335236312&entrance=QUESTION&image_source=TOP_GALLERY&srcp=home_icon&cam_pos=BACK&gsid=1015_1021_1040_1060_1080_1100&question_verify=0&kts=de,b0:1659335248848,b1:245&token=");
            builder.append(getTokenInfo());
            builder.append("_" + TIMES);
            System.out.println(builder.toString());
            String urls = builder.toString();


//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .sslSocketFactory(sslSocketFactory, trustManager)
//                .proxy(proxy)
//                .build();

            OkHttpClient client = new OkHttpClient.Builder()
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


            MediaType mediaType = MediaType.parse(" application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, fromImg);
            Request request = new Request.Builder()
                    .url(urls)
                    .method("POST", body)
                    .addHeader("User-Agent", " Mozilla/5.0 (Linux; Android 8.1.0; Pixel Build/OPM4.171019.021.P1; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/103.0.5060.129 Mobile Safari/537.36 SP-engine/2.38.0 matrixstyle/0 lite baiduboxapp/5.34.0.10 (Baidu; P1 8.1.0) graphplugin/1.0.10")
                    .addHeader("Content-Type", " application/x-www-form-urlencoded")
                    .addHeader("Content-Length", " 111704")
                    .addHeader("Host", " graph.baidu.com")
                    .addHeader("Connection", " Keep-Alive")
                    .addHeader("Cookie", " matrixstyle=0; BAIDUID=E9549645F9FD4180E6DFBEA68E74DDFE:FG=1; BAIDUCUID=" + Buid + "; SP_FW_VER=0.0.0; SG_FW_VER=0.0.0; BCLID=7187975593181227898; antispam_key_id=23; ab_sr=1.0.1_MWZlMDRlY2FiMDMwY2M0MmVlYjVkN2QzZDg1NTNjYjUwZTZjOWU4NWI1YWMzMmJhZTQ1N2YwNTAwODcxOTRkODVhMWY2MmU4MGYxNDI2MmI5ZmI0NTZhZWZkZmM5MzUxZDc5ZTY1NTA5NDc2MjZjZWM5YzMxOGM0NjdlYTJiYzE3MzZkOWNkNmM3OWRkNmU1NjRkM2MxNDJiYjA0NDM2OA==; antispam_data=992cb7bf02b1cfb4c53073864b0b5f9773d462276c2228a5b8a76853df2a6d64db8cde07334b693228dfe938e3f1ad873a1543438166112b47430f426b709e75a86570e53d02aa2f43fa534dbc88a2b7; BAIDUID=56FFAF86131C49096CC72000E595DC93:FG=1; BIDUPSID=56FFAF86131C49096CC72000E595DC93; PSTM=1655803098; SAMPLE_SIDS=1038011")
                    .build();
            Response response = client.newCall(request).execute();
            responseData = response.body().string();
            if (responseData == "") {
                Buid = getRanInArr(BuidArrays);
                responseData = "";
            }
        } catch (Exception e) {
            System.err.println("访问错误" + e.toString());
        }

        return responseData;

    }

    public static class TrustAllManagerBxl implements X509TrustManager {

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
        }

    }


    /**
     * 初始化改变全局变量 BAIDUCUID 该参数锁定机型
     */
    public static void BeginUid() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
//                System.out.println("当前线程：" + Thread.currentThread().getName() + " 当前时间" + LocalDateTime.now());
//                Buid = randomUID(27);
                // 清除当前uid元素
                Buid = commentUID();
                Bua = randomUa(35);
                System.err.println("BAIDUUid 发生改变 : " + Buid + "\t\n Bua ： " + Bua);
            }
        };
        // 在指定延迟0毫秒后开始，随后地执行以2000毫秒间隔执行timerTask
        new Timer().schedule(timerTask, 0L, 240000L);
        System.out.println("当前线程：" + Thread.currentThread().getName() + " 当前时间" + LocalDateTime.now());
    }


    public static void invokeUid() {
        Buid = commentUID();
        Bua = randomUa(35);
        errorCount = 0;
        System.err.println("BAIDUUid 发生改变 : " + Buid + "\t\n Bua ： " + Bua);
    }

    private static String commentUID() {
        return getRanInArr(BuidArrays);
    }

    private static String randomUa(int len) {
        Random random1 = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(str.charAt(random1.nextInt(str.length())));
        }
        // _a-qiyaO2ijBNE6lI5me6NNJ2I_cPX8WoavjhSGHZqqqB
        return "_a-qiyaO" + sb.toString() + "qB";
    }


    /**
     * 数组随机取值
     *
     * @param array 传入字符串数组对象
     * @return
     */
    public static String getRanInArr(String[] array) {
        int length = array.length;
        int index = (int) (Math.random() * length);
        return array[index];
    }


    public static JSONObject createJSONObject() {
        // new LinkedHashMap 可以按照写入顺序插入
        JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
//        String Image = ToBase64(path);
        jsonObject.put("imgkey", generateImageKey(img_base64));
        jsonObject.put("image", img_base64);
        jsonObject.put("type", "QUESTION");
        jsonObject.put("session", "");
        jsonObject.put("scaleSize", 900);
        jsonObject.put("scaleQuality", 80);
        jsonObject.put("langChangeTag", 0);
        jsonObject.put("tokenTime", "0_0");
        jsonObject.put("uploadOriImageWithSelectingLocation", 1);
        jsonObject.put("role", "0");
        jsonObject.put("half_model", 1);
        return jsonObject;
    }


    /**
     * 调用答案接口
     *
     * @param sign
     * @return html
     * @throws IOException
     */
    public static String set_Sign(String sign) throws Exception {

        if ("".equals(sign) || sign == null) {
            System.out.println("Sign 获取失败");
            return null;
        }
        String ApiUrl = "https://graph.baidu.com/s?f=question&sign=" + sign + "&question_verify=1&ua=" + Bua + "&osbranch=a2&pkgname=com.baidu.searchbox.lite&image_source=TOP_GALLERY&cam_pos=BACK";
//        System.out.println("questions ： " + ApiUrl);
        logMap.put("questions", ApiUrl);

        HashMap<String, String> map = new HashMap();
        map.put("Host", "graph.baidu.com");
        map.put("Upgrade-Insecure-Requests", "1");
        map.put("User-Agent", "Mozilla/5.0 (Linux; Android 8.1.0; Pixel Build/OPM4.171019.021.P1; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/76.0.3809.89 Mobile Safari/537.36 T7/13.6 SP-engine/2.38.0 matrixstyle/0 lite baiduboxapp/5.28.0.10 (Baidu; P1 8.1.0) imagesearch/1.0 main/1.0 graphplugin/1.0.10");
        map.put("Sec-Fetch-Mode", "navigate");
        map.put("Sec-Fetch-User", "?1");
        map.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        map.put("X-Requested-With", "com.baidu.searchbox.lite");
        map.put("Sec-Fetch-Site", "none");
        map.put("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
        String response = null;
        try {
            response = httpClientUtil.executeGet(ApiUrl, map);

            if (response.contains("未识别到题目") || response.contains("未找到相关结果")) {
                response = "";
                // すごすて しりた 非法标识 日语 语义倒叙
            } else if (searchCharacter(response)) {
                System.err.println("出现非法标识！！！！！！ UID为 ： " + Buid + " \t ImageId为 ： " + imageMap.get("imageId"));
                Buid = getRanInArr(BuidArrays);
                response = "";
            }
//            System.out.println(response);
        } catch (Exception e) {
            System.err.println("解题异常 : " + e.toString());

        }

        return response;
    }


    /**
     * 删除数组指定元素
     *
     * @param str    指定元素 字符串
     * @param strArr 指定数组 对象
     */
//    public static void removeArr(String str, ArrayList<String> strArr) {
//        strArr.remove(str);
//        String[] strings = null;
//        strings = strArr.toArray(new String[strArr.size()]);
//        BuidArrays = strings;
//        System.err.println("当前可用uid个数 ： " + BuidArrays.length);
//        strArr.clear();
//        Buid = commentUID();
//        if (strArr.size() == 1) {
//            System.exit(0);
//        }
//    }
    public static void removeArr(String str, ArrayList<String> strArr) {
        Buid = commentUID();
        if (strArr.size() == 1) {
            System.exit(0);
        }
    }


    public synchronized static void putLog(String html) throws Exception {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        if (html != null && !"".equals(html)) {
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)

                    .addFormDataPart("ptype", "bond_baidu")
//                    .addFormDataPart("version", "5.28.0.10")
                    .addFormDataPart("version", "5.32.0.10-bxl")
                    .addFormDataPart("cuid", Buid)
                    .addFormDataPart("solarid", imageMap.get("solarId"))
                    .addFormDataPart("proxyip", "tps267.kdlapi.com")
                    .addFormDataPart("imageid", imageMap.get("imageId"))
//                    .addFormDataPart("questions", "https://graph.baidu.com/s?f=question&sign=021a1c49028d9b008643011657294726&is_halfwap=1&question_verify=1&ua=_a2Ii4aO2ig4NEq4I5me6NNJ2I_7aXiWoavjhQmHNqqqB&osbranch=a2&pkgname=com.baidu.searchbox.lite")
                    .addFormDataPart("questions", logMap.get("questions"))
                    .addFormDataPart("html", html)
                    .addFormDataPart("status", "100")
                    .addFormDataPart("remark", "HZA的一个测试")
                    .build();
            Request request = new Request.Builder()
                    .url("http://sdc.zhenguanyu.com/papi?r=log/collect")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            Response response = client.newCall(request).execute();

            System.err.println("写入日志成功");
//            toMiao();
            response.close();
        } else {
            System.err.println("答案未识别或为空不录入日志");
        }
    }


    public static void antiSpam(String antiSign) {
        if (antiSign == null || "".equals("")) {
            return;
        }
        String url = "https://graph.baidu.com/api/antispam?sign=" + antiSign + "&srcp=home_icon&tn=bdbox_half&idctag=tc&sids=10006_10521_10966_10974_11031_17850_17071_18100_17201_17202_18312_19199_19162_19215_19268_19280_19670_19807_20005_20011_20051_20060_20070_20090_20134_20144_20162_20172_20180_20192_20201_20236&gsid=1015_1021_1040_1060_1080_1100&logid=3233542486&entrance=question&pageFrom=bdquestion";

        Map<String, String> antiMap = new HashMap<String, String>();

        antiMap.put("Connection", " keep-alive");
        antiMap.put("Content-Length", " 216");
        antiMap.put("Sec-Fetch-Mode", " cors");
        antiMap.put("User-Agent", "Mozilla/5.0 (Linux; Android 8.1.0; Pixel Build/OPM4.171019.021.P1; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/76.0.3809.89 Mobile Safari/537.36 T7/13.6 SP-engine/2.38.0 matrixstyle/0 lite baiduboxapp/5.28.0.10 (Baidu; P1 8.1.0) imagesearch/1.0 main/1.0 graphplugin/1.0.10");
        antiMap.put("Content-Type", " application/x-www-form-urlencoded");
        antiMap.put("Accept", " */*");
        antiMap.put("Origin", " https://graph.baidu.com");
        antiMap.put("X-Requested-With", " com.baidu.searchbox.lite");
        antiMap.put("Sec-Fetch-Site", " same-origin");
        antiMap.put("Referer", logMap.get("questions"));
        antiMap.put("Accept-Language", " zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
        antiMap.put("Cookie", " BAIDUCUID=" + Buid + "; matrixstyle=0; SP_FW_VER=0.0.0; SG_FW_VER=0.0.0; fontsize=1.0; iadlist=1073741825; BAIDUID=2445EF4E74E0EC302FBC02381D661F0C:FG=1; GCONF_PARAMS=NNw1ojug2kfUNwokYavKf_RJmC914-iPgO2j9NIyxofhO2Ir_PXl8oaS29N-Ixt7_RAUCfJ_AjjAaviggN2HiguYL8_Cavhl_a2li4aq2fg4Nv8qALqqC; ZFY=:Bd:AdgGKZzQYSb5VkZPwhehrx1ago8RvjT2cvIOUsIXY:C; BCLID_BFESS=8552101720281848991; BDSFRCVID_BFESS=LK4OJeCinG_7v5QDvd9oqa0uug50H1TTH60oINFlNC2iWvSnTpqPEG0PyU8g0KubwwAdogKK0mOTHUAF_2uxOjjg8UtVJeC6EG0Ptf8g0f5; H_BDCLCKID_SF_BFESS=Jn4O_CDXJIL3qJTph47hqR-8MxrK2JT3KC_X3b7Efb4-fq7_bfbEhRLDXPJxQx3q5nKjQRDMb45JOUP9Ml5xy55QhprphtnttgT8KUbjtComKJrHQT3m0qQbbN3i-xriMgoZWb3cWKJV8UbS3fQPBTD02-nBat-OQ6npaJ5nJq5nhMJmb67JD-50eG-jtjLOtb4tVbjeK6rDHJTg5DTjhPrMX-4JWMT-0K5XKMA5bD5Gs-Qgetna-j805fttJUkDQNn7_DJOWlrPDhrJD4rR5ltH3h3DQUQxtNR8-CnjtpvhH436LtOobUPUDMc9LUvP0mcdot5yBbc8eIna5hjkbfJBQttjQn3hfIkj0DKLK-oj-D8RD58K3j; antispam_key_id=23; BDUSS=RWQnB0aERUMVVQZU5OS01DTktWWjNxTXNKRFQ3c0lWQ2hIdDBoRE9JVGlTdk5pRVFBQUFBJCQAAAAAAAAAAAEAAACqpEazvdSzvl8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAOK9y2LivctiTz; BDUSS_BFESS=RWQnB0aERUMVVQZU5OS01DTktWWjNxTXNKRFQ3c0lWQ2hIdDBoRE9JVGlTdk5pRVFBQUFBJCQAAAAAAAAAAAEAAACqpEazvdSzvl8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAOK9y2LivctiTz; STOKEN=e5f2ee2cc5e9460a56c9acdc0364b2b62544afaf6bbf7cdc8be9dbb3ad7c9782; WISE_HIS_PM=0; BAIDULOC_BFESS=12966687_4837864_0_131_1657527265658; BAIDULOC=12966688_4837865_0_131_1657536849178; BAIDULOCNEW=12966688_4837865_0_131_1657536849189_0; ab_sr=1.0.1_MjExNmUyZTUxNzBhZWMxZTMyYjY5Zjg4YWUxMWRmMjJkNjJlNWJlMWIzMmQxZTdhOGZmYzVhY2QwZDYwZjQxYjI0MWNkYjFiMmY2YmFlNzM5N2Q3OTE1ODMyOTNhZWVmYmEwMWY2ZGI2ZjkxMDA3MzU2N2UwNmY4YTI2ZGYxNDliNGY5MzNkMWE2MmQ2Nzk3NzI4MGI3YWU4MDAxMWE5YQ==; antispam_data=992cb7bf02b1cfb4c53073864b0b5f9773d462276c2228a5b8a76853df2a6d64db8cde07334b693228dfe938e3f1ad875929accb001f918f08eb9c8fed2ecedc9bd84fe9c9b98dfddb196a2f87cd58e8; BAIDUID=56FFAF86131C49096CC72000E595DC93:FG=1; BIDUPSID=56FFAF86131C49096CC72000E595DC93; PSTM=1655803098");

        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("responseData", "{\"data\":\"992cb7bf02b1cfb4c53073864b0b5f9773d462276c2228a5b8a76853df2a6d64db8cde07334b693228dfe938e3f1ad878ffc8928dd6af530458d2739dcc2e5b3cabc1486f356ce43236473ca82d8bf58\",\"key_id\":\"23\",\"sign\":\"4a38b7be\"}"));

        try {
            postForm(url, list, antiMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析 json  获取 sign
     *
     * @param inter
     * @return
     */
    public static String analysisJson(String inter) throws Exception {
        String substring = null;

        if (inter != null && !inter.equals("")) {
            try {
                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .disableHtmlEscaping()
                        .create();
                SignJson signJson = gson.fromJson(inter, SignJson.class);
//        substring = thumbUrl.substring(39, thumbUrl.indexOf("&"));
                String s = gson.toJson(signJson.getData().getImgsearch().getQpsearch().getDataset().getCommand().getThumbUrl());
                substring = s.substring(s.indexOf("="), s.indexOf("&")).substring(1);
                System.err.println("Sign : " + substring);
                logMap.put("antiSign", substring);

            } catch (Exception e) {
                System.err.println("获取网络图片img错误 : " + e.toString());
                e.printStackTrace();
            }
        } else {
            return null;
        }

        return substring;
    }


    public static void toMiao() throws Exception {
        URL url = new URL("https://miao.baidu.com/abdr");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");

        httpConn.setRequestProperty("Host", "miao.baidu.com");
        httpConn.setRequestProperty("Sec-Fetch-Mode", "cors");
        httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 8.1.0; Pixel Build/OPM4.171019.021.P1; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/76.0.3809.89 Mobile Safari/537.36 T7/13.6 SP-engine/2.38.0 matrixstyle/0 lite baiduboxapp/5.28.0.10 (Baidu; P1 8.1.0) imagesearch/1.0 main/1.0 graphplugin/1.0.10");
        httpConn.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");
        httpConn.setRequestProperty("Accept", "*/*");
        httpConn.setRequestProperty("Origin", "https://graph.baidu.com");
        httpConn.setRequestProperty("X-Requested-With", "com.baidu.searchbox.lite");
        httpConn.setRequestProperty("Sec-Fetch-Site", "same-site");
        httpConn.setRequestProperty("Referer", "https://graph.baidu.com/s?f=question&tn=bdbox_half&sign=021cc72315d4700f7e06d11658217766&sids=10006_10521_10966_10974_11031_17850_17071_18100_17201_17202_18312_19199_19162_19215_19268_19280_19670_19807_20005_20011_20051_20060_20070_20090_20134_20144_20162_20172_20180_20193_20236_20243&logid=4159914497&idctag=tc&pageFrom=bdquestion&is_halfwap=1&more_question=1&jsup=&srcp=home_icon&entrance=QUESTION&question_verify=1&skts=d1%3A15%2Cd2%3A716%2Cd3%3A4&webviewType=half&kts=de,b0:1658217767978,b1:,b2:961,b6:28&service=bdbox&uid=ga2etYiFvag_iS8Oga2zuluPH8giu28glu2yil8h2a8-9QP14iSMuSHoA&from=1023973h&ua=_a-qiyaO2ijBNE6lI5me6NNJ2I_cPX8WoavjhSGHNqqqB&ut=pJGgz9Rovh_cuDiEjNS-qkypmoq0C&osname=baiduboxapp&osbranch=a2&branchname=baiduboxlite&pkgname=com.baidu.searchbox.lite&cfrom=1023973h&ctv=2&cen=ua_uid_ut&typeid=0&c3_aid=A00-5GXVXBKFJ76Y7RANVGY3DELZHDWBTYSF-C4Y4RX5F&zid=ZRyIbZQFt4Cai2va-Wz1QjqY4KuufezvfXej-8tPLqKCYXaLlF4DlLg37Mgr5oCrFqnvrKrV9k_aBewm1F9RZ0g&matrixstyle=0&fv=13.6.0.10&ds_stc=0.4327&ds_lv=4&sdk_version=13.6.0&network=1_0&graphCode=22005&image_source=TOP_GALLERY&cam_pos=BACK");
        httpConn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
        httpConn.setRequestProperty("Cookie", "BAIDUCUID=" + Buid + "; matrixstyle=0; SP_FW_VER=0.0.0; SG_FW_VER=0.0.0; BAIDUID=E9F745BDE3F7A499CE1366146B273EAA:FG=1; iadlist=1073741825; WISE_HIS_PM=1; fontsize=1.0; GCONF_PARAMS=NNw1ojug2kfUNwokYavKf_RJmC914-iPgO2j9NIyxofhO2Ir_PXl8oaS29N-Ixt7_RAUCfJ_AjjAaviggN2HiguYL8_Cavhl_a2li4aq2fg4Nv8qALqqC; ab_jid=518a8b4bc5ad527f237b4b7f954e5cd8f5f1; ab_jid_BFESS=518a8b4bc5ad527f237b4b7f954e5cd8f5f1; BCLID_BFESS=8027336822214231452; BDSFRCVID_BFESS=JHtOJeCinRzRLnQDc4jnqa0uugKX8jRTH60oDm8B8IwCxF2dHd3wEG0PQf8g0KuML5yNogKK0mOTHUkF_2uxOjjg8UtVJeC6EG0Ptf8g0f5; H_BDCLCKID_SF_BFESS=tbKq_CIhJIP3ePov5J0_5PLqMfQXhtvKato2WbCQWqnM8pcNLPr_0RIwXMTI0PtJ2DoD3CJabR3voKnF0lO1j68eQMFO3jvAb27OKDoeWn-5fp5jDh3Gb6ksD-RtWxAtMmoy0hvctn6cShnaMUjrDRLbXU6BK5vPbNcZ0l8K3l02V-bIe-t2b6Qh-p52f6ttJbI83J; BCLID=6011757666125405382; BDSFRCVID=r2-OJeCinRF5IGrDcxR3qa0uukUxBScTH60oencBa_TQ2XSkni_xEG0Pyf8g0KAMoU4CogKK0mOTHUkF_2uxOjjg8UtVJeC6EG0Ptf8g0f5; H_BDCLCKID_SF=tbIHVIDXJCD3jb7GKP__htFJbfRBetJXf5uHsl7F5lOEDxoDjfrnKx0S2bKeb4_jb6TW-4nPLbOxOKLxhPo_-UD_BnOIJqvg2Ko0M-nN3KJmJJL9bT3vLfue3HOm2-biWbRL2MbdQRvP_IoG2Mn8M4bb3qOpBtQmJeTxoUJ25DnJhbLGe4bK-TrXDa-fqUK; BAIDULOCNEW=12958161_4825908_100000_131_1658217731137_1; SAMPLE_SIDS=; ab_bid=4bc5ad527f237b4b7f954e5cd8f5f2437c7c; ab_sr=1.0.1_ODRiNWFhMTY3MTFmMTQ2ZTk5ZWZiMzI3YTg1OTJmZmM2MmFmYmYxN2JiZGI3ZjQwYjIyMmM3NmJjZmQyNWQwZjVmMzkxZDFmZmQzOWU5YjI3MThlOGQ2ZGE5NzYxYjVjNDVlMjdkMWUxYmRhODViMTIzMzM5ZjExNDRjZjFhN2JlYTA0MjdjNWQ3Y2Q4ZTU2ZDYzYzU4ZjUxYjQ4MWFhOA==");

        httpConn.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
        writer.write("eyJkYXRhIjoiNTM2ZDJkNTMzY2ZmMjZhMDI5NzlhNTMzMWEyODZmNzFhYTZlYzJlY2E2YWEwMDI4MzdjODYxMDQwYzI2MmI0YzJlYzhmZWQ1ODI3ODBiYjZhOWMyZjRkMDllYmU2ZmU3MzRkNzAzODMyNzNjODFiZWJlZWUxZjc5ZTgzYjM3YjlmNTEzODUwYTU0NDE1ZTlhMzY4Mzk1NTJkMDc1NmUxODJmZTZhM2NlNDJhYzY4OWQ4OWYzYzNmYzdjYWU3ODFmNWY0OWM1ZjVkYWJkYjVkM2I5MzJjMDZiZmMyZmI4MDdiNjdkODI5OTYzMjE1YmQ0NjI5MjVkMjQ3ZjU3M2MyZmU1ZDg2ZWM4MTI3MDY1NDYzNGRkYzYwYjQ0OTFmMDRjMWY1MTg5MWJmMDIwZjk4ZWQ0YmQwN2M2NWY1MTkwZTU0NWJiODhhY2IyOTQ0NWNlNmY0MjA0MDMzN2I0OGI1MWEwMmU0NzdiZTEwZDhiZmM1Y2ExZjY2YTc5ODBkNTBhMTYyYzkxNjRiNjk2MTEzNWYxNTM5NDJiYTY1OGI0NDZkZmJlNDU5ODA1ODM3OWJmZjBhMDQwYTlhODk2ZGYwOTk4MWZhODBlNzIwMTU1ODMwNmYwOGY5ODk2NmMwYmM4OGMzZDc5Y2QwNzUyZDc3ZTg5YjVkNDUwYWY3NmEyODNmMzMzZGQ0Y2JmZjAyMTllYjg1ZGNmYmE0ODA2YzE2ZmQzOTAxZTVlYWI2MTJjMDIxZDNhMTIwNzZjMDg4MjZlNTBjOGIzMzczYmY5YjhhZjBjOGM5NjQ3ZjY1ZTZhODAzNzA5N2NkOGJmMTM5OWJhM2I4MThhYjdlMmZlNDI4MzNiNjMzMzhlMTgzMWFhYWZhMjAwZWI3OTZlMzZjMjkxYWUwMjk4MWJlYzM5NDFhZmJlNWI1MjFkYWQwN2M3ZGIzZTJhZjJmYmUwM2RlNWU4NmM5M2U0M2Y0YTQ4MjQ1NDJhZmFkNzk3NGZiOTEzOGVhMjlmMWViMzk2MjBhODVmNTE5ZTNlZGNlYzRmYjIyMGZjY2JlNDNlMDkyODUyYmNlMDU5ZTdlODgxMThjOGRjNDBjODgxYzkwMjk2NGQzNzUyMzZiNTg3ZjVkMDg4MWFkMzZhYTYwODliNzc0MDZkMDJlNDU1ZmI2MjczNDE3NGU2ZTllOWM0ZWYxZTc0Y2ExYzEwZDk5ZmNjN2Q0ZDA5ODBmMDExNGUyMDgyOTYzMTI2YjIwMzdkMmU4MjgyZDlhMDYyOGIyMjEwZjYyOTQ3ODQyMDBmNDU0YTFlNWMwZTA1N2YzOTc4ZDk3MzRjMjcxZWMxYjAwMTA4MjFlYjZlZDI3YzM5ZjU3ZDQ2ZThiYzQ4NjRhMzk2ZDliOGFhNjlhMjdhNDIwMDFmNzI4NDYxN2JmNGJiZTc4ZDAwNDI3OWM5ZWNlODczZDVkNzZiYmUxMzcwYzk3YjExZTAxMDVkMjU5Y2NjMzkxYWFlZWEyYTg3MjQ3ZjUzYWM5ZDZhYjQ3M2YxMDZjNjQwOGJiYmU4YjA5NGRmZTA3ODhjMGQ4ZTM1MDg4YzA1NWFlMDhiOWExYTNlNzlmZDczYTRiYTE5MWI2NWMzZGY0MGZjZmIwNzJiZDA4YmVmMWNiM2Y1M2M4ZjgwOTcwMWY0ZjM5MzA1MWQ3MWUwOTgzNWUzNDZjYTY4ODc4NDE3NGM3MzEyMmM0MDQ3OGYwMGU5NDkzZGFjNTZjOGMzMTY3NzMwMjY5N2MzNWFjYzM3ZjhiNGY1YTUxMmNmYjk4NzgxZWFhYTI4ZjI5ZDUxY2IwNTk1MzIwZTdmNzdlYjE3YzIzZDFlYjQyNDZlMDVlMjRlNmJhZTcwOTNiODk0NTg2ZmJkYTdkYWQ3MmM0YWQwOTIwODU3YzA5ZDQ5MWMzZmQ3ZTg0OGQxNjhjOWE5ZDZlNGFhNDY1MzE0YzJkZWUxZjMyNDllODdjOTZiNzZjMDk1NWQwOTcyMzMyZDcxMWM1ZjJkNzQxZTczNDhmN2M5NzlhN2JkZWU1ZjAxM2Q2NGVlNTY2NWU5MTA3MTU2ODA4YzE4ZWVmOGY4ZGM0MGY3NjI2ZTYwYjNlODdhMjUwN2YwZjVjNjFmYmI3MWZlNjg0NmM2M2U1YjA3OWRhNTQwNjczNjljZjRlMWQ0NGVhZWY2Yzc2ZDVhZmU4NWNiZjU1NTZhMjNmZDU2MjYxNWY0ZjUyZjE4NzNmOTAxNzhmMWNhOGM0Y2NmZWJjZjg3NmY3NjBhMzA3OWMxNGFmN2U4NzlkZWNhZmEzYWZhN2E1NjQ1NWFiZTAyZmZkY2Q0ZWMwOGFiYjg4NmFjYjUwZTkzOWUxODBiMjVkMzkyOTE0OTRkMTFjYzQxY2IzMDMyZDQ0ZmU3ZTVlM2M2MmY0Mjk3MjExOWU2NjMyOThkMGE1ZDUyYTQ1Y2MzOTUwNzA4ZTdlYmEwNThmZDYyY2NiZTQ3N2MwYWNlNjQzZWEzMDU3MjY5MmQzM2FiOTczOGFjODJiNmFjYTg1OTU4OTExZTJlYWM5ZTQ3NmUxZDU4OGNiOWM0NTFhNDAzNWY4NGRhYzU2ZGNlZDFiNjg4N2YyMjEyZGUwMTJiODcxOGQ5MDc1NjU1YTBlYWVmMjQ1YjJiODEwOWZmOWU3N2EyNWFhMTQyMGVmNThmM2Y0MDgyMTk5NDI4ZDMzZGNiZDcwMDZlYWNjNDFhMjVkMzJjM2YzN2IzNDdlOTdkY2VhZTdhM2Y5ZmZkYzM4ZjgyYTQ2MjdjNTEzZWQzZWI5ZjFmYzZiY2Q4Y2YyNjk4OGQ2ODQ4NDM5NmQ0ZDhlODQ3ZGEyNWM1ZjkxYjQ0ZjFmMWI0OTc4OTlhN2YzMjRmMjU3ZGMwNGI1NDU2NzBlNzQ1ZGE3OTM1NTAzOTM0ZTI5MWVkMDUwZDQ0YmNlZTA1MWJhZWJiZjllNjM5NGRhMmRmZjY5NDdjOGUzN2U5MWY0MDU1NGVkZThkN2Y1ZjllYTE1OGFhYTA0MGEwMDcwNzM3YzY5NzBhOTI1MzMyODU5MTQ3NzgyZTE4YTIyMjBlOWM4MDY5MTdhNTI4ZWQyNDE5ZDcxZWZjMWE0NWNkNGU3YTEyNWM3Yjk1MmZiOTBhMjg2NWFjOTMzMGU3YjIwYmEwNGJkZDE2MWJkZjk0ZmIyZGQ3NDhjOWY2ODMzN2I2Yzc1ZjJhZjZhODE3MDYyYTk3NWNlZWY2YTExNmUwNmViNjRjOWU3NTM0NGI4YzI1MzEzZGUzNzUyMjE0OWYzN2NiZWI2NmFlNjU1MzM0ZWZiYWEwYjFjMWY3OGI4M2E3OTIxZWRmNDJjODNjMzg4MzAyYWZlODJhOWQ4YWUxMzM2OTBhOWQ2ZTQyZGM1YTQzYmFlNmRmNzdkNGZkMjBiMjRjYjYwNTliNjViMGZjZThkYmM2ZjEwMGM0MTE0NjdiZjhmODA0NjE3YjIzMzBkYzUzZDRiZmY1ZjMxNWMyYTdiYjk2YmMzZjBmOGM2Njc3YTMzY2RkNDc1ZDc3ZTY1OTIwODI1MjcwN2EyNzNmMmNmMWJhN2YwMmRhMmRiMTkwNDdjOTAxNTgyYmU0ZWVhZmNlODkzMDQ2M2NkYzdmMGEyYmI2ZWI2ZTc2OWRkOGU3MzYwMzIzN2UzOWM3NzgwYWY2ZWYxNWYxMmIwOGQ5NzEwNDEzYjMyZTVlZGY1NWZjYTA3MDMyOGFmOTZkMDNiMTdhNTgyMGViZjQ5NDFhNjFiZWI0NjZlM2Q0NzlmODY0NzQwNDc5MzFkMmZkNDQ4NDE2OTY5MDZhYjYwZDQxOWIxOWQwMWEwMjE2NGUzM2M4YjgwYzUwOGQ5YTk1OTYyMTAzMDdkNzJkMDg3ZmEwMDhjNzM1YjEwMzRjYzBmZDBlOTNjMTg1NzI2ZWRkYjg1OTJjMDJhNTA5ZmE0NGM5NWU3MmNhZWMyMTA0Y2RlYWQxMjQ5ODg1NDI1Yjc4ZTk4ZDE5Y2VlNmExODk2ZmEwZjFjMWQxMWJiZDU5OGI1ZGFmYjY3NWFkOTY0YTc0ZTJlMDkwY2I4YmVjNmRmZGFhNmUwZTY5OGU0ZWE2ZDc4MjM2ZWRkODc2MmQxOTg5MWRjNTNlMWM4NGE5YzFjMGM5YTdkZmVmY2RjZDY5Y2VlMTU4NTk1YTdiMmFkNTVmNWVmOTU1MmIxZGU0NzQzMzk2ZTI4OTVjNDU4MWE2NzQzZjg3M2U0NzRjMDVlODRkMDdhMGFiNmVmYjAyMDQxMTRmNzg3MWY0MmVmYzAzYWY2OTc2M2NmYTQxNzBhZWVkNjNjZmU2NTc1ZjE1MzY2NzFjZDNkYzJkMTI3NzBmYzI4NzIzODIwMTdlYjNiMmZiNjE3ZWY4MzlkOTNhMjM0YjVkNTc0YzFmZTllNzhkNzgyNmE5MTk2NmJhMWZjZmVkZjYxMzI1YWE1MDExZGYxYzRkYTRiOTc0MTE4OGQxZThhOWU5MGYyY2VhNWViYTUxZjNiN2RlMTU0MjEzNDBmYzc2NGRmNzY5ZTk2YWQ3MTYwZjM2ZWE3ODQzM2JlMTIyZTg4MDlhYjIwOGFjNmEzMDc5ZWQ0ZDlhNDdkY2M2NmYzNTEwMGY1NzBiYTM0MzA5ZGJkNjAxMjVjMDQ3MDk4OTZkZDhjYjFjOWM3OGFlMWZkNjgzZjUyYTlkMjhlM2JlN2EwODVhNDA2YmNiYTZkMjgwMGE5Y2NkN2IwZjk1N2JhZDVhOTRhOGRhNWU0NmE4Y2RmYjRhMjY2YjI3YjkzYjYwOTVjODg4YWQ0NDQ3MTI0Y2MyODZmM2I5N2QwMDc4YWViODk1NjBiYzUwYmU1N2FjN2ZjOTZlMzdhMTc5MzgxNWU2ZjFiODQ1NzZkZDM1ZGUxNGFjMjVlZGJmOGU5YTE3ZDViN2QwYTY2YmMwNTg3ODhhY2FlNTI3ZDY5YzE1MjkyZTE0NTM1NDNiYzBlZGJkNTdjYWZiYjMwYTYxNzUzYTM5N2FmNTZjNmYxMzQyMWRmN2VlNzU4NmRjMTZjYmFiMGU4NzBjMGJiZjE4ZDc5NjBmOTdkODk5OWU5MTExZTUzYWNmYzY0NWQzYTE2MGU5MWQ4NDkzOWUzOGRiMzMzYjhhNjEwMjk1NDIyYjdjMDRlNWE4NTU2OTVlOGU2NjZjMzYyYmIzNjlkNTBmZmZkNjM0ZDk5ZTVlOGNmZGJmNWZlYTg4NjhiOWQ3NjkyMThlNTI0NjUzOTgwNDhmMTRkZDJjOTQ5NDA1ODkwMzExYTE3ZDg2OWRlMDI0NzExMjZkZjM1NDI2MzVlMzQwOGE1OGZiODllNThiOTY2MzNhZjI2MDZkYmZiMzNhMGQxZDkyZmFhM2MwYWUzMGFiYjQxMmVkMWVkZjAwMzZhMTllOGRjODU4OTNkYzViOTI0YjBkMzI5NTRjNDk2MmNjZDBiZjEyNjk3ZTliYWY4ODBlNWM2Y2ViMTQzMjQxMzI1N2RhMDVlY2QxZTA2MDg3YzIyYzFmYzU1MGY5MjY0NTA5NGNjNGFlYzA3ZjVkZWQxNDNlM2VjNDhjMWY0NWNkNzY2NWMzMTVkOGNhNmZmMGFiM2MwYTQ3M2NlYmJkMDQ2ODhjN2U1MmUyYmI0ZjMwMDI1NGI0NzU4YWMzYzFlNDIwYjU2N2Q2MWNiNWNlOTViYWRmMDVmOGMwNzU2Y2JlOWNkYzk2NmM4Mjg3YjIwY2MyMDk5ODkyOTFiMTBjOTBjODUzNTE5MzQ2MDg3YWM5ZjNhY2M2NzI4NTE1YjhmMmZjMjY3MzcxOWM3NTY1MWViMzRhODQ5OTgzYWZkZmZjNDhiOWNjYjBiN2MzOGUzOGJmZWFjMzhmMTgzYTBmZmE4YzE2MzZmZDg4ZTg0OTZmZTU1NjViM2MwODA2MGJlMTg5OWI3NDljMDgxZDIwZTljNzI4OTkwM2ZmZjFiOTIwYTZiYjdkMjdlMTk1YjRjNmI3NDljMWJjNGY1ZWFjM2VhOWU5MzVmMzBkOWQ2ODRkNjAxNzk1YjllZTcxNjY5YzM1Y2I1OTA2NWQ4NDQ0YTJmZGIzODlkY2FjOWRhYmQxZWJlZDJjYjZiZmM0MTRhYjA0YjljMzA4NTNlNjliMDNkZmY3OGY3MzM1Y2Y5ZDQ0ZDM3MGU3YzE0YjI5NWQ1M2ZmOTQxOTVhMTFmN2ZmOGJhMTJhNjc5YjdjMjQzZDI1ZmNlZDI5YjlmMDk0YzI0ZTVmZGYyMDc1MGNiMTU0ZTM3YzZlODBmZTBmYzAyODVjYWNiMjE3ZjdkNjhkZDQzZjhlODFiZGExY2JjMTY1NDg4ODkxYWVhN2U5MzVkNmQyNjUzMGY1MTM0ZTQxYWE3N2YwODMyNGEzYTZmMzdkMzlkZTMyZDZiNGE4MDlmZWU1YzcwZjgzZDUwN2IxODEwYzdiMzE5Yzk2ODBmOGM0MGYwZjljODZhN2JkNmNmZTMxZTI4MThjYmIiLCJrZXlfaWQiOiJkN2EwZDZjMGZjZjA0YWY5In0=");
        writer.flush();
        writer.close();
        httpConn.getOutputStream().close();

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
//        System.out.println(response);
    }


    public static void zyb() throws Exception {
        String zybUrl = "https://graph.baidu.com/api/zyb/message?service=bdbox&uid=" + Buid + "&from=1023973h&ua=_a-qi4ujvfg4NE6pI5me6NNJ2I_cPX8WoavjhSQHNqqqB&ut=pJGgzkIl-I_UhvClguL-akpBEqorA&osname=baiduboxapp&osbranch=a2&branchname=baiduboxlite&pkgname=com.baidu.searchbox.lite&cfrom=1023973h&ctv=2&cen=ua_uid_ut&typeid=0&c3_aid=A00-5XSVZHZKS4FKVFOJK6JRJOHY7C6AT27R-FIJHVAF5&zid=WXTB83SEHptFQi_SaAw126vekWeK6n9ztliCB37JHrubUPOF_4gHdMZvU9ldBdcnHJEdKjRtay5aoyr8X4jN0Tw&matrixstyle=0&fv=13.6.0.10&ds_stc=0.3919&ds_lv=4&sdk_version=13.6.0&network=1_0&graphCode=22005&plugin_version=com.baidu.searchbox.godeye&enter_search_time=1659089098860&entrance=GENERAL&srcp=home_icon&cam_pos=BACK";
        HashMap<String, String> map = new HashMap();
        map.put("user-agent", "Mozilla/5.0 (Linux; Android 8.1.0; Pixel Build/OPM4.171019.021.P1; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/103.0.5060.129 Mobile Safari/537.36 SP-engine/2.38.0 matrixstyle/0 lite baiduboxapp/5.28.0.10 (Baidu; P1 8.1.0) graphplugin/1.0.10");
        map.put("x-bd-traceid", "4a85a91a70a64547860dd18ac10708ce");
        map.put("Host", "graph.baidu.com");
        map.put("Cookie", "matrixstyle=0; BAIDUID=9A694BAEF03E0321ECD8DE0CAA6354EA:FG=1; BAIDUCUID=" + Buid + "; SP_FW_VER=0.0.0; SG_FW_VER=0.0.0; MBD_AT=1659089050; WISE_HIS_PM=1; fontsize=1.0; BAIDULOCNEW=12958161_4825908_100000_131_1659089077322_1; iadlist=1073741825");

        fromPost(zybUrl, map);
    }

    public static void vsapi() throws Exception {
        URL url = new URL("https://graph.baidu.com/vsapi/partnerconf?service=bdbox&uid=" + Buid + "&from=1023973h&ua=_a-qi4ujvfg4NE6pI5me6NNJ2I_cPX8WoavjhSQHNqqqB&ut=pJGgzkIl-I_UhvClguL-akpBEqorA&osname=baiduboxapp&osbranch=a2&branchname=baiduboxlite&pkgname=com.baidu.searchbox.lite&cfrom=1023973h&ctv=2&cen=ua_uid_ut&typeid=0&c3_aid=A00-5XSVZHZKS4FKVFOJK6JRJOHY7C6AT27R-FIJHVAF5&zid=WXTB83SEHptFQi_SaAw126vekWeK6n9ztliCB37JHrubUPOF_4gHdMZvU9ldBdcnHJEdKjRtay5aoyr8X4jN0Tw&matrixstyle=0&fv=13.6.0.10&ds_stc=0.3919&ds_lv=4&sdk_version=13.6.0&network=1_0&graphCode=22005&plugin_version=com.baidu.searchbox.godeye&enter_search_time=1659089550660&entrance=QUESTION&image_source=TAKE_PHOTO&srcp=home_icon&cam_pos=BACK&gsid=1015_1021_1040_1060_1080_1100");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");

        httpConn.setRequestProperty("user-agent", "Mozilla/5.0 (Linux; Android 8.1.0; Pixel Build/OPM4.171019.021.P1; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/103.0.5060.129 Mobile Safari/537.36 SP-engine/2.38.0 matrixstyle/0 lite baiduboxapp/5.28.0.10 (Baidu; P1 8.1.0) graphplugin/1.0.10");
        httpConn.setRequestProperty("x-bd-traceid", "49dfda9895d2444c995543e1f1f3e986");
        httpConn.setRequestProperty("Host", "graph.baidu.com");
        httpConn.setRequestProperty("Cookie", "matrixstyle=0; BAIDUID=9A694BAEF03E0321ECD8DE0CAA6354EA:FG=1; BAIDUCUID=" + Buid + "; SP_FW_VER=0.0.0; SG_FW_VER=0.0.0; MBD_AT=1659089050; WISE_HIS_PM=1; fontsize=1.0; iadlist=1073741825; SAMPLE_SIDS=1038011; GCONF_PARAMS=NNw1ojug2kfUNwokYavKf_RJmC914-iPgO2j9NIyxofhO2Ir_PXl8oaS29N-Ixt7_RAUCfJ_AjjAaviggN2HiguYL8_Cavhl_a2li4aq2fg4Nv8qALqqC; ab_sr=1.0.1_YWMyNjFmYzhhOTliOTQ2Nzk0ZGE5ZmJlNjA1MjM1OGRlYTUxYTc3NTEwMzg2ZjU4MTM1ZWY2ZDc0MDAyNDAxMzAxZDVlZGNlZjU3YjA3ZjY0YjYxZTAzYjg5MmE0ODcyYjYyY2ZhMjlkYTJiZWNkMGJkYmJmYTU5NzkyOTVlZjFmODc1N2FiOTRkMTE3NDJlYjQ1ZjI4NTkyZGM3ZTMzZg==; antispam_data=992cb7bf02b1cfb4c53073864b0b5f9773d462276c2228a5b8a76853df2a6d64db8cde07334b693228dfe938e3f1ad87d5d52b864080f0ddd05d20328440754e80d7f227e77c8cac25735213fc84607a; antispam_key_id=23; BAIDULOCNEW=12958161_4825908_100000_131_1659089388964_1; antispam_data=992cb7bf02b1cfb4c53073864b0b5f9773d462276c2228a5b8a76853df2a6d64db8cde07334b693228dfe938e3f1ad87d5d52b864080f0ddd05d20328440754e80d7f227e77c8cac25735213fc84607a; antispam_key_id=23");
        httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        httpConn.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
        writer.write("=&role_id=0&version=");
        writer.flush();
        writer.close();
        httpConn.getOutputStream().close();

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
        System.out.println(response);
    }


/*    public static void vsapi() throws Exception {
        String vsapiUrl = "https://graph.baidu.com/vsapi/partnerconf?service=bdbox&uid=" + Buid + "&from=1023973h&ua=_a-qi4ujvfg4NE6pI5me6NNJ2I_cPX8WoavjhSQHNqqqB&ut=pJGgzkIl-I_UhvClguL-akpBEqorA&osname=baiduboxapp&osbranch=a2&branchname=baiduboxlite&pkgname=com.baidu.searchbox.lite&cfrom=1023973h&ctv=2&cen=ua_uid_ut&typeid=0&puid=_avjiguKviGedqqqB&c3_aid=A00-5XSVZHZKS4FKVFOJK6JRJOHY7C6AT27R-FIJHVAF5&zid=OdSB0bpC4W1iTxVLeY01_sI40E0pl9bdCDi57e5niN7pwfIPBQhY56qGwIsI2wZeTxRjYzS6GdQqgik6fxceacQ&matrixstyle=0&fv=13.6.0.10&ds_stc=0.3919&ds_lv=4&sdk_version=13.6.0&network=1_0&graphCode=22005&plugin_version=com.baidu.searchbox.godeye&enter_search_time=1659078730440&private_mode=1&entrance=QUESTION&image_source=TOP_GALLERY&srcp=home_icon&cam_pos=BACK&gsid=1015_1021_1040_1060_1080_1100";

        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("role_id", "0"));
        list.add(new BasicNameValuePair("version", ""));

        HashMap<String, String> map = new HashMap();
        map.put("Host", "graph.baidu.com");
        map.put("Cache-Control", "max-age=0");
        map.put("User-Agent", "Mozilla/5.0 (Linux; Android 4.4.2; Nexus 5 Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36 SP-engine/2.38.0 matrixstyle/0 lite baiduboxapp/5.28.0.10 (Baidu; P1 8.1.0) imagesearch/1.0 main/1.0 graphplugin/1.0.10");
        map.put("Cookie", "BAIDUCUID=" + Buid + "; matrixstyle=0; BAIDUID=B3934752DEE139E8294BEA981A2565A5:FG=1; STOKEN=5123645df054295111b215d46242aff0834bfb5e24b3c4aa25bcb015e5b4a387; BDUSS=W1SYTZNVTZyNWRaTFpjV2JHN2pPdHl4SjA5ZS15dUlRVkdMYTdGaWkwem9GZlppSVFBQUFBJCQAAAAAAAAAAAEAAACqpEazvdSzvl8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAOiIzmLoiM5ib; iadlist=1073741825; BDUSS_BFESS=W1SYTZNVTZyNWRaTFpjV2JHN2pPdHl4SjA5ZS15dUlRVkdMYTdGaWkwem9GZlppSVFBQUFBJCQAAAAAAAAAAAEAAACqpEazvdSzvl8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAOiIzmLoiM5ib; H_WISE_SIDS=110086_179347_185637_188749_196428_197711_199576_204314_204907_207698_208721_209204_209240_209568_210296_210312_210316_210321_210328_210341_212296_212797_213033_213355_214188_214190_214244_214639_214802_215127_215730_215892_215958_216047_216207_216327_216517_216614_216848_216860_216883_217029_217167_217185_217392_217439_217583_218390_218445_218454_218511_218538_218539_218548_218598_218620_218636_218652_218744_218797_218859_218952_218959_219028_219138_219161_219245_219249_219329_219363_219381_219447_219450_219510_219592_219712_219714_219727_219734_219737_219817_219821_219836_219936_219943_219946_220009_220034_220068_220071_220279_220340_220395_220483_220901_220976_221019_8000094_8000101_8000123_8000135_8000143_8000156_8000162_8000171_8000177_8000182_8000187_1431719; SP_FW_VER=0.0.0; SG_FW_VER=0.0.0; WISE_HIS_PM=0; fontsize=1.0; H_WISE_SIDS_BFESS=110086_179347_185637_188749_196428_197711_199576_204314_204907_207698_208721_209204_209240_209568_210296_210312_210316_210321_210328_210341_212296_212797_213033_213355_214188_214190_214244_214639_214802_215127_215730_215892_215958_216047_216207_216327_216517_216614_216848_216860_216883_217029_217167_217185_217392_217439_217583_218390_218445_218454_218511_218538_218539_218548_218598_218620_218636_218652_218744_218797_218859_218952_218959_219028_219138_219161_219245_219249_219329_219363_219381_219447_219450_219510_219592_219712_219714_219727_219734_219737_219817_219821_219836_219936_219943_219946_220009_220034_220068_220071_220279_220340_220395_220483_220901_220976_221019_8000094_8000101_8000123_8000135_8000143_8000156_8000162_8000171_8000177_8000182_8000187_1431719; GCONF_PARAMS=NNw1ojug2kfUNwokYavKf_RJmC914-iPgO2j9NIyxofhO2Ir_PXl8oaS29N-Ixt7_RAUCfJ_AjjAaviggN2HiguYL8_Cavhl_a2li4aq2fg4Nv8qALqqC; antispam_key_id=23; ZFY=pyCccYiaifFNn0BB7fRkVHgUEp5a:ARx:BD6UeO7dZ3Ts:C; SAMPLE_SIDS=1038011; BCLID_BFESS=8388326783421779158; BDSFRCVID_BFESS=DQkOJeCinwo6A-cDIndXqa0uumKK0R6TH60odmMx2b68QjyIYm_nEG0PXf8g0KubuQX3ogKK0mOTHUkF_2uxOjjg8UtVJeC6EG0Ptf8g0f5; H_BDCLCKID_SF_BFESS=tbCeoCI5fCP3HJTRMJOqqR-8MxrK2JT3KC_X3b7EfhbGjp7_bfbEhR-ej4LL5M3AaDnvQRDMb45VOpkx06jxy55QhUOLWRkHKJFObRcXWxTV_nrHQT3mW-5bbN3i-4jtLG-eWb3cWKJV8UbS3tRPBTD02-nBat-OQ6npaJ5nJq5nhMJmb67JD-50exbH55uetnut_UK; BAIDULOC_BFESS=12966695_4837871_0_131_1659065612206; BAIDULOC=12966693_4837869_0_131_1659066556965; BAIDULOCNEW=12958161_4825908_100000_131_1659078505197_1; ab_sr=1.0.1_NDgyNjc5Y2M1NTBiMDE3YTEzMzQ0NjVjNDhhODU5MjZjYmU0ZmFkN2Y4MmU2MDlkZTM1MzJlODJmMGIwN2IwNWRjOTg3ZWYwZTE4NzVjNjhiNDlmNjY4ZTkxMzJiYTNmMTY4NTYyMzE0NDFjZjU5OWEwNDA1ZDMwOTFlNDJhMTI2OTk5ZmI1YmE5YjU2ZDRlNDM1NjBiYTI1Yjg2OWVkYw==; antispam_data=992cb7bf02b1cfb4c53073864b0b5f9773d462276c2228a5b8a76853df2a6d64db8cde07334b693228dfe938e3f1ad875e7dd9592cbb6e970dbd53a7d3eb7396404951e512aa6e40441846028dde7dba");
        map.put("X-BD-TraceId", "31250b1f146b4574b0eb688d4a46596d");
        map.put("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
        map.put("Content-Type", "application/x-www-form-urlencoded");

        postForm(vsapiUrl, list, map);

    }*/

    public static void bug() throws Exception {
        zyb();
//        vsapi();
    }


    public static void HzaRun() throws Exception {
//        Test01();
//        getImgUrl();
        int i = 0;
        while (i < 100) {
            // 每次拍搜都更换cuid
//            Buid = getRanInArr(BuidArrays);
            Thread.sleep(5000);
//            Thread.sleep(2000);
            // 刷新网络图片ß
//            postToSign();
//            putLog(set_Sign(analysisJson(postToSign())));
            String s = analysisJson(postToSign());
//            set_Sign(analysisJson(postToSign()));
/*            i++;
            if (i > 60) {
                bug();
                i = 0;
            }*/
//            set_Sign(analysisJson(postToSign()));
//            putLog(set_Sign(analysisJson(postToSign())));
        }

    }


    public static void main(String[] args) throws Exception {
        Buid = getRanInArr(BuidArrays);
//        bug();
        HzaRun();
    }


    /*public static void main(String[] args) throws Exception {

        X509TrustManager trustManager = new TrustAllManagerBxl();
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{trustManager}, null);
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        while (true) {
            imgInfo();
            StringBuilder builder = new StringBuilder();
            // fromImg 为 post 传递的 body参数
            System.err.println("Buid ===" + Buid);
            String imgBody = createJSONObject().toString();
            System.out.println(imgBody);
            String fromImg = fromImg(imgBody);
            builder.append("https://graph.baidu.com/api/bdbox?path=bdboxn/2/qptranslate_sole&service=bdbox&uid=g5x4ofJS26tnrm6uI0xOof5gFjIWpwjm8pA56k4GE68LkSP_rfWmuz1mA&from=1023973q&ua=_a-qi4ujvfg4NE6pI5me6NNJ2I_naXiWoavjhSQHNqqqB&ut=pJGgzkIl-I_UhvClguL-akpBEqorA&osname=baiduboxapp&osbranch=a2&branchname=baiduboxlite&pkgname=com.baidu.searchbox.lite&cfrom=1023973q&ctv=2&cen=ua_uid_ut&typeid=0&c3_aid=A00-5XSVZHZKS4FKVFOJK6JRJOHY7C6AT27R-FIJHVAF5&zid=-9F5EMDUnxooe_Ek9Ze1znLO4gh1PQzUJmoTGCO0kAeDJD5lSu4Z8-avpnr9ABcsn_xykyweNFfOYHGXTmOIV9A&matrixstyle=0&fv=13.11.0.11&ds_stc=0.3919&ds_lv=4&sdk_version=13.6.0&network=1_0&graphCode=22006&plugin_version=com.baidu.searchbox.godeye&enter_search_time=1659335236312&entrance=QUESTION&image_source=TOP_GALLERY&srcp=home_icon&cam_pos=BACK&gsid=1015_1021_1040_1060_1080_1100&question_verify=0&kts=de,b0:1659335248848,b1:245&token=");
            builder.append(getTokenInfo());
            builder.append("_" + TIMES);
            System.out.println(builder.toString());
            String urls = builder.toString();


            OkHttpClient client = new OkHttpClient().newBuilder()
                    .sslSocketFactory(sslSocketFactory, trustManager)
                    .build();
            MediaType mediaType = MediaType.parse(" application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, fromImg);
            Request request = new Request.Builder()
                    .url(urls)
                    .method("POST", body)
                    .addHeader("User-Agent", " Mozilla/5.0 (Linux; Android 8.1.0; Pixel Build/OPM4.171019.021.P1; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/103.0.5060.129 Mobile Safari/537.36 SP-engine/2.38.0 matrixstyle/0 lite baiduboxapp/5.34.0.10 (Baidu; P1 8.1.0) graphplugin/1.0.10")
                    .addHeader("Content-Type", " application/x-www-form-urlencoded")
                    .addHeader("Content-Length", " 111704")
                    .addHeader("Host", " graph.baidu.com")
                    .addHeader("Connection", " Keep-Alive")
                    .addHeader("Cookie", " matrixstyle=0; BAIDUID=E9549645F9FD4180E6DFBEA68E74DDFE:FG=1; BAIDUCUID=g5x4ofJS26tnrm6uI0xOof5gFjIWpwjm8pA56k4GE68LkSP_rfWmuz1mA; SP_FW_VER=0.0.0; SG_FW_VER=0.0.0; BCLID=7187975593181227898; antispam_key_id=23; ab_sr=1.0.1_MWZlMDRlY2FiMDMwY2M0MmVlYjVkN2QzZDg1NTNjYjUwZTZjOWU4NWI1YWMzMmJhZTQ1N2YwNTAwODcxOTRkODVhMWY2MmU4MGYxNDI2MmI5ZmI0NTZhZWZkZmM5MzUxZDc5ZTY1NTA5NDc2MjZjZWM5YzMxOGM0NjdlYTJiYzE3MzZkOWNkNmM3OWRkNmU1NjRkM2MxNDJiYjA0NDM2OA==; antispam_data=992cb7bf02b1cfb4c53073864b0b5f9773d462276c2228a5b8a76853df2a6d64db8cde07334b693228dfe938e3f1ad873a1543438166112b47430f426b709e75a86570e53d02aa2f43fa534dbc88a2b7; BAIDUID=56FFAF86131C49096CC72000E595DC93:FG=1; BIDUPSID=56FFAF86131C49096CC72000E595DC93; PSTM=1655803098; SAMPLE_SIDS=1038011")
                    .build();
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
        }
    }*/


}
