package com.njcrain.android.healthtracker;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Exercise {
    @PrimaryKey
    public int id;
    public String title;
    public int quantiy;
    public String description;
    public Date timestamp;
}
