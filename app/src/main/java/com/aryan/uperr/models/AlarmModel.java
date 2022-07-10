package com.aryan.uperr.models;

import java.util.Calendar;

public class AlarmModel {

    private int id, status;
    private String name;
    private String time;
    private Calendar c_time;

    public AlarmModel(int id) {
        this.id = id;
        this.status = 0;
        this.name = "some random stuff";
        this.time = "00:00";
    }

    public AlarmModel(String name, int status) {
        this.id = 1;
        this.status = status;
        this.name = name;
        this.time = "00:00";
    }

    public AlarmModel(String name, String t, int status) {
        this.id = 1;
        this.status = status;
        this.name = name;
        this.time = t;
    }

    public AlarmModel(int id, String name, String t, int status) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.time = t;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

   // public String getTimeAsString() { return  String.valueOf(c_time); }

    public void setTime(String time) {
        this.time = time;
    }

    public void setId(int id) {
        this.id = id;
    }
}
