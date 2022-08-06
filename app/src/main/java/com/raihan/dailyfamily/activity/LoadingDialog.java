package com.raihan.dailyfamily.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.raihan.dailyfamily.R;

public class LoadingDialog {

    public Activity activity;
    private AlertDialog alertDialog;

    public LoadingDialog(Activity myActivity) {
        activity = myActivity;
    }


    public  void startDialoglog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity).setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View reg_layout = inflater.inflate(R.layout.loging_dialog, null);

        dialog.setView(reg_layout);
        alertDialog = dialog.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        alertDialog.show();
    }

    public  void dismisstDialoglog() {
        alertDialog.dismiss();
    }
}
