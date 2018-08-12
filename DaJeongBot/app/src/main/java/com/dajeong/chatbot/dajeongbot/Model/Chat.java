package com.dajeong.chatbot.dajeongbot.Model;

import java.util.Date;

public class Chat {
    private int ChatType;
    private Character sender; // null 일 경우 User
    private String content;
    private String time;

    public Chat(int chatType, Character sender, String content, String time) {
        ChatType = chatType;
        this.sender = sender;
        this.content = content;
        this.time = time;
    }

    public int getChatType() {
        return ChatType;
    }

    public Character getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "ChatType=" + ChatType +
                ", sender=" + sender +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
