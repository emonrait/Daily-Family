package com.raihan.dailyfamily.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.raihan.dailyfamily.BuildConfig;
import com.raihan.dailyfamily.R;
import com.raihan.dailyfamily.model.AutoLogout;
import com.raihan.dailyfamily.model.DialogCustom;
import com.raihan.dailyfamily.model.GlobalVariable;
import com.raihan.dailyfamily.model.LogoutService;
import com.raihan.dailyfamily.model.MenuAdapter;
import com.raihan.dailyfamily.model.MenuModel;
import com.google.android.material.navigation.NavigationView;
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

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends AutoLogout {
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, databaseReferencet;
    TextView textView;

    GridView menuGridView;
    ArrayList<MenuModel> list = new ArrayList<>();
    GlobalVariable globalVariable;

    private TextView balancet, nameTV, emaiTV, mobileTV, baltvm;
    Toolbar toolbar;
    CircleImageView circleImageView;
    String member_name;
    String mobile;
    String email;
    String balance;
    String url;
    String role;
    String version = "0";
    double total = 0;
    NavigationView navigationView;
    DrawerLayout drawer;
    private TextView prebalTV;
    private TextView prebalTVtitle;
    String date = "";
    String oldtotal = "";
    String monthlydep = "";
    LoadingDialog loadingDialog = new LoadingDialog(DashboardActivity.this);

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        textView = findViewById(R.id.text);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Dashboard");

        globalVariable = ((GlobalVariable) getApplicationContext());
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_logout);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        menuGridView = (GridView) findViewById(R.id.menuGridView);

        balancet = findViewById(R.id.balTV);
        nameTV = findViewById(R.id.user_name);
        emaiTV = findViewById(R.id.user_email);
        mobileTV = findViewById(R.id.user_mobile);
        circleImageView = findViewById(R.id.pic);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Members");
        databaseReferencet = firebaseDatabase.getReference("Transaction");
        prebalTV = findViewById(R.id.prebalTV);
        prebalTVtitle = findViewById(R.id.prebalTVtitle);
        globalVariable.setUseremail(user.getEmail());

        navigationView.setItemIconTintList(null);


        if (!DialogCustom.isOnline(DashboardActivity.this)) {
            DialogCustom.showInternetConnectionMessage(DashboardActivity.this);
        } else {
            martextshow();
            userInfo();
            userInfobal();
            updateUser();
            menuChange();
            totalBalance();
            //menuView();

        }


        LogoutService.logout(this);

        if (globalVariable.isMemberFlag()) {
            getAllUserEmail();
            globalVariable.setMemberFlag(false);
        }

    }

    private void menuChange() {
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.profile) {
                Intent a = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(a);
            } else if (item.getItemId() == R.id.logout) {
                DialogCustom.englishcustomLogout(DashboardActivity.this);

            } else if (item.getItemId() == R.id.totalbal) {
                Intent a = new Intent(getApplicationContext(), TotalBalanceActivity.class);
                startActivity(a);
            } else if (item.getItemId() == R.id.state) {
                Intent a = new Intent(getApplicationContext(), StatetmentActivity.class);
                startActivity(a);
            } else if (item.getItemId() == R.id.members) {
                Intent a = new Intent(getApplicationContext(), MemberActivity.class);
                startActivity(a);
            } else if (item.getItemId() == R.id.cond) {
                Intent a = new Intent(getApplicationContext(), ConditionActivity.class);
                startActivity(a);
            } else if (item.getItemId() == R.id.contact) {
                Intent a = new Intent(getApplicationContext(), ContactActivity.class);
                startActivity(a);
            } else if (item.getItemId() == R.id.notifications) {
                Intent a = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(a);
            } else if (item.getItemId() == R.id.develop) {
                Intent a = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(a);
            } else if (item.getItemId() == R.id.am) {
                Intent a = new Intent(getApplicationContext(), AddMeal.class);
                startActivity(a);
            } else if (item.getItemId() == R.id.bc) {
                Intent a = new Intent(getApplicationContext(), AddBazarCost.class);
                startActivity(a);
            } else if (item.getItemId() == R.id.mb) {
                Intent a = new Intent(getApplicationContext(), MealBoard.class);
                startActivity(a);
            } else if (item.getItemId() == R.id.passChange) {
                Intent a = new Intent(getApplicationContext(), PasswordChangeNew.class);
                startActivity(a);
            } else if (item.getItemId() == R.id.report) {
                Intent a = new Intent(getApplicationContext(), ReportActivity.class);
                startActivity(a);
            } else if (item.getItemId() == R.id.imgUpload) {
                Intent a = new Intent(getApplicationContext(), ImageUpload.class);
                startActivity(a);
            }

            DrawerLayout drawerLayout1 = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawerLayout1.closeDrawer(GravityCompat.START);

            return true;
        });
    }

    private void userInfo() {
        loadingDialog.startDialoglog();
        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    member_name = "" + ds.child("member_name").getValue();
                    mobile = "" + ds.child("mobile").getValue();
                    email = "" + ds.child("email").getValue();
                    balance = "" + ds.child("balance").getValue();
                    url = "" + ds.child("prolink").getValue();
                    role = "" + ds.child("role").getValue();
                    version = "" + ds.child("version").getValue();
                    Picasso.get().load(url).into(circleImageView);
                    nameTV.setText(member_name);
                    mobileTV.setText(mobile);
                    emaiTV.setText(email);
                    globalVariable.setRole(role);
                    menuView();
                    loadingDialog.dismisstDialoglog();
                    globalVariable.setUrl(url);
                    globalVariable.setVersion(version);
                    //DialogCustom.showErrorMessage(DashboardActivity.this, globalVariable.getVersion());
                    updateversionadd();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                DialogCustom.showErrorMessage(DashboardActivity.this, databaseError.getMessage());
            }
        });
    }

    private void userInfobal() {
        Query queryt = databaseReferencet.orderByChild("email").equalTo(user.getEmail());

        queryt.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object amount = map.get("amount");
                    double pvalue = Double.parseDouble(String.valueOf(amount));
                    total += pvalue;

                    DecimalFormat formater = new DecimalFormat("#,##,###.00");
                    String formatString = formater.format(total);
                    //balancet.setText(formatString + " \u09F3");
                    balancet.setText(ValidationUtil.commaSeparateAmount(String.valueOf(total)));
                    globalVariable.setMyAmount(String.valueOf(total));


                    try {
                        String currentTotal = oldtotal;
                        String monthlyDeposit = monthlydep;
                        String inputString = date;
                        SimpleDateFormat fromUser = new SimpleDateFormat("dd/MM/yyyy");
                        String pattern = "yyyy-MM-dd";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                        String date = simpleDateFormat.format(new Date());
                        String reformattedStr = simpleDateFormat.format(Objects.requireNonNull(fromUser.parse(inputString)));
                        Calendar actDate = new GregorianCalendar();
                        Calendar curDate = new GregorianCalendar();
                        Date thedate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(reformattedStr);
                        actDate.setTime(thedate);

                        Date thedate1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
                        curDate.setTime(thedate1);
                        //breakdown

                        int firstMonth = actDate.get(Calendar.MONTH);
                        int lastMonth = curDate.get(Calendar.MONTH);

                        int firstYear = actDate.get(Calendar.YEAR);
                        int lastYear = curDate.get(Calendar.YEAR);
                        int totalYear = lastYear - firstYear;

                        int totalMonths = (totalYear * 12) - (firstMonth) + lastMonth + 1;
                        double oldotal = Double.parseDouble(ValidationUtil.replacecomma(currentTotal));
                        double currentMonthlyDepo = Double.parseDouble(ValidationUtil.replacecomma(monthlyDeposit));
                        double totalamt = oldotal + (totalMonths * currentMonthlyDepo);
                        double dueamt = total - totalamt;

                        globalVariable.setRequireAmount(String.valueOf(totalamt));
                        //DialogCustom.showSuccessMessage(DashboardActivity.this,String.valueOf(totalamt));

                        if (dueamt > 0) {
                            prebalTV.setText(String.valueOf(ValidationUtil.commaSeparateAmount(String.valueOf(dueamt))));
                            prebalTVtitle.setText("Extra Balance: ");
                        } else if (dueamt == 0) {
                            prebalTV.setText("0" + ValidationUtil.commaSeparateAmount(String.valueOf(dueamt)));
                            prebalTVtitle.setText("Due Balance: ");
                        } else {
                            prebalTV.setText(String.valueOf(ValidationUtil.commaSeparateAmount(String.valueOf(dueamt))));
                            prebalTVtitle.setText("Due Balance: ");
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
                if (globalVariable.isAmountFlag()) {
                    if (Double.parseDouble(ValidationUtil.replacecomma(prebalTV.getText().toString())) <= -2000) {
                        DialogCustom.showErrorMessage(DashboardActivity.this, "Your Due Amount is " + prebalTV.getText().toString() + " Tk. Please Pay Your due amount Immediate.");
                        globalVariable.setAmountFlag(false);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                DialogCustom.showErrorMessage(DashboardActivity.this, databaseError.getMessage());
                // throw databaseError.toException(); // don't ignore errors
            }
        });

    }

    private void updateUser() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View nevhead = navigationView.getHeaderView(0);
        final TextView namet = nevhead.findViewById(R.id.name);
        final TextView emailt = nevhead.findViewById(R.id.email);
        final TextView mobilet = nevhead.findViewById(R.id.mobile);
        final TextView balancet = nevhead.findViewById(R.id.balnace);
        final ImageView imageView = nevhead.findViewById(R.id.profile_image);

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
                    String balance = "" + ds.child("balance").getValue();
                    String url = "" + ds.child("prolink").getValue();
                    //version = "" + ds.child("version").getValue();
                    Picasso.get().load(url).placeholder(R.drawable.logo).into(imageView);
                    namet.setText(member_name);
                    mobilet.setText(mobile);
                    emailt.setText(email);

                    //DecimalFormat formater = new DecimalFormat("#,##,###.00");
                    //String formatString = formater.format(Double.parseDouble(balance));
                    //balancet.setText(formatString + " \u09F3");
                    //balancet.setText(balance);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                DialogCustom.showErrorMessage(DashboardActivity.this, databaseError.getMessage());
            }
        });

        databaseReferencet = FirebaseDatabase.getInstance().getReference("Transaction");
        Query queryt = databaseReferencet.orderByChild("email").equalTo(user.getEmail());
        queryt.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double total = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object amount = map.get("amount");
                    double pvalue = Double.parseDouble(String.valueOf(amount));
                    total += pvalue;

                    DecimalFormat formater = new DecimalFormat("#,##,###.00");
                    String formatString = formater.format(total);
                    balancet.setText(ValidationUtil.commaSeparateAmount(String.valueOf(total)));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                DialogCustom.showErrorMessage(DashboardActivity.this, databaseError.getMessage());
                // throw databaseError.toException(); // don't ignore errors
            }
        });

    }

    @Override
    public void onBackPressed() {
        DialogCustom.englishcustomLogout(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (item.getItemId() == R.id.logout) {
            DialogCustom.englishcustomLogout(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void menuView() {
        list.clear();
        if (globalVariable.getRole().toLowerCase().equals("admin")) {
            list.add(new MenuModel("Profile", R.drawable.ic_user, "pf"));
            list.add(new MenuModel("Balance", R.drawable.taka, "bl"));
            list.add(new MenuModel("Meal Board", R.drawable.mealboard, "mb"));
            list.add(new MenuModel("Add Meal", R.drawable.addfood, "am"));
            list.add(new MenuModel("Add Bazaar Cost", R.drawable.addcart, "abc"));
            list.add(new MenuModel("Statement", R.drawable.ic_bank_statement, "st"));
            list.add(new MenuModel("Members", R.drawable.ic_community, "me"));
            list.add(new MenuModel("Condition", R.drawable.ic_terms_and_conditions, "co"));
            list.add(new MenuModel("Notification", R.drawable.ic_chat, "no"));
            list.add(new MenuModel("Contact", R.drawable.ic_contact_information, "con"));
            list.add(new MenuModel("Developer", R.drawable.ic_programmer, "dev"));
            list.add(new MenuModel("Payment", R.drawable.online_payment, "pa"));
            list.add(new MenuModel("Transaction List", R.drawable.search_utility, "tl"));
            list.add(new MenuModel("Registration", R.drawable.membership, "reg"));
            list.add(new MenuModel("Report", R.drawable.reportnew, "rep"));
            list.add(new MenuModel("Password Change", R.drawable.password, "pac"));
            list.add(new MenuModel("Upload Image", R.drawable.camera, "ui"));
            list.add(new MenuModel("Meal Approval", R.drawable.approvedmeal, "ma"));
            list.add(new MenuModel("Bazaar Approval", R.drawable.approvedbazar, "ba"));
            list.add(new MenuModel("Monthly Cost", R.drawable.monthlycost, "mc"));

        } else if (globalVariable.getRole().toLowerCase().equals("cash")) {
            list.add(new MenuModel("Profile", R.drawable.ic_user, "pf"));
            list.add(new MenuModel("Balance", R.drawable.taka, "bl"));
            list.add(new MenuModel("Meal Board", R.drawable.mealboard, "mb"));
            list.add(new MenuModel("Add Meal", R.drawable.addfood, "am"));
            list.add(new MenuModel("Add Bazaar Cost", R.drawable.addcart, "abc"));
            list.add(new MenuModel("Statement", R.drawable.ic_bank_statement, "st"));
            list.add(new MenuModel("Members", R.drawable.ic_community, "me"));
            list.add(new MenuModel("Condition", R.drawable.ic_terms_and_conditions, "co"));
            list.add(new MenuModel("Notification", R.drawable.ic_chat, "no"));
            list.add(new MenuModel("Contact", R.drawable.ic_contact_information, "con"));
            list.add(new MenuModel("Developer", R.drawable.ic_programmer, "dev"));
            list.add(new MenuModel("Payment", R.drawable.online_payment, "pa"));
            list.add(new MenuModel("Transaction List", R.drawable.search_utility, "tl"));
            list.add(new MenuModel("Report", R.drawable.reportnew, "rep"));
            list.add(new MenuModel("Password Change", R.drawable.password, "pac"));
            list.add(new MenuModel("Upload Image", R.drawable.camera, "ui"));
            list.add(new MenuModel("Meal Approval", R.drawable.approvedmeal, "ma"));
            list.add(new MenuModel("Bazaar Approval", R.drawable.approvedbazar, "ba"));
            list.add(new MenuModel("Monthly Cost", R.drawable.monthlycost, "mc"));


        } else {
            list.add(new MenuModel("Profile", R.drawable.ic_user, "pf"));
            list.add(new MenuModel("Balance", R.drawable.taka, "bl"));
            list.add(new MenuModel("Meal Board", R.drawable.mealboard, "mb"));
            list.add(new MenuModel("Add Meal", R.drawable.addfood, "am"));
            list.add(new MenuModel("Add Bazaar Cost", R.drawable.addcart, "abc"));
            list.add(new MenuModel("Statement", R.drawable.ic_bank_statement, "st"));
            list.add(new MenuModel("Members", R.drawable.ic_community, "me"));
            list.add(new MenuModel("Condition", R.drawable.ic_terms_and_conditions, "co"));
            list.add(new MenuModel("Notification", R.drawable.ic_chat, "no"));
            list.add(new MenuModel("Contact", R.drawable.ic_contact_information, "con"));
            list.add(new MenuModel("Developer", R.drawable.ic_programmer, "dev"));
            list.add(new MenuModel("Report", R.drawable.reportnew, "rep"));
            list.add(new MenuModel("Password Change", R.drawable.password, "pac"));
            list.add(new MenuModel("Upload Image", R.drawable.camera, "ui"));
            //list.add(new MenuModel("Registration", R.drawable.membership, "reg"));
        }

        MenuAdapter adapter = new MenuAdapter(this, list);
        menuGridView.setAdapter(adapter);

        menuGridView.setOnItemClickListener((adapterView, view, pstion, l) -> {
            TextView menu_soft_code = (TextView) view.findViewById(R.id.menu_soft_code);
            TextView menu_name = (TextView) view.findViewById(R.id.menu_name);

            if ("pf".equals(menu_soft_code.getText().toString())) {
                Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
                startActivity(intent);
            } else if ("bl".equals(menu_soft_code.getText().toString())) {
                Intent intent = new Intent(DashboardActivity.this, TotalBalanceActivity.class);
                startActivity(intent);
            } else if ("st".equals(menu_soft_code.getText().toString())) {
                Intent intent = new Intent(DashboardActivity.this, StatetmentActivity.class);
                startActivity(intent);
            } else if ("me".equals(menu_soft_code.getText().toString())) {
                Intent intent = new Intent(DashboardActivity.this, MemberActivity.class);
                startActivity(intent);
            } else if ("co".equals(menu_soft_code.getText().toString())) {
                Intent intent = new Intent(DashboardActivity.this, ConditionActivity.class);
                startActivity(intent);
            } else if ("con".equals(menu_soft_code.getText().toString())) {
                Intent intent = new Intent(DashboardActivity.this, ContactActivity.class);
                startActivity(intent);
            } else if ("no".equals(menu_soft_code.getText().toString())) {
                Intent intent = new Intent(DashboardActivity.this, NotificationActivity.class);
                startActivity(intent);
            } else if ("db".equals(menu_soft_code.getText().toString())) {
                Intent intent = new Intent(DashboardActivity.this, DebitActivity.class);
                startActivity(intent);
            } else if ("cr".equals(menu_soft_code.getText().toString())) {
                Intent intent = new Intent(DashboardActivity.this, CreditActivity.class);
                startActivity(intent);
            } else if ("dev".equals(menu_soft_code.getText().toString())) {
                Intent intent = new Intent(DashboardActivity.this, AboutActivity.class);
                startActivity(intent);
            } else if ("pa".equals(menu_soft_code.getText().toString())) {
                Intent intent = new Intent(DashboardActivity.this, TransactionActivity.class);
                startActivity(intent);
            } else if ("tl".equals(menu_soft_code.getText().toString())) {
                Intent intent = new Intent(DashboardActivity.this, AllTxnListActivity.class);
                startActivity(intent);
            } else if ("reg".equals(menu_soft_code.getText().toString())) {
                Intent intent = new Intent(DashboardActivity.this, RegisterActivity.class);
                startActivity(intent);
            } else if ("rep".equals(menu_soft_code.getText().toString())) {
                Intent intent = new Intent(DashboardActivity.this, ReportActivity.class);
                startActivity(intent);
            } else if ("pac".equals(menu_soft_code.getText().toString())) {
                Intent intent = new Intent(DashboardActivity.this, PasswordChangeNew.class);
                startActivity(intent);
            } else if ("ui".equals(menu_soft_code.getText().toString())) {
                Intent intent = new Intent(DashboardActivity.this, ImageUpload.class);
                startActivity(intent);
            } else if ("mb".equals(menu_soft_code.getText().toString())) {
                Intent intent = new Intent(DashboardActivity.this, MealBoard.class);
                startActivity(intent);
            } else if ("am".equals(menu_soft_code.getText().toString())) {
                Intent intent = new Intent(DashboardActivity.this, AddMeal.class);
                startActivity(intent);
            } else if ("abc".equals(menu_soft_code.getText().toString())) {
                Intent intent = new Intent(DashboardActivity.this, AddBazarCost.class);
                startActivity(intent);
            } else if ("ma".equals(menu_soft_code.getText().toString())) {
                Intent intent = new Intent(DashboardActivity.this, MealApproval.class);
                startActivity(intent);
            } else if ("ba".equals(menu_soft_code.getText().toString())) {
                Intent intent = new Intent(DashboardActivity.this, BazarApproval.class);
                startActivity(intent);
            } else if ("mc".equals(menu_soft_code.getText().toString())) {
                Intent intent = new Intent(DashboardActivity.this, AddMonthlyCost.class);
                startActivity(intent);
            }


        });
    }

    private void totalBalance() {
        if (!DialogCustom.isOnline(DashboardActivity.this)) {
            DialogCustom.showInternetConnectionMessage(DashboardActivity.this);
        } else {
            //loadingDialog.startDialoglog();
            databaseReferencet.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    loadingDialog.dismisstDialoglog();
                    double total = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String lastbal = "" + ds.child("amount").getValue();
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object amount = map.get("amount");
                        double pvalue = Double.parseDouble(String.valueOf(amount));
                        total += pvalue;
                        //DecimalFormat formater = new DecimalFormat("#,##,###.00");
                        //String formatString = formater.format(total);
                        //String formatString1 = formater.format(Double.parseDouble(lastbal));

                        // totv.setText(formatString + " \u09F3");
                        // cbtv.setText(formatString1 + " \u09F3");

                    }
                    globalVariable.setTotalAmount(String.valueOf(total));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    loadingDialog.dismisstDialoglog();
                    //throw databaseError.toException(); // don't ignore errors
                    DialogCustom.showErrorMessage(DashboardActivity.this, databaseError.getMessage());
                }
            });
        }
    }

    private void updateversionadd() {
        Query editQuery = databaseReference.orderByChild("email").equalTo(user.getEmail());

        if (BuildConfig.VERSION_CODE > Integer.parseInt(version)) {
            editQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot edtData : dataSnapshot.getChildren()) {
                        edtData.getRef().child("version").setValue(BuildConfig.VERSION_CODE);
                        edtData.getRef().child("deviceid").setValue(globalVariable.getDeviceid());
                        edtData.getRef().child("model").setValue(globalVariable.getModel());

                    }
                    //Toast.makeText(DashboardActivity.this, "Information Update Successfully....", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(DashboardActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            //Toast.makeText(DashboardActivity.this, "Your apps is up to date", Toast.LENGTH_LONG).show();

        }

    }

    private void getAllUserEmail() {
        ArrayList<String> emailList = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String email = ds.child("email").getValue(String.class);
                    emailList.add(email);
                    globalVariable.setEmailList(emailList);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                DialogCustom.showErrorMessage(DashboardActivity.this, databaseError.getMessage());

            }
        });
    }

    private void martextshow() {
        FirebaseDatabase.getInstance()
                .getReference().child("Message")
                .orderByChild("message2")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            date = "" + ds.child("date").getValue();
                            oldtotal = "" + ds.child("oldtotal").getValue();
                            monthlydep = "" + ds.child("monthlydep").getValue();

                            //DialogCustom.showErrorMessage((Activity) context, date + oldtotal + monthlydep);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        //DialogCustom.showErrorMessage((Activity) context, databaseError.getMessage());

                    }
                });
    }


}