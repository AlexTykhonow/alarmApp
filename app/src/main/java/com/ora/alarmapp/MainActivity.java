package com.ora.alarmapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import static com.ora.alarmapp.DBmethods.printAllNotes;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerViewAlarm;
    EditText editTextAlarm;
    Button buttonAlarm;
    Context context;
    ArrayList<Rekord> rekords = new ArrayList<>();
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

        setRekords();
        mAdapter = new rekordDataAdapter(rekords);
        setUpRecyclerView();
        buttonAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTextAlarm.getText().toString().isEmpty()){
                    //todo начинати з setalarm и запись в базу даних еще надо адаптер добавить
                    Calendar calNow = Calendar.getInstance();
                    Log.i("oraLog",calNow.getTimeInMillis()+" + ");
                    DBmethods.DBinsert(context,editTextAlarm.getText().toString(),calNow.getTimeInMillis());
                    rekords.add(rekords.size(), lastItem());
                    mAdapter.notifyItemInserted(rekords.size());
                    editTextAlarm.setText("");
                }
            }
        });
        printAllNotes(context);
    }
    private void setUpRecyclerView() {
        recyclerViewAlarm.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper touchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback( 0,
                        ItemTouchHelper.LEFT |ItemTouchHelper.RIGHT) {
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                        return true;// true if moved, false otherwise
                    }
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        final int position = viewHolder.getAdapterPosition();
                        if (direction == ItemTouchHelper.LEFT) {
                            animation.startAnimationFrame();
                            Handler handler = new Handler();

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //mAdapter.rekords.remove(position);
                                    //mAdapter.notifyItemRemoved(position);
                                    //mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
                                    Handler handler2 = new Handler();
                                    handler2.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            setAlarmWindow(rekords.get(position).getNoteID());
                                            animation.stopAnimationFrame();
                                        }
                                    }, 400);
                                }
                            }, 100);
                        }
                        else if (direction == ItemTouchHelper.RIGHT) {
                            DBmethods.DBdelete(context,rekords.get(position).getNoteID());
                            AlarmManagerHelper.cancelAlarm(context, rekords.get(position).getNoteID());
                            rekords.remove(rekords.get(position));
                            mAdapter.notifyItemRemoved(position);
                            mAdapter.notifyItemInserted(position);
                            refresh();
                        }
                    }
                    //speed of swipe
                    @Override
                    public float getSwipeEscapeVelocity(float defaultValue) {
                        return super.getSwipeEscapeVelocity(500.0f * defaultValue);
                    }
                    @Override
                    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        View itemView = viewHolder.itemView;
                        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                            //if warurunek budzika
                            if (dX > 0) {
                                Paint paint = new Paint();
                                paint.setColor(Color.WHITE);
                                paint.setStyle(Paint.Style.FILL);
                                c.drawPaint(paint);

                                paint.setColor(Color.BLACK);
                                paint.setTextSize(40);
                                c.drawText("delete", itemView.getLeft()+20, itemView.getTop()+itemView.getHeight() / 2, paint);


                            } else {
                                Paint paint = new Paint();
                                paint.setColor(Color.WHITE);
                                paint.setStyle(Paint.Style.FILL);
                                c.drawPaint(paint);

                                paint.setColor(Color.BLACK);
                                paint.setTextSize(40);
                                c.drawText("alarm", itemView.getRight()-180, itemView.getTop()+itemView.getHeight() / 2, paint);
                            }
                        }else if(actionState == ItemTouchHelper.ANIMATION_TYPE_SWIPE_SUCCESS){
                            recyclerView.clearAnimation();
                        }
                    }
                });
        mAdapter.setTouchHelper(touchHelper);
        recyclerViewAlarm.setAdapter(mAdapter);
        touchHelper.attachToRecyclerView(recyclerViewAlarm);
    }
    public void setAlarmWindow(final int idAlarm){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.time_picker);

        dialog.getWindow().setDimAmount(0f);
        final Calendar c = Calendar.getInstance();
        TextView headerOfTimePicker = dialog.findViewById(R.id.headerOfTimePicker);

        headerOfTimePicker.setText(getString(R.string.selectHour));

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
                refresh();

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
                refresh();

            }
        });
        dialog.show();
        refresh();
    }
    public void refresh(){
        rekords.clear();
        recyclerViewAlarm.setItemViewCacheSize(0);
        setRekords();
        setUpRecyclerView();

    }
    public void setRekords(){
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
    public Rekord lastItem(){
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