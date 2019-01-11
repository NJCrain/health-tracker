package com.njcrain.android.healthtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ExerciseLogActivity extends AppCompatActivity {
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_log);
        //Use of allowMainThreadQueries comes from https://stackoverflow.com/questions/44167111/android-room-simple-select-query-cannot-access-database-on-the-main-thread
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "exercise").allowMainThreadQueries().build();
        if (db.exerciseDao().getById(0) == null) {
            db.exerciseDao().add(new Exercise("Pushups", 15, "did pushups", "1/9/19 7:00PM"));
        }

        ArrayAdapter<Exercise> adapter = new ArrayAdapter<Exercise>(this, android.R.layout.simple_list_item_1, db.exerciseDao().getAll());
        ListView listView = findViewById(R.id.exerciseList);
        listView.setAdapter(adapter);
//        TextView textView = findViewById(R.id.exercisePreview);
//        Exercise e = db.exerciseDao().getById(0);
//        textView.setText(e.title + ": " + e.description);
    }
}
