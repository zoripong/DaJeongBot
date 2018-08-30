package com.dajeong.chatbot.dajeongbot.model;

public class Chat {
    private int nodeType;
    private Character sender; // null 일 경우 User
    private String content;
    private String time;

    public Chat(int nodeType, Character sender, String content, String time) {
        this.nodeType = nodeType;
        this.sender = sender;
        this.content = content;
        this.time = time;
    }

    public int getNodeType() {
        return nodeType;
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
                "nodeType=" + nodeType +
                ", sender=" + sender +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
