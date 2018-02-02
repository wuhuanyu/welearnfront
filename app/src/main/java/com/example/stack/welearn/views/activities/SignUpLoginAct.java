package com.example.stack.welearn.views.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.stack.welearn.R;
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
import com.example.stack.welearn.events.AccEvent;

/**
 * Created by stack on 2018/1/2.
 */

public class SignUpLoginAct extends AppCompatActivity {
    private boolean login=true;
    @BindView(R.id.edit_password) EditText EditPass;
    @BindView(R.id.edit_username) EditText EditName;
    @BindView(R.id.ckBox_ifteacher) CheckBox ckBoxIfTeacher;
    @BindView(R.id.text_signup) TextView TextSignUp;
    @BindView(R.id.btn_sign) Button BtnSignIn;

    private static String TAG="[SignUpLoginAct]:";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_signup_login);
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){

    }

    @OnClick(R.id.text_signup)
        //todo add animation
    void switchMode(View v){
        this.login=false;
        TextSignUp.setVisibility(View.INVISIBLE);
        BtnSignIn.setText("Sign Up");
    }
    @OnClick(R.id.btn_sign)
    void signIn(View v){
        if(login)
            doLogin();
        else
            doSignUp();
    }

    private  void doSignUp(){
        String name=EditName.getText().toString();
        String pass=EditPass.getText().toString();
        // post /acc/tea
        String url=Constants.Net.API_URL+"/acc"+(ckBoxIfTeacher.isChecked()?"/tea":"/stu");

        new Thread(()->{
            AndroidNetworking.post(url)
                    .addBodyParameter("name",name)
                    .addBodyParameter("password",pass)
                    .addBodyParameter("gender",""+Constants.MALE)
                    .build().getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        //{result:id,msg:msg}
                        int id=response.getInt("result");
                        EventBus.getDefault().post(new AccEvent(AccEvent.ACC_SIGNUP,200,id));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(ANError anError) {
                    if(anError.getErrorCode()!=0){
                        EventBus.getDefault().post(new AccEvent(AccEvent.ACC_SIGNUP,anError.getErrorCode(),""));
                    }
                }
            });
        }).start();
    }
    //do actual work,sign in
    private void doLogin() {
        String name=EditName.getText().toString();
        String pass=EditPass.getText().toString();
        String url=Constants.Net.API_URL+"/acc";
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
        if(event.type==AccEvent.ACC_SIGNUP){
            if(event.id!=-1){
                //login success
                ToastUtils.getInstance(this).showMsgShort("Sign Up Successfully");
                //save information
//                SharedPreferences sharePre=getSharedPreferences(getString(R.string.saved_info), Context.MODE_PRIVATE);
//                sharePre.edit()
//                        .putString(getString(R.string.saved_username),EditName.getText().toString())
//                        .putString(getString(R.string.saved_password),EditPass.getText().toString())
//                        .putInt(getString(R.string.saved_type),ckBoxIfTeacher.isChecked()?Constants.ACC_T_Tea:Constants.ACC_T_Stu)
//                        .putInt(getString(R.string.saved_id),event.id)
//                        .apply();

            }
            else if(event.result==400){
                ToastUtils.getInstance(this).showMsgShort("Name exists");

            }

        }
    }

    @Override
    public void onBackPressed(){
        if(login){
//            super.finish();test
            this.finish();
        }
        else{
            this.login=true;
            TextSignUp.setVisibility(View.VISIBLE);
            BtnSignIn.setText("Login");
        }
    }
}

