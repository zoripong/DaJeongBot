package com.dajeong.chatbot.dajeongbot.model;


public class Event {

    public String eventTitle;
    public String eventCotent;
    public int drawableId;

    public Event(String eventTitle, String eventCotent, int drawableId) {
        this.eventTitle = eventTitle;
        this.eventCotent = eventCotent;
        this.drawableId = drawableId;
    }

    public String getContentSender() {
        return eventCotent;
    }
    public int getDrawableSender() {
        return drawableId;
    }
    @Override
    public String toString() {
        return "Event{" +
                "eventTitle=" + eventTitle +
                ", eventCotent='" + eventCotent + '\'' +
                ", drawableId=" + drawableId +
                '}';
    }
}