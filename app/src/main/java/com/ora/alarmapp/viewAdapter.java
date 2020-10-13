package com.ora.alarmapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.recyclerview.widget.RecyclerView;

import com.ora.alarmapp.alarm.AlarmManagerHelper;
import com.ora.alarmapp.alarm.rekordDataAdapter;
import com.ora.alarmapp.data.DBmethods;

import java.util.Calendar;

public class viewAdapter extends MainActivity {
    public static void setAlarmWindow(final int idAlarm, final Context context, final RecyclerView recyclerViewAlarm, final rekordDataAdapter mAdapter){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.time_picker);

        dialog.getWindow().setDimAmount(0f);
        final Calendar c = Calendar.getInstance();
        TextView headerOfTimePicker = dialog.findViewById(R.id.headerOfTimePicker);

        headerOfTimePicker.setText(context.getString(R.string.selectHour));

        final TimePicker timePicker=dialog.findViewById(R.id.timePicker);
        timePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(c.get(Calendar.MINUTE));

        Button cancelTimePicker = dialog.findViewById(R.id.cancelTimePicker);
        Button applyTimePicker = dialog.findViewById(R.id.applyTimePicker);

        cancelTimePicker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                AlarmManagerHelper.cancelAlarm(context, idAlarm);
                DBmethods.DBUpdate(context,idAlarm,c.getTimeInMillis(),0);
                dialog.cancel();
                refresh(rekords,context, recyclerViewAlarm, mAdapter);

            }
        });

        applyTimePicker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    c.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                    c.set(Calendar.MINUTE, timePicker.getMinute());
                }else {
                    c.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    c.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                }
                c.set(Calendar.SECOND, 0);
                Calendar check = Calendar.getInstance();
                if(check.getTimeInMillis()>c.getTimeInMillis()){
                    c.add(Calendar.HOUR_OF_DAY, 24);
                }

                DBmethods.DBUpdate(context,idAlarm,c.getTimeInMillis(),1);
                AlarmManagerHelper.setAlarm(context, idAlarm,c.getTimeInMillis());
                dialog.cancel();
                refresh(rekords,context, recyclerViewAlarm, mAdapter);

            }
        });
        dialog.show();
        refresh(rekords,context, recyclerViewAlarm, mAdapter);
    }

}
