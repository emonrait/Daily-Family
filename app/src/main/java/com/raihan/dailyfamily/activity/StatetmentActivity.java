package com.raihan.dailyfamily.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.raihan.dailyfamily.R;
import com.raihan.dailyfamily.model.AutoLogout;
import com.raihan.dailyfamily.model.Bazaar;
import com.raihan.dailyfamily.model.BazarListAdapter;
import com.raihan.dailyfamily.model.DialogCustom;
import com.raihan.dailyfamily.model.ListItem;
import com.raihan.dailyfamily.model.LogoutService;
import com.raihan.dailyfamily.model.Meal;
import com.raihan.dailyfamily.model.MealListAdapter;
import com.raihan.dailyfamily.model.MyAdpterNew;
import com.raihan.dailyfamily.model.StatementListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StatetmentActivity extends AutoLogout {
    TextView textView;
    DatabaseReference reference;
    RecyclerView recyler_meal;
    RecyclerView recyler_bazaar;
    RecyclerView recyler_payment;
    // ArrayList<ListItem> listdata;
    MyAdpterNew adpter;
    StatementListAdapter statementListAdapter;
    MealListAdapter mealListAdapter;
    BazarListAdapter bazarListAdapter;
    FirebaseDatabase fbd;
    ListItem listitem;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    String check;
    private ImageView ivLogout;
    private ImageView ivBack;
    private TextView tv_genereal_header_title;
    private EditText input_invoice;

    private TabLayout tabLayout;

    ArrayList<ListItem> listdata = new ArrayList<>();
    ArrayList<Meal> mealdata = new ArrayList<>();
    ArrayList<Bazaar> bazaardata = new ArrayList<>();
    LoadingDialog loadingDialog = new LoadingDialog(this);


    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement);

        textView = findViewById(R.id.text);
        ivLogout = findViewById(R.id.ivLogout);
        ivBack = findViewById(R.id.ivBack);
        tv_genereal_header_title = findViewById(R.id.tv_genereal_header_title);
        recyler_meal = findViewById(R.id.recyler_meal);
        recyler_bazaar = findViewById(R.id.recyler_bazaar);
        recyler_payment = findViewById(R.id.recyler_payment);
        input_invoice = findViewById(R.id.input_invoice);
        tabLayout = findViewById(R.id.tab_layout);


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        check = user.getEmail();
        fbd = FirebaseDatabase.getInstance();

        // listdata = new ArrayList<ListItem>();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatetmentActivity.this, DashboardActivity.class);
                DialogCustom.doClearActivity(intent, StatetmentActivity.this);
            }
        });

        tv_genereal_header_title.setText("Statement");

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCustom.englishcustomLogout(StatetmentActivity.this);
            }
        });

        getMealdata();

        tabLayout.addTab(tabLayout.newTab().setText("Meal"));
        tabLayout.addTab(tabLayout.newTab().setText("Bazaar"));
        tabLayout.addTab(tabLayout.newTab().setText("Payment"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    recyler_meal.setVisibility(View.VISIBLE);
                    recyler_bazaar.setVisibility(View.GONE);
                    recyler_payment.setVisibility(View.GONE);
                    getMealdata();

                } else if (tab.getPosition() == 1) {
                    recyler_meal.setVisibility(View.GONE);
                    recyler_bazaar.setVisibility(View.VISIBLE);
                    recyler_payment.setVisibility(View.GONE);
                    getBazardata();
                } else if (tab.getPosition() == 2) {
                    recyler_meal.setVisibility(View.GONE);
                    recyler_bazaar.setVisibility(View.GONE);
                    recyler_payment.setVisibility(View.VISIBLE);
                    getPaymnetdata();

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




    /*    statementListAdapter = new StatementListAdapter(this, listdata, new StatementListAdapter.OnItemClickListener() {
            @Override
            public void onContactSelected(ListItem item) {
                DialogCustom.showSuccessMessage(StatetmentActivity.this, item.getAmount() + item.getEmail());

            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        // adpter = new MyAdpterNew(listdata);


        recyclerView.setHasFixedSize(true);
        getFbddata();
        getToalbal();

        input_invoice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do some thing now
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                statementListAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do something at this time

            }
        });
*/
        LogoutService.logout(this);


    }


    private void getPaymnetdata() {
        listdata.clear();
        loadingDialog.startDialoglog();
        reference = fbd.getReference().child("Transaction");
        reference.orderByChild("email").equalTo(check).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String txn = ds.child("id").getValue(String.class);
                    String date = ds.child("date").getValue(String.class);
                    String amo = ds.child("amount").getValue(String.class);
                    String invoice = ds.child("invoiceno").getValue(String.class);
                    String email = ds.child("email").getValue(String.class);
                    // Log.d("TAG", date + " / "+txn);
                    ListItem listitem = new ListItem(txn, date, amo, invoice, email);
                    listdata.add(listitem);
                    // Log.e("Data--3", listitem.getTxnid());
                    loadingDialog.dismisstDialoglog();

                }

                statementListAdapter = new StatementListAdapter(StatetmentActivity.this, listdata, new StatementListAdapter.OnItemClickListener() {
                    @Override
                    public void onContactSelected(ListItem item) {
                        DialogCustom.showSuccessMessage(StatetmentActivity.this, item.getAmount() + item.getEmail());

                    }
                });


                recyler_payment.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyler_payment.setItemAnimator(new DefaultItemAnimator());
                recyler_payment.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                recyler_payment.setHasFixedSize(true);

                recyler_payment.setAdapter(statementListAdapter);
                statementListAdapter.notifyDataSetChanged();
                loadingDialog.dismisstDialoglog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loadingDialog.dismisstDialoglog();
                DialogCustom.showErrorMessage(StatetmentActivity.this, databaseError.getMessage());
            }
        });
    }

    private void getMealdata() {
        mealdata.clear();
        loadingDialog.startDialoglog();
        reference = fbd.getReference().child("Meal");
        reference.orderByChild("email").equalTo(check).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String flag = ds.child("flag").getValue(String.class);
                    if (flag.equals("Y")) {
                        String email = ds.child("email").getValue(String.class);
                        String date = ds.child("date").getValue(String.class);
                        String breakfast = ds.child("breakfast").getValue(String.class);
                        String launch = ds.child("launch").getValue(String.class);
                        String dinner = ds.child("dinner").getValue(String.class);
                        String id = ds.child("id").getValue(String.class);
                        Meal meal = new Meal(id, date, email, breakfast, launch, dinner, flag);
                        mealdata.add(meal);
                    }

                    loadingDialog.dismisstDialoglog();

                }

                mealListAdapter = new MealListAdapter(StatetmentActivity.this, mealdata, new MealListAdapter.OnItemClickListener() {
                    @Override
                    public void onContactSelected(Meal item) {
                        DialogCustom.showSuccessMessage(StatetmentActivity.this, item.getBreakfast() + item.getEmail());

                    }
                });


                recyler_meal.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyler_meal.setItemAnimator(new DefaultItemAnimator());
                recyler_meal.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                recyler_meal.setHasFixedSize(true);

                recyler_meal.setAdapter(mealListAdapter);
                mealListAdapter.notifyDataSetChanged();
                loadingDialog.dismisstDialoglog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loadingDialog.dismisstDialoglog();
                DialogCustom.showErrorMessage(StatetmentActivity.this, databaseError.getMessage());
            }
        });
    }

    private void getBazardata() {
        bazaardata.clear();
        loadingDialog.startDialoglog();
        reference = fbd.getReference().child("Bazaar");
        reference.orderByChild("email").equalTo(check).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String flag = ds.child("flag").getValue(String.class);
                    if (flag.equals("Y")) {
                        String email = ds.child("email").getValue(String.class);
                        String date = ds.child("date").getValue(String.class);
                        String amount = ds.child("amount").getValue(String.class);
                        String id = ds.child("id").getValue(String.class);
                        String productDetails = ds.child("productDetails").getValue(String.class);


                        Bazaar bazaar = new Bazaar(id, date, email, amount, productDetails, flag);
                        bazaardata.add(bazaar);
                    }

                    // Log.e("Data--3", listitem.getTxnid());
                    loadingDialog.dismisstDialoglog();

                }

                bazarListAdapter = new BazarListAdapter(StatetmentActivity.this, bazaardata, new BazarListAdapter.OnItemClickListener() {
                    @Override
                    public void onContactSelected(Bazaar item) {
                        DialogCustom.showSuccessMessage(StatetmentActivity.this, item.getAmount() + item.getEmail());

                    }
                });


                recyler_bazaar.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyler_bazaar.setItemAnimator(new DefaultItemAnimator());
                recyler_bazaar.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                recyler_bazaar.setHasFixedSize(true);

                recyler_bazaar.setAdapter(bazarListAdapter);
                bazarListAdapter.notifyDataSetChanged();
                loadingDialog.dismisstDialoglog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loadingDialog.dismisstDialoglog();
                DialogCustom.showErrorMessage(StatetmentActivity.this, databaseError.getMessage());
            }
        });
    }


}
