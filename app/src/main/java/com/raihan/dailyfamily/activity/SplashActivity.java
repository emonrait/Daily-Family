package com.raihan.dailyfamily.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.raihan.dailyfamily.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        /****** Create Thread that will sleep for 3 seconds****/
        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 3 seconds
                    sleep(3 * 1000);

//                    int waited = 0;
//                    while (_active && (waited < SPLASH_DISPLAY_LENGTH)) {
//                        sleep(100);
//                        if (_active) {
//                            waited += 100;
//                        }
//                    }

                } catch (Exception e) {
                } finally {
                    // After 5 seconds redirect to another intent
                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(i);

                    //Remove activity
                    finish();
                }
            }
        };
        // start thread
        background.start();
    }


}