package com.example.stack.welearn.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

import com.example.stack.welearn.R;

/**
 * Created by stack on 2018/1/5.
 */

public class CommentDialog extends DialogFragment {
   CommentDialogListener mListener;

    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_comment,null))
                .setPositiveButton("发表评论", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setNegativeButton("算了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

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
        public void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegetiveClick(DialogFragment dialog);
    }
}
