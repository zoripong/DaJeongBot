package com.dajeong.chatbot.dajeongbot.model;

import android.widget.ImageView;

/**
 * Created by s2017 on 2018-08-31.
 */

public class Memory {
    private  int eventId;
    private String image;
    private String content;

    public Memory(int eventId, String image, String content) {
        this.eventId = eventId;
        this.image = image;
        this.content = content;
    }

    public int getEventId() {
        return eventId;
    }

    public String getImage() {
        return image;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Memory{" +
                "image=" + image +
                ", content='" + content + '\'' +
                '}';
    }
}
