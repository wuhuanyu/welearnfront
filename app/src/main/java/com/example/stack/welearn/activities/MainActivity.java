package com.example.stack.welearn.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.stack.welearn.R;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.config.MQTTClient;
import com.example.stack.welearn.events.Event;
import com.example.stack.welearn.fragments.ChatFragment;
import com.example.stack.welearn.fragments.QuestionsFragment;
import com.example.stack.welearn.fragments.CoursesFragment;
import com.example.stack.welearn.fragments.MeFragment;
import com.example.stack.welearn.utils.ToastUtils;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * Created by stack on 2018/1/4.
 */

public class MainActivity extends BaseActivity {
    public static final String TAG= MainActivity.class.getSimpleName();
    private HashMap<String,Fragment> fragmentHashMap=new HashMap<>();
    @BindView(R.id.bottom)BottomNavigationViewEx bottomNav;
    @BindView(R.id.main_toolbar) android.support.v7.widget.Toolbar mToolbar;
    @BindView(R.id.empty_view) ConstraintLayout mEmptyView;
    TextView tvBulletinCount;
    @BindView(R.id.fragment_container) FrameLayout mFragmentContainer;
    private boolean isWifiConnected;
    private boolean isCelluarConnected;
    private boolean haveSetUpMqtt =false;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }
    @Override
    public void doRegister() {


    }

    @Override
    public void initView() {
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        isWifiConnected=wifiNetwork.isConnected();
        NetworkInfo celluarNetwork=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        isCelluarConnected=celluarNetwork.isConnected();
        // 网络正常
        if(isCelluarConnected||isWifiConnected){
            Log.i(TAG,"网络正常");
            bottomNav.setVisibility(View.VISIBLE);
            mToolbar.setVisibility(View.VISIBLE);
            mFragmentContainer.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);

            Fragment courseFragment=new CoursesFragment();
            fragmentHashMap.put("course",courseFragment);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragmentHashMap.get("course")).commit();

            bottomNav.enableItemShiftingMode(false);
            bottomNav.enableShiftingMode(false);
            bottomNav.setOnNavigationItemSelectedListener((item -> {
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                Fragment selectedFrag=null;
                switch (item.getItemId()){
                    case R.id.bottom_course:
                        selectedFrag=fragmentHashMap.get("course");
                        item.setChecked(true);
                        break;

                    case R.id.bottom_question:
                        if(!fragmentHashMap.containsKey("question"))
                            fragmentHashMap.put("question",new QuestionsFragment());
                        selectedFrag=fragmentHashMap.get("question");
                        item.setChecked(true);
                        break;
                    case R.id.bottom_chat:
                        if(!fragmentHashMap.containsKey("chat"))
                            fragmentHashMap.put("chat",new ChatFragment());
                        selectedFrag=fragmentHashMap.get("chat");
                        item.setChecked(true);
                        break;
                    case R.id.bottom_me:
                        if(!fragmentHashMap.containsKey("me"))
                            fragmentHashMap.put("me",new MeFragment());
                        selectedFrag=fragmentHashMap.get("me");
                        item.setChecked(true);
                        break;
                }
                transaction.replace(R.id.fragment_container,selectedFrag);
                transaction.commit();
                return false;
            }));
            addBadgeAt(2,4);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("我们爱学习");
        }
        //网络故障
        else {
            Log.i(TAG,"网络故障");
            mToolbar.setVisibility(View.GONE);
            mFragmentContainer.setVisibility(View.GONE);
            bottomNav.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        View notifications  =menu.findItem(R.id.toolbar_info).getActionView();
        tvBulletinCount=(TextView) notifications.findViewById(R.id.text_bulletin_count);
        tvBulletinCount.setOnClickListener((v)->{
            Intent intent=new Intent(this,BulletinActivity.class);
            startActivity(intent);
        });
        return true;
    }
    @Override
    public int getLayout() {
        return R.layout.act_main;
    }

    private Badge addBadgeAt(int position,int number){
        return new QBadgeView(this)
                .setBadgeNumber(number)
                .setGravityOffset(12,2,true)
                .bindTarget(bottomNav.getBottomNavigationItemView(position))
                .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                    @Override
                    public void onDragStateChanged(int dragState, Badge badge, View targetView) {

                    }
                });
    }
    public void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event<String> event){
        Log.i(TAG,"processing subscribe event");
        switch (event.code()){
            case Event.SUBSCRIBE_OK:
                if(!haveSetUpMqtt) setUpMqttClient();
                ToastUtils.getInstance().showMsgLong("话题订阅成功");
                break;
            default:break;
        }
    }
    private void setUpMqttClient(){
        MqttAndroidClient client=MQTTClient.instance().getClient();
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.i(TAG,new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
        this.haveSetUpMqtt=true;
    }
}
