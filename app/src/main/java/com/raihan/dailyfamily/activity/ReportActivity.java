
package com.raihan.dailyfamily.activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.raihan.dailyfamily.R;
import com.raihan.dailyfamily.model.AutoLogout;
import com.raihan.dailyfamily.model.DialogCustom;
import com.raihan.dailyfamily.model.Report;
import com.raihan.dailyfamily.model.ReportListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.raihan.dailyfamily.model.ValidationUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class ReportActivity extends AutoLogout {
    DatabaseReference reference;
    RecyclerView recylerReport;
    ReportListAdapter reportListAdapter;
    FirebaseDatabase fbd;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    String check;
    private ImageView ivLogout;
    private ImageView ivBack;
    private TextView tv_genereal_header_title;
    private EditText input_name;
    ArrayList<Report> listdata = new ArrayList<>();
    LoadingDialog loadingDialog = new LoadingDialog(this);
    private double alltotalMeal = 0.0;
    private double perMealAmount = 0.0;
    private double alltotalCostAmount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        ivLogout = findViewById(R.id.ivLogout);
        ivBack = findViewById(R.id.ivBack);
        tv_genereal_header_title = findViewById(R.id.tv_genereal_header_title);
        recylerReport = findViewById(R.id.recylerReport);
        input_name = findViewById(R.id.input_name);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        assert user != null;
        check = user.getEmail();
        fbd = FirebaseDatabase.getInstance();


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportActivity.this, DashboardActivity.class);
                DialogCustom.doClearActivity(intent, ReportActivity.this);
            }
        });

        tv_genereal_header_title.setText(R.string.report);

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCustom.englishcustomLogout(ReportActivity.this);
            }
        });

        reportListAdapter = new ReportListAdapter(this, listdata, new ReportListAdapter.OnItemClickListener() {
            @Override
            public void onContactSelected(Report item) {
                DialogCustom.showSuccessMessage(ReportActivity.this, item.getName() + item.getEmail());

            }
        });

        allTotalBazar();


        input_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do some thing now
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                reportListAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do something at this time

            }
        });


    }

    void getFbddata() {
        loadingDialog.startDialoglog();

        reference = fbd.getReference().child("Members");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {


                    String member_name = ds.child("member_name").getValue(String.class);
                    String date = ds.child("date").getValue(String.class);
                    String amo = ds.child("amount").getValue(String.class);
                    String invoice = ds.child("invoiceno").getValue(String.class);
                    String email = ds.child("email").getValue(String.class);
                    String nick = ds.child("nick").getValue(String.class);
                    // Log.d("TAG", date + " / "+txn);
                    Report report = new Report(member_name, date, amo, invoice, email, nick, alltotalMeal, perMealAmount, alltotalCostAmount);
                    listdata.add(report);
                    // Log.e("Data--3", listitem.getTxnid());
                    loadingDialog.dismisstDialoglog();

                }

                Collections.sort(listdata, new Comparator<Report>() {
                    @Override
                    public int compare(Report m1, Report m2) {
                        return Integer.compare(ValidationUtil.replacePoet(m1.getNick()), ValidationUtil.replacePoet(m2.getNick()));
                    }

                });
                reportListAdapter = new ReportListAdapter(ReportActivity.this, listdata, new ReportListAdapter.OnItemClickListener() {
                    @Override
                    public void onContactSelected(Report item) {
                        DialogCustom.showSuccessMessage(ReportActivity.this, item.getName() + item.getEmail());

                    }
                });
                recylerReport.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recylerReport.setItemAnimator(new DefaultItemAnimator());
                recylerReport.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

                recylerReport.setHasFixedSize(true);
                recylerReport.setAdapter(reportListAdapter);
                reportListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                DialogCustom.showErrorMessage(ReportActivity.this, databaseError.getMessage());

            }
        });

    }

    private void allTotalMeal() {
        if (!DialogCustom.isOnline((ReportActivity.this))) {
            DialogCustom.showInternetConnectionMessage(ReportActivity.this);
        } else {
            FirebaseDatabase.getInstance().getReference().child("Meal").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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

                    alltotalMeal = (breakfasttotal * .5) + launchtotal + dinnertotal;
                    perMealAmount = alltotalCostAmount / alltotalMeal;

                    Log.d("alltotalMeal", String.valueOf(alltotalMeal));
                    Log.d("perMealAmount", String.valueOf(perMealAmount));

                    getFbddata();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    DialogCustom.showErrorMessage(ReportActivity.this, databaseError.getMessage());
                }
            });
        }
    }

    private void allTotalBazar() {

        if (!DialogCustom.isOnline(ReportActivity.this)) {
            DialogCustom.showInternetConnectionMessage(ReportActivity.this);
        } else {
            loadingDialog.startDialoglog();
            FirebaseDatabase.getInstance().getReference().child("Bazaar").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    double bazartotal = 0;

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        if (map.get("flag").equals("Y")) {
                            Object breakfast = map.get("amount");

                            double bazarvalue = Double.parseDouble(ValidationUtil.replacecomma(String.valueOf(breakfast)));
                            bazartotal += bazarvalue;
                            alltotalCostAmount = bazartotal;

                            Log.d("alltotalCostAmount", String.valueOf(alltotalCostAmount));

                        }
                    }
                    loadingDialog.dismisstDialoglog();
                    allTotalMeal();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    DialogCustom.showErrorMessage(ReportActivity.this, databaseError.getMessage());
                    loadingDialog.dismisstDialoglog();
                }
            });
        }
    }
}