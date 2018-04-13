package com.example.stack.welearn.utils;

import android.support.annotation.NonNull;

import java.util.Calendar;

/**
 * Created by stack on 2018/1/14.
 */

public class TimeUtils {
    private static Calendar calendar=Calendar.getInstance();
    //毫秒
    public static int dateDiff(long small,long large){
        long diff=large-small;
        return (int)Math.floor(diff/(1000*60*60*24));
    }
    public static int dateDiff(String small,long large){
        return dateDiff(Long.parseLong(small),large);
    }
    //http://www.cnblogs.com/zyw-205520/p/4632490.html
    public static int dateDiff(long small){
        return dateDiff(small,System.currentTimeMillis());
    }

    public static int dateDiff(String small){
        return dateDiff(Long.parseLong(small));
    }

    @NonNull
    public static String toDate(long millis){
        calendar.setTimeInMillis(millis);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int hour=calendar.get(Calendar.HOUR);
        int minute=calendar.get(Calendar.MINUTE);
        return month+"/"+day+" "+hour+":"+minute;
    }
}
