package com.ora.alarmapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmManagerHelper {
    private AlarmManager alarmManager;
    private Context applicationContext;
    private DataManager dataManager;

    public AlarmManagerHelper(Context context){
        this.applicationContext = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.dataManager = new DataManager(applicationContext);
    }

    public static void setAlarm(Context context, int id, long timeMillis){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, com.ora.alarmapp.AlarmReceiver.class);
        alarmIntent.putExtra("requestCode", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.e("ALARM MANAGER HELPER"," is ok");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            AlarmManager.AlarmClockInfo ac =
                    new AlarmManager.AlarmClockInfo(timeMillis,pendingIntent);
            alarmManager.setAlarmClock(ac, pendingIntent);
            Log.e("ALARM MANAGER HELPER", "set by setExactAndAllowWhileIdle() ID:"+ id+" time "+ timeMillis);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeMillis, pendingIntent);
            Log.e("ALARM MANAGER HELPER", "set by setExactAndAllowWhileIdle() ID:"+ id);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Log.e("ALARM MANAGER HELPER", "set by setExact() ID: "+ id);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeMillis, pendingIntent);
        } else {
            Log.e("ALARM MANAGER HELPER", "set by set()  ID: "+ id);
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeMillis, pendingIntent);
        }
    }

    public static void cancelAlarm(Context context,int id){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(context, com.ora.alarmapp.AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        //Log.e("ALARM MANAGER HELPER", "cancel alarm ID: "+ id);
    }
    public void cancelAll(Context context) {
        ArrayList<Rekord> activeAlarms = dataManager.getExistAlarms(context);
        for (Rekord rekord : activeAlarms) {
            int noteId = rekord.getNoteID();
            cancelAlarm(context, noteId);
        }
    }
}
