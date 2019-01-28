package com.njcrain.android.healthtracker.activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.njcrain.android.healthtracker.R;

import org.json.JSONObject;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences("userPrefs", 0);

    }

    public void login(View v) {
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                getToken();
                return null;
            }
        };

        asyncTask.execute();
    }

    private void getToken() {
        String username = ((EditText) findViewById(R.id.username)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=password&username=" + username + "&password=" + password + "&client_id=testjwtclientid&client_secret=XY7kmzoNzl100");
        Request request = new Request.Builder()
                .url("https://nc-health-tracker-backend.herokuapp.com/oauth/token")
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonObject = new JSONObject(response.body().string());
            String token = jsonObject.getString("access_token");
            Log.i("TOKEN", token);
            preferences.edit().putString("token", token).apply();
            getUserDetails();
        } catch (Exception e) {
            Log.i("TOKENREQUEST", e.toString());
        }
    }

    private void getUserDetails() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://nc-health-tracker-backend.herokuapp.com/user")
                .get()
                .addHeader("Authorization", "Bearer " + preferences.getString("token", ""))
                .build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonObject = new JSONObject(response.body().string());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("clickerScore", jsonObject.getInt("clickerScore"));
            editor.putInt("homeVisits", jsonObject.getInt("homePageVisits"));
            editor.putString("username", jsonObject.getString("username"));
            editor.putString("avatarUri", jsonObject.getString("avatarUri"));
            editor.apply();
            setResult(RESULT_OK);
            finish();
        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }
    }
}
