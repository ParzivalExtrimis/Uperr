package com.aryan.uperr.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.aryan.uperr.Fragments.EditBoxFragment;
import com.aryan.uperr.MainActivity;
import com.aryan.uperr.R;
import com.aryan.uperr.models.AlarmModel;
import com.aryan.uperr.utils.DatabaseHandler;

import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {
    private List<AlarmModel> alarmList;
    private final MainActivity mainActivity;
    public DatabaseHandler db;

    public AlarmAdapter(MainActivity mainActivity, DatabaseHandler db) {

        this.mainActivity = mainActivity;
        this.db = db;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_list_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        db.openDatabase();

        AlarmModel item = alarmList.get(position);


//        //get hours from model in the correct format.
//        if((item.getTime().get(Calendar.HOUR_OF_DAY) / 10) < 1) {
//            str_hour = "0" + item.getTime().get(Calendar.HOUR_OF_DAY);
//        } else {
//            str_hour = Integer.toString(item.getTime().get(Calendar.HOUR_OF_DAY));
//        }

//        //get minutes from model in the right format0.
//        if((item.getTime().get(Calendar.MINUTE) / 10) < 1) {
//            str_min = "0" + item.getTime().get(Calendar.HOUR_OF_DAY);
//        } else {
//            str_min = Integer.toString(item.getTime().get(Calendar.MINUTE));
//        }
//        str_time += str_hour + " : " + str_min;

        db.insertAlarm(item);

        viewHolder.titleDisplay.setText(item.getName());
        viewHolder.timeDisplay.setText(item.getTime());
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Button deleteButton;
        TextView titleDisplay, timeDisplay;
        public ViewHolder(View view) {
            super(view);
            deleteButton = view.findViewById(R.id.b_delete_alarm);
            titleDisplay = view.findViewById(R.id.title_display);
            timeDisplay = view.findViewById(R.id.time_display);
        }
    }

    public void editItem(int position) {
        AlarmModel model = alarmList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", model.getId());
        bundle.putString("name", model.getName());
        bundle.putInt("status",model.getStatus());
        bundle.putString("time", model.getTime());

        EditBoxFragment fragment= new EditBoxFragment();
        fragment.setArguments(bundle);
        fragment.show(mainActivity.getSupportFragmentManager(), EditBoxFragment.TAG);
    }

    public void setAlarmList(List<AlarmModel> alarmList) {
        this.alarmList = alarmList;
        notifyDataSetChanged();
    }

    public Context getContext() {
        return mainActivity;
    }

    public boolean toBoolean(int n) {
        return n > 0;
    }
}
