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
import android.widget.EditText;
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
import com.raihan.dailyfamily.model.Members;
import com.raihan.dailyfamily.model.Report;
import com.raihan.dailyfamily.model.ReportListAdapter;
import com.raihan.dailyfamily.model.ValidationUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MealBoard extends AutoLogout {
    GlobalVariable globalVariable;
    private ImageView ivLogout;
    private ImageView ivBack;
    private TextView tv_genereal_header_title;

    private TextView date_value;
    private TextView breakfast_value;
    private TextView launch_value;
    private TextView dinner_value;

    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceMeal;
    DatabaseReference databaseReferenceM;
    ArrayList<Report> listdata;
    RecyclerView meal_recyclerView;
    ReportListAdapter adpter;
    LoadingDialog loadingDialog = new LoadingDialog(MealBoard.this);

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

        globalVariable = ((GlobalVariable) getApplicationContext());
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceMeal = FirebaseDatabase.getInstance().getReference("Meal");
        databaseReferenceM = FirebaseDatabase.getInstance().getReference("Members");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MealBoard.this, DashboardActivity.class);
                DialogCustom.doClearActivity(intent, MealBoard.this);
            }
        });

        tv_genereal_header_title.setText(R.string.meal_board);

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCustom.englishcustomLogout(MealBoard.this);
            }
        });

        date_value.setText(ValidationUtil.getTransactionCurrentDate());
        totalMeal();
        allMemberMealInfo();

    }

    private void totalMeal() {
        if (!DialogCustom.isOnline(MealBoard.this)) {
            DialogCustom.showInternetConnectionMessage(MealBoard.this);
        } else {
            //loadingDialog.startDialoglog();
            Query queryt = databaseReferenceMeal.orderByChild("email").equalTo(user.getEmail());
            queryt.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // loadingDialog.dismisstDialoglog();
                    double breakfasttotal = 0;
                    double launchtotal = 0;
                    double dinnertotal = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        if (map.get("date").equals(ValidationUtil.getTransactionCurrentDate())) {
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
                    DialogCustom.showErrorMessage(MealBoard.this, databaseError.getMessage());
                }
            });
        }
    }

    private void allMemberMealInfo() {
        LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.startDialoglog();
        ArrayList<String> emailList = new ArrayList<>();
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
                    //Log.d("TAG", name + " / "+email+" / "+mobile+" / "+profile);
                    Report members = new Report(name, mobile, email, profile, nick, "");
                    if (globalVariable.isMemberFlag()) {
                        emailList.add(email);
                        globalVariable.setEmailList(emailList);
                        globalVariable.setMemberFlag(false);
                    }
                    listdata.add(members);
                    loadingDialog.dismisstDialoglog();


                }
                Collections.sort(listdata, new Comparator<Report>() {
                    @Override
                    public int compare(Report m1, Report m2) {
                        return Integer.compare(ValidationUtil.replacePoet(m1.getNick()), ValidationUtil.replacePoet(m2.getNick()));
                    }

                });
                adpter = new ReportListAdapter(MealBoard.this, listdata, new ReportListAdapter.OnItemClickListener() {
                    @Override
                    public void onContactSelected(Report item) {
                        DialogCustom.showSuccessMessage(MealBoard.this, item.getName() + item.getEmail());

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
                DialogCustom.showErrorMessage(MealBoard.this, databaseError.getMessage());

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MealBoard.this, DashboardActivity.class);
        DialogCustom.doClearActivity(intent, MealBoard.this);
    }
}