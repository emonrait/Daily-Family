package com.raihan.dailyfamily.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.raihan.dailyfamily.R;
import com.raihan.dailyfamily.model.AutoLogout;
import com.raihan.dailyfamily.model.DialogCustom;
import com.raihan.dailyfamily.model.GlobalVariable;
import com.raihan.dailyfamily.model.LogoutService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class NotificationActivity extends AutoLogout {

    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, databaseReference2;
    private TextView notTV1, messtv;
    private ImageView baniv;

    private ImageView ivLogout;
    private ImageView ivBack;
    private TextView tv_genereal_header_title;
    GlobalVariable globalVariable;

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        //textView = findViewById(R.id.text);
        //toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("Notifications");
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7341F1")));
        //getSupportActionBar().setSubtitle("sairam");

        globalVariable = ((GlobalVariable) getApplicationContext());
        ivLogout = findViewById(R.id.ivLogout);
        ivBack = findViewById(R.id.ivBack);
        tv_genereal_header_title = findViewById(R.id.tv_genereal_header_title);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationActivity.this, DashboardActivity.class);
                DialogCustom.doClearActivity(intent, NotificationActivity.this);
            }
        });

        tv_genereal_header_title.setText("Notifications");

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCustom.englishcustomLogout(NotificationActivity.this);
            }
        });

        notTV1 = findViewById(R.id.notTV);
        messtv = findViewById(R.id.messageTV);
        baniv = findViewById(R.id.bannerIV);


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Members");

        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String member_name = "" + ds.child("member_name").getValue();
                    String mobile = "" + ds.child("mobile").getValue();
                    String email = "" + ds.child("email").getValue();
                    String father = "" + ds.child("father_name").getValue();
                    String nid = "" + ds.child("nid").getValue();
                    String address = "" + ds.child("address").getValue();
                    String occupation = "" + ds.child("occupation").getValue();
                    String notify = "" + ds.child("notify").getValue();

                    notTV1.setText(notify);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance()
                .getReference().child("Message")
                .orderByChild("text")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            String text = "" + ds.child("text").getValue();
                            String textid = "" + ds.child("textid").getValue();

                            messtv.setText(text);
                            Picasso.get().load(textid).into(baniv);

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        LogoutService.logout(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout: {
                DialogCustom.englishcustomLogout(this);
            }
        }
        return super.onOptionsItemSelected(item);
    }


}
