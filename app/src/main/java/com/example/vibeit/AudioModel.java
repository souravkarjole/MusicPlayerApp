package com.example.vibeit;

import java.io.Serializable;

public class AudioModel implements Serializable {
    String data;
    String title;
    String duration;

    public AudioModel(String data, String title, String duration) {
        this.data = data;
        this.title = title;
        this.duration = duration;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
