package com.raihan.dailyfamily.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.raihan.dailyfamily.R;
import com.raihan.dailyfamily.model.AutoLogout;
import com.raihan.dailyfamily.model.DialogCustom;
import com.raihan.dailyfamily.model.GlobalVariable;
import com.raihan.dailyfamily.model.LogoutService;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class AboutActivity extends AutoLogout {

    TextView textView;
    Toolbar toolbar;
    ImageView profile1;
    private ImageView ivLogout;
    private ImageView ivBack;
    private TextView tv_genereal_header_title;
    private TextView link;
    private TextView fblink;
    private TextView user_email;
    private TextView user_mobile;
    private TextView user_mobile2;
    GlobalVariable globalVariable;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        textView = findViewById(R.id.text);
        toolbar = findViewById(R.id.toolbar);
        profile1 = findViewById(R.id.profile1);
        link = findViewById(R.id.link);
        fblink = findViewById(R.id.fblink);
        user_email = findViewById(R.id.user_email);
        user_mobile = findViewById(R.id.user_mobile);
        user_mobile2 = findViewById(R.id.user_mobile2);

        globalVariable = ((GlobalVariable) getApplicationContext());
        ivLogout = findViewById(R.id.ivLogout);
        ivBack = findViewById(R.id.ivBack);
        tv_genereal_header_title = findViewById(R.id.tv_genereal_header_title);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this, DashboardActivity.class);
                DialogCustom.doClearActivity(intent, AboutActivity.this);
            }
        });

        tv_genereal_header_title.setText("About Developer");

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCustom.englishcustomLogout(AboutActivity.this);
            }
        });

        String imagelink =
                "https://1.bp.blogspot.com/-lBVZsV0Q68w/XZ9r_8pasEI/AAAAAAAAe-A/Y12PrSDspn85qT_QlLIIfdOLY9EfmlPUQCLcBGAsYHQ/s1600/DSC_0563.JPG";

        Picasso.get().load(imagelink).placeholder(R.drawable.emon).into(profile1);

        LogoutService.logout(this);

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl("http://www.emonra.blogspot.com", AboutActivity.this);
            }
        });

        fblink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl("http://www.facebook.com/emon.raihan", AboutActivity.this);
            }
        });

        user_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermission()) {
                    requestPermission();
                } else {
                    Intent callintetnt = new Intent(Intent.ACTION_CALL);
                    callintetnt.setData(Uri.parse("tel:" + user_mobile.getText().toString().trim()));
                    startActivity(callintetnt);
                }
            }
        });

        user_mobile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermission()) {
                    requestPermission();
                } else {
                    Intent callintetnt = new Intent(Intent.ACTION_CALL);
                    callintetnt.setData(Uri.parse("tel:" + user_mobile2.getText().toString().trim()));
                    startActivity(callintetnt);
                }
            }
        });

        user_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "emonrait@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

    }

    private void goToUrl(String url, Activity activity) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        activity.startActivity(launchBrowser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            DialogCustom.englishcustomLogout(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkPermission() {
        boolean var10000;

        int result = ContextCompat.checkSelfPermission((Context) this, "android.permission.CALL_PHONE");
        var10000 = result == 0;

        return var10000;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(AboutActivity.this, new String[]{
                Manifest.permission.CALL_PHONE}, PackageManager.PERMISSION_GRANTED);

    }

}
