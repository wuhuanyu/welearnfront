package com.example.stack.welearn.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by stack on 2018/1/2.
 */

public class ToastUtils {
    private static  ToastUtils INSTANCE;

    private Toast toast;
    public static ToastUtils getInstance(Context context) {
        if(INSTANCE==null){
            INSTANCE=new ToastUtils(context.getApplicationContext());
        }
        return INSTANCE;
    }

    private ToastUtils(Context context) {
        this.toast=Toast.makeText(context,"",Toast.LENGTH_SHORT);
    }



    public void showMsgLong(String msg){
        if(toast==null){
            return;
        }
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setText(msg);
        toast.show();
    }
    public void showMsgShort(String msg){
        if(toast==null){
            return;
        }
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setText(msg);
        toast.show();
    }


}
