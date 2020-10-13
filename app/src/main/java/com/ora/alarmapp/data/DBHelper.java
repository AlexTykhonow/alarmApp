package com.ora.alarmapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//class for database
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "myDB", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists alarm_notes('note_id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                "'content' TEXT,'alarm_time' TEXT DEFAULT NULL, 'active' INTEGER DEFAULT 0);");
    }
    //when update db use this method
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}