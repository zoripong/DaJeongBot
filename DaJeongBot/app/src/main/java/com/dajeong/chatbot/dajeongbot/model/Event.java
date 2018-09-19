package com.dajeong.chatbot.dajeongbot.model;


public class Event {

    private int EventId;
    private String eventTitle;
    private String scheduleWhat;
    private String scheduleWhen;
    private String scheduleWhere;
    private int drawableId;

    public int getEventId() {
        return EventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public String getScheduleWhat() {
        return scheduleWhat;
    }

    public String getScheduleWhen() {
        return scheduleWhen;
    }

    public String getScheduleWhere() {
        return scheduleWhere;
    }

    public int getDrawableId() {
        return drawableId;
    }

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