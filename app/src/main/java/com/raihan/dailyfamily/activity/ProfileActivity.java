package com.raihan.dailyfamily.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.raihan.dailyfamily.R;
import com.raihan.dailyfamily.model.AutoLogout;
import com.raihan.dailyfamily.model.DialogCustom;
import com.raihan.dailyfamily.model.GlobalVariable;
import com.raihan.dailyfamily.model.ListItem;
import com.raihan.dailyfamily.model.LogoutService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.raihan.dailyfamily.model.ValidationUtil;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AutoLogout {
    GlobalVariable globalVariable;
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
    double currentTotal = 0;
    private TextView faTV, nameTV, emaiTV, mobileTV, nidTv, addTV, ocpTV, baltv, idTv;
    //private ImageView barim;
    private Button edit, sendmoney;
    //private StorageReference mStorageRef;
    //private Context context;
    private CircleImageView imTV;
    Toolbar toolbar;

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textView = findViewById(R.id.text);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7341F1")));

        //barim = findViewById(R.id.barimage);
        idTv = findViewById(R.id.user_id);
        nidTv = findViewById(R.id.nid);
        faTV = findViewById(R.id.fname);
        nameTV = findViewById(R.id.user_name);
        emaiTV = findViewById(R.id.user_email);
        mobileTV = findViewById(R.id.user_mobile);
        addTV = findViewById(R.id.address);
        ocpTV = findViewById(R.id.ocp);
        imTV = findViewById(R.id.profile1);
        baltv = findViewById(R.id.bal);
        edit = findViewById(R.id.btn_edit);
        sendmoney = findViewById(R.id.btn_sendMoney);

        //initQrcode();

        // mStorageRef = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Members");
        globalVariable = ((GlobalVariable) getApplicationContext());


        //sendSms();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        imTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), QRcodeActivity.class);
                //startActivity(i);
                sendSms();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();

            }
        });
        sendmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMoneyDialog();

            }
        });

        databaseReferencet = FirebaseDatabase.getInstance().getReference("Transaction");
        Query queryt = databaseReferencet.orderByChild("email").equalTo(user.getEmail());
        queryt.addValueEventListener(new ValueEventListener() {
            double total = 0.00;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object amount = map.get("amount");
                        double pvalue = Double.parseDouble(String.valueOf(amount));
                        total += pvalue;

                        baltv.setText(ValidationUtil.commaSeparateAmount(String.valueOf(total)));
                        currentTotal = total;
                    }
                } else {
                    baltv.setText(ValidationUtil.commaSeparateAmount(String.valueOf(total)));
                    currentTotal = total;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException(); // don't ignore errors
            }
        });


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

                    Picasso.get().load(url).into(imTV);

                    nameTV.setText(member_name);
                    mobileTV.setText(mobile);
                    emaiTV.setText(email);
                    faTV.setText(father);
                    nidTv.setText(nid);
                    addTV.setText(address);
                    ocpTV.setText(occupation);
                    idTv.setText(id);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        LogoutService.logout(this);


    }

//    private void initQrcode() {
//        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
//        try {
//            BitMatrix bitMatrix = multiFormatWriter.encode(email + member_name + mobile + father + nid
//                            + address + occupation + balance + url + id
//                    , BarcodeFormat.QR_CODE, 200, 200);
//            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
//            Bitmap bitmap1 = barcodeEncoder.createBitmap(bitMatrix);
//            //barim.setImageBitmap(bitmap1);
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            DialogCustom.englishcustomLogout(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this).setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View reg_layout = inflater.inflate(R.layout.content, null);

        final EditText fname = reg_layout.findViewById(R.id.edit_fname);
        final EditText mobilet = reg_layout.findViewById(R.id.edit_mobile);
        final EditText nidt = reg_layout.findViewById(R.id.edit_nid);
        final EditText occupationt = reg_layout.findViewById(R.id.edit_ocu);
        final EditText addresst = reg_layout.findViewById(R.id.edit_address);
        final TextView emailt = reg_layout.findViewById(R.id.edit_email);
        final Button btn_cancel = reg_layout.findViewById(R.id.btn_cancel);
        final Button btn_update = reg_layout.findViewById(R.id.btn_update);
        dialog.setView(reg_layout);
        final android.app.AlertDialog alertDialog = dialog.create();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


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


                    mobilet.setText(mobile);
                    fname.setText(father);
                    nidt.setText(nid);
                    addresst.setText(address);
                    occupationt.setText(occupation);
                    emailt.setText(email);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mobile = mobilet.getText().toString();
                final String fanme = fname.getText().toString();
                final String nid = nidt.getText().toString();
                final String address = addresst.getText().toString();
                final String ocup = occupationt.getText().toString();


                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("Members");
                Query editQuery = databaseReference.orderByChild("email").equalTo(emailt.getText().toString());
                if (!TextUtils.isEmpty(ocup) &&
                        !TextUtils.isEmpty(fanme) &&
                        !TextUtils.isEmpty(nid) &&
                        !TextUtils.isEmpty(address) &&
                        !TextUtils.isEmpty(mobile)) {
                    editQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot edtData : dataSnapshot.getChildren()) {
                                edtData.getRef().child("mobile").setValue(mobile);
                                edtData.getRef().child("father_name").setValue(fanme);
                                edtData.getRef().child("address").setValue(address);
                                edtData.getRef().child("occupation").setValue(ocup);
                                edtData.getRef().child("nid").setValue(nid);
                            }
                            Toast.makeText(ProfileActivity.this, "Information Update Successfully....", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(ProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(ProfileActivity.this, "Information Field is Empty, Please Try Again", Toast.LENGTH_LONG).show();

                }
            }
        });
        alertDialog.show();
    }

    private void sendMoneyDialog() {
        final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this).setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View reg_layout = inflater.inflate(R.layout.send_money, null);

        final TextView myemail = reg_layout.findViewById(R.id.my_email);
        final TextView mybalance = reg_layout.findViewById(R.id.my_balance);
        final Spinner sendemail = reg_layout.findViewById(R.id.send_email);
        final TextView senddate = reg_layout.findViewById(R.id.send_date);
        final EditText sendamount = reg_layout.findViewById(R.id.send_amount);
        final Button btn_cancel = reg_layout.findViewById(R.id.btn_cancel);
        final Button btn_send = reg_layout.findViewById(R.id.btn_send);


        final DatabaseReference spinerdata = FirebaseDatabase.getInstance().getReference("Members");
        final ArrayList<String> spinerList = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, spinerList);
        sendemail.setAdapter(adapter);

        dialog.setView(reg_layout);
        final android.app.AlertDialog alertDialog = dialog.create();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(year, month, day);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = dateFormat.format(cal.getTime());
        senddate.setText(dateString);

        spinerdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    String emaillist = "" + item.child("email").getValue();
                    if (!emaillist.trim().equals(Objects.requireNonNull(user.getEmail()).trim())) {
                        spinerList.add(emaillist);

                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        myemail.setText(Objects.requireNonNull(user.getEmail()).trim());
        mybalance.setText(ValidationUtil.commaSeparateAmount(String.valueOf(currentTotal)));


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mymail = user.getEmail().trim();
                final String sendmail = sendemail.getSelectedItem().toString().trim();
                final String senmode1 = "Money Send by : ";
                final String senmode2 = "Money Send to : ";
                final String sendate = senddate.getText().toString().trim();
                final String sendtaka = sendamount.getText().toString().trim();
                Random rand = new Random();
                // Generate random integers in range 0 to 9999
                final int rand_int1 = rand.nextInt(100000000);

                databasepayment = FirebaseDatabase.getInstance().getReference("Transaction");
                if (Double.parseDouble(globalVariable.getRequireAmount()) >= currentTotal) {
                    DialogCustom.showErrorMessage(ProfileActivity.this, "Not Permit to send money. You have due balance. Pay due balance before.");
                } else if (sendmail.isEmpty()) {
                    DialogCustom.showErrorMessage(ProfileActivity.this, "Please Select Receiver Email Address");
                    sendamount.requestFocus();
                } else if (sendtaka.isEmpty()) {
                    DialogCustom.showErrorMessage(ProfileActivity.this, "Please Enter Amount");
                    sendamount.requestFocus();
                } else if (Double.parseDouble(sendtaka) >= currentTotal) {
                    DialogCustom.showErrorMessage(ProfileActivity.this, "Not Permit to send money. You send more balance than your current balance");
                } else {
                    String id = databasepayment.push().getKey();
                    ListItem listItem = new ListItem(id, senmode1 + mymail, sendate, "+" + sendtaka, sendmail, String.valueOf(rand_int1));
                    databasepayment.child(id).setValue(listItem);
                    Toast.makeText(ProfileActivity.this, "Send Money is Success", Toast.LENGTH_LONG).show();

                    //sendTran();

                    databasepayment = FirebaseDatabase.getInstance().getReference("Transaction");
                    String id1 = databasepayment.push().getKey();
                    ListItem listItem1 = new ListItem(id1, senmode2 + sendmail, sendate, "-" + sendtaka, mymail, String.valueOf(rand_int1));
                    databasepayment.child(id1).setValue(listItem1);
                    Toast.makeText(ProfileActivity.this, "2nd Send Money is Success", Toast.LENGTH_LONG).show();

                }


              /*  if (!TextUtils.isEmpty(sendmail) && !TextUtils.isEmpty(sendate) && !TextUtils.isEmpty(senmode1) && !TextUtils.isEmpty(sendtaka)) {
                    String id = databasepayment.push().getKey();
                    ListItem listItem = new ListItem(id, senmode1 + mymail, sendate, "+" + sendtaka, sendmail, String.valueOf(rand_int1));
                    databasepayment.child(id).setValue(listItem);
                    Toast.makeText(ProfileActivity.this, "Send Money is Success", Toast.LENGTH_LONG).show();

                    //sendTran();

                    databasepayment = FirebaseDatabase.getInstance().getReference("Transaction");
                    String id1 = databasepayment.push().getKey();
                    ListItem listItem1 = new ListItem(id1, senmode2 + sendmail, sendate, "-" + sendtaka, mymail, String.valueOf(rand_int1));
                    databasepayment.child(id1).setValue(listItem1);
                    Toast.makeText(ProfileActivity.this, "2nd Send Money is Success", Toast.LENGTH_LONG).show();

                    //sendTran();

                } else {
                    Toast.makeText(ProfileActivity.this, "Send Money is not Success", Toast.LENGTH_LONG).show();
                }*/
            }
        });

        alertDialog.show();
    }

    public void sendSms() {
        try {
            // Construct data
            String apiKey = "apikey=" + "NTI1NDMwMzc2YTU2NTg0OTcwNmM3MjYxNmE0MTQ1Nzc=";
            String message = "&message=" + "This is your message";
            String sender = "&sender=" + "Emon Raihan";
            String numbers = "&numbers=" + "4412163547758";

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.txtlocal.com/send/?").openConnection();
            String data = apiKey + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                //stringBuffer.append(line);
                DialogCustom.showErrorMessage(this, line);
            }
            rd.close();

            //return stringBuffer.toString();
        } catch (Exception e) {
            System.out.println("Error SMS " + e);
            DialogCustom.showErrorMessage(this, "Error SMS " + e);
            Log.e("ErrorSMS", e.toString());
            //return "Error " + e;
        }
    }


}
