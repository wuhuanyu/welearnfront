package com.example.stack.welearn.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
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

public class MainAct extends DynamicBaseAct {
    public static final String TAG= MainAct.class.getSimpleName();
    private HashMap<String,Fragment> fragmentHashMap=new HashMap<>();
    @BindView(R.id.bottom)BottomNavigationViewEx bottomNav;
    @BindView(R.id.main_toolbar) android.support.v7.widget.Toolbar mToolbar;
    TextView tvBulletinCount;
    @BindView(R.id.fragment_container) FrameLayout mFragmentContainer;

    private Fragment currentFragment;

    public static void startAct(Context context) {
        Intent starter = new Intent(context, MainAct.class);
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
    public void setUp() {

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
        if(!isServiceRunning(MQTTService.class)){
            MQTTService.startService(this);
        }
    }



    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        View notifications  =menu.findItem(R.id.toolbar_info).getActionView();
        tvBulletinCount=(TextView) notifications.findViewById(R.id.text_bulletin_count);
        tvBulletinCount.setOnClickListener((v)->{
            Intent intent=new Intent(this,BulletinAct.class);
            startActivity(intent);
        });
        return true;
    }
    @Override
    public int getLayout() {
        return R.layout.act_main;
    }

    @Override
    public void prepareData() {

    }

    @Override
    public ViewGroup getRoot() {
        return findViewById(R.id.root_main);
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

    @Override
    public void register() {

    }

    @Override
    public void unRegister() {

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


}
