package com.ora.alarmapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ora.alarmapp.data.DBHelper;

public class DBmethods {

    public static void DBinsert(Context context, String text, Long time){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("content", text);
        contentValues.put("alarm_time", time);

        database.insert("alarm_notes", null, contentValues);
        //printAllNotes(context);
    }
    public static void DBUpdate(Context context, Integer id, Long time, Integer active){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("note_id", id);
        contentValues.put("alarm_time", time);
        contentValues.put("active", active);

        database.update("alarm_notes", contentValues, "note_id=" + id, null);
        //printAllNotes(context);
    }
    public static void DBdelete(Context context, Integer id){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase database  = dbHelper.getWritableDatabase();
        database.delete("alarm_notes", "note_id=?", new String[]{String.valueOf(id)});
    }
    public static void printAllNotes(Context context){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor all = db.rawQuery("Select * From alarm_notes", null);
        String[] columns = all.getColumnNames();
        for (all.moveToFirst(); !all.isAfterLast(); all.moveToNext()) {
            String message = "";
            for (String column : columns) {
                message += column+": ";
                switch(all.getType(all.getColumnIndex(column))) {
                    case Cursor.FIELD_TYPE_INTEGER:
                        message += all.getInt(all.getColumnIndex(column))+", ";
                        break;
                    case Cursor.FIELD_TYPE_STRING:
                        message += all.getString(all.getColumnIndex(column))+", ";
                        break;
                    /*case Cursor.FIELD_TYPE_NULL:
                        message += all.getString(all.getColumnIndex(column))+", ";
                        break;*/
                    case Cursor.FIELD_TYPE_FLOAT:
                        message += all.getFloat(all.getColumnIndex(column))+", ";
                        break;
                    default:
                        break;
                }
            }
            Log.e("oraLog", message + " () "+all.getCount());
        }
        all.close();
        Log.e("oraLog","---------------------------------------------------------------------------");
    }


}
