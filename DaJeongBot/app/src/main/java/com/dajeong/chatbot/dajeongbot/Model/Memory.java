package com.dajeong.chatbot.dajeongbot.model;

import android.media.Image;

/**
 * Created by s2017 on 2018-08-31.
 */

public class Memory {
    private String image;
    private String content;

    public Memory(String image, String content) {
        this.image = image;
        this.content = content;
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
