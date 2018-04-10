package com.example.stack.welearn.utils;


import android.util.Base64;

public class Base64Utils {
    public static String encode(String orign){
        return Base64.encodeToString(orign.getBytes(),Base64.NO_WRAP);
    }

    public static String decode(String encoded){
        return Base64.decode(encoded,Base64.NO_WRAP).toString();
    }

    public static String encode(int type,int userId,String token){
        return encode(String.valueOf(type)+":"+String.valueOf(userId)+":"+token);
    }

    public static int decodeUserId(String auth){
        String authentication=decode(auth);
        String infos[]=authentication.split(":");
        if(infos.length!=3)
            throw new IllegalArgumentException("Authorization must be [type]:[id]:[token]");
        return Integer.parseInt(infos[1]);
    }
}
