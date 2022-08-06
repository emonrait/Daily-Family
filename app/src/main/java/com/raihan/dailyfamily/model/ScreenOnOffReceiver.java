package com.raihan.dailyfamily.model;

/**
 * Created by Emon Raihan
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;

import com.raihan.dailyfamily.activity.MainActivity;


/**
 * Created by Enamul on 1/29/17.
 */

public class ScreenOnOffReceiver extends BroadcastReceiver{

    CountDownTimer timer;

    SharedPreferences user_session_sp;

    @Override
    public void onReceive(final Context context, Intent intent) {


            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                // setTimeForSleep(SessionTimeOut.startTime,context);
                //setTimeForSleep(1*60*1000,context);
                try {//Log.e("screen ", "on");
                    // some code
                    Log.e("Sleep Time", getTimeForSleep(context) + "");
                    //Log.e("screen ", "on");
                    // some code

                    timer.cancel();
                }catch (Exception e){

                }
            }

            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                // some code
                Log.e("screen ", "off");
                int sleepTime = 0;
                try {
                    sleepTime = getTimeForSleep(context);
                } catch (Exception e) {
                    // TODO: handle exception
                }

                timer = new CountDownTimer(((3 * 60) * 1000) - sleepTime, 1000) {

                    public void onTick(long millisUntilFinished) {
                        //Some code
                        //Log.e("Time Remaining ", millisUntilFinished/1000+"");

                    }

                    public void onFinish() {
                        //Logout
                   /* user_session_sp = context.getSharedPreferences("use_session_ps", context.MODE_PRIVATE);

                    SharedPreferences.Editor spEditor = user_session_sp.edit();
                    spEditor.putString("session", "");

                    spEditor.commit();
                    */
                        Log.e("Login intent ::", "Logout!");
                        try {
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("sessionTimeoutMessage", LogoutTime.sessionMssage);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);


                        } catch (Exception e) {
                            Log.e("Login intent----- ", "Logout!" + e.toString());
                        }

                        //Log.e("finish ", "Logout!");
                        timer.cancel();
                    }


                }.start();
            }


    }

    private void setTimeForSleep(int time, Context context){
        Settings.System.putInt(context.getContentResolver(),
                Settings.System.SCREEN_OFF_TIMEOUT,  time);
    }

    private int getTimeForSleep(Context context){
        int sleppTime = 0;
        try {
            sleppTime = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
        } catch (Settings.SettingNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //Log.e("Sleep Time", sleppTime+"");
        return sleppTime;
    }

    public  void doCounterLogout(Context context){

        Intent intent = new Intent(context,  MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        intent.putExtra("sessionTimeoutMessage", LogoutTime.sessionMssage);
        context.startActivity(intent);

    }

}