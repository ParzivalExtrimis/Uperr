package com.aryan.uperr;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aryan.uperr.Fragments.EditBoxFragment;
import com.aryan.uperr.Fragments.TimePickerFragment;
import com.aryan.uperr.adapters.AlarmAdapter;
import com.aryan.uperr.models.AlarmModel;
import com.aryan.uperr.utils.DatabaseHandler;
import com.aryan.uperr.utils.DialogCloseListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DialogCloseListener {

    private RecyclerView alarmListRecyclerView;
    private AlarmAdapter adapter;
    private FloatingActionButton b_newAlarmPop;
    private Button cancelNew;
    private PopupWindow popupWindow;
    private LayoutInflater inflater;
    private View popupView;

    private List<AlarmModel> alarmList;
    public DatabaseHandler db;

    private TextView timeView;
    private TextView titleView;
    private Button b_confirm;
    private String m_hour;
    private String m_min;
    private boolean AlarmIsSet = false;
    private boolean isTimeSet = false;
    private boolean isTItleSet = false;
    private String defaultTime = "12 : 00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toast.makeText(getApplicationContext(), "init", Toast.LENGTH_LONG).show();
        Log.d("test logger", "logging");
        //initialize
        alarmList = new ArrayList<>();

        db = new DatabaseHandler(this);
        db.openDatabase();
        //hide action bar
        if(getSupportActionBar() != null) getSupportActionBar().hide();

        alarmListRecyclerView = findViewById(R.id.main_recycler_view);
        alarmListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AlarmAdapter(this, db);
        alarmListRecyclerView.setAdapter(adapter);

        //test
//        AlarmModel dummy;
//        for(int i = 0; i < 10; i++) {
//            dummy = new AlarmModel(i);
//            alarmList.add(dummy);
//        }
//
        adapter.setAlarmList(alarmList);


        alarmList = db.getAllAlarms();
        Collections.reverse(alarmList);
        adapter.setAlarmList(alarmList);
    }

    @Override
    public void handleDialogClose() {
        alarmList = db.getAllAlarms();
        Collections.reverse(alarmList);
        adapter.setAlarmList(alarmList);
        adapter.notifyDataSetChanged();
    }

    //on confirm
    public void onAlarmSet(View view) {
        if(isTimeSet && isTItleSet) {
            AlarmIsSet = true;
        }


    }


    //popup window definition
    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.activity_new_alarm, null);


        // create the popup window
        int width = RelativeLayout.LayoutParams.MATCH_PARENT;
        int height = RelativeLayout.LayoutParams.MATCH_PARENT;
        popupWindow = new PopupWindow(popupView, width, height);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

//        TextView time_view = popupView.findViewById(R.id.time_view);
//        if(time_view != null && isTimeSet) {
//            time_view.setText(m_hour + " : " + m_min);
//        }
    }

    //cancel button closes pop up
    public void cancelNewButtonPressed(View view) {
        popupWindow.dismiss();
    }

    public void setTime(View view) {
        DialogFragment timeFrag = new TimePickerFragment();
        timeFrag.show(getSupportFragmentManager(), "time picker");
    }
    //gets the time set on time fragment
    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int min) {
        setTimeInCorrectFormat(hour, min);

        TextView time_view = popupView.findViewById(R.id.time_view);
        time_view.setText(m_hour + " : " + m_min);

        //Toast.makeText(this, "time at main " + hour + " : " + min, Toast.LENGTH_LONG).show();
        isTimeSet = true;
    }
    public void setTitle(View view) {
        EditBoxFragment.getInstance().show(getSupportFragmentManager(), EditBoxFragment.TAG);
        isTItleSet = true;
    }

    private void setTimeInCorrectFormat(int hour, int min) {
        //hours
        if((hour/10) < 1) {
            m_hour = "0" + hour;
            Toast.makeText(getApplicationContext(), m_hour, Toast.LENGTH_SHORT).show();
        } else {
            m_hour = String.valueOf(hour);
        }

        //minutes
        if((min/10) < 1) {
            m_min = "0" + min;
        } else {
            m_min = String.valueOf(min);
        }
    }

    @Override
    public void onBackPressed() {

        if(popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        else {
            super.onBackPressed();
        }
    }

}