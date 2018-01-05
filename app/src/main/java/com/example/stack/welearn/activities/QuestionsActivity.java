package com.example.stack.welearn.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import com.example.stack.welearn.R;
import com.example.stack.welearn.WeLearnApp;
import com.example.stack.welearn.fragments.CommentDialog;
import com.example.stack.welearn.fragments.QuestionDetailFragment;
import com.example.stack.welearn.utils.ToastUtils;

import butterknife.BindView;
import github.chenupt.springindicator.SpringIndicator;

/**
 * Created by stack on 2018/1/5.
 */
//TODO:add pager indicator
public class QuestionsActivity extends BaseActivity implements CommentDialog.CommentDialogListener {
    int current;
    @BindView(R.id.my_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.vp_questions)
    ViewPager vpQuestionsContainer;

    PagerAdapter pagerAdapter;
    @BindView(R.id.fab_comment)
    FloatingActionButton fbComment;
    final int NUM=5;

    @Override
    public void doRegister() {
        vpQuestionsContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                current=position;
                //ToastUtils.getInstance(WeLearnApp.getContext()).showMsgShort(""+position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        fbComment.setOnClickListener(view -> {
            CommentDialog dialog=new CommentDialog();
            dialog.show(getSupportFragmentManager(),"想说什么");
        });
    }

    @Override
    public void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("题目");
        pagerAdapter=new QuestionPagerAdapter(getSupportFragmentManager());
        vpQuestionsContainer.setAdapter(pagerAdapter);
        current=0;
//        mIndicator.setViewPager(vpQuestionsContainer);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public int getLayout() {
        return R.layout.act_questions;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegetiveClick(DialogFragment dialog) {

    }
}
class QuestionPagerAdapter extends FragmentStatePagerAdapter{

    public QuestionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new QuestionDetailFragment();
    }

    @Override
    public int getCount() {
        return 5;
    }
}

