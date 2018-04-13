package com.example.stack.welearn.views.activities;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.stack.welearn.R;
import com.example.stack.welearn.views.activities.iactivity.StaticBaseAct;

import butterknife.BindView;

/**
 * Created by stack on 2018/1/5.
 */

public class QuestionEditAct extends StaticBaseAct {
    @BindView(R.id.tb_edit_question)
    Toolbar mToolbar;


    @Override
    public void setUp() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.edit_action_bar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getLayout() {
        return R.layout.act_question_edit;
    }


    @Override
    public void register() {

    }

    @Override
    public void unRegister() {

    }
}
