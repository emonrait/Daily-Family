package com.raihan.dailyfamily.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.raihan.dailyfamily.R;
import com.raihan.dailyfamily.model.AutoLogout;
import com.raihan.dailyfamily.model.LogoutService;
import com.raihan.dailyfamily.model.MenuAdapter;
import com.raihan.dailyfamily.model.MenuModel;

import java.util.ArrayList;

public class NewDashboardActivity extends AutoLogout {
    GridView menuGridView;
    ArrayList<MenuModel> list = new ArrayList<>();
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dashboard);

        menuGridView = (GridView) findViewById(R.id.menuGridView);

        menuView();

        LogoutService.logout(this);

    }

    private void menuView() {
        list.clear();
        list.add(new MenuModel("A/C Balance", R.drawable.ic_bank_statement, "ACBAL"));
        list.add(new MenuModel("A/C Statement", R.drawable.ic_bank_statement, "ACBAL"));
        list.add(new MenuModel("Fund Transfer", R.drawable.ic_bank_statement, "ACSTMT"));
        list.add(new MenuModel("Standing Ins", R.drawable.ic_bank_statement, "ACSTMT"));
        list.add(new MenuModel("Mobile Recharge", R.drawable.ic_bank_statement, "FTHOME"));
        list.add(new MenuModel("Bill Pay", R.drawable.ic_bank_statement, "STINS"));
        list.add(new MenuModel("Stop Cheque", R.drawable.ic_bank_statement, "STINS"));
        list.add(new MenuModel("Cheque Status", R.drawable.ic_bank_statement, "STINS"));
        list.add(new MenuModel("Ecommerce", R.drawable.ic_bank_statement, "STINS"));
        list.add(new MenuModel("bKash", R.drawable.ic_bank_statement, "STINS"));
        list.add(new MenuModel("Payoneer Payment", R.drawable.ic_bank_statement, "STINS"));
        list.add(new MenuModel("Credit Card", R.drawable.ic_bank_statement, "STINS"));
        MenuAdapter adapter = new MenuAdapter(this, list);
        menuGridView.setAdapter(adapter);

        menuGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pstion, long l) {
                TextView menu_soft_code = (TextView) view.findViewById(R.id.menu_soft_code);
                TextView menu_name = (TextView) view.findViewById(R.id.menu_name);

                if ("ACBAL".equals(menu_soft_code.getText().toString())) {
                    Intent intent = new Intent(NewDashboardActivity.this, DashboardActivity.class);
                    startActivity(intent);

                }


            }
        });
    }


}