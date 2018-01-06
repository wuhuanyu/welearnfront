package com.example.stack.welearn.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import com.example.stack.welearn.R;
import com.example.stack.welearn.fragments.QuestionsFragment;
import com.example.stack.welearn.fragments.CoursesFragment;
import com.example.stack.welearn.fragments.MeFragment;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by stack on 2018/1/4.
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.bottom)BottomNavigationViewEx bottomNav;

    @BindView(R.id.main_toolbar)
    android.support.v7.widget.Toolbar mToolbar;
//    @BindView(R.id.)
//    ActionMenuView mActionMenuView;
    private HashMap<String,Fragment> fragmentHashMap=new HashMap<>();

    @Override
    public void doRegister() {
//        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        Fragment courseFragment=new CoursesFragment();
        fragmentHashMap.put("course",courseFragment);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragmentHashMap.get("course")).commit();

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
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    @Override
    public int getLayout() {
        return R.layout.act_main;
    }
}
