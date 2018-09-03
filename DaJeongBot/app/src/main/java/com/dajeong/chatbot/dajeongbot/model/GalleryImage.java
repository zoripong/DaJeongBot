package com.dajeong.chatbot.dajeongbot.model;

import java.io.Serializable;

// 핸드폰에서 가져온 이미지 모델
// imgPath : 경로
// selected : 선택 여부
public class GalleryImage implements Serializable {

    private String imgPath;
    private boolean selected;

    public GalleryImage(String imgPath, boolean selected) {
        this.imgPath = imgPath;
        this.selected = selected;
    }

    public String getImgPath() {
        return imgPath;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "GalleryImage{" +
                "imgPath='" + imgPath + '\'' +
                ", selected=" + selected +
                '}';
    }
}
