package com.raihan.dailyfamily.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.raihan.dailyfamily.BuildConfig;
import com.raihan.dailyfamily.R;
import com.raihan.dailyfamily.model.AutoLogout;
import com.raihan.dailyfamily.model.DialogCustom;
import com.raihan.dailyfamily.model.GlobalVariable;
import com.raihan.dailyfamily.model.ListItem;
import com.raihan.dailyfamily.model.Meal;
import com.raihan.dailyfamily.model.Members;
import com.raihan.dailyfamily.model.ValidationUtil;

import java.util.Calendar;

public class AddMeal extends AutoLogout {
    GlobalVariable globalVariable;
    private ImageView ivLogout;
    private ImageView ivBack;
    private TextView tv_genereal_header_title;

    private EditText date_value;
    private EditText breakfast_value;
    private EditText launch_value;
    private EditText dinner_value;
    private Button btnSubmit;
    final Calendar myCalendar = Calendar.getInstance();

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);
        ivLogout = findViewById(R.id.ivLogout);
        ivBack = findViewById(R.id.ivBack);
        tv_genereal_header_title = findViewById(R.id.tv_genereal_header_title);

        date_value = findViewById(R.id.date_value);
        breakfast_value = findViewById(R.id.breakfast_value);
        launch_value = findViewById(R.id.launch_value);
        dinner_value = findViewById(R.id.dinner_value);
        btnSubmit = findViewById(R.id.btnSubmit);


        globalVariable = ((GlobalVariable) getApplicationContext());

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceMeal = FirebaseDatabase.getInstance().getReference("Meal");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMeal.this, DashboardActivity.class);
                DialogCustom.doClearActivity(intent, AddMeal.this);
            }
        });

        tv_genereal_header_title.setText(R.string.add_daily_meal);

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCustom.englishcustomLogout(AddMeal.this);
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
                new DatePickerDialog(AddMeal.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (date_value.getText().toString().trim().isEmpty()) {
                    date_value.requestFocus();
                    DialogCustom.showErrorMessage(AddMeal.this, "Please Enter Date.");

                } else if (breakfast_value.getText().toString().trim().isEmpty()) {
                    breakfast_value.requestFocus();
                    DialogCustom.showErrorMessage(AddMeal.this, "Please Enter Your Breakfast Meal.");

                } else if (launch_value.getText().toString().trim().isEmpty()) {
                    launch_value.requestFocus();
                    DialogCustom.showErrorMessage(AddMeal.this, "Please Enter Your Launch Meal.");

                } else if (dinner_value.getText().toString().trim().isEmpty()) {
                    dinner_value.requestFocus();
                    DialogCustom.showErrorMessage(AddMeal.this, "Please Enter Your Dinner Meal.");

                } else if (ValidationUtil.getNumber(breakfast_value.getText().toString().trim(), launch_value.getText().toString().trim(), dinner_value.getText().toString().trim()) <= 0) {
                    dinner_value.requestFocus();
                    DialogCustom.showErrorMessage(AddMeal.this, "Please Select at least any type of one Meal.");
                } else {
                    String id = databaseReferenceMeal.push().getKey();
                    String date = date_value.getText().toString().trim();
                    String email = firebaseAuth.getCurrentUser().getEmail();
                    String breakfast = breakfast_value.getText().toString().trim();
                    String launch = launch_value.getText().toString().trim();
                    String dinner = dinner_value.getText().toString().trim();
                    String flag = "Y";
                    Meal meal = new Meal(id, date, email, breakfast, launch, dinner, flag);
                    // assert id != null;
                    //databaseReferenceMeal.child(id).setValue(meal);
                    final LoadingDialog loadingDialog = new LoadingDialog(AddMeal.this);
                    loadingDialog.startDialoglog();
                    try {
                        databaseReferenceMeal.child(id)
                                .setValue(meal)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            loadingDialog.dismisstDialoglog();
                                            DialogCustom.showSuccessMessage(AddMeal.this, "Your Meal Add Successfully.");


                                        } else {
                                            DialogCustom.showErrorMessage(AddMeal.this, task.getResult() + "Unsuccessful");
                                            loadingDialog.dismisstDialoglog();

                                        }
                                        loadingDialog.dismisstDialoglog();
                                    }
                                });
                    } catch (Exception e) {
                        DialogCustom.showErrorMessage(AddMeal.this, e.getMessage());
                    }

                }
            }
        });


    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddMeal.this, DashboardActivity.class);
        DialogCustom.doClearActivity(intent, AddMeal.this);
    }
}