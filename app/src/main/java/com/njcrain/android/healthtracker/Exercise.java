package com.njcrain.android.healthtracker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Exercise {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public int quantity;
    public String description;
    public String timestamp;
    //TODO: Make this just one string for the location via a GeoCoder
    public double latitude;
    public double longitude;

    public Exercise() {}

    public Exercise(String title, int quantity, String description, String timestamp) {
        this.title = title;
        this.quantity = quantity;
        this.description = description;
        this.timestamp = timestamp;
    }

    //TODO: change this for when a location is an address
    public String toString() {
        return this.title + ": " + this.description + " - " + this.quantity + "\n" + this.timestamp + " at " + this.latitude + ", " + this.longitude;
    }
}
