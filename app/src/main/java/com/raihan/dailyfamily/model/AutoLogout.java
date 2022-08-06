package com.raihan.dailyfamily.model;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.raihan.dailyfamily.activity.MainActivity;


public class AutoLogout extends AppCompatActivity implements View.OnTouchListener {

    public Runnable mRunnable;
    public Handler mHandler;
    private long mTime = 3 * 60 * 1000;
    private int count;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MainActivity.class);
        mHandler = new Handler(Looper.getMainLooper());

        mRunnable = () -> {
            count++;
            if (count == 1) {
                Toast.makeText(getApplicationContext(),
                        "User inactive for" + String.valueOf(new long[]{mTime / 1000}) + "secs!",
                        Toast.LENGTH_SHORT).show();
                Log.e("Called-------", String.valueOf(mTime));
                stopHandler();
                //startActivity(intent)
                DialogCustom.doClearActivityWithSession(intent, this);
            }
            stopHandler();
        };
        startHandler();

        //startActivity(intent);


    }

    @Override
    public void onUserInteraction() {
        stopHandler();
        startHandler();
        super.onUserInteraction();
    }

    @Override
    protected void onPause() {
        stopHandler();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startHandler();
    }

    // start handler function
    private void startHandler() {
        mHandler.postDelayed(mRunnable, mTime);
    }

    // stop handler function
    private void stopHandler() {
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Removes the handler callbacks (if any)
        stopHandler();
        // Runs the handler (for the specified time)
        // If any touch or motion is detected before
        // the specified time, this override function is again called
        startHandler();
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    hideKeyboard(this);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }


    public void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}
