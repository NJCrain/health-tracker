package com.njcrain.android.healthtracker.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.njcrain.android.healthtracker.Exercise;
import com.njcrain.android.healthtracker.R;
import com.njcrain.android.healthtracker.database.AppDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExerciseLogActivity extends AppCompatActivity {
    AppDatabase db;
    ArrayAdapter<Exercise> adapter;
    ListView listView;

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

        listView = findViewById(R.id.exerciseList);
        getAllExercises();
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

        getAllExercises();
    }

    private void getAllExercises() {

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, db.exerciseDao().getAll());

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://nc-health-tracker-backend.herokuapp.com/exercises";

        //Requests a JSON array from the backend server hosted at the above url
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Exercise> exercises = new ArrayList<>();
                        for (int i = 0; i < response.length(); i ++) {
                            try {
                                //Parse each object in the response into an exercise by grabbing each value
                                Exercise e = new Exercise();
                                JSONObject jsonExercise =  response.getJSONObject(i);
                                e.title = jsonExercise.getString("title");
                                e.quantity = jsonExercise.getInt("quantity");
                                e.description = jsonExercise.getString("description");
                                e.timestamp = jsonExercise.getString("timestamp");
                                exercises.add(e);
                            } catch (JSONException e) {
                                //TODO: do something for an exception
                            }
                        }
                        //Add all our exercises from the server into the adapter
                        adapter.addAll(exercises);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }

        });

// Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
        //Set the listview to display all the exercises
        listView.setAdapter(adapter);
    }
}
