package com.example.stack.welearn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.stack.welearn.utils.ToastUtils;

/**
 * Created by stack on 2/2/18.
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    private static final String TAG=BootCompleteReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"-----------boot-complete----------");
//        ToastUtils.getInstance(context).showMsgShort("boot complete");
    }
}
