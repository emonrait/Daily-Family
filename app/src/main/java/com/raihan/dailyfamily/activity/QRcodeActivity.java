package com.raihan.dailyfamily.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.raihan.dailyfamily.R;
import com.raihan.dailyfamily.model.AutoLogout;
import com.raihan.dailyfamily.model.LogoutService;
import com.raihan.dailyfamily.model.NumberToWordsConverter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import java.text.DecimalFormat;

public class QRcodeActivity extends AutoLogout {

    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, databaseReferencet;
    DatabaseReference databasepayment;
    TextView textView;
    String member_name;
    String mobile;
    String email;
    String father;
    String nid;
    String address;
    String occupation;
    String balance;
    String url;
    String id;
    private TextView nameTV, amt;
    private ImageView propic, barim;
    private Button edit, sendmoney;
    private StorageReference mStorageRef;
    private Context context;
    Toolbar toolbar;

    @SuppressLint({"RestrictedApi", "WrongViewCast"})
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_rcode);
        textView = findViewById(R.id.text);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("QR Code");

        propic = findViewById(R.id.propiclo);
        barim = findViewById(R.id.barcod);
        nameTV = findViewById(R.id.nameuser);
        amt = findViewById(R.id.amt);


        mStorageRef = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Members");


        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    member_name = "" + ds.child("member_name").getValue();
                    mobile = "" + ds.child("mobile").getValue();
                    email = "" + ds.child("email").getValue();
                    father = "" + ds.child("father_name").getValue();
                    nid = "" + ds.child("nid").getValue();
                    address = "" + ds.child("address").getValue();
                    occupation = "" + ds.child("occupation").getValue();
                    balance = "" + ds.child("balance").getValue();
                    url = "" + ds.child("prolink").getValue();
                    id = "" + ds.child("nick").getValue();
                    getSupportActionBar().setSubtitle(member_name);

                    Picasso.get().load(url).into(propic);
                    // Picasso.get().load(url).into(barim);
                    nameTV.setText(member_name);
                    DecimalFormat formater = new DecimalFormat("#,##,###.00");
                    String formatString = formater.format(Double.parseDouble(balance));
                    //baltv.setText(formatString + " Tk");
                    //initQrcode();

                    String value = NumberToWordsConverter.convert(36000);
                    int num = (int) Math.round(Float.parseFloat(balance));

                    amt.setText(NumberToWordsConverter.convert(num) + " Taka");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        LogoutService.logout(this);

    }

       /* private void initQrcode() {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(email + member_name + mobile + father + nid
                            + address + occupation + balance + url + id
                    , BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap1 = barcodeEncoder.createBitmap(bitMatrix);
            barim.setImageBitmap(bitmap1);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }*/



}