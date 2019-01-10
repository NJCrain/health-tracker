package com.njcrain.android.healthtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;

public class ExerciseLogActivity extends AppCompatActivity {
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_log);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").build();
    }
}
