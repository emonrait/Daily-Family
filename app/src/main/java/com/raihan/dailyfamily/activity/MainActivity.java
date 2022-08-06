package com.raihan.dailyfamily.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.raihan.dailyfamily.BuildConfig;
import com.raihan.dailyfamily.R;
import com.raihan.dailyfamily.model.CustomKeyboardHide;
import com.raihan.dailyfamily.model.DialogCustom;
import com.raihan.dailyfamily.model.GlobalVariable;
import com.raihan.dailyfamily.model.LogoutTime;
import com.raihan.dailyfamily.model.SendMailMessage;
import com.raihan.dailyfamily.model.ValidationUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MainActivity extends CustomKeyboardHide {

    private static final String PREF_NAME = "prefs";
    private static final String KEY_REMEMBER = "remeber";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASS = "password";
    String email, password;
    Boolean savelogin;
    private TextView forgetpass, martax, versionname;
    private TextView model;
    private TextView deviceid;
    private FirebaseAuth mAuth;
    private EditText emailTV, passwordTV;
    private Button loginBtn, paybtn, register;
    private CheckBox checkBox;
    String useremail;
    String forgetpassword;
    String member_name;
    boolean doubleBackToExitPressedOnce = false;
    //BiometricManager mBiometricManager;
    String check = "";

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    GlobalVariable globalVariable;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE}, PackageManager.PERMISSION_GRANTED);


        emailTV = findViewById(R.id.input_email);
        passwordTV = findViewById(R.id.input_password);
        mAuth = FirebaseAuth.getInstance();
        register = findViewById(R.id.link_refister);
        loginBtn = findViewById(R.id.btn_register);
        paybtn = findViewById(R.id.btn_payment);
        checkBox = findViewById(R.id.input_check);
        martax = findViewById(R.id.mart);
        forgetpass = findViewById(R.id.forgetpass);
        versionname = findViewById(R.id.versionname);
        model = findViewById(R.id.model);
        deviceid = findViewById(R.id.deviceid);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Members");
        globalVariable = ((GlobalVariable) getApplicationContext());
        globalVariable.setNewversioncode(String.valueOf(BuildConfig.VERSION_CODE));
        globalVariable.setVersionName(BuildConfig.VERSION_NAME);
        // globalVariable.setDeviceid(ValidationUtil.getDeviceId(this));
        globalVariable.setModel(ValidationUtil.getDeviceName());

        if (!checkPermission()) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.CALL_PHONE}, PackageManager.PERMISSION_GRANTED);

        } else {
            globalVariable.setDeviceid(ValidationUtil.getDeviceId(this));
        }

        martax.setSelected(true);
        martextshow();


        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        globalVariable.setAmountFlag(true);
        globalVariable.setMemberFlag(true);
        globalVariable.setRequireAmount("0");
        globalVariable.setMyAmount("0");
        globalVariable.setTotalAmount("0");

        versionname.setText("Version :- " + globalVariable.getVersionName());
        model.setText("Model :- " + globalVariable.getModel());
        deviceid.setText("Device ID :- " + globalVariable.getDeviceid());


        paybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), TransactionActivity.class);
                startActivity(i);
                Snackbar.make(view, "Welcome To The Daily Family", Snackbar.LENGTH_LONG).show();

            }
        });

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });
        //DialogCustom.showErrorMessage(this,BuildConfig.SERVER_URL);


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(KEY_USERNAME, emailTV.getText().toString().trim());
                    editor.putString(KEY_PASS, passwordTV.getText().toString().trim());
                    editor.putBoolean(KEY_REMEMBER, true);
                    editor.apply();

                    Toast.makeText(MainActivity.this, "Checked", Toast.LENGTH_SHORT).show();

                } else if (!compoundButton.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(KEY_REMEMBER, false);
                    editor.remove(KEY_PASS);
                    editor.remove(KEY_USERNAME);
                    editor.apply();

                    Toast.makeText(MainActivity.this, "Unchecked", Toast.LENGTH_SHORT).show();
                }
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DialogCustom.isOnline(MainActivity.this)) {
                    DialogCustom.showInternetConnectionMessage(MainActivity.this);
                } else {
                    loginUserAccount();
                    //bioAuthenticate();
                }

                //showDialoglog();

            }
        });
        savelogin = sharedPreferences.getBoolean("savelogin", true);
        if (savelogin) {
            emailTV.setText(sharedPreferences.getString("username", null));
            passwordTV.setText(sharedPreferences.getString("password", null));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (!Settings.System.canWrite(getApplicationContext())) {
                Log.e("system setting--", "test----");
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
                DialogCustom.showSuccessMessage(MainActivity.this, "Please, allow system settings for automatic logout . If system is idle for 5 minutes (maximum), user will automatically logout from apps.");
                //   .setContentText("Please, allow system settings for automatic logout . If system is idle for 5 minutes (maximum), user will automatically logout from apps.").show();
                Toast.makeText(getApplicationContext(), "Please, allow system settings for automatic logout . If system is idle for 5 minutes (maximum), user will automatically logout from apps.", Toast.LENGTH_LONG).show();
                startActivityForResult(intent, 200);

            }

            if (Settings.System.canWrite(getApplicationContext())) {
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, LogoutTime.deviceSleepMode);
            }
        }
    }

    private void martextshow() {
        FirebaseDatabase.getInstance()
                .getReference().child("Message")
                .orderByChild("text")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String martex = "" + ds.child("martext").getValue();
                            martax.setText(martex);

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void loginUserAccount() {
        final LoadingDialog loadingDialog = new LoadingDialog(MainActivity.this);

        email = emailTV.getText().toString().trim();
        password = passwordTV.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            DialogCustom.showErrorMessage(this, "Please enter email id!");
            emailTV.requestFocus();
        } else if (ValidationUtil.getEmailValidate(email).equals("1")) {
            DialogCustom.showErrorMessage(this, "Please enter Valid Format email id! (example@mail.com)");
            emailTV.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            DialogCustom.showErrorMessage(this, "Please enter password!");
            passwordTV.requestFocus();
        } else {
            loadingDialog.startDialoglog();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                loadingDialog.dismisstDialoglog();
                                if (checkBox.isChecked()) {
                                    SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean(KEY_REMEMBER, true);
                                    editor.putString(KEY_USERNAME, emailTV.getText().toString());
                                    editor.putString(KEY_PASS, passwordTV.getText().toString());
                                    editor.apply();
                                    // Toast.makeText(MainActivity.this, "You Click Remember Me", Toast.LENGTH_SHORT).show();
                                } else {

                                    //Toast.makeText(MainActivity.this, "You Don`t Click Remember Me", Toast.LENGTH_SHORT).show();
                                }
                                Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                                startActivity(intent);
                                //Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                                //startActivity(intent);
                            } else {
                                //Toast.makeText(getApplicationContext(), "Login failed! Emai or Password is wrong", Toast.LENGTH_LONG).show();
                                loadingDialog.dismisstDialoglog();
                                DialogCustom.showErrorMessage(MainActivity.this, "Login failed! Email or Password is wrong");
                            }
                        }
                    });
        }


    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }


    private void showDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this).setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View reg_layout = inflater.inflate(R.layout.forgetpass, null);

        final EditText emailt = (EditText) reg_layout.findViewById(R.id.forget_email);
        final Button savet = (Button) reg_layout.findViewById(R.id.btn_save);
        final Button cancelt = (Button) reg_layout.findViewById(R.id.btn_cancel);
        dialog.setView(reg_layout);
        final AlertDialog alertDialog = dialog.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        savet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                useremail = emailt.getText().toString().trim();
                // Toast.makeText(MainActivity.this, useremail, Toast.LENGTH_SHORT).show();
                if (emailt.getText().toString().isEmpty()) {
                    DialogCustom.showErrorMessage(MainActivity.this, getString(R.string.please_enter_email_id));
                    emailt.requestFocus();
                } else if (useremail.isEmpty()) {
                    DialogCustom.showErrorMessage(MainActivity.this, getString(R.string.please_enter_email_id));
                    emailt.requestFocus();
                } else if (ValidationUtil.getEmailValidate(useremail).equals("1")) {
                    DialogCustom.showErrorMessage(MainActivity.this, "Please enter Valid Format email id! (example@mail.com)");
                    emailTV.requestFocus();
                } else if (!DialogCustom.isOnline(MainActivity.this)) {
                    DialogCustom.showInternetConnectionMessage(MainActivity.this);
                } else {
                    useremail = emailt.getText().toString().trim();
                    showPassword();
                }


            }
        });

        cancelt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void showPassword() {

        Query query = FirebaseDatabase.getInstance()
                .getReference().child("Members")
                .orderByChild("email").equalTo(useremail);


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        forgetpassword = "" + ds.child("password").getValue();
                        member_name = "" + ds.child("member_name").getValue();
                        sendEmail();

                    }
                } else {
                    DialogCustom.showErrorMessage(MainActivity.this, "No data Found! Please Enter Valid Email Address");

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                DialogCustom.showErrorMessage(MainActivity.this, databaseError.getMessage());
                //Log.d("error--",databaseError.toString());
            }
        });


    }

    private void sendEmail() {
        try {
            String subject = "Your Password";

            String message = "Dear " + member_name + "," + "\n" + "\n" + "Your 'Daily Family' Android Apps login information as below"
                    + "\n" + "Your User Id is: " + useremail + ". "
                    + "\n" + "Your Password is: " + forgetpassword + ". "
                    + "\n" + "Thank you so much for staying with us. Any query? Please notify us by this email. Please preserve your login information for future."
                    + "\n" + "\n" + "Thanks & Regards" + "\n" + "Daily Family" + "\n" + "Email: dailyfamilym22@gmail.com";

            SendMailMessage sm = new SendMailMessage(this, useremail, subject, message);
            sm.execute();
        } catch (Exception e) {
            DialogCustom.showErrorMessage(this, e.toString());
        }
    }

    /*@Override
    public void onSdkVersionNotSupported() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_error_sdk_not_supported), Toast.LENGTH_LONG).show();
        loginUserAccount();
    }

    @Override
    public void onBiometricAuthenticationNotSupported() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_error_hardware_not_supported), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationNotAvailable() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_error_fingerprint_not_available), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationPermissionNotGranted() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_error_permission_not_granted), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationInternalError(String error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationFailed() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_failure), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationCancelled() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_cancelled), Toast.LENGTH_LONG).show();
        //mBiometricManager.cancelAuthentication();
        //mBiometricManager
        // bioAuthenticate();
        loginUserAccount();
    }

    @Override
    public void onAuthenticationSuccessful() {
//        Toast.makeText(getApplicationContext(), getString(R.string.biometric_success), Toast.LENGTH_LONG).show();
//        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
//        startActivity(intent);
        loginUserAccount();
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        Toast.makeText(getApplicationContext(), helpString, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        Toast.makeText(getApplicationContext(), errString, Toast.LENGTH_LONG).show();
        bioAuthenticate();
    }

    private void bioAuthenticate() {
        if (emailTV.getText().toString().isEmpty()) {
            DialogCustom.showErrorMessage(this, "Please Enter Your Email Address");
            emailTV.requestFocus();
        } else if (passwordTV.getText().toString().isEmpty()) {
            DialogCustom.showErrorMessage(this, "Please Enter Your Password");
            passwordTV.requestFocus();

        } else {
            mBiometricManager = new BiometricManager.BiometricBuilder(MainActivity.this)
                    .setTitle(getString(R.string.biometric_title))
                    .setSubtitle(getString(R.string.biometric_subtitle))
                    .setDescription(getString(R.string.biometric_description))
                    .setNegativeButtonText(getString(R.string.biometric_negative_button_text))
                    .build();

            //start authentication
            mBiometricManager.authenticate(MainActivity.this);
        }

    }*/

    private boolean usernameExists(String username) {

        Query fdbRefer = FirebaseDatabase.getInstance()
                .getReference().child("Members")
                .orderByChild("email").equalTo(username);

        fdbRefer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    forgetpassword = "" + ds.child("password").getValue();
                    member_name = "" + ds.child("member_name").getValue();
                    check = "" + ds.child("email").getValue();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                DialogCustom.showErrorMessage(MainActivity.this, databaseError.getMessage());
            }
        });


        return (username.equals(check));
    }

    private final boolean checkPermission() {
        boolean var10000;
        if (Build.VERSION.SDK_INT >= 30) {
            var10000 = Environment.isExternalStorageManager();
        } else {
            int READ_PHONE_STATE = ContextCompat.checkSelfPermission((Context) this, "android.permission.READ_PHONE_STATE");
            int ACCESS_NETWORK_STATE = ContextCompat.checkSelfPermission((Context) this, "android.permission.ACCESS_NETWORK_STATE");
            var10000 = READ_PHONE_STATE == 0 && ACCESS_NETWORK_STATE == 0;
        }

        return var10000;
    }

}



