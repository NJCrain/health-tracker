package com.njcrain.android.healthtracker;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int clicks = 0;
    boolean stopWatchRunning;
    long startTime;
    long elapsedTime;
    long pausedTime;
    Handler handler;
    int imageIdx;
    InspirationalImage[] images = {(new InspirationalImage(R.drawable.image_1, "This could be you in 500 button clicks")),
                                   (new InspirationalImage(R.drawable.image_1, "This will be the second image")),
                                   (new InspirationalImage(R.drawable.image_1, "This will be the third image"))};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView imgCaption = findViewById(R.id.captionText);
        imgCaption.setText(images[0].getCaption());

        handler = new Handler();
        stopWatchRunning = false;
        imageIdx = 0;
    }

    //Runs when the "click me" button is clicked, increments the total number of clicks and updates the display text
    public void incrementExercise(View v) {
        clicks++;
        TextView textView = findViewById(R.id.textView);
        textView.setText("Clicked: " + clicks + " times");
    }

    //Based on the stopwatch's current status, will either start or stop it
    public void updateStopWatch(View v) {
        Button startStop = findViewById(R.id.button2);
        Button reset = findViewById(R.id.button3);
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

    //Method called when the next button is clicked. increments the imageIdx, grabs the corresponding InspirationalImage from images, and updates the imageView and the captionText
    public  void nextImage(View v) {
        ImageView image = findViewById(R.id.imageView);
        TextView caption = findViewById(R.id.captionText);
        TextView imageLocation = findViewById(R.id.imageLocation);

        //Logic so it can repeat the images instead of going out of bounds
        if (imageIdx < images.length -1) {
            imageIdx++;
        } else {
            imageIdx = 0;
        }
        image.setImageResource(images[imageIdx].getId());
        caption.setText(images[imageIdx].getCaption());
        imageLocation.setText((imageIdx + 1) + "/" + images.length);

    }

    //Method called when the prev button is clicked. decrements the imageIdx, grabs the corresponding InspirationalImage from images, and updates the imageView and the captionText
    public  void previousImage(View v) {
        ImageView image = findViewById(R.id.imageView);
        TextView caption = findViewById(R.id.captionText);
        TextView imageLocation = findViewById(R.id.imageLocation);

        //Logic so it can repeat the images instead of going out of bounds
        if (imageIdx > 0) {
            imageIdx--;
        } else {
            imageIdx = images.length - 1;
        }
        image.setImageResource(images[imageIdx].getId());
        caption.setText(images[imageIdx].getCaption());
        imageLocation.setText((imageIdx + 1) + "/" + images.length);

    }
}
