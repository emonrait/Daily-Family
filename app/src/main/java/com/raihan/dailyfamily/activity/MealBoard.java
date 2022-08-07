package com.raihan.dailyfamily.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.raihan.dailyfamily.R;
import com.raihan.dailyfamily.model.AutoLogout;
import com.raihan.dailyfamily.model.DialogCustom;
import com.raihan.dailyfamily.model.GlobalVariable;
import com.raihan.dailyfamily.model.ValidationUtil;

public class MealBoard extends AutoLogout {
    GlobalVariable globalVariable;
    private ImageView ivLogout;
    private ImageView ivBack;
    private TextView tv_genereal_header_title;

    private TextView date_value;
    private TextView breakfast_value;
    private TextView launch_value;
    private TextView dinner_value;

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

        globalVariable = ((GlobalVariable) getApplicationContext());

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

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MealBoard.this, DashboardActivity.class);
        DialogCustom.doClearActivity(intent, MealBoard.this);
    }
}