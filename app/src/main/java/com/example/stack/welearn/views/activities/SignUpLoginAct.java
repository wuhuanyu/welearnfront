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
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.tasks.AccTask;
import com.example.stack.welearn.utils.Constants;
import com.example.stack.welearn.utils.ThreadPoolManager;
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
    @BindView(R.id.text_signup) TextView SignUpOrLogin;
    @BindView(R.id.btn_sign) Button BtnSubmit;

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
        SignUpOrLogin.setVisibility(View.INVISIBLE);
        BtnSubmit.setText("注册");
    }
    @OnClick(R.id.btn_sign)
    void btnPressed(View v){
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
                        int id=response.getInt("result");
                        EventBus.getDefault().post(new Event<Integer>(Event.SIGNUP_OK,id));
                    } catch (JSONException e) {
                        e.printStackTrace();
//                        EventBus.getDefault()
                    }
                }

                @Override
                public void onError(ANError anError) {
                    if(anError.getErrorCode()!=0){
                        EventBus.getDefault().post(new Event<String>(Event.SIGNUP_FAIL));
                    }
                }
            });
        }).start();
    }

    private void doLogin() {
        String name=EditName.getText().toString();
        String pass=EditPass.getText().toString();
        if(name.length()==0||pass.length()==0) return;
        int type=(ckBoxIfTeacher.isChecked()?Constants.ACC_T_Tea:Constants.ACC_T_Stu);
        ThreadPoolManager.getInstance().getService().submit(AccTask.instance().doLogin(name,pass,type));
    }

    @Override
    public void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop(){
        EventBus.getDefault().unregister(this);
        super.onStop();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event<?> event){
        int code=event.code();
        switch(code){
            case Event.LOGIN_OK:{
                ToastUtils.getInstance(this).showMsgShort("Login Successfully");
                String name=EditName.getText().toString();
                String pass=EditPass.getText().toString();
                int type=ckBoxIfTeacher.isChecked()?Constants.ACC_T_Tea:Constants.ACC_T_Stu;
                AccTask.instance().persist(name,pass,type,((Event<JSONObject>)event).t());

                MainActivity.start(this);

                this.finish();

//                SharedPreferences sharePre=getSharedPreferences(getString(R.string.saved_info), Context.MODE_PRIVATE);
//                JSONObject authJson=(JSONObject)event.t();
//                try {
//                    sharePre.edit()
//                            .putString(getString(R.string.saved_username),EditName.getText().toString())
//                            .putString(getString(R.string.saved_password),EditPass.getText().toString())
//                            .putInt(getString(R.string.saved_type),ckBoxIfTeacher.isChecked()?Constants.ACC_T_Tea:Constants.ACC_T_Stu)
//                            .putString("token",authJson.getString("token"))
//                            .putInt("id",authJson.getInt("id"))
//                            .apply();
//
//                    WeLearnApp.info().setToken(authJson.getString("token"))
//                            .setId(authJson.getInt("id"))
//                            .setPassword(EditPass.getText().toString())
//                            .setUserName(EditName.getText().toString())
//                            .setUserType(ckBoxIfTeacher.isChecked()?Constants.ACC_T_Tea:Constants.ACC_T_Stu);
//
//                    MainActivity.start(this);


            } break;
            case Event.LOGIN_FAIL:{
                ToastUtils.getInstance().showMsgShort("Login fail,check your name or password");
            }break;
            case Event.SIGNUP_OK:{
                ToastUtils.getInstance().showMsgShort("Sign up ok,trying to login");
                doLogin();
            }break;
            case Event.SIGNUP_FAIL:{
                ToastUtils.getInstance().showMsgShort("Sign up fail,please change to another name");
            }break;
            default:break;
        }
    }

    @Override
    public void onBackPressed(){
        if(login){
            this.finish();
        }
        else{
            this.login=true;
            SignUpOrLogin.setVisibility(View.VISIBLE);
            BtnSubmit.setText("Signup");
        }
    }
}

