package com.ora.alarmapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ora.alarmapp.alarm.AlarmManagerHelper;
import com.ora.alarmapp.alarm.SwipeAndDragHelper;
import com.ora.alarmapp.alarm.rekordDataAdapter;
import com.ora.alarmapp.data.DBHelper;
import com.ora.alarmapp.data.DBmethods;
import com.ora.alarmapp.data.DataManager;
import com.ora.alarmapp.util.Rekord;
import com.ora.alarmapp.util.animation;

import java.util.ArrayList;
import java.util.Calendar;

import static com.ora.alarmapp.data.DBmethods.printAllNotes;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerViewAlarm;
    EditText editTextAlarm;
    Button buttonAlarm;
    Context context;
    static ArrayList<Rekord> rekords = new ArrayList<>();
    rekordDataAdapter mAdapter;
    SQLiteDatabase db;
    DBHelper dbHelper;
    private int intrinsicWidth, intrinsicHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        recyclerViewAlarm = findViewById(R.id.recyclerViewAlarm);
        editTextAlarm = findViewById(R.id.editTextAlarm);
        buttonAlarm = findViewById(R.id.buttonAlarm);
        //DataManager.clearDatabase(context);

        DataManager.setRekords(context,rekords);
        mAdapter = new rekordDataAdapter(rekords);
        setUpRecyclerView(rekords,context,mAdapter,recyclerViewAlarm);
        buttonAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTextAlarm.getText().toString().isEmpty()){
                    //todo начинати з setalarm и запись в базу даних еще надо адаптер добавить
                    Calendar calNow = Calendar.getInstance();
                    Log.i("oraLog",calNow.getTimeInMillis()+" + ");
                    DBmethods.DBinsert(context,editTextAlarm.getText().toString(),calNow.getTimeInMillis());
                    rekords.add(rekords.size(), DataManager.lastItem(context));
                    mAdapter.notifyItemInserted(rekords.size());
                    editTextAlarm.setText("");
                }
            }
        });
        printAllNotes(context);
    }
    public static void setUpRecyclerView(ArrayList<Rekord> rekords,Context context, rekordDataAdapter mAdapter, RecyclerView recyclerViewAlarm) {
        recyclerViewAlarm.setLayoutManager(new LinearLayoutManager(context));
        SwipeAndDragHelper swipeAndDragHelper = new SwipeAndDragHelper(mAdapter, context, rekords, recyclerViewAlarm);
        ItemTouchHelper touchHelper = new ItemTouchHelper(swipeAndDragHelper);
        mAdapter.setTouchHelper(touchHelper);
        recyclerViewAlarm.setAdapter(mAdapter);
        touchHelper.attachToRecyclerView(recyclerViewAlarm);
    }


    public static void refresh(ArrayList<Rekord> rekords,Context context , RecyclerView recyclerViewAlarm, rekordDataAdapter mAdapter){
        rekords.clear();
        recyclerViewAlarm.setItemViewCacheSize(0);
        DataManager.setRekords(context,rekords);
        setUpRecyclerView(rekords, context, mAdapter, recyclerViewAlarm);
    }
}