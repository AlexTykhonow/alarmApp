package com.ora.alarmapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import static com.ora.alarmapp.Constants.ALARM_RECEIVER_POWER_MANAGER_TAG;

public class AlarmReceiver extends BroadcastReceiver {

    private static DBHelper dbHelper = null;
    private static SQLiteDatabase db = null;

    private final String TAG = "[ALARM - RECEIVER]";


    public void onReceive(final Context context, Intent intent) {

        if (dbHelper == null)
            dbHelper = new DBHelper(context);
        if(db == null)
            db = dbHelper.getWritableDatabase();

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP, ALARM_RECEIVER_POWER_MANAGER_TAG);
        wl.acquire(10000 /*5 seconds*/);

        int id =  intent.getIntExtra("requestCode", 0);
        Cursor c = db.rawQuery("Select content, alarm_time from alarm_notes Where note_id = '"+id+"'",null);
        Log.e("ALARM MANAGER HELPER","IS recieve");
        if (c.moveToFirst()) {
            String noteText = c.getString(c.getColumnIndex("content"));
            String alarmTime = c.getString(c.getColumnIndex("alarm_time"));
            c.close();
            //Toast.makeText(context, "IS recieve "+noteText+" | "+alarmTime, Toast.LENGTH_LONG).show(); // For example
            //Log.e("ALARM MANAGER HELPER","IS recieve"+noteText+" | "+alarmTime);
            NotificationHelper.showNotification(context, id, noteText, alarmTime);
        }
        Calendar time = Calendar.getInstance();
        DBmethods.DBUpdate(context,id,time.getTimeInMillis(),0);
        wl.release();
    }

}