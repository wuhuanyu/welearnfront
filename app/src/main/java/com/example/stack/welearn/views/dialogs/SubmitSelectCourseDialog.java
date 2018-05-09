package com.example.stack.welearn.views.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class SubmitSelectCourseDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("确认提交")
                .setNegativeButton("取消",(dialogInterface, i) -> {
                    this.dismiss();
                })
                .setPositiveButton("确认",(dialogInterface, i) -> {
                    SubmitCallback callback=(SubmitCallback)getActivity();
                    callback.onConfirmSubmit();
                    this.dismiss();
                });
        return builder.create();
    }

    public interface SubmitCallback{
        void onConfirmSubmit();
    }
}
