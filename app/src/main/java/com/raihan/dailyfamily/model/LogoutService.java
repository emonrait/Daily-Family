package com.raihan.dailyfamily.model;

import android.app.Activity;


public class LogoutService {

    public static void logout(Activity context) {

        /****** Create Thread that will sleep for 3 seconds****/
       /* Thread background = new Thread() {
            public void run() {
                int timet = 5 * 60; // Convert to seconds
                long delay = timet * 1000;

                do {

                    int minutes = timet / 60;
                    int seconds = timet % 60;
                    Log.e("Timecount---", minutes + " minute(s), " + seconds + " second(s)");
                    try {
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    timet = timet - 1;
                    delay = delay - 1000;


                }


                while (delay != 0);
               // Log.e("Timecount---", "Time's Up!");
               // Intent i = new Intent(context, MainActivity.class);
               // context.startActivity(i);
            }
        };
        // start thread
        background.start();
*/

    }

}
