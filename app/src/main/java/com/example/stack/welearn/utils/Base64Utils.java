package com.example.stack.welearn.utils;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

public class Base64Utils {
    public static String encode(String origin) {
        try {
            byte[] originBytes = origin.getBytes("UTF-8");
            String encoded = Base64.encodeBase64String(originBytes);
            return encoded;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
