package com.aryan.uperr.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.TimeZoneFormat;
import android.os.Build;

import com.aryan.uperr.models.AlarmModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {

    private int COUNTER = 0;

    private static final int VERSION = 1;
    private static final String NAME = "alarm_db";
    private static final String TABLE = "alarm_table";
    private static final String ID = "id";
    private static final String STATUS = "status";
    private static final String ALARM_NAME = "alarm_name";
    private static final String TIME = "time";
    private static final String CREATE_ALARM_TABLE = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s INTEGER)", TABLE, ID, ALARM_NAME, TIME, STATUS);
    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ALARM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        //drop old table
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE));
        //make new
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getReadableDatabase();
    }

    public void insertAlarm(AlarmModel model) {
        ContentValues val = new ContentValues();
        val.put(ID, this.getCounter());
        val.put(ALARM_NAME, model.getName());
        val.put(TIME, model.getTime());
        val.put(STATUS, 0);

        db.insert(TABLE, null, val);
    }

    public int getCounter() {
        COUNTER++;
        return COUNTER;
    }
    public List<AlarmModel> getAllAlarms() {
        List<AlarmModel> alarms;
        alarms = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.query(TABLE, null, null, null, null, null, null, null);
            if (cur != null) {
                if(cur.moveToFirst()) {
                    do {
                        AlarmModel model = new AlarmModel(0);
                        model.setId(cur.getInt(cur.getColumnIndex(ID)));
                        model.setName(cur.getString(cur.getColumnIndex(ALARM_NAME)));
                        model.setTime(cur.getString(cur.getColumnIndex(TIME)));
                        model.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        alarms.add(model);
                    } while (cur.moveToNext());
                    cur.moveToPrevious();
                    COUNTER = cur.getInt(cur.getColumnIndex(ID));
                }
            }

        } finally {
            db.endTransaction();
            cur.close();
        }
        return alarms;
    }

    public void updateStatus(int id, int status) {
        ContentValues val = new ContentValues();
        val.put(STATUS, status);
        db.update(TABLE, val, ID + "=?", new String[] { String.valueOf(id) });
    }

    public void updateName(int id, String name) {
        ContentValues val = new ContentValues();
        val.put(ALARM_NAME, name);
        db.update(TABLE, val, ID + "=?", new String[] { String.valueOf(id) });
    }

    public void updateTime(int id, String time) {
        ContentValues val = new ContentValues();
        val.put(STATUS, time);
        db.update(TABLE, val, ID + "=?", new String[] { String.valueOf(id) });
    }

    public void deleteAlarm(int id) {
        db.delete(TABLE, ID + "=?", new String[] { String.valueOf(id)});
    }

    public Calendar convertToCalendar(String string) throws ParseException {
        try{
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            cal.setTime(sdf.parse(string));
            return cal;
        }
        catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}