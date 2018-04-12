package com.example.stack.welearn.views.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.stack.welearn.R;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.tasks.AccTask;
import com.example.stack.welearn.utils.Constants;
import com.example.stack.welearn.utils.NetworkUtils;
import com.example.stack.welearn.utils.ThreadPoolManager;
import com.example.stack.welearn.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import butterknife.BindView;

/**
 * Created by stack on 2018/1/2.
 */

public class SignUpLoginAct extends StaticBaseAct {
    private boolean isToLogin =true;
    @BindView(R.id.edit_password) EditText EditPass;
    @BindView(R.id.edit_username) EditText EditName;
    @BindView(R.id.ckBox_ifteacher) CheckBox ckBoxIfTeacher;
    @BindView(R.id.text_signup) TextView SignUpOrLogin;
    @BindView(R.id.btn_sign) Button BtnSubmit;

    private static String TAG="[SignUpLoginAct]:";

    @Override
    public void setUp() {
        SignUpOrLogin.setOnClickListener(view -> {
            if(isToLogin){
                isToLogin=false;
                SignUpOrLogin.setVisibility(View.GONE);
                BtnSubmit.setText("SignUp");
            }
        });

        BtnSubmit.setOnClickListener(view -> {
            if(isToLogin)
                doLogin();
            else
                doSignUp();
        });
    }


    @Override
    public int getLayout() {
        return R.layout.act_signup_login;
    }

    private  void doSignUp(){
        if(!NetworkUtils.isNetworkConnected()){
            ToastUtils.getInstance().showMsgShort(getString(R.string.hint_no_network));
            return;
        }
        String name=EditName.getText().toString();
        String pass=EditPass.getText().toString();
        int type=ckBoxIfTeacher.isChecked()?Constants.ACC_T_Tea:Constants.ACC_T_Stu;
        ThreadPoolManager.getInstance().getService()
                .submit(AccTask.instance().signUp(name,pass,type));

    }

    private void doLogin() {
        if(!NetworkUtils.isNetworkConnected()){
            ToastUtils.getInstance().showMsgShort(getString(R.string.hint_no_network));
            return;
        }

        String name=EditName.getText().toString();
        String pass=EditPass.getText().toString();
        if(name.length()==0||pass.length()==0) return;
        int type=(ckBoxIfTeacher.isChecked()?Constants.ACC_T_Tea:Constants.ACC_T_Stu);
        ThreadPoolManager.getInstance().getService().submit(AccTask.instance().doLogin(name,pass,type));
    }

    @Override
    public void register() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void unRegister() {
        EventBus.getDefault().unregister(this);
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

                MainAct.startAct(this);

                this.finish();

            } break;
            case Event.LOGIN_FAIL:{
                ToastUtils.getInstance().showMsgShort("Login fail,check your name or password");
            }break;
            case Event.SIGNUP_OK:{
                ToastUtils.getInstance().showMsgShort("Sign up ok,you can login now");
                isToLogin=true;
                SignUpOrLogin.setVisibility(View.VISIBLE);
                BtnSubmit.setText("Login");
            }break;
            case Event.SIGNUP_FAIL:{
                ToastUtils.getInstance().showMsgShort("Sign up fail,please change to another name");
            }break;
            default:break;
        }
    }

    @Override
    public void onBackPressed(){
        if(isToLogin){
            this.finish();
        }
        else{
            this.isToLogin =true;
            SignUpOrLogin.setVisibility(View.VISIBLE);
            BtnSubmit.setText("Signup");
        }
    }

    public static void start(Context context){
        Intent intent=new Intent(context,SignUpLoginAct.class);
        context.startActivity(intent);
    }


}

