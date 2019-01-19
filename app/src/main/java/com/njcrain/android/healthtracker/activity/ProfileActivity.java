package com.njcrain.android.healthtracker.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.njcrain.android.healthtracker.R;

import java.io.File;

public class ProfileActivity extends AppCompatActivity {

    private ImageView avatar;
    private SharedPreferences preferences;
    private TextView header;
    private EditText editUsername;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        avatar = findViewById(R.id.profileAvatar);
        preferences = getSharedPreferences("userPrefs", 0);
        header = findViewById(R.id.username_Big);
        editUsername = findViewById(R.id.username_Edit);

        header.setText(preferences.getString("username", ""));
        editUsername.setHint(preferences.getString("username", ""));

        displayAvatar();
    }

    private void displayAvatar() {
        if (!preferences.contains("avatarUri")) {
            avatar.setImageResource(R.drawable.defaultuser);
        } else {
            File image = new File(preferences.getString("avatarUri", ""));
            Bitmap avatarImage = BitmapFactory.decodeFile(image.getAbsolutePath());
            avatar.setImageBitmap(avatarImage);
        }

    }

}
