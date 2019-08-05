package com.cl.clog.util;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密类（封装jdk自带的md5加密方法）
 *
 * @author Rlon
 */
public class MD5Util {

    public static String encrypt(String source) {
        return encodeMd5(source.getBytes());
    }

    public static String encrypt16(String source) {
        return encodeMd5(source.getBytes()).substring(8, 24);
    }

    private static String encodeMd5(byte[] source) {
        try {
            byte[] retByte=MessageDigest.getInstance("MD5").digest(source);
            return encodeHex(retByte);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
    private static String encodeHex(byte[] bytes) {
        StringBuffer buffer = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if (((int) bytes[i] & 0xff) < 0x10)
                buffer.append("0");
            buffer.append(Long.toString((int) bytes[i] & 0xff, 16));
        }
        return buffer.toString();
    }

    public static void main(String[] args) {
        System.out.println(encrypt("123456"));
    }
}
