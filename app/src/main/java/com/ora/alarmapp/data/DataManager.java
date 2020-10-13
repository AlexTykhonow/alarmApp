package com.ora.alarmapp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ora.alarmapp.util.Rekord;

import java.util.ArrayList;

public class DataManager {
    private SQLiteDatabase db;

    public DataManager(Context context){

    }

    public static void clearDatabase(Context context){

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM alarm_notes WHERE 1");
    }

    public static ArrayList<Rekord> getExistAlarms(Context context){
        SQLiteDatabase db;
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        ArrayList<Rekord> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT note_id FROM alarm_notes", null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Rekord tmp = new Rekord();
            tmp.setNoteID(cursor.getInt(cursor.getColumnIndex("note_id")));
            list.add(tmp);
        }
        cursor.close();
        return list;
    }
    public static void setRekords(Context context,ArrayList<Rekord> rekords) {
        SQLiteDatabase db;
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery("Select * From alarm_notes", null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Rekord rekord = new Rekord();
            rekord.setAlarmTime(c.getLong(c.getColumnIndex("alarm_time")));
            rekord.setTextNotification(c.getString(c.getColumnIndex("content")));
            rekord.setNoteID(c.getInt(c.getColumnIndex("note_id")));
            rekord.setactive(c.getInt(c.getColumnIndex("active")));
            rekords.add(rekord);
        }
        c.close();
    }

    public static Rekord lastItem(Context context) {
        SQLiteDatabase db;
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM alarm_notes ORDER BY note_id DESC LIMIT 1;", null);
        c.moveToFirst();
        Rekord rekord = new Rekord();
        rekord.setAlarmTime(c.getLong(c.getColumnIndex("alarm_time")));
        rekord.setTextNotification(c.getString(c.getColumnIndex("content")));
        rekord.setNoteID(c.getInt(c.getColumnIndex("note_id")));
        rekord.setactive(c.getInt(c.getColumnIndex("active")));

        c.close();
        return rekord;
    }
}
