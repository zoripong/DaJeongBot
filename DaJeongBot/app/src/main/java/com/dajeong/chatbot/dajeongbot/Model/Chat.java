package com.dajeong.chatbot.dajeongbot.Model;

import java.util.Date;

public class Chat {
    private Character sender; // null 일 경우 User
    private String content;
    private Date time;

    public Chat(Character sender, String content, Date time) {
        this.sender = sender;
        this.content = content;
        this.time = time;
    }

    public Character getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public Date getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "sender=" + sender +
                ", content='" + content + '\'' +
                ", time=" + time +
                '}';
    }
}
