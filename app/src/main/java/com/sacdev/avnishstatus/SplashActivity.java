package com.sacdev.avnishstatus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
         constants.preferences = getSharedPreferences("LoginContent", MODE_PRIVATE);
        Boolean loggedin = constants.preferences.getBoolean("logged_in", false);
        if(loggedin){
            constants.authtoken = constants.preferences.getString("auth_token", null);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (loggedin && constants.authtoken!=null){
                    startActivity(  constants.changeActivity(SplashActivity.this,MainActivity.class));
                }else{
                    startActivity(constants.changeActivity(SplashActivity.this,LoginActivity.class));
                }
            }
        },3000);
    }

}