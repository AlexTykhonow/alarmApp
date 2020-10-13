
package com.ora.alarmapp;

import android.annotation.SuppressLint;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

import static com.ora.alarmapp.DatesFactory.getStringFormat;


public class rekordDataAdapter extends RecyclerView.Adapter<rekordDataAdapter.rekordViewHolder> implements View.OnTouchListener {

    public List<Rekord> rekords;

    private ItemTouchHelper touchHelper;

    @Override
    public boolean onTouch(final View v, MotionEvent event) {
        return false;
    }

    public void setTouchHelper(ItemTouchHelper touchHelper) {
        this.touchHelper = touchHelper;
    }


    public class rekordViewHolder extends RecyclerView.ViewHolder {
        private TextView notification,timeOfAlarm;

        public rekordViewHolder(View v) {
            super(v);
            notification = v.findViewById(R.id.notification);
            timeOfAlarm=v.findViewById(R.id.timeOfAlarm);
        }

    }

    public rekordDataAdapter(List<Rekord> rekords) {
        this.rekords = rekords;
    }

    @NonNull
    @Override
    public rekordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        return new rekordViewHolder(itemView);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final rekordViewHolder holder, final int position) {
        final Rekord rekord = rekords.get(position);
        holder.notification.setText(rekord.getTextNotification());
        holder.notification.setTag("text");
        holder.timeOfAlarm.setTag("time");
        Calendar calendar = Calendar.getInstance();
        if(rekord.getactive()!=0){
            holder.timeOfAlarm.setText(getStringFormat(rekord.getAlarmTime()));
        }else{
            holder.timeOfAlarm.setText("");
        }


    }

    @Override
    public int getItemCount() {
        return rekords.size();
    }

}
