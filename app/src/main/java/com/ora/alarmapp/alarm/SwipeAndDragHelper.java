package com.ora.alarmapp.alarm;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.os.Handler;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.ora.alarmapp.MainActivity;
import com.ora.alarmapp.data.DBmethods;
import com.ora.alarmapp.util.Rekord;
import com.ora.alarmapp.util.animation;
import com.ora.alarmapp.viewAdapter;

import java.util.ArrayList;

import static com.ora.alarmapp.MainActivity.refresh;


public class SwipeAndDragHelper extends ItemTouchHelper.Callback {
    private rekordDataAdapter mAdapter;
    private Context context;
    private ArrayList<Rekord> rekords = new ArrayList<>();
    RecyclerView recyclerViewAlarm;


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(0, swipeFlags);
    }
    public SwipeAndDragHelper(rekordDataAdapter contract, Context context, ArrayList<Rekord> rekords, RecyclerView recyclerViewAlarm) {
        this.mAdapter = contract;
        this.context = context;
        this.rekords = rekords;
        this.recyclerViewAlarm = recyclerViewAlarm;

    }

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

                    Handler handler2 = new Handler();
                    handler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            viewAdapter.setAlarmWindow(rekords.get(position).getNoteID(),context, recyclerViewAlarm, mAdapter);
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
            refresh(rekords,context, recyclerViewAlarm, mAdapter);
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
}
