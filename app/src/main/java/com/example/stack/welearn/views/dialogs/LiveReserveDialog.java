package com.example.stack.welearn.views.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.stack.welearn.R;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Calendar;

import butterknife.ButterKnife;

public class LiveReserveDialog extends DialogFragment {
    DatePicker datePicker;
    TimePicker timePicker;
    Button btnSubmmit;
    TextView txTime;
    EditText editTitle;

    int hour,minute,year,month,day;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View root=inflater.inflate(R.layout.dialog_live_reserve,null);

        datePicker=(DatePicker)root.findViewById(R.id.dp_live_reserve_date);
        timePicker=(TimePicker)root.findViewById(R.id.tp_live_reserve_time);
        txTime=(TextView)root.findViewById(R.id.text_live_reserve_time);
        editTitle=(EditText)root.findViewById(R.id.edit_live_reserve_title);

        initTime();
        builder.setView(root)
                .setPositiveButton("Submit",(dialogInterface, i) -> {
                   try{
                       LiveReserveListener listener=(LiveReserveListener)getActivity();
                       long left=new DateTime(year,month,day,hour,minute, DateTimeZone.UTC).getMillis();
                       listener.onSubmit(left,editTitle.getText().toString());
                       this.getDialog().dismiss();
                   }catch (ClassCastException e){
                       e.printStackTrace();
                   }
                })
                .setNegativeButton("Cancel",(dialogInterface, i) -> {
                    this.getDialog().dismiss();
                });
        return builder.create();
    }

    public  interface LiveReserveListener{
        void onSubmit(long time,String title);
    }

    private void setUpTime(){
        txTime.setText("Reserved at "+year+"/"+month+"/"+day+" "+hour+":"+minute);
    }

    private void initTime(){
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                (dp,year,month,day)->{
                    this.year=year;
                    this.month=month;
                    this.day=day;
                    setUpTime();
                });

        timePicker.setOnTimeChangedListener(
                (tp,hour,minite)->{
                   this.hour=hour;
                   this.minute=minite;
                   setUpTime();
                }
        );


    }
}
