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
import android.database.Cursor;
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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    private boolean CAMERA_PERMISSION;
    private boolean FILES_PERMISSION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        avatar = findViewById(R.id.avatar);

        preferences = getSharedPreferences("userPrefs", 0);

        if (!preferences.contains("username")) {
            preferences.edit().putString("username", "USERNAME_HERE").apply();
        }

        TextView username = findViewById(R.id.username_main);
        username.setText(preferences.getString("username", ""));

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
        if (!preferences.contains("avatarUri")) {
            avatar.setImageResource(R.drawable.defaultuser);
        } else {
            File image = new File(preferences.getString("avatarUri", ""));
            Bitmap avatarImage = BitmapFactory.decodeFile(image.getAbsolutePath());
            avatar.setImageBitmap(avatarImage);
        }

    }

    public void viewProfile(View v) {
        Intent goToProfile = new Intent(this, ProfileActivity.class);
        startActivity(goToProfile);
    }

    //Called from the select image button, starts an intent to let the user pick an image already saved on their phone
    public void selectAvatar(View v) {
        verifyPermissions();
        if (FILES_PERMISSION) {
            //This comes from https://stackoverflow.com/questions/5309190/android-pick-images-from-gallery
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");

            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");

            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

            startActivityForResult(chooserIntent, 2);
        }
    }

    //---------Everything below this line includes code from https://developer.android.com/training/camera/photobasics and https://developer.android.com/training/permissions/requesting------------

    //Called when the user want to upload a new photo. Calls the method to create a new file for that photo, then starts the intent to use the camera
    public void updateAvatar(View v) {
        verifyPermissions();
    if (CAMERA_PERMISSION && FILES_PERMISSION) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.i("FileCreationError", ex.toString());
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

    }

    public void verifyPermissions() {
        //Check permissions before operating, request them if needed.
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_ID);
        } else {
        CAMERA_PERMISSION =true;
        FILES_PERMISSION = true;
        }
    }

    //Code needed to check if the user actually granted permissions after requesting
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CAMERA_PERMISSION = true;
                } else {
                CAMERA_PERMISSION = false;
                } if (grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    FILES_PERMISSION = true;
                } else {
                    FILES_PERMISSION = false;
                }
            }

        }
    }

    //What to do when the activity is returned to in the case of it needed another activity to handle something
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Case of the user took a new picture for their avatar
        if (requestCode == 1 && resultCode == RESULT_OK) {
            displayAvatar();
        }
        //Case of user went to select a photo from saved images. Grab the proper path for that image, then save it in preferences and call displayAvatar().
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri imagePath = data.getData();
            String realPath = getRealPathFromURI(this, imagePath);
            preferences.edit().putString("avatarUri", realPath).apply();
            displayAvatar();
        }
    }

    //Used when the user wants to take a new photo for their avatar. Creates a file to save it to and sets the location to that image in preferences
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

    //This method come from https://stackoverflow.com/questions/3401579/get-filename-and-path-from-uri-from-mediastore
    //It takes the URI that comes back from a selected image and transforms it into a usable path
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
