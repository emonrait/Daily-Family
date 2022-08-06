
package com.raihan.dailyfamily.activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

        tv_genereal_header_title.setText(R.string.balance_informatiom);

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



        getFbddata();

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
                    Report report = new Report(member_name, date, amo, invoice, email,nick);
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
}