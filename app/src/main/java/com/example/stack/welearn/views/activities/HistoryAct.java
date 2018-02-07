package com.example.stack.welearn.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by stack on 2/7/18.
 */

public class HistoryAct extends AppCompatActivity {
    public static void start(Context context, Bundle data){
        Intent intent=new Intent(context,HistoryAct.class);
        intent.putExtras(data);
        context.startActivity(intent);
    }

}
