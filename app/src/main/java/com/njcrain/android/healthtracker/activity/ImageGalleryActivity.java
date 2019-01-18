package com.njcrain.android.healthtracker.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.njcrain.android.healthtracker.InspirationalImage;
import com.njcrain.android.healthtracker.R;

public class ImageGalleryActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    int imageIdx;
    InspirationalImage[] images = {(new InspirationalImage(R.drawable.image_1, "This could be you in 500 button clicks")),
            (new InspirationalImage(R.drawable.buff_seagull, "This Seagull used the app, and look at them now!")),
            (new InspirationalImage(R.drawable.puppy, "Here's a cute puppy for inspiration"))};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);
        TextView imgCaption = findViewById(R.id.captionText);
        imgCaption.setText(images[0].getCaption());

        imageIdx = 0;

        preferences = getSharedPreferences("userPrefs", 0);

        TextView username = findViewById(R.id.username_image);
        username.setText(preferences.getString("username", ""));
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
}
