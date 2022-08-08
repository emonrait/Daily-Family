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
import com.raihan.dailyfamily.model.Meal;
import com.raihan.dailyfamily.model.ValidationUtil;

import java.util.Calendar;
import java.util.Objects;

public class AddBazarCost extends AutoLogout {
    GlobalVariable globalVariable;
    private ImageView ivLogout;
    private ImageView ivBack;
    private TextView tv_genereal_header_title;

    private EditText date_value;
    private EditText amount_value;
    private EditText product_details_value;

    private Button btnSubmit;
    final Calendar myCalendar = Calendar.getInstance();

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceBazaar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bazar_cost);

        ivLogout = findViewById(R.id.ivLogout);
        ivBack = findViewById(R.id.ivBack);
        tv_genereal_header_title = findViewById(R.id.tv_genereal_header_title);

        date_value = findViewById(R.id.date_value);
        amount_value = findViewById(R.id.amount_value);
        product_details_value = findViewById(R.id.product_details_value);

        btnSubmit = findViewById(R.id.btnSubmit);


        globalVariable = ((GlobalVariable) getApplicationContext());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceBazaar = FirebaseDatabase.getInstance().getReference("Bazaar");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBazarCost.this, DashboardActivity.class);
                DialogCustom.doClearActivity(intent, AddBazarCost.this);
            }
        });

        tv_genereal_header_title.setText(R.string.add_daily_bazar_cost);

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCustom.englishcustomLogout(AddBazarCost.this);
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
                new DatePickerDialog(AddBazarCost.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (date_value.getText().toString().trim().isEmpty()) {
                    date_value.requestFocus();
                    DialogCustom.showErrorMessage(AddBazarCost.this, "Please Enter Date.");

                } else if (amount_value.getText().toString().trim().isEmpty()) {
                    amount_value.requestFocus();
                    DialogCustom.showErrorMessage(AddBazarCost.this, "Please Enter Total Amount.");

                } else if (product_details_value.getText().toString().trim().isEmpty()) {
                    product_details_value.requestFocus();
                    DialogCustom.showErrorMessage(AddBazarCost.this, "Please Enter Product Details.");

                } else {
                    String id = databaseReferenceBazaar.push().getKey();
                    String date = date_value.getText().toString().trim();
                    String email = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();
                    String amount = amount_value.getText().toString().trim();
                    String product_details = product_details_value.getText().toString().trim();
                    String flag = "N";
                    Bazaar meal = new Bazaar(id, date, email, amount, product_details, flag);

                    final LoadingDialog loadingDialog = new LoadingDialog(AddBazarCost.this);
                    loadingDialog.startDialoglog();
                    try {
                        assert id != null;
                        databaseReferenceBazaar.child(id)
                                .setValue(meal)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            loadingDialog.dismisstDialoglog();
                                            DialogCustom.showSuccessMessage(AddBazarCost.this, "Your Product Cost Add Successfully.");


                                        } else {
                                            DialogCustom.showErrorMessage(AddBazarCost.this, task.getResult() + "Unsuccessful");
                                            loadingDialog.dismisstDialoglog();

                                        }
                                        loadingDialog.dismisstDialoglog();
                                    }
                                });
                    } catch (Exception e) {
                        DialogCustom.showErrorMessage(AddBazarCost.this, e.getMessage());
                        loadingDialog.dismisstDialoglog();
                    }

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddBazarCost.this, DashboardActivity.class);
        DialogCustom.doClearActivity(intent, AddBazarCost.this);
    }
}