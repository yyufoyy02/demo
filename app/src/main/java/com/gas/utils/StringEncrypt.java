package com.property.utils;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by DongZ on 2015/8/17 0017.
 */
public class StringEncrypt {
    private final static String TAG = StringEncrypt.class.getSimpleName();

    /**
     * 采用非对称性算法加密字符串
     *
     * @param srcString   原始字符串
     * @param encryptType 加密类型
     * @param cas         字母大小写
     * @return
     */
    public static String encodeByAsymmetric(String srcString, String encryptType, Case cas) {
        if (encryptType == null) {
            return srcString;
        }

        String outStr = null;

        //我在这里只处理MD5,SHA1,SHA256三种
        if (encryptType.equals(EncodeType.MD5) ||
                encryptType.equals(EncodeType.SHA1) ||
                encryptType.equals(EncodeType.SHA256)) {
            try {
                MessageDigest md = MessageDigest.getInstance(encryptType);
                // 返回的是byet[]，要转化为String存储比较方便
                byte[] digest = md.digest(srcString.getBytes());
                outStr = byteToString(digest, cas);
            } catch (NoSuchAlgorithmException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return outStr;
        } else {
            return srcString;
        }
    }

    /**
     * 字节转字符串
     *
     * @param digest 字节
     * @param cas    字母大小写
     * @return
     */
    private static String byteToString(byte[] digest, Case cas) {
        String str = "";
        String tempStr;

        for (byte b : digest) {
            tempStr = (Integer.toHexString(b & 0xff));

            if (tempStr.length() == 1) {
                str = str + "0" + tempStr;
            } else {
                str = str + tempStr;
            }
        }
        if (cas == Case.UPPER) {
            return str.toUpperCase();
        } else {
            return str.toLowerCase();
        }
    }

    /**
     * 加密类型
     */
    public class EncodeType {
        public static final String MD5 = "MD5";
        public static final String SHA1 = "SHA-1";
        public static final String SHA256 = "SHA-256";
    }

    /**
     * 字母大小写
     */
    public enum Case {
        UPPER, LOWER
    }
}
