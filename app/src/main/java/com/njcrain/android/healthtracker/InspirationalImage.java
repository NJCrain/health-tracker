package com.njcrain.android.healthtracker;

public class InspirationalImage {
    private String src;
    private String caption;

    public InspirationalImage(String src, String caption) {
        this.src = src;
        this.caption = caption;
    }

    public String getSrc() {
        return this.src;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
    public void setSrc(String src) {
        this.src = src;
    }
}
