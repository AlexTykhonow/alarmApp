package com.ora.alarmapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

}
