package com.dajeong.chatbot.dajeongbot.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class Chat {
    private int nodeType;
    private int chatType;
    private Character sender; // null 일 경우 User
    private String content;
    private String time;
    private ArrayList<Slot> slotList;
    private ArrayList<Memory> carouselList;

    public Chat(int nodeType, int chatType, Character sender, String content, String time) {
        this.nodeType = nodeType;
        this.chatType = chatType ;
        this.sender = sender;
        this.content = content;
        this.time = time;
        this.slotList = null;
        this.carouselList = null;
    }

    public Chat(int nodeType, int chatType, Character sender, String content, String time, ArrayList<Slot> slotList, ArrayList<Memory> carouselList) {
        this.nodeType = nodeType;
        this.chatType = chatType ;
        this.sender = sender;
        this.content = content;
        this.time = time;
        this.slotList = slotList;
        this.carouselList = carouselList;
    }


    public ArrayList<Slot> getSlotList() {
        return slotList;
    }

    public ArrayList<Memory> getCarouselList() {
        return carouselList;
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

    public int getChatType() {
        return chatType;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "nodeType=" + nodeType +
                ", chatType=" + chatType +
                ", sender=" + sender +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", optionList=" + slotList +
                ", carouselList=" + carouselList +
                '}';
    }
}
