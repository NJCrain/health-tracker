package com.njcrain.android.healthtracker.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.njcrain.android.healthtracker.R;

public class FingerClickerActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    int clicks = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_clicker);

        preferences = getSharedPreferences("userPrefs", 0);

        TextView username = findViewById(R.id.username_clicker);
        username.setText(preferences.getString("username", ""));

        clicks = preferences.getInt("clickerScore", 0);
        TextView text = findViewById(R.id.textView);
        text.setText("Clicked " + clicks + " times");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        preferences.edit().putInt("clickerScore", clicks).apply();
    }

    //Runs when the "click me" button is clicked, increments the total number of clicks and updates the display text
    public void incrementExercise(View v) {
        clicks++;
        TextView textView = findViewById(R.id.textView);
        textView.setText("Clicked " + clicks + " times");
    }
}
