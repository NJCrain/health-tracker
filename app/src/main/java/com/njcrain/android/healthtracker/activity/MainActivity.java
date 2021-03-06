package com.njcrain.android.healthtracker.activity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.njcrain.android.healthtracker.NotificationPublisher;
import com.njcrain.android.healthtracker.R;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "channelId";
    public static final long DELAY = 7200000;
    private ImageView avatar;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        avatar = findViewById(R.id.avatar);

        preferences = getSharedPreferences("userPrefs", 0);

        TextView username = findViewById(R.id.username_main);
        TextView scoreText = findViewById(R.id.scoreText);
        TextView visitsText = findViewById(R.id.visitsText);
        Button editProfile = findViewById(R.id.button7);
        Button login = findViewById(R.id.login);
        Button signup = findViewById(R.id.signup);
        Button logout = findViewById(R.id.logout);
        Button exercises = findViewById(R.id.button3);

        if (preferences.contains("token")) {
            username.setVisibility(View.VISIBLE);
            scoreText.setVisibility(View.VISIBLE);
            visitsText.setVisibility(View.VISIBLE);
            editProfile.setVisibility(View.VISIBLE);
            avatar.setVisibility(View.VISIBLE);
            login.setVisibility(View.INVISIBLE);
            signup.setVisibility(View.INVISIBLE);
            logout.setVisibility(View.VISIBLE);
            exercises.setVisibility(View.VISIBLE);
        } else {
            preferences.edit().putInt("homeVisits", preferences.getInt("homeVisits", 0) + 1).apply();
            username.setText(preferences.getString("username", ""));
            scoreText.setText("Clicker Exercise Score: " + preferences.getInt("clickerScore", 0));
            visitsText.setText("Home Page Visits: " + preferences.getInt("homeVisits", 0));
        }
        displayAvatar();
    }

    @Override
    protected void onResume() {
        super.onResume();

        preferences.edit().putInt("homeVisits", preferences.getInt("homeVisits", 0) + 1).apply();

        TextView scoreText = findViewById(R.id.scoreText);
        TextView visitsText = findViewById(R.id.visitsText);

        scoreText.setText("Clicker Exercise Score: " + preferences.getInt("clickerScore", 0));
        visitsText.setText("Home Page Visits: " + preferences.getInt("homeVisits", 1));
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

    //Runs when the "Login" button is clicked, sends the user to the ExerciseLogActivity
    public void goToLoginActivity(View v) {
        Intent goToLogin = new Intent(this, LoginActivity.class);
        startActivityForResult(goToLogin, 13);
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
        startActivityForResult(goToProfile, 4);
    }

    public void performLogout(View v) {
        preferences.edit().remove("token").remove("clickerScore").apply();
        //TODO: add code for sending user stats to db. Maybe add it to every activities onDestroy too?
        recreate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Case of user went to select a photo from saved images. Grab the proper path for that image, then save it in preferences and call displayAvatar().
        if (requestCode == 4 && resultCode == RESULT_OK) {
            displayAvatar();
            TextView username = findViewById(R.id.username_main);
            username.setText(preferences.getString("username", ""));
        }
        if (requestCode == 13 && resultCode == RESULT_OK) {
            recreate();
        }
    }

}
