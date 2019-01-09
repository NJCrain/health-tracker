package com.njcrain.android.healthtracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "channelId";
    private int notificationId = 1;
    private static final long DELAY = 5000;

    int imageIdx;
    InspirationalImage[] images = {(new InspirationalImage(R.drawable.image_1, "This could be you in 500 button clicks")),
                                   (new InspirationalImage(R.drawable.buff_seagull, "This Seagull used the app, and look at them now!")),
                                   (new InspirationalImage(R.drawable.puppy, "Here's a cute puppy for inspiration"))};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView imgCaption = findViewById(R.id.captionText);
        imgCaption.setText(images[0].getCaption());

        imageIdx = 0;
        createNotificationChannel();
    }

    //Runs when the "click me" button is clicked, increments the total number of clicks and updates the display text
    public void goToFingerClickerExercise(View v) {
        Intent goToFingerClicker = new Intent(this, FingerClickerActivity.class);
        startActivity(goToFingerClicker);
    }

    public void goToStopwatchActivity(View v) {
        Intent goToStopwatch = new Intent(this, StopwatchActivity.class);
        startActivity(goToStopwatch);
    }






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

    //The creation for the notification also comes from https://developer.android.com/training/notify-user/build-notification
    //The PendingIntent
    public void enableNotifications(View v) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Water Time")
                .setContentText("Time to drink some water!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId++, mBuilder.build());
    }

//    //This comes from the Google docs on creating a notification (https://developer.android.com/training/notify-user/build-notification)
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Health Tracker";
            String description = "Water";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
