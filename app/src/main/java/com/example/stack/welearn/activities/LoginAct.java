package com.example.stack.welearn.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.stack.welearn.R;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.utils.Constants;
import com.example.stack.welearn.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import events.AccEvent;
import events.BaseEvent;

/**
 * Created by stack on 2018/1/2.
 */

public class LoginAct extends AppCompatActivity {
    @BindView(R.id.edit_password) EditText EditPass;
    @BindView(R.id.edit_username) EditText EditName;
    @BindView(R.id.ckBox_ifteacher)
    CheckBox ckBoxIfTeacher;

    private static String TAG="[LoginAct]:";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_sign)
    void signIn(View v){
//        Log.i(TAG,"Click sign in");
        doSignIn();
    }
    private void doSignIn() {

        String name=EditName.getText().toString();
        String pass=EditPass.getText().toString();
        String url= Constants.Net.API_URL+"/acc";

        int type=(ckBoxIfTeacher.isChecked()?Constants.ACC_T_Tea:Constants.ACC_T_Stu);
        new Thread(()->{
            AndroidNetworking.post(url)
                    .addBodyParameter("name",name)
                    .addBodyParameter("password",pass)
                    .addBodyParameter("type",""+type)
                    .build().getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        int result=response.getInt("result");
                        EventBus.getDefault().post(new AccEvent(AccEvent.ACC_LOGIN,200,result));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(ANError anError) {
                    if(anError.getErrorCode()!=0){
                        EventBus.getDefault().post(new AccEvent(AccEvent.ACC_LOGIN,anError.getErrorCode(),""));
                    }
                }
            });
        }).start();
    }

    @Override
    public void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAccEvent(AccEvent event){
        if(event.type== AccEvent.ACC_LOGIN){
            if(event.id!=-1){
                //login success
                ToastUtils.getInstance(this).showMsgShort("Login Succesfully");
                //save information
                SharedPreferences sharePre=getSharedPreferences(getString(R.string.saved_info), Context.MODE_PRIVATE);
                sharePre.edit()
                        .putString(getString(R.string.saved_username),EditName.getText().toString())
                        .putString(getString(R.string.saved_password),EditPass.getText().toString())
                        .putInt(getString(R.string.saved_type),ckBoxIfTeacher.isChecked()?Constants.ACC_T_Tea:Constants.ACC_T_Stu)
                        .putInt(getString(R.string.saved_id),event.id)
                        .apply();
            }
            else if(event.result==404){
                ToastUtils.getInstance(this).showMsgShort("No such user");
            }else if(event.result==401){
                ToastUtils.getInstance(this).showMsgShort("Wrong password");
            }
        }
        return;
    }

}

