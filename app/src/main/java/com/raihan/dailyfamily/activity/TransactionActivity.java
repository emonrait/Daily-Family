package com.raihan.dailyfamily.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.Volley;
import com.raihan.dailyfamily.R;
import com.raihan.dailyfamily.model.AutoLogout;
import com.raihan.dailyfamily.model.DialogCustom;
import com.raihan.dailyfamily.model.GlobalVariable;
import com.raihan.dailyfamily.model.ListItem;
import com.raihan.dailyfamily.model.NumberToWordsConverter;
import com.raihan.dailyfamily.model.SendMail;
import com.raihan.dailyfamily.model.SendMailAll;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.raihan.dailyfamily.model.ValidationUtil;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;


public class TransactionActivity extends AutoLogout {

    TextView oldbalTv, oldamountnewTv;
    Button btpayment, notice, adjust, picture, btnbalance, btnnew;
    EditText amet, datet;
    Spinner sp, paymentmode;
    CheckBox check, input_clear;
    ValueEventListener listener;
    ArrayList<String> spinerList;
    String member_name;
    String balancet;
    String email;
    String date;
    String txnid;
    String amount;
    String mobile;
    String nick;
    String useremail;
    String message;
    String updateBy;
    private Intent mShareIntent;
    Uri path;
    String id;
    String invoice;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceT;
    DatabaseReference databaseReferenceM;
    DatabaseReference databaseReferenceMe;
    ArrayAdapter<String> adapter;
    double newtotal = 0;
    Bitmap bmp, scaledbmp, bmpback, scaledbmpback;
    Bitmap seal, sealbmp;
    private static final int CREATEPDF = 1;
    int rand_int1;
    String dateFormat1;
    Uri caminhDoArquivo;
    private ImageView ivLogout;
    private ImageView ivBack;
    private TextView tv_genereal_header_title;

    private DatePickerDialog.OnDateSetListener dateSetListener;
    GlobalVariable globalVariable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        /*textView = findViewById(R.id.text);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Payment");*/
        globalVariable = ((GlobalVariable) getApplicationContext());

        // Firebase Initilize //

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceT = FirebaseDatabase.getInstance().getReference("Transaction");
        databaseReferenceM = firebaseDatabase.getReference("Members");
        databaseReferenceMe = firebaseDatabase.getReference("Message");


        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.poetlogo);
        seal = BitmapFactory.decodeResource(getResources(), R.drawable.stamp);
        //bmpback = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 190, 190, false);
        sealbmp = Bitmap.createScaledBitmap(seal, 190, 190, false);
        //scaledbmpback = Bitmap.createScaledBitmap(bmpback, 1200, 245, false);

        spinerList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(TransactionActivity.this, android.R.layout.simple_spinner_dropdown_item, spinerList);

        // Generate random integers in range 0 to 999
        Random rand = new Random();
        rand_int1 = rand.nextInt(1000);

        // Field initialize //
        btnnew = findViewById(R.id.next);
        btnbalance = findViewById(R.id.balance);
        btpayment = findViewById(R.id.submit);
        notice = findViewById(R.id.notice);
        adjust = findViewById(R.id.adjust);
        picture = findViewById(R.id.picture);
        sp = findViewById(R.id.spinner);
        paymentmode = findViewById(R.id.paymentmode);
        amet = findViewById(R.id.amountnew);
        datet = findViewById(R.id.date);
        oldbalTv = findViewById(R.id.oldamount);
        oldamountnewTv = findViewById(R.id.oldamountnew);
        check = findViewById(R.id.input_check);
        input_clear = findViewById(R.id.input_clear);
        ivLogout = findViewById(R.id.ivLogout);
        ivBack = findViewById(R.id.ivBack);
        tv_genereal_header_title = findViewById(R.id.tv_genereal_header_title);

        DateFormat df = new SimpleDateFormat("ddMMyyhhmmss");
        Date dateobj = new Date();
        dateFormat1 = df.format(dateobj);

        //String invoicename = String.valueOf("ptu" + rand_int1 + dateFormat1).toUpperCase();
        invoice = dateFormat1.toUpperCase();

        ActivityCompat.requestPermissions(TransactionActivity.this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);


        if (globalVariable.getRole().trim().toLowerCase().equals("cash")) {
            adjust.setVisibility(View.GONE);
            notice.setVisibility(View.GONE);
            picture.setVisibility(View.GONE);
            btnbalance.setVisibility(View.GONE);
            btnnew.setVisibility(View.GONE);
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionActivity.this, DashboardActivity.class);
                DialogCustom.doClearActivity(intent, TransactionActivity.this);
            }
        });

        tv_genereal_header_title.setText("Payment");

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCustom.englishcustomLogout(TransactionActivity.this);
            }
        });

        sp.setAdapter(adapter);
        //active();
        if (!DialogCustom.isOnline(TransactionActivity.this)) {
            DialogCustom.showInternetConnectionMessage(TransactionActivity.this);
        } else {
            retriveData();
        }

        amet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (amet.getText().toString().isEmpty()) {
                    oldamountnewTv.setText("Please Enter a Valid Amount");

                } else {
                    double oldbalnace = Double.parseDouble(ValidationUtil.replacecomma(oldbalTv.getText().toString()));
                    double inputbalnace = Double.parseDouble(amet.getText().toString());
                    double newbalance = oldbalnace + inputbalnace;
                    oldamountnewTv.setText(String.valueOf(newbalance));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnnew.setOnClickListener(view -> {
            if (!DialogCustom.isOnline(TransactionActivity.this)) {
                DialogCustom.showInternetConnectionMessage(TransactionActivity.this);
            } else {
                userTxnList();
            }


            // Toast.makeText(TransactionActivity.this, "Next Activity Cooming Soon.....", Toast.LENGTH_SHORT).show();
        });
        input_clear.setOnCheckedChangeListener((compoundButton, b) -> oldbalTv.setText(ValidationUtil.commaSeparateAmount("0")));

        // check.setOnCheckedChangeListener((compoundButton, b) -> active());

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                oldbalTv.setText(ValidationUtil.commaSeparateAmount("0"));
                if (!DialogCustom.isOnline(TransactionActivity.this)) {
                    DialogCustom.showInternetConnectionMessage(TransactionActivity.this);
                } else {
                    updateMember();
                    newbal();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        datet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog dialog = new DatePickerDialog(
                            TransactionActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            dateSetListener,
                            year, month, day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                }

                return false;
            }
        });


        dateSetListener = (datePicker, year, month, day) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String dateString = dateFormat.format(calendar.getTime());
            datet.setText(dateString);
        };

        btpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DialogCustom.isOnline(TransactionActivity.this)) {
                    DialogCustom.showInternetConnectionMessage(TransactionActivity.this);
                } else {
                    addPayment();
                }

            }
        });
        btnbalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DialogCustom.isOnline(TransactionActivity.this)) {
                    DialogCustom.showInternetConnectionMessage(TransactionActivity.this);
                } else {
                    showTransactionList();
                }

            }
        });

        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DialogCustom.isOnline(TransactionActivity.this)) {
                    DialogCustom.showInternetConnectionMessage(TransactionActivity.this);
                } else {
                    showNotice();
                    FirebaseMessaging.getInstance().getToken()
                            .addOnCompleteListener(task -> {
                                if (!task.isSuccessful()) {
                                    //Could not get FirebaseMessagingToken
                                    return;
                                }
                                if (null != task.getResult()) {
                                    //Got FirebaseMessagingToken
                                    String firebaseMessagingToken = Objects.requireNonNull(task.getResult());
                                    //Use firebaseMessagingToken further
                                    //Toast.makeText(TransactionActivity.this, "Notification " + firebaseMessagingToken, Toast.LENGTH_SHORT).show();

                                    //sendFCMPush(firebaseMessagingToken);
                                }
                            });

                }

            }
        });

        adjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //adJustment();
                //amet.setText("");
                updateID();

            }
        });

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DialogCustom.isOnline(TransactionActivity.this)) {
                    DialogCustom.showInternetConnectionMessage(TransactionActivity.this);
                } else {
                    showPicture();
                }

            }
        });
        // LogoutService.logout(this);

    }


    private void userTxnList() {

        final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this).setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View reg_layout = inflater.inflate(R.layout.user_txn_list, null);

        final ArrayList<String> splist = new ArrayList<>();
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(TransactionActivity.this, R.layout.spinner_item, splist);


        final Spinner txnlist = reg_layout.findViewById(R.id.txn_listall);
        final Button btn_cancel = reg_layout.findViewById(R.id.btn_cancel);
        final Button btn_delete = reg_layout.findViewById(R.id.btn_delete);
        final Button btn_update = reg_layout.findViewById(R.id.btn_update);
        RecyclerView recyclerView;
        List<ListItem> listdata;
        FirebaseDatabase fbd;


        dialog.setView(reg_layout);
        txnlist.setAdapter(adapter1);
        final android.app.AlertDialog alertDialog = dialog.create();

        btn_cancel.setOnClickListener(v -> alertDialog.dismiss());

        databaseReferenceM.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    String emaillist = "" + item.child("email").getValue();
                    splist.add(emaillist);
                }
                adapter1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                DialogCustom.showErrorMessage(TransactionActivity.this, databaseError.getMessage());
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        txnlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Query query = databaseReferenceT.orderByChild("email").equalTo(txnlist.getSelectedItem().toString().trim());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String modeall = "" + Objects.requireNonNull(ds.getValue()).toString().trim();

                            Toast.makeText(TransactionActivity.this, modeall, Toast.LENGTH_SHORT).show();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        DialogCustom.showErrorMessage(TransactionActivity.this, databaseError.getMessage());
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        alertDialog.show();

    }


    public void createPdf(String title) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, title);
        startActivityForResult(intent, CREATEPDF);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATEPDF) {
            if (data.getData() != null) {
                if (!(TextUtils.isEmpty(datet.getText())) && !(TextUtils.isEmpty(sp.getSelectedItem().toString())) && !(TextUtils.isEmpty(amet.getText())) && !(TextUtils.isEmpty(paymentmode.getSelectedItem().toString()))) {
                    caminhDoArquivo = data.getData();

                    Paint paint = new Paint();
                    Paint titlepaint = new Paint();
                    path = caminhDoArquivo;

                    Double total2 = Double.parseDouble(ValidationUtil.replacecomma(amet.getText().toString().trim()));
                    DecimalFormat formater = new DecimalFormat("#,##,###.00");
                    String formatString = formater.format(total2);
                    String formatString1 = formater.format(newtotal);


                    PdfDocument pdfDocument = new PdfDocument();
                    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
                    PdfDocument.Page page = pdfDocument.startPage(pageInfo);
                    Canvas canvas = page.getCanvas();
                    //canvas.drawBitmap(scaledbmpback, 0, 0, titlepaint);
                    titlepaint.setTextAlign(Paint.Align.CENTER);
                    titlepaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    titlepaint.setTextSize(45f);
                    canvas.drawText("Daily Family", 1200 / 2, 290, titlepaint);
                    titlepaint.setTextSize(25f);
                    canvas.drawText("(Maijdee Court, Sadar, Noakhali-3800)", 1200 / 2, 330, titlepaint);
                    canvas.drawText("(dailyfamilym22@gmail.com)", 1200 / 2, 360, titlepaint);
                    canvas.drawText("(ESTD:01/01/2022)", 1200 / 2, 395, titlepaint);
                    canvas.drawBitmap(scaledbmp, 500, 45, titlepaint);


                    //paint.setColor(Color.rgb(255, 255, 255));
                    //paint.setTextSize(35f);
                    //paint.setTextAlign(Paint.Align.RIGHT);
                    //canvas.drawText("Email: dailyfamilym22@gmail.com", 1180, 35, paint);
                    //canvas.drawText("01816028491", 1180, 90, paint);

                    titlepaint.setTextAlign(Paint.Align.CENTER);
                    titlepaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC));
                    titlepaint.setTextSize(40);
                    canvas.drawText("Monthly Payment Invoice", 1200 / 2, 470, titlepaint);


                    paint.setTextAlign(Paint.Align.LEFT);
                    paint.setTextSize(30f);
                    paint.setColor(Color.BLACK);

                    int num = (int) Math.round(Float.parseFloat(String.valueOf(newtotal)));

                    canvas.drawText("Member Name: " + member_name, 30, 540, paint);
                    canvas.drawText("Mobile No: " + mobile, 30, 580, paint);
                    canvas.drawText("Email: " + sp.getSelectedItem().toString(), 30, 630, paint);
                    canvas.drawText("Txnid: " + id, 30, 680, paint);
                    canvas.drawText("Amount in Words: " + String.valueOf(NumberToWordsConverter.convert(num) + " Tk"), 30, 750, paint);

                    //canvas.drawText("TxnId: " + id, 30, 740, paint);

                    paint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText("Invoice No: " + invoice, 1200 - 30, 580, paint);
                    canvas.drawText("Payment Date: " + datet.getText().toString(), 1200 - 30, 630, paint);
                    canvas.drawText("Total Balance: " + String.valueOf(formatString1 + " \u09F3"), 1200 - 30, 680, paint);
                    //canvas.drawText("Amount in Words: " + String.valueOf(NumberToWordsConverter.convert(num) + " Taka"), 1200 - 30, 740, paint);

                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(2);
                    canvas.drawRect(20, 780, 1200 - 20, 860, paint);
                    paint.setTextAlign(Paint.Align.LEFT);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawText("SL No", 40, 830, paint);
                    canvas.drawText("Description", 310, 830, paint);
                    canvas.drawText("Amount", 712, 830, paint);
                    canvas.drawText("Demand", 890, 830, paint);
                    canvas.drawText("Total", 1055, 830, paint);

                    canvas.drawLine(180, 790, 180, 840, paint);
                    canvas.drawLine(680, 790, 680, 840, paint);
                    canvas.drawLine(880, 790, 880, 840, paint);
                    canvas.drawLine(1030, 790, 1030, 840, paint);


                    canvas.drawText("1", 40, 950, paint);
                    canvas.drawText("Monthly Payment by " + paymentmode.getSelectedItem().toString(), 190, 950, paint);
                    canvas.drawText(formatString, 715, 950, paint);
                    canvas.drawText("0.00", 915, 950, paint);
                    paint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText(formatString, 1200 - 40, 950, paint);
                    paint.setTextAlign(Paint.Align.LEFT);

                    canvas.drawLine(680, 1200, 1200 - 20, 1200, paint);
                    canvas.drawText("Sub Total", 700, 1250, paint);
                    canvas.drawText(":", 900, 1250, paint);
                    paint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText(formatString + " \u09F3", 1200 - 40, 1250, paint);

                    paint.setTextAlign(Paint.Align.LEFT);
                    canvas.drawText("Tax (0%)", 700, 1300, paint);
                    canvas.drawText(":", 900, 1300, paint);
                    paint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText("0.00" + " \u09F3", 1200 - 40, 1300, paint);
                    paint.setTextAlign(Paint.Align.LEFT);
                    canvas.drawLine(680, 1350, 1200 - 20, 1350, paint);


                    paint.setColor(Color.BLACK);
                    paint.setTextSize(40f);
                    paint.setTextAlign(Paint.Align.LEFT);
                    canvas.drawText("Total", 700, 1415, paint);

                    int num1 = (int) Math.round(Float.parseFloat(amet.getText().toString()));

                    paint.setColor(Color.BLACK);
                    paint.setTextSize(35f);
                    paint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText(formatString + " \u09F3", 1200 - 40, 1415, paint);
                    canvas.drawText("Amount in Words: " + String.valueOf(NumberToWordsConverter.convert(num1)) + " Tk", 1200 - 40, 1485, paint);


                    //paint.setColor(Color.BLACK);
                    // paint.setTextSize(50f);
                    // paint.setTextAlign(Paint.Align.LEFT);
                    // canvas.drawText("Received & Checked by", 30, 1590, paint);


                    //paint.setColor(Color.BLACK);
                    // paint.setTextSize(40f);
                    // paint.setTextAlign(Paint.Align.LEFT);
                    // canvas.drawText("Daily Family", 30, 1640, paint);

                    // paint.setColor(Color.BLACK);
                    // paint.setTextSize(40f);
                    // paint.setTextAlign(Paint.Align.LEFT);
                    //canvas.drawText("dailyfamilym22@gmail.com", 30, 1690, paint);

                    canvas.drawBitmap(sealbmp, 350, 1400, titlepaint);

                    paint.setColor(Color.BLACK);
                    paint.setTextSize(30f);
                    paint.setTextAlign(Paint.Align.CENTER);
                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    canvas.drawText("*This is a Automatic Generate PDF Payment Invoice, Please Preserve this for future*", 1200 / 2, 1800, paint);
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    String formatted = format1.format(cal.getTime());

                    paint.setColor(Color.BLACK);
                    paint.setTextSize(25f);
                    paint.setTextAlign(Paint.Align.CENTER);
                    canvas.drawText("Developed by: Emon Raihan (01816028491) | Print Date: " + formatted, 1200 / 2, 1950, paint);

                    pdfDocument.finishPage(page);
                    gravarPdf(caminhDoArquivo, pdfDocument);
                    //shareDocument(caminhDoArquivo);
                    //sendEmail(caminhDoArquivo);

                }
            }
        }
    }


    private void shareDocument(Uri uri) {

        mShareIntent = new Intent();
        mShareIntent.setAction(Intent.ACTION_SEND);
        mShareIntent.setType("application/pdf");
        // Assuming it may go via eMail:
        mShareIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{sp.getSelectedItem().toString()});
        mShareIntent.putExtra(Intent.EXTRA_SUBJECT, "Monthly Payment Invoice " + invoice);
        mShareIntent.putExtra(Intent.EXTRA_TEXT, message);
        // Attach the PDf as a Uri, since Android can't take it as bytes yet.
        mShareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(mShareIntent);
    }

    private void gravarPdf(Uri uri, PdfDocument pdfDocument) {
        try {
            BufferedOutputStream stream = new BufferedOutputStream(Objects.requireNonNull(getContentResolver().openOutputStream(uri)));
            pdfDocument.writeTo(stream);
            pdfDocument.close();
            stream.flush();
            sendEmail("/" + invoice + ".pdf");
            sendwts(mobile, getMessage());
            amet.setText("");
            Toast.makeText(this, "PDF Create Success", Toast.LENGTH_LONG).show();


        } catch (IOException e) {
            Toast.makeText(this, "Error" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void active() {
        if (check.isChecked()) {
            check.setText("Active");
            Toast.makeText(TransactionActivity.this, "Button is Active Now", Toast.LENGTH_SHORT).show();
            picture.setVisibility(View.VISIBLE);
            adjust.setVisibility(View.VISIBLE);
            notice.setVisibility(View.VISIBLE);
            btpayment.setVisibility(View.VISIBLE);
            btnbalance.setVisibility(View.VISIBLE);
            btnnew.setVisibility(View.VISIBLE);

        } else {
            check.setText("Inactive");
            Toast.makeText(TransactionActivity.this, "Button is Inactive Now", Toast.LENGTH_SHORT).show();
            picture.setVisibility(View.INVISIBLE);
            adjust.setVisibility(View.INVISIBLE);
            notice.setVisibility(View.INVISIBLE);
            btpayment.setVisibility(View.INVISIBLE);
            btnbalance.setVisibility(View.INVISIBLE);
            btnnew.setVisibility(View.INVISIBLE);
        }
    }

    public void updateMember() {
        final Query queryt = databaseReferenceT.orderByChild("email").equalTo(sp.getSelectedItem().toString());
        queryt.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double total2 = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object amount = map.get("amount");
                    double pvalue = Double.parseDouble(String.valueOf(amount));
                    total2 += pvalue;

                    if (map.isEmpty()) {
                        oldbalTv.setText(String.valueOf("0"));

                    } else {
                        oldbalTv.setText(String.valueOf(total2));
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    private void newpaymentupdate() {
        Query queryt = databaseReferenceT.orderByChild("email").equalTo(sp.getSelectedItem().toString());
        queryt.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object amount = map.get("amount");
                    double pvalue = Double.parseDouble(String.valueOf(amount));
                    newtotal += pvalue;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //throw databaseError.toException();
                DialogCustom.showErrorMessage(TransactionActivity.this, databaseError.getMessage());
            }
        });

    }

    private String getMessage() {
        String newtotal1 = oldamountnewTv.getText().toString().trim();
        String total2 = amet.getText().toString().trim();
        String formatString = ValidationUtil.commaSeparateAmount(total2);
        String formatString1 = ValidationUtil.commaSeparateAmount(newtotal1);
        String message = "Dear " + member_name + "," + "\n" + "\n" + "Your 'Daily Family' Monthly Payment" + " "
                + formatString + " is Completed date of " + date + " by " + paymentmode.getSelectedItem().toString().trim() + "."
                + "\n" + "Your Invoice no is: " + invoice + ". "
                + "\n" + "Your TXNID is: " + id + ". "
                + "\n" + "Your Total Balance is " + formatString1 + " ."
                + "\n" + "Thank you so much for staying with us. Any query? Please notify us by this email also confirm your payment."
                + "\n" + "\n" + "Thanks & Regards" + "\n" + "Daily Family" + "\n" + "Email: dailyfamilym22@gmail.com";

        return message;
    }

    private void sendEmail(String pathn) {
        String email = sp.getSelectedItem().toString().trim();
        String subject = "Monthly Payment of - " + member_name + " and Invoice No: " + invoice;
        //String path = String.valueOf(caminhDoArquivo);


        double newtotal1 = Double.parseDouble(oldamountnewTv.getText().toString().trim());
        double total2 = Double.parseDouble(amet.getText().toString().trim());
        //DecimalFormat formater = new DecimalFormat("#,##,###.00");
        String formatString = ValidationUtil.commaSeparateAmount(String.valueOf(total2));
        String formatString1 = ValidationUtil.commaSeparateAmount(String.valueOf(newtotal1));


        String message = "Dear " + member_name + "," + "\n" + "\n" + "Your 'Daily Family' Monthly Payment" + " "
                + formatString + " is Completed date of " + date + " by " + paymentmode.getSelectedItem().toString().trim() + "."
                + "\n" + "Your Invoice no is: " + invoice + ". "
                + "\n" + "Your TXNID is: " + id + ". "
                + "\n" + "Your Total Balance is " + formatString1 + "."
                + "\n" + "Thank you so much for staying with us. Any query? Please notify us by this email also confirm your payment."
                + "\n" + "\n" + "Thanks & Regards" + "\n" + "Daily Family" + "\n" + "Email: dailyfamilym22@gmail.com";


        try {
            SendMail sm = new SendMail(this, email, subject, message, String.valueOf(pathn));
            sm.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void updatePayment() {
        email = sp.getSelectedItem().toString();
        final double bal = Double.parseDouble(amet.getText().toString());
        Query editQuery = databaseReferenceM.orderByChild("email").equalTo(email);
        editQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot edtData : dataSnapshot.getChildren()) {
                    edtData.getRef().child("balance").setValue(String.valueOf(newtotal));
                    edtData.getRef().child("notify").setValue("1. Your 'Daily Family' Monthly Payment" + " " + String.valueOf(bal)
                            + " Tk is Completed in " + " " + datet.getText().toString() + ". Your Invoice no is: " + invoice
                            + ". Your TXNID is: " + id + ". Your Total Balance is " + String.valueOf(newtotal) + " Tk."
                            + "Thank you so much for staying with us. Any query? Please notify us by this email (dailyfamilym22@gmail.com) also confirm your payment.");
                }
                Toast.makeText(TransactionActivity.this, "Balance Update", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TransactionActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void adJustment() {
        final String amounto = amet.getText().toString();
        email = sp.getSelectedItem().toString();
        Query editQuery = databaseReferenceM.orderByChild("email").equalTo(email);
        if (email.isEmpty() || amounto.isEmpty()) {
            Toast.makeText(TransactionActivity.this, "Please Enter the Amount", Toast.LENGTH_LONG).show();

        } else {
            editQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot edtData : dataSnapshot.getChildren()) {
                        edtData.getRef().child("balance").setValue(amounto);
                    }
                    Toast.makeText(TransactionActivity.this, "Balance Adjust Successful", Toast.LENGTH_LONG).show();
                    amet.setText("");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(TransactionActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    private void retriveData() {
        listener = databaseReferenceM.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    String emaillist = "" + item.child("email").getValue();
                    spinerList.add(emaillist);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void newbal() {
        Query query = databaseReferenceM.orderByChild("email").equalTo(sp.getSelectedItem().toString().trim());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    member_name = "" + ds.child("member_name").getValue();
                    balancet = "" + ds.child("balance").getValue();
                    useremail = "" + ds.child("email").getValue();
                    mobile = "" + ds.child("mobile").getValue();
                    nick = "" + ds.child("nick").getValue();
                    // oldbalance = "" + ds.child("mobile").getValue();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void addPayment() {
        email = sp.getSelectedItem().toString().trim();
        date = datet.getText().toString().trim();
        txnid = paymentmode.getSelectedItem().toString().trim();
        amount = amet.getText().toString().trim();
        updateBy = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail();
        if (paymentmode.getSelectedItemId() == 0) {
            DialogCustom.showErrorMessage(TransactionActivity.this, "Please Select Payment Mode");
        } else if (email.isEmpty()) {
            DialogCustom.showErrorMessage(TransactionActivity.this, "Please Select Email Id");
        } else if (txnid.isEmpty()) {
            DialogCustom.showErrorMessage(TransactionActivity.this, "Please Select Payment Mode");
        } else if (date.isEmpty()) {
            datet.requestFocus();
            DialogCustom.showErrorMessage(TransactionActivity.this, "Please Enter Date");
        } else if (amount.isEmpty()) {
            amet.requestFocus();
            DialogCustom.showErrorMessage(TransactionActivity.this, "Please Enter Amount");
        } else if (!DialogCustom.isOnline(TransactionActivity.this)) {
            DialogCustom.showInternetConnectionMessage(TransactionActivity.this);
        } else {
            id = databaseReferenceT.push().getKey();
            ListItem listItem = new ListItem(id, txnid, date, amount, email, invoice, updateBy);
            assert id != null;
            databaseReferenceT.child(id).setValue(listItem);
            //Toast.makeText(TransactionActivity.this, "Payment Success", Toast.LENGTH_LONG).show();
            newpaymentupdate();
            //updatePayment();
            updateMember();
            message = getMessage();
            createPdf(invoice);
            //sendEmail();

            DialogCustom.showSuccessMessagePayment(
                    TransactionActivity.this,
                    "Payment Success",
                    email,
                    ValidationUtil.commaSeparateAmount(amount),
                    ValidationUtil.commaSeparateAmount(oldamountnewTv.getText().toString().trim()),
                    date,
                    invoice,
                    id);

        }

    }

    private void showNotice() {
        final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this).setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View reg_layout = inflater.inflate(R.layout.notice, null);

        final EditText noticet = reg_layout.findViewById(R.id.edit_notice);
        final Button btn_cancel = reg_layout.findViewById(R.id.btn_cancel);
        final Button btn_update = reg_layout.findViewById(R.id.btn_update);

        dialog.setView(reg_layout);
        final android.app.AlertDialog alertDialog = dialog.create();

        btn_cancel.setOnClickListener(v -> alertDialog.dismiss());

        btn_update.setOnClickListener(v -> {
            final String notice5 = noticet.getText().toString();

            if (!TextUtils.isEmpty(notice5)) {
                databaseReferenceMe.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot edtData : dataSnapshot.getChildren()) {
                            edtData.getRef().child("text").setValue(notice5);
                        }
                        Toast.makeText(TransactionActivity.this, "Notice Update Successfully.....", Toast.LENGTH_LONG).show();

                        String email = "emonrait@gmail.com";
                        String subject = "Important Information & Notice";

                        String message = "Dear All," + "\n" + "\n" + notice5 + "\n" + "\n" + "Thanks & Regards" + "\n" + "Daily Family" + "\n" + "Email: dailyfamilym22@gmail.com";
                        ;
                        SendMailAll sma = new SendMailAll(TransactionActivity.this, email, subject, message, globalVariable.getEmailList());
                        sma.execute();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        DialogCustom.showErrorMessage(TransactionActivity.this, databaseError.getMessage());
                    }
                });

            } else {
                DialogCustom.showSuccessMessage(TransactionActivity.this, "Notice Field Empty, Please write the notice first");
            }

        });

        databaseReferenceMe.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String notice = "" + ds.child("text").getValue();
                    noticet.setText(notice);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                DialogCustom.showErrorMessage(TransactionActivity.this, databaseError.getMessage());
            }
        });

        alertDialog.show();
    }


    private void showTransactionList() {
        final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this).setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View reg_layout = inflater.inflate(R.layout.txn_list, null);

        final ArrayList<String> splist = new ArrayList<>();
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(TransactionActivity.this, R.layout.spinner_item, splist);


        final EditText myeamil = reg_layout.findViewById(R.id.my_email);
        final Spinner txnlist = reg_layout.findViewById(R.id.txn_listall);
        final EditText date = reg_layout.findViewById(R.id.send_date);
        final EditText taka = reg_layout.findViewById(R.id.pay_amount);
        final EditText mode = reg_layout.findViewById(R.id.pay_mode);
        final EditText invo = reg_layout.findViewById(R.id.invoice);
        final Button btn_cancel = reg_layout.findViewById(R.id.btn_cancel);
        final Button btn_delete = reg_layout.findViewById(R.id.btn_delete);
        final Button btn_update = reg_layout.findViewById(R.id.btn_update);
        dialog.setView(reg_layout);
        txnlist.setAdapter(adapter1);
        final android.app.AlertDialog alertDialog = dialog.create();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        databaseReferenceT.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    String emaillist = "" + item.child("id").getValue();
                    splist.add(emaillist);
                }
                adapter1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                DialogCustom.showErrorMessage(TransactionActivity.this, databaseError.getMessage());

            }
        });

        btn_update.setOnClickListener(v -> {
            final String amount1 = taka.getText().toString();
            final String myem = myeamil.getText().toString();
            final String mode1 = mode.getText().toString();
            final String date1 = date.getText().toString();
            final String invi1 = invo.getText().toString();
            Query query = databaseReferenceT.orderByChild("id").equalTo(txnlist.getSelectedItem().toString());

            if (!TextUtils.isEmpty(amount1)) {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot edtData : dataSnapshot.getChildren()) {
                            edtData.getRef().child("amount").setValue(amount1);
                            edtData.getRef().child("date").setValue(date1);
                            edtData.getRef().child("txnid").setValue(mode1);
                            edtData.getRef().child("email").setValue(myem);
                            edtData.getRef().child("invoiceno").setValue(invi1);
                        }
                        Toast.makeText(TransactionActivity.this, "Transaction Update Successfully.....", Toast.LENGTH_LONG).show();
                        DialogCustom.showSuccessMessage(TransactionActivity.this, "Transaction Update Successfully.....");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        DialogCustom.showErrorMessage(TransactionActivity.this, databaseError.getMessage());
                    }
                });

            } else {
                DialogCustom.showErrorMessage(TransactionActivity.this, "Picture Field Empty, Please write the notice first");

            }

        });

        btn_delete.setOnClickListener(v -> {
            Query applesQuery = databaseReferenceT.orderByChild("id").equalTo(txnlist.getSelectedItem().toString());
            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                        appleSnapshot.getRef().removeValue();
                        DialogCustom.showSuccessMessage(TransactionActivity.this, "TXNID Delete Successfully....");

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    DialogCustom.showErrorMessage(TransactionActivity.this, databaseError.getMessage());
                }
            });

        });

        txnlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Query query = databaseReferenceT.orderByChild("id").equalTo(txnlist.getSelectedItem().toString());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String emailall = "" + ds.child("email").getValue();
                            String dateall = "" + ds.child("date").getValue();
                            String takaall = "" + ds.child("amount").getValue();
                            String modeall = "" + ds.child("txnid").getValue();
                            String invoall = "" + ds.child("invoiceno").getValue();

                            myeamil.setText(emailall);
                            date.setText(dateall);
                            taka.setText(takaall);
                            mode.setText(modeall);
                            invo.setText(invoall);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        DialogCustom.showErrorMessage(TransactionActivity.this, databaseError.getMessage());
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        alertDialog.show();

    }

    private void updateID() {

        final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this).setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View reg_layout = inflater.inflate(R.layout.idupdate, null);

        final ArrayList<String> splist = new ArrayList<>();
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(TransactionActivity.this, R.layout.spinner_item, splist);


        final EditText name = reg_layout.findViewById(R.id.name);
        final EditText id = reg_layout.findViewById(R.id.id);
        final EditText version = reg_layout.findViewById(R.id.version);
        final EditText deviceid = reg_layout.findViewById(R.id.deviceid);
        final EditText model = reg_layout.findViewById(R.id.model);
        final EditText role = reg_layout.findViewById(R.id.role);
        final Spinner emaillist = reg_layout.findViewById(R.id.spinner);

        final Button btn_cancel = reg_layout.findViewById(R.id.btn_cancel);
        final Button btnUpdate = reg_layout.findViewById(R.id.btnUpdate);
        dialog.setView(reg_layout);
        emaillist.setAdapter(adapter1);
        final android.app.AlertDialog alertDialog = dialog.create();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        databaseReferenceM.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    String emaillist = "" + item.child("email").getValue();
                    splist.add(emaillist);
                }
                adapter1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                DialogCustom.showErrorMessage(TransactionActivity.this, databaseError.getMessage());

            }
        });

        btnUpdate.setOnClickListener(v -> {
            final String username = name.getText().toString();
            final String poetId = id.getText().toString();
            final String poetversion = version.getText().toString();
            final String poetrole = role.getText().toString();

            Query query = databaseReferenceM.orderByChild("email").equalTo(emaillist.getSelectedItem().toString().trim());

            if (!TextUtils.isEmpty(poetId)) {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot edtData : dataSnapshot.getChildren()) {
                            edtData.getRef().child("member_name").setValue(username);
                            edtData.getRef().child("nick").setValue(poetId);
                            edtData.getRef().child("version").setValue(poetversion);
                            edtData.getRef().child("role").setValue(poetrole);

                        }
                        Toast.makeText(TransactionActivity.this, "Information Update Successfully.....", Toast.LENGTH_LONG).show();
                        DialogCustom.showSuccessMessage(TransactionActivity.this, "Information Update Successfully.....");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        DialogCustom.showErrorMessage(TransactionActivity.this, databaseError.getMessage());
                    }
                });

            } else {
                DialogCustom.showErrorMessage(TransactionActivity.this, "Emty");

            }

        });


        emaillist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Query query = databaseReferenceM.orderByChild("email").equalTo(emaillist.getSelectedItem().toString());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String nick = "" + ds.child("nick").getValue();
                            String versionnew = "" + ds.child("version").getValue();
                            String member_name = "" + ds.child("member_name").getValue();
                            String modeln = "" + ds.child("model").getValue();
                            String deviceidn = "" + ds.child("deviceid").getValue();
                            String rolen = "" + ds.child("role").getValue();
                            id.setText(nick);
                            name.setText(member_name);
                            version.setText(versionnew);
                            model.setText(modeln);
                            deviceid.setText(deviceidn);
                            role.setText(rolen);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        DialogCustom.showErrorMessage(TransactionActivity.this, databaseError.getMessage());
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        alertDialog.show();

    }


    private void showPicture() {
        final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this).setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View reg_layout = inflater.inflate(R.layout.picture, null);


        final EditText picturet = reg_layout.findViewById(R.id.edit_pic);
        final Button btn_cancel = reg_layout.findViewById(R.id.btn_cancel);
        final Button btn_update = reg_layout.findViewById(R.id.btn_update);

        dialog.setView(reg_layout);
        final android.app.AlertDialog alertDialog = dialog.create();

        btn_cancel.setOnClickListener(v -> alertDialog.dismiss());
        btn_update.setOnClickListener(v -> {
            final String picture5 = picturet.getText().toString();

            if (!TextUtils.isEmpty(picture5)) {
                databaseReferenceMe.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot edtData : dataSnapshot.getChildren()) {
                            edtData.getRef().child("textid").setValue(picture5);
                        }
                        DialogCustom.showSuccessMessage(TransactionActivity.this, "Picture Update Successfully");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        DialogCustom.showErrorMessage(TransactionActivity.this, databaseError.getMessage());
                    }
                });

            } else {
                DialogCustom.showErrorMessage(TransactionActivity.this, "Picture Field Empty, Please write the notice first");

            }

        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceMe.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String picture = "" + ds.child("textid").getValue();
                    picturet.setText(picture);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                DialogCustom.showSuccessMessage(TransactionActivity.this, databaseError.getMessage());

            }
        });

        alertDialog.show();
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(TransactionActivity.this, DashboardActivity.class);
        DialogCustom.doClearActivity(intent, TransactionActivity.this);
    }

    private void sendFCMPush(String token) {

        String SERVER_KEY = "AIzaSyDMLIZ332_MWzBBdl64BKOdmMJy_xCTEQs";
        String msg = "this is test message";
        String title = "my title";
        String url = "https://positive-thinkers05012020.firebaseio.com";
        //String token = FCM_TOKEN;

        JSONObject obj = null;
        JSONObject objData = null;
        JSONObject dataobjData = null;

        try {
            obj = new JSONObject();
            objData = new JSONObject();

            objData.put("body", msg);
            objData.put("title", title);
            objData.put("sound", "default");
            objData.put("icon", "icon_name"); //   icon_name
            objData.put("tag", token);
            objData.put("priority", "high");

            dataobjData = new JSONObject();
            dataobjData.put("text", msg);
            dataobjData.put("title", title);

            obj.put("to", token);
            //obj.put("priority", "high");

            obj.put("notification", objData);
            obj.put("data", dataobjData);
            Log.e("return here>>", obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, obj,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.e("True", response + "");
//                        //DialogCustom.showErrorMessage(TransactionActivity.this,response.toString());
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("False", error + "");
//                        //DialogCustom.showErrorMessage(TransactionActivity.this,error.getMessage());
//                    }
//                }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Authorization", "key=" + SERVER_KEY);
//                params.put("Content-Type", "application/json");
//                return params;
//            }
//        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        // jsObjRequest.setRetryPolicy(policy);
        // requestQueue.add(jsObjRequest);
    }

    protected void sendwts(String mobile, String message) {

        PackageManager pm = getPackageManager();
        try {
            String phoneNumberWithCountryCode = "+88" + mobile.trim();
            //String message = "Hallo";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumberWithCountryCode + "&text=" + message)));


        } catch (Exception e) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }
    }


}
