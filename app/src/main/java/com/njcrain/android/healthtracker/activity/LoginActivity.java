package com.njcrain.android.healthtracker.activity;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
//import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.njcrain.android.healthtracker.Exercise;
import com.njcrain.android.healthtracker.ParamStringBuilder;
import com.njcrain.android.healthtracker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences("userPrefs", 0);

    }

//    public void login(View v) {
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url ="http://testjwtclientid:XY7kmzoNzl100@172.16.15.49:8080/oauth/token";
////        String url = "http://172.16.15.49:8080/";
//
//        final JSONObject body = new JSONObject();
//        try {
//            body.put("grant_type", "password");
//            body.put("username", "john");
//            body.put("password", "123456");
//        } catch (JSONException e) {
//            Log.i("JSONException", e.toString());
//        }
//        final String reqBody = body.toString();
//
//
//        //Requests a JSON array from the backend server hosted at the above url. This comes from https://developer.android.com/training/volley/simple
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
////                            try {
//////                                String token = response.getString("access_token");
////                                Log.i("ResponseToken", token());
////                            } catch (JSONException e) {
////                                //TODO: do something for an exception
////                            }
//                        Log.i("ResponseToken", response.toString());
//
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("VOLLEY", error.toString());
//            }
//
//        })
//                //These 2 override methods come from https://stackoverflow.com/questions/33573803/how-to-send-a-post-request-using-volley-with-string-body
//        {
//            @Override
//            public String getBodyContentType() {
//                return "application/x-www-form-urlencoded; charset=utf-8";
//            }
//
//            @Override
//            public byte[] getBody() {
//                try {
//                    return reqBody == null ? null : reqBody.getBytes("utf-8");
//                } catch (UnsupportedEncodingException uee) {
//                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", reqBody, "utf-8");
//                    return null;
//                }
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("Content-Type", "application/x-www-form-urlencoded");
//                return params;
//            }
//        };
//
//// Add the request to the RequestQueue.
//        queue.add(jsonObjectRequest);
//
//
//    }
//
//    public void login2(View v) {
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url ="http://testjwtclientid:XY7kmzoNzl100@172.16.15.49:8080/oauth/token";
//
////        String url = "http://172.16.15.49:8080/";
//
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.i("ResponseToken", response);
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("VOLLEY", new String(error.networkResponse.data));
//                    }
//                }) {
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String,String> params = new HashMap<>();
//                params.put("Content-Type","application/x-www-form-urlencoded");
//                return params;
//            }
//
//            @Override
//            public String getBodyContentType() {
//                return "application/x-www-form-urlencoded; charset=utf-8";
//            }
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("grant_type", "password");
//                params.put("username", "john");
//                params.put("password", "123456");
//                return params;
//            }
//
//        };
//
//        queue.add(stringRequest);
//    }

    public void login3(View v) {
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
//        try {
//            URL url = new URL("http://testjwtclientid:XY7kmzoNzl100@172.16.15.49:8080/oauth/token");
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("POST");
//            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            con.setRequestProperty("Postman-Token", "6c2b0ae0-e07c-46ba-88c9-dd5afd28e6eb");
//
////            Map<String, String> parameters = new HashMap<>();
////            parameters.put("grant_type", "password");
////            parameters.put("username", "john");
////            parameters.put("password", "123456");
//
//            con.setDoOutput(true);
//            DataOutputStream out = new DataOutputStream(con.getOutputStream());
//            String outString = "grant_type=password&username=john&password=123456";
//            out.writeBytes(outString);
//            out.flush();
//            out.close();
//
////            Log.i("RESPONSE", con.getErrorStream() + "...");
//
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(con.getErrorStream()));
//            String inputLine;
//            StringBuffer content = new StringBuffer();
//            while ((inputLine = in.readLine()) != null) {
//                content.append(inputLine);
//            }
//            in.close();
//
//            Log.i("SERVERRESPONSE", content.toString());
//
//
//            con.disconnect();
//        } catch (Exception e) {
//            Log.i("UHOH", e.toString());
//        }

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=password&username=john&password=123456");
        Request request = new Request.Builder()
                .url("https://testjwtclientid:XY7kmzoNzl100@nc-health-tracker-backend.herokuapp.com/oauth/token")
                .post(body)
                .addHeader("cache-control", "no-cache")
                .addHeader("Postman-Token", "c38b60be-49ea-4bdd-9575-b7c21dd4f5e8")
                .build();

        try {
            Response response = client.newCall(request).execute();
            Log.i("RESPONSE",response.toString());
        } catch (IOException e) {

        }
    }
}
