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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.raihan.dailyfamily.R;
import com.raihan.dailyfamily.model.AutoLogout;
import com.raihan.dailyfamily.model.Bazaar;
import com.raihan.dailyfamily.model.DialogCustom;
import com.raihan.dailyfamily.model.GlobalVariable;
import com.raihan.dailyfamily.model.Meal;
import com.raihan.dailyfamily.model.ValidationUtil;

import java.util.Calendar;
import java.util.Map;
import java.util.Objects;

public class AddBazarCost extends AutoLogout {
    GlobalVariable globalVariable;
    private ImageView ivLogout;
    private ImageView ivBack;
    private TextView tv_genereal_header_title;

    private TextView tv_date;
    private TextView tv_amount;
    private TextView tv_product_details;

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

        tv_date = findViewById(R.id.tv_date);
        tv_amount = findViewById(R.id.tv_amount);
        tv_product_details = findViewById(R.id.tv_product_details);

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
        totalBazar();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                date_value.setText(ValidationUtil.dateFormate(myCalendar));
                totalBazar();
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

                } else if (amount_value.getText().toString().trim().equals("0")) {
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
                                            amount_value.setText("");
                                            product_details_value.setText("");
                                            loadingDialog.dismisstDialoglog();
                                            DialogCustom.showSuccessMessage(AddBazarCost.this, "Your Product Cost Add Successfully.");


                                        } else {
                                            DialogCustom.showErrorMessage(AddBazarCost.this, task.getResult() + "Unsuccessful");
                                            loadingDialog.dismisstDialoglog();

                                        }
                                        loadingDialog.dismisstDialoglog();
                                    }
                                });
                        totalBazar();
                    } catch (Exception e) {
                        DialogCustom.showErrorMessage(AddBazarCost.this, e.getMessage());
                        loadingDialog.dismisstDialoglog();
                    }

                }
            }
        });

    }

    private void totalBazar() {
        if (!DialogCustom.isOnline(AddBazarCost.this)) {
            DialogCustom.showInternetConnectionMessage(AddBazarCost.this);
        } else {
            //loadingDialog.startDialoglog();
            Query queryt = databaseReferenceBazaar.orderByChild("date").equalTo(date_value.getText().toString().trim());
            queryt.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // loadingDialog.dismisstDialoglog();
                    double amounttotal = 0;
                    String productDetailValue = "";
                    double dinnertotal = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        if (map.get("email").equals(firebaseAuth.getCurrentUser().getEmail().trim()) && !map.get("flag").equals("R")) {
                            Object amount = map.get("amount");

                            double amountvalue = Double.parseDouble(ValidationUtil.replacecomma(String.valueOf(amount)));
                            amounttotal += amountvalue;
                            Object productDetails = map.get("productDetails");
                            productDetailValue = String.valueOf(productDetails);

                        }


                    }
                    //globalVariable.setTotalAmount(String.valueOf(total));
                    tv_date.setText(date_value.getText().toString().trim());
                    tv_amount.setText(ValidationUtil.commaSeparateAmount(String.valueOf(amounttotal)));
                    tv_product_details.setText(String.valueOf(productDetailValue));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    DialogCustom.showErrorMessage(AddBazarCost.this, databaseError.getMessage());
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddBazarCost.this, DashboardActivity.class);
        DialogCustom.doClearActivity(intent, AddBazarCost.this);
    }
}