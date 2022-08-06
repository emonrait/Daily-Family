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

import com.raihan.dailyfamily.R;
import com.raihan.dailyfamily.model.AutoLogout;
import com.raihan.dailyfamily.model.DialogCustom;
import com.raihan.dailyfamily.model.ListItem;
import com.raihan.dailyfamily.model.LogoutService;
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
    RecyclerView recyclerView;
    // ArrayList<ListItem> listdata;
    MyAdpterNew adpter;
    StatementListAdapter statementListAdapter;
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
        recyclerView = findViewById(R.id.recyler);
        input_invoice = findViewById(R.id.input_invoice);

      /*  setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Statement");
        //getSupportActionBar().setSubtitle("Your total Amount is: ");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7341F1")));*/

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

        statementListAdapter = new StatementListAdapter(this, listdata, new StatementListAdapter.OnItemClickListener() {
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

        LogoutService.logout(this);


    }

    private void getToalbal() {
        Query queryt = reference.orderByChild("email").equalTo(user.getEmail());
        queryt.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double total = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object amount = map.get("amount");
                    double pvalue = Double.parseDouble(String.valueOf(amount));
                    total += pvalue;
                    DecimalFormat formater = new DecimalFormat("#,##,###.00");
                    String formatString = formater.format(total);
                    //  getSupportActionBar().setSubtitle("Total: "+formatString+" \u09F3");
                    //baltv.setText(formatString + " \u09F3");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException(); // don't ignore errors
            }
        });
    }

    void getFbddata() {
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
                    ListItem listitem = new ListItem(txn, date, amo, invoice,email);
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
                DialogCustom.showErrorMessage(StatetmentActivity.this,databaseError.getMessage());
            }
        });
    }


   /* public class MyAdpterNew extends RecyclerView.Adapter<MyAdpterNew.BlogViewHolder> {
        List<ListItem> listArry;

        public MyAdpterNew(List<ListItem> list) {
            this.listArry = list;

        }

        @NonNull
        @Override
        public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_statement_listview, parent, false);
            return new BlogViewHolder(view);
        }

        @Override
        public void onBindViewHolder(BlogViewHolder holder, int position) {
            ListItem data = listArry.get(position);

            holder.tv_txnid.setText(data.getId());
            holder.tv_invoiceno.setText(data.getInvoiceno());
            holder.tv_date.setText(data.getDate());
            holder.tv_amount.setText(data.getAmount());
        }

        @Override
        public int getItemCount() {
            return listArry.size();
        }

        public class BlogViewHolder extends RecyclerView.ViewHolder {
            TextView tv_txnid, tv_invoiceno, tv_date, tv_amount;

            public BlogViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_txnid = (TextView) itemView.findViewById(R.id.tv_txnid);
                tv_invoiceno = (TextView) itemView.findViewById(R.id.tv_invoiceno);
                tv_date = (TextView) itemView.findViewById(R.id.tv_date);
                tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);

            }
        }
    }*/


}
