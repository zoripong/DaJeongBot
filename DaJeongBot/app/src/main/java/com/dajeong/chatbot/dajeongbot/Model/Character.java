package com.dajeong.chatbot.dajeongbot.Model;

public class Character {
    private String name;
    private int profile;// 이미지 (R.drawable.---)

    public Character(String name, int profile) {
        this.name = name;
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public int getProfile() {
        return profile;
    }

    @Override
    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                ", profile=" + profile +
                '}';
    }
}
