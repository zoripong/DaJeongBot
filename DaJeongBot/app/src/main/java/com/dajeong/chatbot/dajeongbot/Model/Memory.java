package com.dajeong.chatbot.dajeongbot.model;

import android.widget.ImageView;

/**
 * Created by s2017 on 2018-08-31.
 */

public class Memory {
    private  int eventId;
    private String image;
    private String content;
    private String detail;
    private String review;

    public Memory(int eventId, String image, String content, String detail, String review) {
        this.eventId = eventId;
        this.image = image;
        this.content = content;
        this.detail = detail;
        this.review = review;
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

    public String getDetail() {
        return detail;
    }

    public String getReview() {
        return review;
    }

    @Override
    public String toString() {
        return "Memory{" +
                "eventId=" + eventId +
                ", image='" + image + '\'' +
                ", content='" + content + '\'' +
                ", detail='" + detail + '\'' +
                ", review='" + review + '\'' +
                '}';
    }
}
