package com.raihan.dailyfamily.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.raihan.dailyfamily.R;
import com.raihan.dailyfamily.model.AutoLogout;
import com.raihan.dailyfamily.model.DialogCustom;
import com.raihan.dailyfamily.model.GlobalVariable;
import com.raihan.dailyfamily.model.LogoutService;
import com.raihan.dailyfamily.model.Members;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.raihan.dailyfamily.model.ValidationUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberActivity extends AutoLogout {
    GlobalVariable globalVariable;
    private ImageView ivLogout;
    private ImageView ivBack;
    private TextView tv_genereal_header_title;
    DatabaseReference reference;
    RecyclerView recyclerView;
    List<Members> listdata;
    MyAdpter adpter;
    FirebaseDatabase fbd;
    Members members;


    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        globalVariable = ((GlobalVariable) getApplicationContext());
        recyclerView = findViewById(R.id.recyler);
        ivLogout = findViewById(R.id.ivLogout);
        ivBack = findViewById(R.id.ivBack);
        tv_genereal_header_title = findViewById(R.id.tv_genereal_header_title);
        fbd = FirebaseDatabase.getInstance();

        listdata = new ArrayList();


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberActivity.this, DashboardActivity.class);
                DialogCustom.doClearActivity(intent, MemberActivity.this);
            }
        });

        tv_genereal_header_title.setText(R.string.member);

        ivLogout.setOnClickListener(v -> DialogCustom.englishcustomLogout(MemberActivity.this));


        LogoutService.logout(this);

        if (!checkPermission()) {
            requestPermission();
        }

        if (!DialogCustom.isOnline(MemberActivity.this)) {
            DialogCustom.showInternetConnectionMessage(MemberActivity.this);
        } else {
            getFbddata();
        }


    }


    void getFbddata() {
        LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.startDialoglog();
        reference = fbd.getReference().child("Members");
        ArrayList<String> emailList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    members = new Members();

                    String name = ds.child("member_name").getValue(String.class);
                    String email = ds.child("email").getValue(String.class);
                    String mobile = ds.child("mobile").getValue(String.class);
                    String profile = ds.child("prolink").getValue(String.class);
                    String nick = ds.child("nick").getValue(String.class);
                    //Log.d("TAG", name + " / "+email+" / "+mobile+" / "+profile);
                    Members members = new Members(name, mobile, email, profile, nick);
                    if (globalVariable.isMemberFlag()) {
                        emailList.add(email);
                        globalVariable.setEmailList(emailList);
                        globalVariable.setMemberFlag(false);
                    }
                    listdata.add(members);
                    loadingDialog.dismisstDialoglog();


                }
                Collections.sort(listdata, new Comparator<Members>() {
                    @Override
                    public int compare(Members m1, Members m2) {
                        return Integer.compare(ValidationUtil.replacePoet(m1.getNick()), ValidationUtil.replacePoet(m2.getNick()));
                    }

                });
                adpter = new MyAdpter(listdata);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(adpter);
                adpter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                DialogCustom.showErrorMessage(MemberActivity.this, databaseError.getMessage());

            }
        });
    }


    public class MyAdpter extends RecyclerView.Adapter<MyAdpter.BlogViewHolder> {
        List<Members> listArry;

        public MyAdpter(List<Members> list) {
            this.listArry = list;

        }

        @NonNull
        @Override
        public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
            return new BlogViewHolder(view);
        }

        @Override
        public void onBindViewHolder(BlogViewHolder holder, int position) {
            final Members data = listArry.get(position);

            holder.name.setText(data.getMember_name());
            holder.mobile.setText(data.getMobile());
            holder.email.setText(data.getEmail());
            holder.profiId.setText("ID: " + data.getNick());
            Picasso.get().load(data.getProlink()).placeholder(R.drawable.logo).into(holder.pic);
            holder.mobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!checkPermission()) {
                        requestPermission();
                    } else {
                        Intent callintetnt = new Intent(Intent.ACTION_CALL);
                        callintetnt.setData(Uri.parse("tel:" + data.getMobile().trim()));
                        startActivity(callintetnt);
                        //sendwts(data.getMobile().trim());
                    }


                }
            });

            holder.email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", data.getEmail().trim(), null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }
            });


        }

        @Override
        public int getItemCount() {
            return listArry.size();
        }

        public class BlogViewHolder extends RecyclerView.ViewHolder {
            TextView name, email, mobile, profiId;
            CircleImageView pic;

            public BlogViewHolder(@NonNull View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.profilename);
                email = (TextView) itemView.findViewById(R.id.profileemail);
                mobile = (TextView) itemView.findViewById(R.id.profilemobile);
                profiId = (TextView) itemView.findViewById(R.id.profiId);
                pic = (CircleImageView) itemView.findViewById(R.id.profilepic);

            }
        }


    }

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

    private boolean checkPermission() {
        boolean var10000;

        int result = ContextCompat.checkSelfPermission((Context) this, "android.permission.CALL_PHONE");
        var10000 = result == 0;

        return var10000;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(MemberActivity.this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE}, PackageManager.PERMISSION_GRANTED);

    }

    protected void sendwts(String mobile) {

        PackageManager pm = getPackageManager();
        try {
            String phoneNumberWithCountryCode = "+88"+mobile.trim();
            String message = "Hallo";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumberWithCountryCode + "&text=" + message)));


        } catch (Exception e) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }
    }

}
