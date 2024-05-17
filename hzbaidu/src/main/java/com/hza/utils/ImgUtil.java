package com.hza.utils;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.codec.binary.Base64;

import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Random;

public class ImgUtil {

    private static final String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String strs = "=0123456789";

    /**
     * 将网络图片转换成Base64编码字符串
     *
     * @param imgUrl 网络图片Url
     * @return
     */
    public static String getImgUrlToBase64(String imgUrl) {
        SSLSocketFactory sslSocketFactory = null;
        try {
            X509TrustManager trustManager = new TrustAllManager();
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception ignore) {
        }


        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        byte[] buffer = null;
        try {
            // 创建URL
            URL url = new URL(imgUrl);
            // 创建链接
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(sslSocketFactory);
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            inputStream = conn.getInputStream();
            outputStream = new ByteArrayOutputStream();
            // 将内容读取内存中
            buffer = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            buffer = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    // 关闭inputStream流
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    // 关闭outputStream流
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // 对字节数组Base64编码
//        return new BASE64Encoder().encode(buffer);
        return Base64.encodeBase64String(buffer);
    }

    public static class TrustAllManager implements X509TrustManager {

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
        }

    }


    /**
     * 随机生成
     *
     * @param len 自定义位数
     * @return str
     */
    public static String randomString(int len) {
        Random random1 = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(str.charAt(random1.nextInt(str.length())));
        }
        return sb.toString();
    }

    public static String randomStrings(int len) {
        Random random1 = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(strs.charAt(random1.nextInt(strs.length())));
        }
        return sb.toString();
    }


    /**
     * 判断是否包含敏感字符
     *
     * @param rep 返回的response 响应
     * @return 返回true 为包含敏感字符
     */
    public static boolean searchCharacter(String rep) {
        String str = "露饮风餐|亲思倍节佳逢每|家当各女儿庄村|山重万过已舟轻|纷纷雪雁吹风北|日瞳瞳户万门千|手招遥问借人路|早来归学散童儿|角尖尖露才荷小|烟紫生炉香照日|鸢纸放风东趁忙|川前挂布瀑看遥|眠愁对火渔枫江|中雨烟台楼少多|人应不惊鱼得怕|楼鹤黄辞西人故|う|中水铺阳残道一|律定理查|数函偶|题命假|镜透聚|数名复|丝险保|数理有|数指幂|彩结灯张|天九落河银是疑|早来归学散童儿|性逆对|律定体气想理|晚林枫爱坐车停|圆心同|异角位同|数量质|の|还日一陵江里|面平复|数方平|遥指点|数名单|瑟瑟江半|象图|畏不|の|更君劝｜数理无|朝城渭|飞莺长|幫|制位密|东水碧|上鹭|鹂黄有上|し|面平复|数方平|遥指点|数名单|瑟瑟江半|象图|畏不|の|更君劝|烟春醉柳杨堤拂|亲思倍节佳逢每|家当各女儿庄村|山重万过已舟轻|纷纷雪雁吹风北|日瞳瞳户万门千|手招遥问借人路|早来归学散童儿|角尖尖露才荷小|烟紫生炉香照日|鸢纸放风东趁忙|川前挂布瀑看遥|眠愁对火渔枫江|中雨烟台楼少多|人应不惊鱼得怕|楼鹤黄辞西人故|う|中水铺阳残道一|律定理查|数函偶|题命假|镜透聚|数名复|丝险保|数理有|数指幂";
        String replace = str.replace("|", " ");
        String[] s = replace.split(" ");
        boolean contains = false;
        boolean resultBoolean = false;

        for (String result : s) {
            contains = StrUtil.containsAny(rep, result);
            if (contains == true) {
                resultBoolean = true;
                break;
            }
        }
        return resultBoolean;
    }


}
