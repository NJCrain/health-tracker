package com.njcrain.android.healthtracker.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
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

    private static final int REQUEST_ID = 13;
    private boolean COARSE_LOCATION_PERMISSION;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location lastLocation;

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
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();
    }

    public void createExercise(View v) {

        EditText title = findViewById(R.id.exerciseTitle);
        EditText description = findViewById(R.id.description);
        EditText quantity = findViewById(R.id.quantity);

        Date now = new Date();
        String timestamp = DateFormat.format("M/d/yy h:mma", now).toString();



        Exercise toAdd = new Exercise(title.getText().toString(),  Integer.parseInt(quantity.getText().toString()), description.getText().toString(), timestamp);
        if (lastLocation != null) {
            toAdd.latitude = lastLocation.getLatitude();
            toAdd.longitude = lastLocation.getLongitude();
        }
        sendToServer(toAdd);
        db.exerciseDao().add(toAdd);

        title.setText("");
        description.setText("");
        quantity.setText("");
    }


    //-------------------------INTERNET + DATABASE STUFF--------------------------------------------

    private void getAllExercises() {

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, db.exerciseDao().getAll());

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://nc-health-tracker-backend.herokuapp.com/exercises";

        //Requests a JSON array from the backend server hosted at the above url. This comes from https://developer.android.com/training/volley/simple
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
                                e.longitude = jsonExercise.getDouble("longitude");
                                e.latitude = jsonExercise.getDouble("latitude");
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

    private void sendToServer(Exercise e) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://nc-health-tracker-backend.herokuapp.com/exercises" +"?title=" + e.title + "&description=" + e.description + "&quantity=" + e.quantity + "&timestamp=" + e.timestamp + "&latitude=" + e.timestamp + "&longitude=" + e.longitude;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getAllExercises();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }

        });

        queue.add(stringRequest);

    }

    //-----------------------------------PERMISSIONS STUFF -----------------------------------------

    public void verifyPermissions() {
        //Check permissions before operating, request them if needed.
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ID);
        } else {
            COARSE_LOCATION_PERMISSION = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    COARSE_LOCATION_PERMISSION = true;
                } else {
                    COARSE_LOCATION_PERMISSION = false;
                }
            }

        }
    }

    private void getLocation() {
        verifyPermissions();
        if (COARSE_LOCATION_PERMISSION) {
            try {
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    // Logic to handle location object
//                                    Log.i("LocationInfo", location.toString());
                                    lastLocation = location;
                                } else {
                                    Log.i("LocationInfo", "No Location returned");

                                }
                            }
                        });
            } catch (SecurityException e) {
                Log.i("SecurityException", "Something went bad trying to get location", e);
            }
        }
    }

}
