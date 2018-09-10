package com.dajeong.chatbot.dajeongbot.model;


public class Event {

    public int EventId;
    public String eventTitle;
    public String scheduleWhat;
    public String scheduleWhen;
    public String scheduleWhere;
    public int drawableId;


    public Event(int EventId, String scheduleWhat, String scheduleWhen, String scheduleWhere, int drawableId) {
        this.scheduleWhat = scheduleWhat;
        this.scheduleWhen = scheduleWhen;
        this.scheduleWhere = scheduleWhere;
        this.EventId = EventId;
        this.drawableId = drawableId;
    }

    public int getDrawableSender() {
        return drawableId;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + EventId + '\'' +
                ", schedule_what='" + scheduleWhat + '\'' +
                ", schedule_when='" + scheduleWhen + '\'' +
                ", schedule_where='" + scheduleWhere + '\'' +
                ", detail='" + scheduleWhere + '\'' +
                ", drawableId=" + drawableId +
                '}';
    }
}