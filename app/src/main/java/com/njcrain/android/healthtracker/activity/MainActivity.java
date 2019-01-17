package com.njcrain.android.healthtracker.activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.njcrain.android.healthtracker.NotificationPublisher;
import com.njcrain.android.healthtracker.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ID = 12;
    private static final String CHANNEL_ID = "channelId";
    private static final long DELAY = 5000;
    private ImageView avatar;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        avatar = findViewById(R.id.avatar);

        preferences = getPreferences(Context.MODE_PRIVATE);

        displayAvatar();
    }

    //Runs when the "Clicker Exercise" button is clicked, sends the user to the FingerClickerActivity
    public void goToFingerClickerExercise(View v) {
        Intent goToFingerClicker = new Intent(this, FingerClickerActivity.class);
        startActivity(goToFingerClicker);
    }

    //Runs when the "Stopwatch" button is clicked, sends the user to the StopwatchActivity
    public void goToStopwatchActivity(View v) {
        Intent goToStopwatch = new Intent(this, StopwatchActivity.class);
        startActivity(goToStopwatch);
    }

    //Runs when the "Exercise Log" button is clicked, sends the user to the ExerciseLogActivity
    public void goToExerciseLogActivity(View v) {
        Intent goToExerciseLog = new Intent(this, ExerciseLogActivity.class);
        startActivity(goToExerciseLog);
    }

    //Runs when the "Exercise Log" button is clicked, sends the user to the ExerciseLogActivity
    public void goToImageGalleryActivity(View v) {
        Intent goToImageGallery = new Intent(this, ImageGalleryActivity.class);
        startActivity(goToImageGallery);
    }

    //The Intent/Pending Intent comes from https://gist.github.com/BrandonSmith/6679223
    public void enableNotifications(View v) {
        Intent intent = new Intent(this, NotificationPublisher.class);
        intent.putExtra("notification", createNotification());
        intent.putExtra("notificationId", 1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + DELAY, pendingIntent);

    }

    //The creation for the notification also comes from https://developer.android.com/training/notify-user/build-notification
    public Notification createNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Water Time")
                .setContentText("Time to drink some water!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        return mBuilder.build();
    }
//
//    //This comes from the Google docs on creating a notification, specifically on giving it a channel (https://developer.android.com/training/notify-user/build-notification)
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

    public void disableNotifications(View v) {

        Intent intent = new Intent(this, NotificationPublisher.class);
        intent.putExtra("notification", createNotification());
        intent.putExtra("notificationId", 1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    //checks if the user has set their own avatar. If they haven't, display the default. If they have, display that image
    private void displayAvatar() {
        SharedPreferences.Editor editor = preferences.edit();
        if (!preferences.contains("avatarUri")) {
            avatar.setImageResource(R.drawable.defaultuser);
        } else {
            File image = new File(preferences.getString("avatarUri", ""));
            Bitmap avatarImage = BitmapFactory.decodeFile(image.getAbsolutePath());
            avatar.setImageBitmap(avatarImage);
        }

    }

    //---------Everything below this line includes code from https://developer.android.com/training/camera/photobasics and https://developer.android.com/training/permissions/requesting------------
    public void updateAvatar() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 1);
            }
        }

    }

    public void verifyPermissions(View v) {
        //Check permissions before operating, request them if needed.
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_ID);
        } else {
            updateAvatar();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    updateAvatar();
                } else {
                    //TODO: find a good way to handle permissions were denied
                }
                return;
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            avatar.setImageBitmap(imageBitmap);
            displayAvatar();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        preferences.edit().putString("avatarUri", image.getAbsolutePath()).apply();
        return image;
    }
}
