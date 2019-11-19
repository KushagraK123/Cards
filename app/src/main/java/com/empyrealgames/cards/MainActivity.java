package com.empyrealgames.cards;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {
    public static final String JSON = "JSON";
    public static final String DATA = "DATA";

    @TargetApi(23)
    protected void askPermissions() {
        /*asking internet permissions*/
        String[] permissions = {
                "android.permission.ACCESS_NETWORK_STATE",
                "android.permission.INTERNET"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);

    }

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*ask for permission for internet*/
        askPermissions();

        /*we try tp fetch data only if internet is available
        otherwise we show error message*/
        if(!isNetworkAvailable()){
            TextView t = findViewById(R.id.textview);
            t.setText("Failed to connect to internet!");
            ProgressBar p = findViewById(R.id.progressBar);
            p.setVisibility(View.GONE);
        }else{
            Thread t = new Thread() {
                public void run() {
                    String url = "https://git.io/fjaqJ";
                    ReadFile readFile = new ReadFile();
                    String jsonStr = readFile.readFile(url);
                    String s[] = readJSON(jsonStr);
                    if (s != null) {
                        /*if json is parsed successfully, then pass all the strings to Cards.class
                        to display the data in cards */
                        Intent i = new Intent(MainActivity.this, Cards.class);
                        i.putExtra(JSON, s);
                        startActivity(i);
                    }


                }
            };
            t.start();

        }




    }

    /*Parses JSON String into string array*/
    public String[] readJSON(String jsonStr) {
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            // Getting JSON Array node
            JSONArray data = jsonObj.getJSONArray("data");
            String s[] = new String[data.length()];
            // looping through All Objects to parse ids and texts
            for (int i = 0; i < data.length(); i++) {
                JSONObject d = data.getJSONObject(i);
                String id = d.getString("id");
                String text = d.getString("text");
                s[i] = text;
            }
            return s;
        } catch (final JSONException e) {

        }
        return null;
    }


    /*method to check if internet is connected or not*/
    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}