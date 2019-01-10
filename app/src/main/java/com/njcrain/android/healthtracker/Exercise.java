package com.njcrain.android.healthtracker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Exercise {
    @PrimaryKey
    public int id;
    public String title;
    public int quantity;
    public String description;
    public String timestamp;

    public Exercise(String title, int quantity, String description, String timestamp) {
        this.title = title;
        this.quantity = quantity;
        this.description = description;
        this.timestamp = timestamp;
    }
}
