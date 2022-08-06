package com.raihan.dailyfamily.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.raihan.dailyfamily.R;
import com.raihan.dailyfamily.model.AutoLogout;
import com.raihan.dailyfamily.model.DialogCustom;
import com.raihan.dailyfamily.model.GlobalVariable;
import com.raihan.dailyfamily.model.LogoutService;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class ConditionActivity extends AutoLogout {
    private ImageView ivLogout;
    private ImageView ivBack;
    private TextView tv_genereal_header_title;
    GlobalVariable globalVariable;
    private PDFView pdfView;
    Uri uri;
    String url;


    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition);

        globalVariable = ((GlobalVariable) getApplicationContext());
        ivLogout = findViewById(R.id.ivLogout);
        ivBack = findViewById(R.id.ivBack);
        tv_genereal_header_title = findViewById(R.id.tv_genereal_header_title);
        pdfView = (PDFView) findViewById(R.id.pdfView);


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConditionActivity.this, DashboardActivity.class);
                DialogCustom.doClearActivity(intent, ConditionActivity.this);
            }
        });

        tv_genereal_header_title.setText("Terms & Conditions");

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCustom.englishcustomLogout(ConditionActivity.this);
            }
        });

        pdfView.fromAsset("filename.pdf")

                .load();



        LogoutService.logout(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout: {
                DialogCustom.englishcustomLogout(this);
            }
        }
        return super.onOptionsItemSelected(item);
    }

}

