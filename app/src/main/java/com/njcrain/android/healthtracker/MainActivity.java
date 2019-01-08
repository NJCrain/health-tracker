package com.njcrain.android.healthtracker;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int clicks = 0;
    boolean stopWatchRunning;
    long startTime;
    long elapsedTime;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();
        stopWatchRunning = false;
    }

    public void incrementExcercise(View v) {
        clicks++;
        TextView textView = findViewById(R.id.textView);
        textView.setText("Clicked: " + clicks + " times");
    }

    public void updateStopwwatch(View v) {
        Button startStop = findViewById(R.id.button2);
        if (!stopWatchRunning) {
            stopWatchRunning = true;
            startTime = System.currentTimeMillis();
            handler.postDelayed(timer, 1);
            startStop.setText("Stop");
        }
        else if (stopWatchRunning) {
            handler.removeCallbacks(timer);
            startStop.setText("Start");
            stopWatchRunning = false;
        }

    }

    //The idea for using a Runnable and Handler came from https://www.c-sharpcorner.com/article/creating-stop-watch-android-application-tutorial/
    //The handler takes a Runnable object, which has a run method that runs when the runnable is called. The callback takes a runnable and how often to run it
    public Runnable timer = new Runnable() {
        @Override
        public void run() {
            elapsedTime = System.currentTimeMillis() - startTime;

            TextView timer = findViewById(R.id.textView2);
            long seconds = elapsedTime / 1000;
            long hours = seconds / 3600;
            long minutes = seconds / 60;
            long milliseconds = elapsedTime % 1000;
            seconds = seconds % 60;
            timer.setText("" + hours + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + "." + String.format("%03d", milliseconds));

            handler.postDelayed(this, 1);
        }
    };
}
