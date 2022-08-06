package com.raihan.dailyfamily.model;

import android.content.Context;
import android.os.CountDownTimer;


/**
 * Created by Emon Raihan
 */

public class MyTimerCountDownTimer extends CountDownTimer {
    Context context;

    public MyTimerCountDownTimer(Context context, long startTime, long interval) {


        super(startTime, interval);

        this.context = context;
       // Log.e("MyTimerCountDownTimer: ",""+context);
    }

    @Override
    public void onFinish() {
        // DO WHATEVER YOU WANT HERE

        ScreenOnOffReceiver screenOnOffReceiver = new ScreenOnOffReceiver();
        screenOnOffReceiver.doCounterLogout(context);

        /*Log.e("Timer Finished  :::","Timer Finished");

        Intent intent = new Intent(context,
                MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("sessionTimeoutMessage", LogoutTime.sessionMssage);
        Log.e("Befor Messsage: ",LogoutTime.sessionMssage);
        context.startActivity(intent);
        */

       // context.finish();

    }



    @Override
    public void onTick(long millisUntilFinished) {
    }
}
