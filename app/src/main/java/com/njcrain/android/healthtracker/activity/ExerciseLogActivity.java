package com.njcrain.android.healthtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.njcrain.android.healthtracker.database.AppDatabase;

import java.util.Date;

public class ExerciseLogActivity extends AppCompatActivity {
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_log);
        //Use of allowMainThreadQueries comes from https://stackoverflow.com/questions/44167111/android-room-simple-select-query-cannot-access-database-on-the-main-thread
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "exercise").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        if (db.exerciseDao().getAll().isEmpty()) {
            db.exerciseDao().add(new Exercise("Pushups", 15, "did pushups", "1/9/19 7:00PM"));
        }

        ArrayAdapter<Exercise> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, db.exerciseDao().getAll());
        ListView listView = findViewById(R.id.exerciseList);
        listView.setAdapter(adapter);

    }

    public void createExercise(View v) {
        EditText title = findViewById(R.id.exerciseTitle);
        EditText description = findViewById(R.id.description);
        EditText quantity = findViewById(R.id.quantity);

        Date now = new Date();
        String timestamp = DateFormat.format("M/d/yy h:mma", now).toString();

        Exercise toAdd = new Exercise(title.getText().toString(),  Integer.parseInt(quantity.getText().toString()), description.getText().toString(), timestamp);
        db.exerciseDao().add(toAdd);

        title.setText("");
        description.setText("");
        quantity.setText("");

        ArrayAdapter<Exercise> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, db.exerciseDao().getAll());
        ListView listView = findViewById(R.id.exerciseList);
        listView.setAdapter(adapter);
    }
}
