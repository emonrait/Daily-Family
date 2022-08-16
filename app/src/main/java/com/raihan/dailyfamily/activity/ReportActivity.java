
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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.Query;
import com.google.gson.Gson;
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
    DatabaseReference databaseReferenceMonthCost;
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
    String id = "";
    String date = "";
    String houseRent = "";
    String electricyBill = "";
    String buaBill = "";
    String otherBill = "";
    String member = "";
    String cityBill = "";

    String nperMealAmount = "";
    String ntotalCostAmount = "";
    String ndate = "";
    String nhouseRent = "";
    String nelectricyBill = "";
    String nbuaBill = "";
    String notherBill = "";
    String nmember = "";
    String ncityBill = "";
    String nname = "";
    String nemail = "";
    String nmobile = "";

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
        databaseReferenceMonthCost = FirebaseDatabase.getInstance().getReference("MonthCost");


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
                DialogCustom.showSuccessMessage(ReportActivity.this, item.toString());

            }
        });

        allTotalBazar();
        getMonthlyCost();


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
                    String datem = ds.child("date").getValue(String.class);
                    String amo = ds.child("amount").getValue(String.class);
                    String mobile = ds.child("mobile").getValue(String.class);
                    String email = ds.child("email").getValue(String.class);
                    String nick = ds.child("nick").getValue(String.class);
                    // Log.d("TAG", date + " / "+txn);

                    Report report = new Report(member_name,
                            datem,
                            amo,
                            mobile,
                            email,
                            nick,
                            alltotalMeal,
                            perMealAmount,
                            alltotalCostAmount,
                            id,
                            date,
                            houseRent,
                            electricyBill,
                            buaBill,
                            otherBill,
                            member,
                            cityBill,
                            mobile

                    );

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

                        Gson gson = new Gson();
                        String json = gson.toJson(item);
                        Log.d("item-->", String.valueOf(json));
                        DialogCustom.showSuccessMessage(ReportActivity.this, json.toString());
                        nperMealAmount = String.valueOf(item.getPerMealAmount());
                        ntotalCostAmount = "";
                        ndate = item.getDate();
                        nhouseRent = item.getHouseRent();
                        nelectricyBill = item.getElectricyBill();
                        nbuaBill = item.getBuaBill();
                        notherBill = item.getOtherBill();
                        nmember = item.getMember();
                        ncityBill = item.getCityBill();
                        nname = item.getName();
                        nemail = item.getEmail();
                        nmobile = item.getMobile();
                        showDialog();
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

    private void getMonthlyCost() {

        if (!DialogCustom.isOnline(ReportActivity.this)) {
            DialogCustom.showInternetConnectionMessage(ReportActivity.this);
        } else {
            //loadingDialog.startDialoglog();

            Query queryt = databaseReferenceMonthCost.orderByChild("flag").equalTo("Y");
            queryt.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    double temphouseRent = 0.0;
                    double tempelectricyBill = 0.0;
                    double tempbuaBill = 0.0;
                    double tempotherBill = 0.0;
                    double tempmember = 0.0;
                    double tempcityBill = 0.0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        id = "" + ds.child("id").getValue();
                        date = "" + ds.child("date").getValue();
                        temphouseRent = Double.parseDouble(ValidationUtil.replacecomma(String.valueOf(ds.child("houseRent").getValue())));
                        tempelectricyBill = Double.parseDouble(ValidationUtil.replacecomma(String.valueOf(ds.child("electricyBill").getValue())));
                        tempbuaBill = Double.parseDouble(ValidationUtil.replacecomma(String.valueOf(ds.child("buaBill").getValue())));
                        tempotherBill = Double.parseDouble(ValidationUtil.replacecomma(String.valueOf(ds.child("otherBill").getValue())));
                        tempmember = Double.parseDouble(ValidationUtil.replacecomma(String.valueOf(ds.child("member").getValue())));
                        tempcityBill = Double.parseDouble(ValidationUtil.replacecomma(String.valueOf(ds.child("cityBill").getValue())));

                        houseRent = String.valueOf(temphouseRent / tempmember);
                        electricyBill = String.valueOf(tempelectricyBill / tempmember);
                        buaBill = String.valueOf(tempbuaBill / tempmember);
                        otherBill = String.valueOf(tempotherBill / tempmember);
                        cityBill = String.valueOf(tempcityBill / tempmember);
                        member = String.valueOf(tempmember);


                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    DialogCustom.showErrorMessage(ReportActivity.this, databaseError.getMessage());
                }
            });
        }
    }


    private void showDialog() {
        final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this).setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View reg_layout = inflater.inflate(R.layout.mess_bill_invoice, null);
        dialog.setView(reg_layout);
        final android.app.AlertDialog alertDialog = dialog.create();
       /* WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// before
   */

        final TextView tv_name = reg_layout.findViewById(R.id.tv_name);
        final TextView tv_email = reg_layout.findViewById(R.id.tv_email);
        final TextView tv_mobile_no = reg_layout.findViewById(R.id.tv_mobile_no);
        final TextView tv_months = reg_layout.findViewById(R.id.tv_months);

        final TextView tv_total_breakfast = reg_layout.findViewById(R.id.tv_total_breakfast);
        final TextView tv_total_launch_meal = reg_layout.findViewById(R.id.tv_total_launch_meal);
        final TextView tv_total_diner_meal = reg_layout.findViewById(R.id.tv_total_diner_meal);
        final TextView tv_total_meal = reg_layout.findViewById(R.id.tv_total_meal);
        final TextView tv_total_meal_cost = reg_layout.findViewById(R.id.tv_total_meal_cost);
        final TextView tv_total_shopping_cost = reg_layout.findViewById(R.id.tv_total_shopping_cost);

        final TextView tv_house_rent = reg_layout.findViewById(R.id.tv_house_rent);
        final TextView tv_city_cor_bill = reg_layout.findViewById(R.id.tv_city_cor_bill);
        final TextView tv_elictricity_bill = reg_layout.findViewById(R.id.tv_elictricity_bill);
        final TextView tv_others_bill = reg_layout.findViewById(R.id.tv_others_bill);
        final TextView tv_bua_bill = reg_layout.findViewById(R.id.tv_bua_bill);

        final Button btn_reportCancel = reg_layout.findViewById(R.id.btn_reportCancel);
        final Button btn_save_receipt = reg_layout.findViewById(R.id.btn_save_receipt);

        tv_name.setText(nname);
        tv_email.setText(nemail);
        tv_mobile_no.setText(nmobile);
        tv_months.setText(ndate);

        tv_total_breakfast.setText(ndate);
        tv_total_launch_meal.setText(ndate);
        tv_total_diner_meal.setText(ndate);
        tv_total_meal.setText(ndate);
        tv_total_meal_cost.setText(ndate);
        tv_total_shopping_cost.setText(ndate);

        tv_house_rent.setText(nhouseRent);
        tv_city_cor_bill.setText(ncityBill);
        tv_elictricity_bill.setText(nelectricyBill);
        tv_bua_bill.setText(nbuaBill);
        tv_others_bill.setText(notherBill);


        btn_reportCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btn_save_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
        //  alertDialog.getWindow().setAttributes(lp);
    }
}