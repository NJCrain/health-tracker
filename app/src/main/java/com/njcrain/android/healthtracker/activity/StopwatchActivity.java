package com.njcrain.android.healthtracker.activity;

import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.njcrain.android.healthtracker.R;

public class StopwatchActivity extends AppCompatActivity {

    boolean stopWatchRunning;
    long startTime;
    long elapsedTime;
    long pausedTime;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);

        handler = new Handler();
        stopWatchRunning = false;
    }

    //Based on the stopwatch's current status, will either start or stop it
    public void updateStopWatch(View v) {
        Button startStop = findViewById(R.id.startStop);
        Button reset = findViewById(R.id.reset);
        //If the stop watch is not running, set the time for when it was started and start the runnable thread to handle the continual updating of the stopwatch
        //Also sets the start button to read "stop" and hides the reset button
        if (!stopWatchRunning) {
            stopWatchRunning = true;
            startTime = System.currentTimeMillis();
            handler.postDelayed(timer, 1);
            startStop.setText("Stop");
            reset.setVisibility(View.INVISIBLE);
        }
        //Stops the running of the stopwatch, saves the time it ran for so it can be resumed. Sets the stop buttons text to "start" and shows the reset button
        else if (stopWatchRunning) {
            handler.removeCallbacks(timer);
            startStop.setText("Start");
            reset.setVisibility(View.VISIBLE);
            pausedTime += elapsedTime;
            stopWatchRunning = false;
        }

    }

    //Method called when the reset button is clicked. Sets the stopwatch back to 0 internally and resets the text in the app
    public void resetStopWatch(View v) {
        TextView timer = findViewById(R.id.textView2);
        pausedTime = 0;
        timer.setText("0:00:00.000");
    }

    //The idea for using a Runnable and Handler came from https://www.c-sharpcorner.com/article/creating-stop-watch-android-application-tutorial/
    //The handler takes a Runnable object, which has a run method that runs when the runnable is called. The callback takes a runnable and how often to run it

    //This method calculates the time passed since the stopwatch was started, and adds it to the time it had already run for (if it was previously paused)
    //It then takes that total time running, and converts it into various values to be used for updating the stopwatch text, and then calls itself again.
    public Runnable timer = new Runnable() {
        @Override
        public void run() {
            elapsedTime = System.currentTimeMillis() - startTime;
            long runningTime = pausedTime + elapsedTime;
            TextView timer = findViewById(R.id.textView2);
            long seconds = (runningTime / 1000) % 60;
            long hours = runningTime / 3600000;
            long minutes = runningTime / 60000;
            long milliseconds = runningTime % 1000;
            timer.setText(hours + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + "." + String.format("%03d", milliseconds));

            handler.postDelayed(this, 1);
        }
    };
}
