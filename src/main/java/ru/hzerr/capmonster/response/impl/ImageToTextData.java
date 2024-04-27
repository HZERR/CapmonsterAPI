package ru.hzerr.capmonster.response.impl;

import com.google.gson.annotations.SerializedName;

public class ImageToTextData {

    @SerializedName("text")
    private String text;

    public ImageToTextData() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
