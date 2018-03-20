package com.example.stack.welearn.utils;

import android.util.Log;

import java.sql.Time;
import java.util.Calendar;

/**
 * Created by stack on 2018/1/14.
 */

public class TimeUtils {
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
//        Calendar cal=Calendar.getInstance();
//        int zoneOffset=cal.get(Calendar.ZONE_OFFSET);
//        int dstOffset=cal.get(Calendar.DST_OFFSET);
//        cal.add(Calendar.MILLISECOND,-(zoneOffset+dstOffset));
////        Log.i(cal.getTimeInMillis())
//        Log.i(TimeUtils.class.getSimpleName(),""+cal.getTimeInMillis());
        return dateDiff(small,System.currentTimeMillis());
    }

    public static int dateDiff(String small){
        return dateDiff(Long.parseLong(small));
    }
}
