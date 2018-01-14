package com.example.stack.welearn.utils;

/**
 * Created by stack on 2018/1/2.
 * copy from server/ constants.js
 */

public final class Constants {

    // account type
    public static final int ACC_T_Tea=11;
    public static final int ACC_T_Stu=12;
    public static final int ACC_T_Admin=13;
    //gender info
    public static final int MALE=21;
    public static final int FEMALE=22;

    //question type
    public static final int SELECT=31;
    public static final int QA=32;

    public static final class Net{
        public static final String HOST="192.168.1.103";
        public static final String PORT="3000";
        public static final String API_URL="http://"+HOST+":"+PORT+"/api/v1";
        public static final String IMAGE_URL="http://"+HOST+":"+PORT+"/images/";
    }




}
