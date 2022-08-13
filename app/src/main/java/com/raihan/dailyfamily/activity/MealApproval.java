package com.raihan.dailyfamily.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.raihan.dailyfamily.model.MealApprovalAdapter;
import com.raihan.dailyfamily.model.MealReportDailyListAdapter;
import com.raihan.dailyfamily.model.ValidationUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class MealApproval extends AutoLogout {
    GlobalVariable globalVariable;
    private ImageView ivLogout;
    private ImageView ivBack;
    private TextView tv_genereal_header_title;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceMeal;
    DatabaseReference databaseReferenceBazaar;
    DatabaseReference databaseReferenceM;
    ArrayList<Meal> listdata;
    RecyclerView meal_recyclerView;
    MealApprovalAdapter adpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_approval);
        ivLogout = findViewById(R.id.ivLogout);
        ivBack = findViewById(R.id.ivBack);
        tv_genereal_header_title = findViewById(R.id.tv_genereal_header_title);
        meal_recyclerView = findViewById(R.id.meal_recyclerView);

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
                Intent intent = new Intent(MealApproval.this, DashboardActivity.class);
                DialogCustom.doClearActivity(intent, MealApproval.this);
            }
        });

        tv_genereal_header_title.setText(R.string.meal_board);

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCustom.englishcustomLogout(MealApproval.this);
            }
        });

        allMemberMealInfo();


    }

    private void allMemberMealInfo() {
        LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.startDialoglog();
        listdata = new ArrayList<>();
        Query queryt = databaseReferenceMeal.orderByChild("flag").equalTo("N");

        queryt.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadingDialog.dismisstDialoglog();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String email = ds.child("email").getValue(String.class);
                    String date = ds.child("date").getValue(String.class);
                    String breakfast = ds.child("breakfast").getValue(String.class);
                    String launch = ds.child("launch").getValue(String.class);
                    String dinner = ds.child("dinner").getValue(String.class);
                    String flag = ds.child("flag").getValue(String.class);
                    String id = ds.child("id").getValue(String.class);

                    Meal meal = new Meal(email, date, email, breakfast, launch, dinner, flag, id);

                    listdata.add(meal);
                    loadingDialog.dismisstDialoglog();


                }
                /*Collections.sort(listdata, new Comparator<Meal>() {
                    @Override
                    public int compare(Meal m1, Meal m2) {
                        return Integer.compare(ValidationUtil.replacePoet(m1.getNick()), ValidationUtil.replacePoet(m2.getNick()));
                    }

                });*/
                adpter = new MealApprovalAdapter(MealApproval.this, listdata, new MealApprovalAdapter.OnItemClickListener() {
                    @Override
                    public void onContactSelected(Meal item) {
                        DialogCustom.showSuccessMessage(MealApproval.this, item.getName() + item.getEmail());

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
                DialogCustom.showErrorMessage(MealApproval.this, databaseError.getMessage());
                loadingDialog.dismisstDialoglog();

            }

        });
    }

    private void totalMeal() {
        if (!DialogCustom.isOnline(MealApproval.this)) {
            DialogCustom.showInternetConnectionMessage(MealApproval.this);
        } else {
            //loadingDialog.startDialoglog();
            Query queryt = databaseReferenceMeal.orderByChild("flag").equalTo("N");
            queryt.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // loadingDialog.dismisstDialoglog();
                    double breakfasttotal = 0;
                    double launchtotal = 0;
                    double dinnertotal = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        if (!map.isEmpty()) {
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
                  /*  breakfast_value.setText(String.valueOf(breakfasttotal));
                    launch_value.setText(String.valueOf(launchtotal));
                    dinner_value.setText(String.valueOf(dinnertotal));*/
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //loadingDialog.dismisstDialoglog();
                    //throw databaseError.toException(); // don't ignore errors
                    DialogCustom.showErrorMessage(MealApproval.this, databaseError.getMessage());
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MealApproval.this, DashboardActivity.class);
        DialogCustom.doClearActivity(intent, MealApproval.this);
    }
}