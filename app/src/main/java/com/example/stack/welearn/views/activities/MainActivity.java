package com.example.stack.welearn.views.activities;

import android.app.ActivityManager;
import android.app.Service;
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

import com.example.stack.welearn.MQTTService;
import com.example.stack.welearn.R;
import com.example.stack.welearn.views.IView;
import com.example.stack.welearn.views.fragments.ChatFragment;
import com.example.stack.welearn.views.fragments.CoursesFragment;
import com.example.stack.welearn.views.fragments.MeFragment;
import com.example.stack.welearn.views.fragments.QuestionsFragment;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

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

    private Fragment currentFragment;

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if(savedInstanceState==null){
            fragmentHashMap.put("course",new CoursesFragment());
            currentFragment=fragmentHashMap.get("course");
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container,currentFragment).commit();
        }
    }
    @Override
    public void doRegister() {
        if(!isServiceRunning(MQTTService.class)){
            Intent intent=new Intent(this, MQTTService.class);
            startService(intent);
        }

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

            bottomNav.enableItemShiftingMode(false);
            bottomNav.enableShiftingMode(false);
            bottomNav.setOnNavigationItemSelectedListener((item -> {
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
                switchFragment(currentFragment,selectedFrag);
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
//        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void switchFragment(Fragment from,Fragment to){
        if(from!=to){
            currentFragment=to;
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            if(!to.isAdded()){
                transaction.hide(from).add(R.id.fragment_container,to).commit();
            }
            else{
                transaction.hide(from).show(to).commit();
            }
        }
    }


    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void refresh() {
        ((IView)currentFragment).refresh();
    }

    private boolean isServiceRunning(Class<? extends Service> serviceClz){
        ActivityManager manager=(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo serviceInfo:manager.getRunningServices(Integer.MAX_VALUE)){
            if(serviceClz.getName().equals(serviceInfo.service.getClassName())){
                Log.i(TAG,"service"+serviceClz.getSimpleName()+" is running");
                return true;
            }
        }
        Log.i(TAG,"service"+serviceClz.getSimpleName()+" is not running");
        return false;
    }
}
