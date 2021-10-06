package com.ripalay.taskapp.models;

import java.io.Serializable;

public class NewsModel implements Serializable {
    private String title;
    private String time;

    public NewsModel(String title, String time) {
        this.title = title;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
