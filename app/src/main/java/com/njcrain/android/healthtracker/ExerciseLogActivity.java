package com.njcrain.android.healthtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.widget.TextView;

public class ExerciseLogActivity extends AppCompatActivity {
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_log);
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "exercise").allowMainThreadQueries().build();
        if (db.exerciseDao().getById(0) == null) {
            db.exerciseDao().add(new Exercise("Pushups", 15, "did pushups", "1/9/19 7:00PM"));
        }

        TextView textView = findViewById(R.id.exercisePreview);
        Exercise e = db.exerciseDao().getById(0);
        textView.setText(e.title + ": " + e.description);
    }
}
