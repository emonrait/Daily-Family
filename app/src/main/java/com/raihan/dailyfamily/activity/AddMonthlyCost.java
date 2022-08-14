package com.raihan.dailyfamily.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.raihan.dailyfamily.R;
import com.raihan.dailyfamily.model.AutoLogout;
import com.raihan.dailyfamily.model.Bazaar;
import com.raihan.dailyfamily.model.DialogCustom;
import com.raihan.dailyfamily.model.GlobalVariable;
import com.raihan.dailyfamily.model.MonthCost;
import com.raihan.dailyfamily.model.ValidationUtil;

import java.util.Calendar;
import java.util.Objects;

public class AddMonthlyCost extends AutoLogout {
    GlobalVariable globalVariable;
    private ImageView ivLogout;
    private ImageView ivBack;
    private TextView tv_genereal_header_title;


    private EditText date_value;
    private EditText house_rent_value;
    private EditText city_cor_bill_value;
    private EditText elictricity_value;
    private EditText bua_value;
    private EditText others_value;
    private EditText member_value;

    private Button btnSubmit;
    final Calendar myCalendar = Calendar.getInstance();

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceMonthCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_monthly_cost);

        ivLogout = findViewById(R.id.ivLogout);
        ivBack = findViewById(R.id.ivBack);
        tv_genereal_header_title = findViewById(R.id.tv_genereal_header_title);


        date_value = findViewById(R.id.date_value);
        house_rent_value = findViewById(R.id.house_rent_value);
        city_cor_bill_value = findViewById(R.id.city_cor_bill_value);
        elictricity_value = findViewById(R.id.elictricity_value);
        bua_value = findViewById(R.id.bua_value);
        others_value = findViewById(R.id.others_value);
        member_value = findViewById(R.id.member_value);

        btnSubmit = findViewById(R.id.btnSubmit);


        globalVariable = ((GlobalVariable) getApplicationContext());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceMonthCost = FirebaseDatabase.getInstance().getReference("MonthCost");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMonthlyCost.this, DashboardActivity.class);
                DialogCustom.doClearActivity(intent, AddMonthlyCost.this);
            }
        });

        tv_genereal_header_title.setText(R.string.add_daily_bazar_cost);

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCustom.englishcustomLogout(AddMonthlyCost.this);
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

            }
        };
        date_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddMonthlyCost.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (date_value.getText().toString().trim().isEmpty()) {
                    date_value.requestFocus();
                    DialogCustom.showErrorMessage(AddMonthlyCost.this, "Please Enter Date.");

                } else if (house_rent_value.getText().toString().trim().isEmpty()) {
                    house_rent_value.requestFocus();
                    DialogCustom.showErrorMessage(AddMonthlyCost.this, "Please Enter House Rent Bill.");

                } else if (city_cor_bill_value.getText().toString().trim().isEmpty()) {
                    city_cor_bill_value.requestFocus();
                    DialogCustom.showErrorMessage(AddMonthlyCost.this, "Please Enter City Corporation Bill.");

                } else if (elictricity_value.getText().toString().trim().isEmpty()) {
                    elictricity_value.requestFocus();
                    DialogCustom.showErrorMessage(AddMonthlyCost.this, "Please Enter Electricity Bill.");

                } else if (bua_value.getText().toString().trim().isEmpty()) {
                    bua_value.requestFocus();
                    DialogCustom.showErrorMessage(AddMonthlyCost.this, "Please Enter Bua Bill.");

                } else if (member_value.getText().toString().trim().isEmpty()) {
                    member_value.requestFocus();
                    DialogCustom.showErrorMessage(AddMonthlyCost.this, "Please Enter Total Mess Member.");

                } else {
                    String id = databaseReferenceMonthCost.push().getKey();
                    String date = date_value.getText().toString().trim();
                    String houseRent = house_rent_value.getText().toString().trim();
                    String electricyBill = elictricity_value.getText().toString().trim();
                    String buaBill = bua_value.getText().toString().trim();
                    String otherBill = others_value.getText().toString().trim();
                    String member = member_value.getText().toString().trim();
                    String cityBill = city_cor_bill_value.getText().toString().trim();
                    String flag = "Y";
                    String updateBy = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();
                    MonthCost monthCost = new MonthCost(id, date, houseRent, electricyBill, buaBill, otherBill, member, cityBill, flag, updateBy);


                    final LoadingDialog loadingDialog = new LoadingDialog(AddMonthlyCost.this);
                    loadingDialog.startDialoglog();
                    try {
                        assert id != null;
                        databaseReferenceMonthCost.child(id)
                                .setValue(monthCost)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            house_rent_value.setText("");
                                            elictricity_value.setText("");
                                            bua_value.setText("");
                                            others_value.setText("");
                                            member_value.setText("");
                                            city_cor_bill_value.setText("");
                                            loadingDialog.dismisstDialoglog();
                                            DialogCustom.showSuccessMessage(AddMonthlyCost.this, "Your Product Cost Add Successfully.");


                                        } else {
                                            DialogCustom.showErrorMessage(AddMonthlyCost.this, task.getResult() + "Unsuccessful");
                                            loadingDialog.dismisstDialoglog();

                                        }
                                        loadingDialog.dismisstDialoglog();
                                    }
                                });

                    } catch (Exception e) {
                        DialogCustom.showErrorMessage(AddMonthlyCost.this, e.getMessage());
                        loadingDialog.dismisstDialoglog();
                    }

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddMonthlyCost.this, DashboardActivity.class);
        DialogCustom.doClearActivity(intent, AddMonthlyCost.this);
    }
}