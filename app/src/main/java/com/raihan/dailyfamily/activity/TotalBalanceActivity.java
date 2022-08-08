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

import com.google.firebase.database.Query;
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
    GlobalVariable globalVariable;

    TextView textView, totv, cbtv;
    DatabaseReference databaseReferenceT;
    DatabaseReference databaseReferenceMeal;
    DatabaseReference databaseReferenceBazaar;
    DatabaseReference databaseReferenceM;
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


    private TextView available_balance_value;
    private TextView total_deposit_amount_value;
    private TextView total_cost_value;
    private TextView total_meal_value;
    private TextView per_meal_cost_value;
    private TextView total_meal_cost_value;
    private TextView total_breakfast_value;
    private TextView total_launch_meal_value;
    private TextView total_diner_meal_value;
    private TextView available_balance_label;
    final LoadingDialog loadingDialog = new LoadingDialog(TotalBalanceActivity.this);

    private double totalDepositAmount = 0.0;
    private double totalCostAmount = 0.0;
    private double totalMeal = 0.0;
    private double perMealAmount = 0.0;
    private double availableBalance = 0.0;

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

        available_balance_value = findViewById(R.id.available_balance_value);
        total_deposit_amount_value = findViewById(R.id.total_deposit_amount_value);
        total_cost_value = findViewById(R.id.total_cost_value);
        total_meal_value = findViewById(R.id.total_meal_value);
        per_meal_cost_value = findViewById(R.id.per_meal_cost_value);
        total_meal_cost_value = findViewById(R.id.total_meal_cost_value);
        total_breakfast_value = findViewById(R.id.total_breakfast_value);
        total_launch_meal_value = findViewById(R.id.total_launch_meal_value);
        total_diner_meal_value = findViewById(R.id.total_diner_meal_value);
        available_balance_label = findViewById(R.id.available_balance_label);

        databaseReferenceT = FirebaseDatabase.getInstance().getReference("Transaction");
        databaseReferenceMeal = FirebaseDatabase.getInstance().getReference("Meal");
        databaseReferenceM = FirebaseDatabase.getInstance().getReference("Members");
        databaseReferenceBazaar = FirebaseDatabase.getInstance().getReference("Bazaar");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TotalBalanceActivity.this, DashboardActivity.class);
                DialogCustom.doClearActivity(intent, TotalBalanceActivity.this);
            }
        });

        tv_genereal_header_title.setText("Total Information");

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCustom.englishcustomLogout(TotalBalanceActivity.this);
            }
        });

        totalAmount();
        totalBazar();
        totalMeal();


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

    private void totalMeal() {
        if (!DialogCustom.isOnline(TotalBalanceActivity.this)) {
            DialogCustom.showInternetConnectionMessage(TotalBalanceActivity.this);
        } else {
            //loadingDialog.startDialoglog();
            Query queryt = databaseReferenceMeal.orderByChild("date").equalTo(ValidationUtil.getTransactionCurrentDate());
            databaseReferenceMeal.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // loadingDialog.dismisstDialoglog();
                    double breakfasttotal = 0;
                    double launchtotal = 0;
                    double dinnertotal = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        if (map.get("flag").equals("Y")) {
                            Object breakfast = map.get("breakfast");
                            Object launch = map.get("launch");
                            Object dinner = map.get("dinner");
                            double breakfastvalue = Double.parseDouble(ValidationUtil.replacecomma(String.valueOf(breakfast)));
                            double launchvalue = Double.parseDouble(ValidationUtil.replacecomma(String.valueOf(launch)));
                            double dinnervalue = Double.parseDouble(ValidationUtil.replacecomma(String.valueOf(dinner)));
                            breakfasttotal += breakfastvalue;
                            launchtotal += launchvalue;
                            dinnertotal += dinnervalue;
                        }


                    }

                    totalMeal = (breakfasttotal * .5) + launchtotal + dinnertotal;
                    perMealAmount = totalCostAmount / totalMeal;
                    availableBalance = totalDepositAmount - totalCostAmount;


                    total_meal_value.setText(String.valueOf(totalMeal));
                    total_breakfast_value.setText(String.valueOf((breakfasttotal)));
                    total_launch_meal_value.setText(String.valueOf((launchtotal)));
                    total_diner_meal_value.setText(String.valueOf((dinnertotal)));


                    per_meal_cost_value.setText(ValidationUtil.commaSeparateAmount(String.valueOf(perMealAmount)));
                    total_meal_cost_value.setText(ValidationUtil.commaSeparateAmount(String.valueOf(perMealAmount * totalMeal)));

                    if (totalDepositAmount >= totalCostAmount) {
                        available_balance_value.setText(ValidationUtil.commaSeparateAmount(String.valueOf(availableBalance)));
                    } else {
                        available_balance_value.setText(ValidationUtil.commaSeparateAmount(String.valueOf(availableBalance)));
                        available_balance_label.setText("Due Balance");
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //loadingDialog.dismisstDialoglog();
                    //throw databaseError.toException(); // don't ignore errors
                    DialogCustom.showErrorMessage(TotalBalanceActivity.this, databaseError.getMessage());
                }
            });
        }
    }

    private void totalBazar() {
        if (!DialogCustom.isOnline(TotalBalanceActivity.this)) {
            DialogCustom.showInternetConnectionMessage(TotalBalanceActivity.this);
        } else {
            //loadingDialog.startDialoglog();
            // Query queryt = databaseReferenceBazaar.orderByChild("date").equalTo(ValidationUtil.getTransactionCurrentDate());
            databaseReferenceBazaar.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // loadingDialog.dismisstDialoglog();
                    double bazartotal = 0;

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        if (map.get("flag").equals("Y")) {
                            Object breakfast = map.get("amount");

                            double bazarvalue = Double.parseDouble(ValidationUtil.replacecomma(String.valueOf(breakfast)));
                            bazartotal += bazarvalue;
                            totalCostAmount = bazartotal;

                        }
                    }
                    //globalVariable.setTotalAmount(String.valueOf(total));

                    total_cost_value.setText(ValidationUtil.commaSeparateAmount(String.valueOf(bazartotal)));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //loadingDialog.dismisstDialoglog();
                    //throw databaseError.toException(); // don't ignore errors
                    DialogCustom.showErrorMessage(TotalBalanceActivity.this, databaseError.getMessage());
                }
            });
        }
    }

    private void totalAmount() {
        if (!DialogCustom.isOnline(TotalBalanceActivity.this)) {
            DialogCustom.showInternetConnectionMessage(TotalBalanceActivity.this);
        } else {
            loadingDialog.startDialoglog();
            databaseReferenceT.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    loadingDialog.dismisstDialoglog();
                    double total = 0.0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String lastbal = "" + ds.child("amount").getValue();
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();

                        Object amount = map.get("amount");
                        double pvalue = Double.parseDouble(String.valueOf(amount));
                        total += pvalue;


                    }

                    totv.setText(ValidationUtil.commaSeparateAmount(String.valueOf(total)));
                    total_deposit_amount_value.setText(ValidationUtil.commaSeparateAmount(String.valueOf(total)));
                    //cbtv.setText(ValidationUtil.commaSeparateAmount(String.valueOf(lastbal)));
                    totalDepositAmount = total;

                    globalVariable.setTotalAmount(String.valueOf(total));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //throw databaseError.toException(); // don't ignore errors
                    DialogCustom.showErrorMessage(TotalBalanceActivity.this, databaseError.getMessage());
                    loadingDialog.startDialoglog();
                }
            });
        }
    }


}
