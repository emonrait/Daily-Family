package com.raihan.dailyfamily.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
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
import com.raihan.dailyfamily.model.ListItem;
import com.raihan.dailyfamily.model.LogoutService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.raihan.dailyfamily.model.ValidationUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

public class TotalBalanceActivity extends AutoLogout {
    TextView textView, totv, cbtv;
    DatabaseReference databaseReference;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    ListItem star;
    ArrayList<ListItem> mUploads;
    Toolbar toolbar;
    TextView textView3, textView5;
    ProgressBar progressBar;
    private ImageView ivLogout;
    private ImageView ivBack;
    private TextView tv_genereal_header_title;
    GlobalVariable globalVariable;

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_balance);
        globalVariable = ((GlobalVariable) getApplicationContext());

        //  textView = findViewById(R.id.text);
        totv = findViewById(R.id.total_tv);
        cbtv = findViewById(R.id.current_tv);
        //   toolbar = findViewById(R.id.toolbar);
        textView3 = findViewById(R.id.textView3);
        textView5 = findViewById(R.id.textView5);
        progressBar = findViewById(R.id.progressBar);
        ivLogout = findViewById(R.id.ivLogout);
        ivBack = findViewById(R.id.ivBack);
        tv_genereal_header_title = findViewById(R.id.tv_genereal_header_title);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Total Balance");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TotalBalanceActivity.this, DashboardActivity.class);
                DialogCustom.doClearActivity(intent, TotalBalanceActivity.this);
            }
        });

        tv_genereal_header_title.setText("Total Balance");

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCustom.englishcustomLogout(TotalBalanceActivity.this);
            }
        });
        final LoadingDialog loadingDialog = new LoadingDialog(TotalBalanceActivity.this);

        databaseReference = FirebaseDatabase.getInstance().getReference("Transaction");

        if (!DialogCustom.isOnline(TotalBalanceActivity.this)) {
            DialogCustom.showInternetConnectionMessage(TotalBalanceActivity.this);
        } else {
            loadingDialog.startDialoglog();
            databaseReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    loadingDialog.dismisstDialoglog();
                    double total = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String lastbal = "" + ds.child("amount").getValue();
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object amount = map.get("amount");
                        double pvalue = Double.parseDouble(String.valueOf(amount));
                        total += pvalue;
                        DecimalFormat formater = new DecimalFormat("#,##,###.00");
                        String formatString = formater.format(total);
                        String formatString1 = formater.format(Double.parseDouble(lastbal));

                        //totv.setText(formatString + " \u09F3");
                        //cbtv.setText(formatString1 + " \u09F3");

                        totv.setText(ValidationUtil.commaSeparateAmount(String.valueOf(total)));
                        cbtv.setText(ValidationUtil.commaSeparateAmount(String.valueOf(lastbal)));


                    }
                    globalVariable.setTotalAmount(String.valueOf(total));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //throw databaseError.toException(); // don't ignore errors
                    DialogCustom.showErrorMessage(TotalBalanceActivity.this, databaseError.getMessage());
                }
            });
        }

        int present = (int) CustomMethod.calculatePercentage(ValidationUtil.replacecommaDouble(globalVariable.getMyAmount()), ValidationUtil.replacecommaDouble(globalVariable.getTotalAmount()));


        textView3.setText(ValidationUtil.commaSeparateAmount(globalVariable.getMyAmount()));
        textView5.setText(present + "%");
        progressBar.setProgress(present);


        LogoutService.logout(this);
        // DialogCustom.showErrorMessage(this,globalVariable.getMyAmount()+"total"+globalVariable.getTotalAmount());

        // Log.e("totalamt",globalVariable.getMyAmount()+"total"+globalVariable.getTotalAmount());

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


}
