package com.ora.alarmapp;

public class Rekord {
    private String content;
    private Long Data_Time;
    private Integer note_id, active;

    public String getTextNotification() {
        return content;
    }

    public void setTextNotification(String content) {
        this.content = content;
    }

    public Integer getNoteID() {
        return note_id;
    }

    public void setNoteID(Integer note_id) {
        this.note_id = note_id;
    }

    public Long getAlarmTime() {
        return Data_Time;
    }

    public void setAlarmTime(Long Data_Time) {
        this.Data_Time = Data_Time;
    }

    public Integer getactive() {
        return active;
    }

    public void setactive(Integer active) {
        this.active = active;
    }

}
