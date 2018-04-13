package com.example.stack.welearn.views.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.stack.welearn.R;

/**
 * Created by stack on 2018/1/5.
 */

public class CommentDialog extends DialogFragment {
   CommentDialogListener mListener;
    public  static final String TAG=CommentDialog.class.getSimpleName();
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View v =inflater.inflate(R.layout.dialog_comment,null);
        EditText input=(EditText)v.findViewById(R.id.text_comment_input);
        builder.setView(v)
                .setPositiveButton("发表评论", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i(TAG,"---clicked comment dialog-positive-------");
                        mListener.onPositiveClick(input.getText().toString(),CommentDialog.this);
                    }
                }).setNegativeButton("算了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i(TAG,"---clicked comment dialog-negative-------");
                mListener.onNegativeClick(CommentDialog.this);
            }
        });
        return builder.create();
    }

    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            mListener=(CommentDialogListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+"must implement CommnetDialogListener");
        }
    }
    public interface CommentDialogListener{
        void onPositiveClick(String input,CommentDialog dialog);
        void onNegativeClick(CommentDialog dialog);
    }
    public void onDetach(){
        this.mListener=null;
        super.onDetach();
    }
}
