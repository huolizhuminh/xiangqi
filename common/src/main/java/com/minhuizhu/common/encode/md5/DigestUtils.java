package com.minhuizhu.common.encode.md5;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestUtils {
    private DigestUtils() {}
    private static final String MD5Add = "abcdefghijklmnop";
    public static String getMD5(String content) {
        return getMD5(content.getBytes());
    }

    public static String getMD5(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] buf = md.digest(data);
            return EncodeUtils.toHexString(buf);
        } catch (NoSuchAlgorithmException e) {
            traceException(e);
        }
        return null;
    }

    public static String getMD5(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        try (FileInputStream inStream = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int numRead;
            MessageDigest md = MessageDigest.getInstance("md5");
            while ((numRead = inStream.read(buffer)) > 0) {
                md.update(buffer, 0, numRead);
            }
            return EncodeUtils.toHexString(md.digest());
        } catch (Exception e) {
            traceException(e);
        }
        return null;
    }

    private static void traceException(Exception e) {
    }
    public  static String getEncryptKey(String key) {
        if (key == null) {
            return md5(MD5Add);
        } else if (key.length() < 16) {
            key = key + MD5Add.substring(0, 16 - key.length());
            return (md5(key));
        } else {
            return md5(key);
        }
    }
    private static String md5(String md5Add) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        char[] charArray = md5Add.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
