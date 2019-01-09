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

    public void incrementExercise(View v) {
        clicks++;
        TextView textView = findViewById(R.id.textView);
        textView.setText("Clicked: " + clicks + " times");
    }

    public void updateStopWatch(View v) {
        Button startStop = findViewById(R.id.button2);
        Button reset = findViewById(R.id.button3);
        if (!stopWatchRunning) {
            stopWatchRunning = true;
            startTime = System.currentTimeMillis();
            handler.postDelayed(timer, 1);
            startStop.setText("Stop");
            reset.setVisibility(View.INVISIBLE);
        }
        else if (stopWatchRunning) {
            handler.removeCallbacks(timer);
            startStop.setText("Start");
            reset.setVisibility(View.VISIBLE);
            pausedTime += elapsedTime;
            stopWatchRunning = false;
        }

    }

    public void resetStopWatch(View v) {
        TextView timer = findViewById(R.id.textView2);
        pausedTime = 0;
        timer.setText("0:00:00.000");
    }

    //The idea for using a Runnable and Handler came from https://www.c-sharpcorner.com/article/creating-stop-watch-android-application-tutorial/
    //The handler takes a Runnable object, which has a run method that runs when the runnable is called. The callback takes a runnable and how often to run it
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
            timer.setText("" + hours + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + "." + String.format("%03d", milliseconds));

            handler.postDelayed(this, 1);
        }
    };

    public  void nextImage(View v) {
        ImageView image = findViewById(R.id.imageView);
        TextView caption = findViewById(R.id.captionText);
        TextView imageLocation = findViewById(R.id.imageLocation);

        if (imageIdx < images.length -1) {
            imageIdx++;
        } else {
            imageIdx = 0;
        }
        image.setImageResource(images[imageIdx].getId());
        caption.setText(images[imageIdx].getCaption());
        imageLocation.setText("" + (imageIdx + 1) + "/" + images.length);

    }
}
