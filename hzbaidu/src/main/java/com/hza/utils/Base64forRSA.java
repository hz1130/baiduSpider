package com.hza.utils;

public class Base64forRSA {

    private static char[] LEGALCHARS;

    static {
        Base64forRSA.LEGALCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
    }


    public static String encode(byte[] arg8) {
        int v8;
        int v0 = arg8.length;
        StringBuffer v1 = new StringBuffer(arg8.length * 3 / 2);
        int v2 = v0 - 3;
        int v4;
        for (v4 = 0; v4 <= v2; v4 += 3) {
            int v5 = (arg8[v4] & 0xFF) << 16 | (arg8[v4 + 1] & 0xFF) << 8 | arg8[v4 + 2] & 0xFF;
            v1.append(Base64forRSA.LEGALCHARS[v5 >> 18 & 0x3F]);
            v1.append(Base64forRSA.LEGALCHARS[v5 >> 12 & 0x3F]);
            v1.append(Base64forRSA.LEGALCHARS[v5 >> 6 & 0x3F]);
            v1.append(Base64forRSA.LEGALCHARS[v5 & 0x3F]);
        }

        int v3 = v0;
        if (v4 == v3 - 2) {
            v8 = (arg8[v4 + 1] & 0xFF) << 8 | (arg8[v4] & 0xFF) << 16;
            v1.append(Base64forRSA.LEGALCHARS[v8 >> 18 & 0x3F]);
            v1.append(Base64forRSA.LEGALCHARS[v8 >> 12 & 0x3F]);
            v1.append(Base64forRSA.LEGALCHARS[v8 >> 6 & 0x3F]);
            v1.append("=");
        } else if (v4 == v3 - 1) {
            v8 = (arg8[v4] & 0xFF) << 16;
            v1.append(Base64forRSA.LEGALCHARS[v8 >> 18 & 0x3F]);
            v1.append(Base64forRSA.LEGALCHARS[v8 >> 12 & 0x3F]);
            v1.append("==");
        }
        return v1.toString();
    }


    public static void main(String[] args) {
        String str = "l3h1wAzoglF+5Q/vIulTKWRzj4VbxXScZwiiACaOb95Jr3uG0DqJXOmr+ofsWVTnP+jJQQIRKiyi9qTV1Ut60Q==";
        System.out.println(encode(str.getBytes()));
    }


}
