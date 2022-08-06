package com.raihan.dailyfamily.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.raihan.dailyfamily.R;
import com.raihan.dailyfamily.model.AutoLogout;
import com.raihan.dailyfamily.model.CustomMethod;
import com.raihan.dailyfamily.model.DialogCustom;
import com.raihan.dailyfamily.model.GlobalVariable;
import com.raihan.dailyfamily.model.LogoutService;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class DebitActivity extends AutoLogout {
    GlobalVariable globalVariable;
//    TextView textView;
//    //Button btnpdf;
//    //PDFView pdfView;
//    Toolbar toolbar;
    TextView textView3, textView5, textView7, textView11, textView9;
    ProgressBar progressBar, progressBar14, progressBar15, progressBar151;
    CircleImageView textView91;
    private ImageView ivLogout;
    private ImageView ivBack;
    private TextView tv_genereal_header_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debit);

        globalVariable = ((GlobalVariable) getApplicationContext());
//        textView = findViewById(R.id.text);
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Debit Home");
        //pdfView = findViewById(R.id.pdfbt);
        textView3 = findViewById(R.id.textView3);
        textView5 = findViewById(R.id.textView5);
        progressBar = findViewById(R.id.progressBar);
        progressBar14 = findViewById(R.id.progressBar14);
        textView7 = findViewById(R.id.textView7);
        textView11 = findViewById(R.id.textView11);
        textView9 = findViewById(R.id.textView9);
        progressBar15 = findViewById(R.id.progressBar15);
        textView91 = findViewById(R.id.textView91);
        progressBar151 = findViewById(R.id.progressBar151);

        textView3.setText(globalVariable.getMyAmount() + " TK");

        ivLogout = findViewById(R.id.ivLogout);
        ivBack = findViewById(R.id.ivBack);
        tv_genereal_header_title = findViewById(R.id.tv_genereal_header_title);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DebitActivity.this, DashboardActivity.class);
                DialogCustom.doClearActivity(intent, DebitActivity.this);
            }
        });

        tv_genereal_header_title.setText("Debit Home");

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCustom.englishcustomLogout(DebitActivity.this);
            }
        });
        int persent = (int) CustomMethod.calculatePercentage(Double.parseDouble(globalVariable.getMyAmount()), Double.parseDouble(globalVariable.getTotalAmount()));
        int goal = (int) CustomMethod.calculatePercentage(Double.parseDouble(globalVariable.getTotalAmount()), 2000000);

        textView5.setText(String.valueOf(persent) + "%");
        textView11.setText(String.valueOf(persent) + "%");
        textView9.setText(String.valueOf(goal) + "%");

        progressBar.setProgress(persent);
        progressBar14.setProgress(persent);
        progressBar15.setProgress(goal);
        progressBar151.setProgress(persent);

        Picasso.get().load(globalVariable.getUrl()).into(textView91);


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
