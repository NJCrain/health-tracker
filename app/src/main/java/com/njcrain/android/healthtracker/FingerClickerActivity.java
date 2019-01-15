package com.njcrain.android.healthtracker;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FingerClickerActivity extends AppCompatActivity {

    int clicks = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_clicker);
    }

    //Runs when the "click me" button is clicked, increments the total number of clicks and updates the display text
    public void incrementExercise(View v) {
        clicks++;
        TextView textView = findViewById(R.id.textView);
        textView.setText("Clicked " + clicks + " times");
    }
}
