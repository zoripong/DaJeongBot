package com.dajeong.chatbot.dajeongbot.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class Chat {
    private int nodeType;
    private Character sender; // null 일 경우 User
    private String content;
    private String time;
    private JsonArray optionList;
    private ArrayList<Memory> carouselList;

    public Chat(int nodeType, Character sender, String content, String time) {
        this.nodeType = nodeType;
        this.sender = sender;
        this.content = content;
        this.time = time;
        this.optionList = null;
        this.carouselList = null;
    }

    public Chat(int nodeType, Character sender, String content, String time, JsonArray optionList) {
        this.nodeType = nodeType;
        this.sender = sender;
        this.content = content;
        this.time = time;
        this.optionList = optionList;
        this.carouselList = null;
    }

    public Chat(int nodeType, Character sender, String content, String time, ArrayList<Memory> carouselList) {
        this.nodeType = nodeType;
        this.sender = sender;
        this.content = content;
        this.time = time;
        this.optionList = null;
        this.carouselList = carouselList;
    }


    public JsonArray getOptionList() {
        return optionList;
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


    @Override
    public String toString() {
        return "Chat{" +
                "nodeType=" + nodeType +
                ", sender=" + sender +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", optionList=" + optionList +
                ", carouselList=" + carouselList +
                '}';
    }
}
