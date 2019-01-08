package com.njcrain.android.healthtracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int clicks = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void incrementExcercise(View v) {
        clicks++;
        TextView textView = findViewById(R.id.textView);
        textView.setText("Clicked: " + clicks + " times");
    }
}
