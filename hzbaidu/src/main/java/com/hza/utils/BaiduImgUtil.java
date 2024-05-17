package com.hza.utils;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class BaiduImgUtil {

    private static final char[] HEX_DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final char[] HEX_DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    /**
     * 原方法截取img_base64
     *
     * @param str base64编码的图片
     * @return
     */
    public static String extractImgBase64Feature(String str) {
        if (str == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int length = str.length();
        if (length < 20) {
            for (int i = 1; i <= 20; i++) {
                sb.append('0');
            }
        } else {
            sb.append(str.substring(0, 20));
        }
        if (length < 1000) {
            for (int i2 = 1; i2 <= 10; i2++) {
                sb.append('0');
            }
        } else {
            sb.append(str.substring(length - 1000, length - 990));
        }
        if (length < 34) {
            for (int i3 = 1; i3 <= 34; i3++) {
                sb.append('0');
            }
        } else {
            sb.append(str.substring(length - 34, length));
        }
        return sb.toString();
    }

    /**
     * token 前置所需的md5
     *
     * @return
     */
    public static String getMd5(String data) {
        return toMd5Lower(data);
    }

    public static String toMd5Lower(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.reset();
            instance.update(str.getBytes("UTF-8"));
            return toHexString2(instance.digest()).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    private static String toHexString2(byte[] bArr) {
        StringBuilder sb = new StringBuilder(bArr.length * 2);
        for (int i = 0; i < bArr.length; i++) {
            sb.append(HEX_DIGITS_UPPER[(bArr[i] & 240) >>> 4]);
            sb.append(HEX_DIGITS_UPPER[bArr[i] & 15]);
        }
        return sb.toString();
    }

    /**
     * So层 RSA非对称加密
     *
     * @param bytes
     * @return responseData - JSON
     * @throws Exception
     */
    public static byte[] encryptByte(byte[] bytes) throws Exception {
        BouncyCastleProvider bouncyCastleProvider = new BouncyCastleProvider();
        Cipher cipherIns = Cipher.getInstance("RSA/ECB/PKCS1Padding", bouncyCastleProvider);
        String publicKey = "MDwwDQYJKoZIhvcNAQEBBQADKwAwKAIhAKx0c3t8hE1bLpT9UdRr1N+S5KV9iEO6lYOdz3r0MzpVAgMBAAE=";
        byte[] publicKeyBytes = Base64.decodeBase64(publicKey);

        X509EncodedKeySpec X509EKSIns = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactoryIns = KeyFactory.getInstance("RSA", bouncyCastleProvider);

        PublicKey publicKey1 = keyFactoryIns.generatePublic(X509EKSIns);
        cipherIns.init(Cipher.ENCRYPT_MODE, publicKey1);
        int blockSize = cipherIns.getBlockSize();

        int arg_len = bytes.length;
        int v32 = 0;
        ByteArrayOutputStream v29 = new ByteArrayOutputStream();
        do {
            if ((int) (v32 + blockSize) > arg_len)
                blockSize = arg_len - v32;

            byte[] v33 = new byte[blockSize];
            System.arraycopy(bytes, v32, v33, 0, blockSize);
            byte[] v34 = cipherIns.doFinal(v33);
//            (*env)->SetByteArrayRegion(env, v33, 0LL, blockSize, &arg_[v32]);
//            v34 = _JNIEnv::CallObjectMethod(env, cipherIns, v28, v33);
//            result = (void *)append_jbyte_array(env, v29, v34);
            v29.write(v34);
            v32 += blockSize;
        } while (v32 < arg_len);
        return v29.toByteArray();
    }


    /**
     * 随机数 16位 这里用作imgkey
     *
     * @return
     */
    public static String generateImageKey(String str) {
        StringBuilder sb = new StringBuilder();
        try {
            String md5Upper = MD5Utils.toMd5Upper(str);
            if (!md5Upper.isEmpty()) {
                int i = 0;
                for (int i2 = 0; i2 < 4; i2++) {
                    sb.append(getKey(md5Upper, i));
                    i += 4;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getKey(String str, int i) {
        int i2;
        if (str == null || str.length() < (i2 = i + 20)) {
            return null;
        }
        return String.format("%04x", Integer.valueOf(Integer.parseInt(str.substring(i + 16, i2), 16) ^ Integer.parseInt(str.substring(i, i + 4), 16)));
    }

    public static String fromImg(String str) {
        return "=&data=" + str.replace("{", "%7B").replace("\"", "%22").replace(":", "%3A").replace("/", "%2F").replace(",", "%2C").replace("{", "").replace("}", "%7D").replace("+", "%2B").replace("=", "%3D");
    }


}
