package com.raihan.dailyfamily.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.raihan.dailyfamily.R;
import com.raihan.dailyfamily.activity.MainActivity;
import com.raihan.dailyfamily.activity.TransactionActivity;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

public class DialogCustom {

    static FirebaseDatabase firebaseDatabase;
    static DatabaseReference databaseReferenceM;

    public static Drawable circleDilog(Activity activity) {

        CircularProgressDrawable progressDrawable = new CircularProgressDrawable(activity);
        progressDrawable.setStrokeWidth(5f);
        progressDrawable.setCenterRadius(30f);
        progressDrawable.start();

        return progressDrawable;
    }


    public static void customlogout(Activity activity) {

        englishLogout(activity);
    }

    public static void logout(Activity activity) {

        englishcustomLogout(activity);
    }

    public static void englishLogout(Activity activity) {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent intent = new Intent(activity, MainActivity.class);
                        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                        activity.startActivity(intent);
                        //activity.finish();
                        activity.finishAffinity();

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        // do nothing

                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setIcon(R.drawable.ic_logout);

        builder.setTitle(R.string.log_out_title_bagnla)
                //builder.setMessage(R.string.log_out_title)
                .setPositiveButton(R.string.log_out_ok_bagnla, dialogClickListener)
                .setNegativeButton(R.string.log_out_cancel_bagnla, dialogClickListener).show();


    }

    public static void englishcustomLogout(Activity activity) {

        final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(activity).setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View reg_layout = inflater.inflate(R.layout.test_dialog, null);

        final Button btn_no = reg_layout.findViewById(R.id.btn_no);
        final Button btn_yes = reg_layout.findViewById(R.id.btn_yes);

        dialog.setView(reg_layout);
        final android.app.AlertDialog alertDialog = dialog.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                Intent a = new Intent(activity, MainActivity.class);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(a);
                // activity.finish();
                activity.finishAffinity();


            }
        });


        alertDialog.show();

    }

    public static void showSuccessMessage(Activity activity, String message) {

        final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(activity).setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View reg_layout = inflater.inflate(R.layout.test_dialog, null);

        final Button btn_no = reg_layout.findViewById(R.id.btn_no);
        final Button btn_yes = reg_layout.findViewById(R.id.btn_yes);
        final TextView tv_message = reg_layout.findViewById(R.id.tv_message);
        final ImageView iamge = reg_layout.findViewById(R.id.iamge);
        final ImageView logout_icon = reg_layout.findViewById(R.id.logout_icon);

        dialog.setView(reg_layout);
        final android.app.AlertDialog alertDialog = dialog.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tv_message.setText(message);
        btn_no.setVisibility(View.GONE);
        logout_icon.setVisibility(View.GONE);
        iamge.setVisibility(View.VISIBLE);


        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.cancel();

            }
        });


        alertDialog.show();

    }

    public static void showSuccessMessagePass(Activity activity, String message) {

        final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(activity).setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View reg_layout = inflater.inflate(R.layout.test_dialog, null);

        final Button btn_no = reg_layout.findViewById(R.id.btn_no);
        final Button btn_yes = reg_layout.findViewById(R.id.btn_yes);
        final TextView tv_message = reg_layout.findViewById(R.id.tv_message);
        final ImageView iamge = reg_layout.findViewById(R.id.iamge);
        final ImageView logout_icon = reg_layout.findViewById(R.id.logout_icon);

        dialog.setView(reg_layout);
        final android.app.AlertDialog alertDialog = dialog.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tv_message.setText(message);
        btn_no.setVisibility(View.GONE);
        logout_icon.setVisibility(View.GONE);
        iamge.setVisibility(View.VISIBLE);


        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MainActivity.class);
                DialogCustom.doClearActivity(intent, activity);
                alertDialog.cancel();

            }
        });


        alertDialog.show();

    }


    public static boolean isOnline(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static void showInternetConnectionMessage(Activity activity) {

        final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(activity).setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View reg_layout = inflater.inflate(R.layout.test_dialog, null);

        final Button btn_no = reg_layout.findViewById(R.id.btn_no);
        final Button btn_yes = reg_layout.findViewById(R.id.btn_yes);
        final TextView tv_message = reg_layout.findViewById(R.id.tv_message);
        final ImageView internet_icon = reg_layout.findViewById(R.id.internet_icon);
        final ImageView logout_icon = reg_layout.findViewById(R.id.logout_icon);
        final TextView tv_title = reg_layout.findViewById(R.id.tv_title);

        dialog.setView(reg_layout);
        final android.app.AlertDialog alertDialog = dialog.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tv_message.setText("Please turn on your internet connection and try again");
        tv_title.setText("Internet Connection");
        btn_no.setVisibility(View.GONE);
        logout_icon.setVisibility(View.GONE);
        internet_icon.setVisibility(View.VISIBLE);


        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.cancel();


            }
        });


        alertDialog.show();

    }

    public static void showErrorMessage(Activity activity, String message) {

        final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(activity).setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View reg_layout = inflater.inflate(R.layout.test_dialog, null);

        final Button btn_no = reg_layout.findViewById(R.id.btn_no);
        final Button btn_yes = reg_layout.findViewById(R.id.btn_yes);
        final TextView tv_message = reg_layout.findViewById(R.id.tv_message);
        final ImageView iamge = reg_layout.findViewById(R.id.iamge);
        final ImageView internet_icon = reg_layout.findViewById(R.id.internet_icon);
        final ImageView error_icon = reg_layout.findViewById(R.id.error_icon);
        final ImageView logout_icon = reg_layout.findViewById(R.id.logout_icon);

        dialog.setView(reg_layout);
        final android.app.AlertDialog alertDialog = dialog.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tv_message.setText(message);
        btn_no.setVisibility(View.GONE);
        logout_icon.setVisibility(View.GONE);
        error_icon.setVisibility(View.VISIBLE);

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.cancel();


            }
        });


        alertDialog.show();

    }

    public static void showSuccessMessagePayment(Activity activity, String message, String email, String amount, String newtotal, String date, String invoice, String id) {

        final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(activity).setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View reg_layout = inflater.inflate(R.layout.test_dialog, null);

        final Button btn_no = reg_layout.findViewById(R.id.btn_no);
        final Button btn_yes = reg_layout.findViewById(R.id.btn_yes);
        final TextView tv_message = reg_layout.findViewById(R.id.tv_message);
        final ImageView iamge = reg_layout.findViewById(R.id.iamge);
        final ImageView logout_icon = reg_layout.findViewById(R.id.logout_icon);

        dialog.setView(reg_layout);
        final android.app.AlertDialog alertDialog = dialog.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tv_message.setText(message);
        btn_no.setVisibility(View.GONE);
        logout_icon.setVisibility(View.GONE);
        iamge.setVisibility(View.VISIBLE);


        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // alertDialog.cancel();
                alertDialog.dismiss();

                updatePayment(email, amount, newtotal, date, invoice, id, activity);

                Intent a = new Intent(activity, TransactionActivity.class);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(a);
                activity.finish();


            }
        });


        alertDialog.show();

    }

    private static void updatePayment(String email, String amount, String newtotal, String date, String invoice, String id, Activity activity) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceM = firebaseDatabase.getReference("Members");
        Query editQuery = databaseReferenceM.orderByChild("email").equalTo(email);
        editQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot edtData : dataSnapshot.getChildren()) {
                    edtData.getRef().child("balance").setValue(String.valueOf(newtotal));
                    edtData.getRef().child("notify").setValue("1. Your 'Daily Family' Monthly Payment" + " " + amount
                            + " Tk is Completed in " + " " + date + ". Your Invoice no is: " + invoice
                            + ". Your TXNID is: " + id + ". Your Total Balance is " + newtotal + " Tk."
                            + "Thank you so much for staying with us. Any query? Please notify us by this email (dailyfamilym22@gmail.com) also confirm your payment.");
                }
                Toast.makeText(activity, "Balance Update", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(activity, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    public static void doClearActivity(Intent intent, Activity activity) {

        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
        activity.startActivity(intent);
        activity.finish();
        //activity.finishAffinity();

    }

    public static void doClearActivityWithSession(Intent intent, Activity activity) {
        final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(activity).setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View reg_layout = inflater.inflate(R.layout.test_dialog, null);

        final Button btn_no = reg_layout.findViewById(R.id.btn_no);
        final Button btn_yes = reg_layout.findViewById(R.id.btn_yes);
        final TextView tv_message = reg_layout.findViewById(R.id.tv_message);
        final ImageView error_icon = reg_layout.findViewById(R.id.error_icon);
        final ImageView logout_icon = reg_layout.findViewById(R.id.logout_icon);

        dialog.setView(reg_layout);
        final android.app.AlertDialog alertDialog = dialog.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tv_message.setText("Your Active Session is Expired! Please login again");
        btn_no.setVisibility(View.GONE);
        logout_icon.setVisibility(View.GONE);
        error_icon.setVisibility(View.VISIBLE);


        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // alertDialog.cancel();
                alertDialog.dismiss();
                doClearActivity(intent, activity);


            }
        });


        alertDialog.show();

    }
}