package com.dajeong.chatbot.dajeongbot.model;

import android.widget.ImageView;

/**
 * Created by s2017 on 2018-08-31.
 */

public class Memory {
    private String content;
    private int image;

    public Memory(String content, int image) {
        this.content = content;
        this.image = image;
    }

    public int getImage() {
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
