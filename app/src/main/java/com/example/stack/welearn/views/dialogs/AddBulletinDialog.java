package com.example.stack.welearn.views.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.stack.welearn.R;
import com.example.stack.welearn.utils.ToastUtils;

public class AddBulletinDialog extends DialogFragment {
    public AddBulletinDialog setCourseNames(String[] courseNames) {
        this.courseNames = courseNames;
        return this;
    }

    String courseNames[];
    String selectedCourse;

    public static AddBulletinDialog newInstance(String[] courseNames){
        AddBulletinDialog frag=new AddBulletinDialog();
        frag.setCourseNames(courseNames);
        return frag;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View root=getLayoutInflater().inflate(R.layout.dialog_add_bulletin,null);
        EditText editText=(EditText)root.findViewById(R.id.edit_new_bulletin);
        Spinner courseSpinner=(Spinner) root.findViewById(R.id.spinner_choose_course);
        courseSpinner.setSelection(0);
        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                AddBulletinDialog.this.selectedCourse=AddBulletinDialog.this.courseNames[i];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<String> spinnerAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,this.courseNames);

        courseSpinner.setAdapter(spinnerAdapter);
        builder.setView(root);

        builder.setPositiveButton("Publish",(dialogInterface, i) -> {
            try {
                NewBulletinListener listener= (NewBulletinListener) getActivity();
                if(this.selectedCourse!=null)
                    listener.onSubmitNewBulletin(editText.getText().toString(),this.selectedCourse);

                this.dismiss();
            }catch (ClassCastException e){
                e.printStackTrace();
            }
        }).setNegativeButton("Cancel",(dialogInterface, i) -> {
            this.dismiss();
        });
        return builder.create();
    }

    public interface NewBulletinListener{
        void onSubmitNewBulletin(String newBulletin,String courseName);
    }
}
