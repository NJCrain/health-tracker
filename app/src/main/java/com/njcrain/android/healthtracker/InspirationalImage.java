package com.njcrain.android.healthtracker;

public class InspirationalImage {
    private int id;
    private String caption;

    public InspirationalImage(int src, String caption) {
        this.id = src;
        this.caption = caption;
    }

    public int getId() {
        return this.id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
    public void setId(int id) {
        this.id = id;
    }
}
