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
import com.raihan.dailyfamily.model.ListItem;
import com.raihan.dailyfamily.model.MyAdpterNew;
import com.raihan.dailyfamily.model.StatementListAllAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllTxnListActivity extends AutoLogout {

    DatabaseReference reference;
    RecyclerView recyclerView;
    MyAdpterNew adpter;
    StatementListAllAdapter statementListAdapter;
    FirebaseDatabase fbd;
    ListItem listitem;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    String check;
    private ImageView ivLogout;
    private ImageView ivBack;
    private TextView tv_genereal_header_title;
    private EditText input_invoice;
    ArrayList<ListItem> listdata = new ArrayList<>();
    LoadingDialog loadingDialog = new LoadingDialog(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_txn_list);

        ivLogout = findViewById(R.id.ivLogout);
        ivBack = findViewById(R.id.ivBack);
        tv_genereal_header_title = findViewById(R.id.tv_genereal_header_title);
        recyclerView = findViewById(R.id.recyler);
        input_invoice = findViewById(R.id.input_invoice);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        assert user != null;
        check = user.getEmail();
        fbd = FirebaseDatabase.getInstance();


        // listdata = new ArrayList<ListItem>();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllTxnListActivity.this, DashboardActivity.class);
                DialogCustom.doClearActivity(intent, AllTxnListActivity.this);
            }
        });

        tv_genereal_header_title.setText("All TXN List");

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCustom.englishcustomLogout(AllTxnListActivity.this);
            }
        });

        statementListAdapter = new StatementListAllAdapter(this, listdata, new StatementListAllAdapter.OnItemClickListener() {
            @Override
            public void onContactSelected(ListItem item) {
                DialogCustom.showSuccessMessage(AllTxnListActivity.this, item.getAmount() + item.getEmail());

            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        // adpter = new MyAdpterNew(listdata);


        recyclerView.setHasFixedSize(true);
        getFbddata();
        //  getToalbal();

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


    }

    void getFbddata() {
        loadingDialog.startDialoglog();

        reference = fbd.getReference().child("Transaction");
        reference.addValueEventListener(new ValueEventListener() {
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

                recyclerView.setAdapter(statementListAdapter);
                statementListAdapter.notifyDataSetChanged();

                loadingDialog.dismisstDialoglog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loadingDialog.dismisstDialoglog();
                DialogCustom.showErrorMessage(AllTxnListActivity.this, databaseError.getMessage());

            }
        });

    }

}