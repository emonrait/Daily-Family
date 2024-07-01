package com.raihan.dailyfamily.activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.raihan.dailyfamily.R;
import com.raihan.dailyfamily.model.AutoLogout;
import com.raihan.dailyfamily.model.DialogCustom;
import com.raihan.dailyfamily.model.GlobalVariable;
import com.raihan.dailyfamily.model.Meal;
import com.raihan.dailyfamily.model.MealReportDailyListAdapter;
import com.raihan.dailyfamily.model.ValidationUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class MealBoardActivity extends AutoLogout {
    GlobalVariable globalVariable;
    private ImageView ivLogout;
    private ImageView ivBack;
    private TextView tv_genereal_header_title;

    private TextView date_value;
    private TextView breakfast_value;
    private TextView launch_value;
    private TextView dinner_value;
    private TextView total_bazar_value;

    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceMeal;
    DatabaseReference databaseReferenceBazaar;
    DatabaseReference databaseReferenceM;
    ArrayList<Meal> listdata;
    RecyclerView meal_recyclerView;
    MealReportDailyListAdapter adpter;
    LoadingDialog loadingDialog = new LoadingDialog(MealBoardActivity.this);
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_board);
        ivLogout = findViewById(R.id.ivLogout);
        ivBack = findViewById(R.id.ivBack);
        tv_genereal_header_title = findViewById(R.id.tv_genereal_header_title);

        date_value = findViewById(R.id.date_value);
        breakfast_value = findViewById(R.id.breakfast_value);
        launch_value = findViewById(R.id.launch_value);
        dinner_value = findViewById(R.id.dinner_value);
        meal_recyclerView = findViewById(R.id.meal_recyclerView);
        total_bazar_value = findViewById(R.id.total_bazar_value);

        globalVariable = ((GlobalVariable) getApplicationContext());
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceMeal = FirebaseDatabase.getInstance().getReference("Meal");
        databaseReferenceM = FirebaseDatabase.getInstance().getReference("Members");
        databaseReferenceBazaar = FirebaseDatabase.getInstance().getReference("Bazaar");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MealBoardActivity.this, DashboardActivity.class);
                DialogCustom.doClearActivity(intent, MealBoardActivity.this);
            }
        });

        tv_genereal_header_title.setText(R.string.meal_board);

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCustom.englishcustomLogout(MealBoardActivity.this);
            }
        });

        date_value.setText(ValidationUtil.getTransactionCurrentDate());
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                date_value.setText(ValidationUtil.dateFormate(myCalendar));
                totalMeal();
                allMemberMealInfo();
                totalBazar();
            }
        };
        date_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MealBoardActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        totalMeal();
        allMemberMealInfo();
        totalBazar();

    }

    private void totalMeal() {
        if (!DialogCustom.isOnline(MealBoardActivity.this)) {
            DialogCustom.showInternetConnectionMessage(MealBoardActivity.this);
        } else {
            //loadingDialog.startDialoglog();
            Query queryt = databaseReferenceMeal.orderByChild("date").equalTo(date_value.getText().toString().trim());
            queryt.addValueEventListener(new ValueEventListener() {

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
                    //globalVariable.setTotalAmount(String.valueOf(total));
                    breakfast_value.setText(String.valueOf(breakfasttotal));
                    launch_value.setText(String.valueOf(launchtotal));
                    dinner_value.setText(String.valueOf(dinnertotal));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //loadingDialog.dismisstDialoglog();
                    //throw databaseError.toException(); // don't ignore errors
                    DialogCustom.showErrorMessage(MealBoardActivity.this, databaseError.getMessage());
                }
            });
        }
    }

    private void totalBazar() {
        if (!DialogCustom.isOnline(MealBoardActivity.this)) {
            DialogCustom.showInternetConnectionMessage(MealBoardActivity.this);
        } else {
            //loadingDialog.startDialoglog();
            Query queryt = databaseReferenceBazaar.orderByChild("date").equalTo(date_value.getText().toString().trim());
            queryt.addValueEventListener(new ValueEventListener() {

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


                        }
                    }
                    //globalVariable.setTotalAmount(String.valueOf(total));

                    total_bazar_value.setText(ValidationUtil.commaSeparateAmount(String.valueOf(bazartotal)));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    DialogCustom.showErrorMessage(MealBoardActivity.this, databaseError.getMessage());
                }
            });
        }
    }

    private void allMemberMealInfo() {
        LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.startDialoglog();
        ArrayList<String> emailList = new ArrayList<>();
        listdata = new ArrayList<>();
        databaseReferenceM.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    // Members members = new Members();

                    String name = ds.child("member_name").getValue(String.class);
                    String email = ds.child("email").getValue(String.class);
                    String mobile = ds.child("mobile").getValue(String.class);
                    String profile = ds.child("prolink").getValue(String.class);
                    String nick = ds.child("nick").getValue(String.class);
                    String date = date_value.getText().toString().trim();
                    //Log.d("TAG", name + " / "+email+" / "+mobile+" / "+profile);


                    Meal members = new Meal(mobile, date, email, name, name, name, name, nick);

                    listdata.add(members);
                    loadingDialog.dismisstDialoglog();


                }
                Collections.sort(listdata, new Comparator<Meal>() {
                    @Override
                    public int compare(Meal m1, Meal m2) {
                        return Integer.compare(ValidationUtil.replacePoet(m1.getNick()), ValidationUtil.replacePoet(m2.getNick()));
                    }

                });
                adpter = new MealReportDailyListAdapter(MealBoardActivity.this, listdata, new MealReportDailyListAdapter.OnItemClickListener() {
                    @Override
                    public void onContactSelected(Meal item) {
                        DialogCustom.showSuccessMessage(MealBoardActivity.this, item.getName() + item.getEmail());

                    }
                });

                meal_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                meal_recyclerView.setItemAnimator(new DefaultItemAnimator());
                meal_recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                meal_recyclerView.setHasFixedSize(true);
                meal_recyclerView.setAdapter(adpter);
                adpter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                DialogCustom.showErrorMessage(MealBoardActivity.this, databaseError.getMessage());

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MealBoardActivity.this, DashboardActivity.class);
        DialogCustom.doClearActivity(intent, MealBoardActivity.this);
    }
}