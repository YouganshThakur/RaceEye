package com.techrex.raceeye;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("CONNECTION", MODE_PRIVATE);
        String address=prefs.getString("ADDRESS","XX");
        if(address.contains("XX"))
        {
            startActivity(new Intent(SplashActivity.this, Wizard.class));
        }
        else
        {
            startActivity(new Intent(SplashActivity.this, Home.class));
        }

        //close splash activity
       finish();
    }
}
