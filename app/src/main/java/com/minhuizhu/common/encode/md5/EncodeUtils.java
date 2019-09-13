package com.minhuizhu.common.encode.md5;

import android.util.Base64;

import com.minhuizhu.common.encode.Aes;

/**
 * Created by pc on 2015/11/22.
 */
public class EncodeUtils {
    public static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            sb.append(String.format("%02x", bytes[i]));
        }
        return sb.toString();
    }

    public static byte[] parseHexString(String hexString) {
        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < bytes.length; i ++) {
            bytes[i] = (byte) Integer.parseInt(hexString.substring(i * 2, i * 2 + 2), 16);
        }
        return bytes;
    }

    public static String toBase64String(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    public static byte[] parseBase64String(String base64Str) {
        return Base64.decode(base64Str.getBytes(), Base64.NO_WRAP);
    }

    public static String generateRandomKey() {
       int randmo= (int)  Math.random();
        return String.valueOf(randmo);
    }

    public static String getUserAuth(int uin, String salt, String password, String randomKey) {
        String key=uin+salt+password;
        String value=uin+salt+password+randomKey;
        return Aes.decrypt(value,key);
    }
}
