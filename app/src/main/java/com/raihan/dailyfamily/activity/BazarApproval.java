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
import com.raihan.dailyfamily.model.Bazaar;
import com.raihan.dailyfamily.model.BazarApprovalAdapter;
import com.raihan.dailyfamily.model.DialogCustom;
import com.raihan.dailyfamily.model.GlobalVariable;
import com.raihan.dailyfamily.model.Meal;
import com.raihan.dailyfamily.model.MealApprovalAdapter;

import java.util.ArrayList;

public class BazarApproval extends AutoLogout {
    GlobalVariable globalVariable;
    private ImageView ivLogout;
    private ImageView ivBack;
    private TextView tv_genereal_header_title;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceMeal;
    DatabaseReference databaseReferenceBazaar;
    DatabaseReference databaseReferenceM;
    ArrayList<Bazaar> listdata;
    RecyclerView meal_recyclerView;
    BazarApprovalAdapter adpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bazar_approval);
        ivLogout = findViewById(R.id.ivLogout);
        ivBack = findViewById(R.id.ivBack);
        tv_genereal_header_title = findViewById(R.id.tv_genereal_header_title);
        meal_recyclerView = findViewById(R.id.meal_recyclerView);

        globalVariable = ((GlobalVariable) getApplicationContext());
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceMeal = FirebaseDatabase.getInstance().getReference("Meal");
        databaseReferenceM = FirebaseDatabase.getInstance().getReference("Members");
        databaseReferenceBazaar = FirebaseDatabase.getInstance().getReference("Bazaar");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BazarApproval.this, DashboardActivity.class);
                DialogCustom.doClearActivity(intent, BazarApproval.this);
            }
        });

        tv_genereal_header_title.setText(R.string.bazaar_approval);

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCustom.englishcustomLogout(BazarApproval.this);
            }
        });

        allMemberMealInfo();
    }

    private void allMemberMealInfo() {
        LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.startDialoglog();
        listdata = new ArrayList<>();
        Query queryt = databaseReferenceBazaar.orderByChild("flag").equalTo("N");

        queryt.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadingDialog.dismisstDialoglog();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String email = ds.child("email").getValue(String.class);
                    String date = ds.child("date").getValue(String.class);
                    String amount = ds.child("amount").getValue(String.class);
                    String id = ds.child("id").getValue(String.class);
                    String productDetails = ds.child("productDetails").getValue(String.class);
                    String flag = ds.child("flag").getValue(String.class);

                    Bazaar bazaar = new Bazaar(id, date, email, amount, productDetails, flag);

                    listdata.add(bazaar);
                    loadingDialog.dismisstDialoglog();


                }
                /*Collections.sort(listdata, new Comparator<Meal>() {
                    @Override
                    public int compare(Meal m1, Meal m2) {
                        return Integer.compare(ValidationUtil.replacePoet(m1.getNick()), ValidationUtil.replacePoet(m2.getNick()));
                    }

                });*/
                adpter = new BazarApprovalAdapter(BazarApproval.this, listdata, new BazarApprovalAdapter.OnItemClickListener() {
                    @Override
                    public void onContactSelected(Bazaar item) {
                        DialogCustom.showSuccessMessage(BazarApproval.this, item.getAmount() + item.getEmail());

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
                DialogCustom.showErrorMessage(BazarApproval.this, databaseError.getMessage());
                loadingDialog.dismisstDialoglog();

            }

        });
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BazarApproval.this, DashboardActivity.class);
        DialogCustom.doClearActivity(intent, BazarApproval.this);
    }
}