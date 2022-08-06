package com.raihan.dailyfamily.activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.raihan.dailyfamily.R;
import com.raihan.dailyfamily.model.AutoLogout;
import com.raihan.dailyfamily.model.DialogCustom;
import com.raihan.dailyfamily.model.ValidationUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PasswordChangeNew extends AutoLogout {

    DatabaseReference databaseReferencet;
    RecyclerView recylerReport;
    FirebaseDatabase fbd;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String check;
    private ImageView ivLogout;
    private ImageView ivBack;
    private TextView tv_genereal_header_title;
    private EditText input_oldpass;
    private EditText input_newpass;
    private EditText input_confirmpass;
    private Button btn_ok;
    LoadingDialog loadingDialog = new LoadingDialog(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change_new);

        ivLogout = findViewById(R.id.ivLogout);
        ivBack = findViewById(R.id.ivBack);
        tv_genereal_header_title = findViewById(R.id.tv_genereal_header_title);
        recylerReport = findViewById(R.id.recylerReport);
        input_oldpass = findViewById(R.id.input_oldpass);
        input_newpass = findViewById(R.id.input_newpass);
        input_confirmpass = findViewById(R.id.input_confirmpass);
        btn_ok = findViewById(R.id.btn_ok);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        check = user.getEmail();
        fbd = FirebaseDatabase.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Members");
        databaseReferencet = firebaseDatabase.getReference("Transaction");


        ivBack.setOnClickListener(v -> {
            Intent intent = new Intent(PasswordChangeNew.this, DashboardActivity.class);
            DialogCustom.doClearActivity(intent, PasswordChangeNew.this);
        });

        tv_genereal_header_title.setText(R.string.passwordchange);

        ivLogout.setOnClickListener(v -> DialogCustom.englishcustomLogout(PasswordChangeNew.this));

        btn_ok.setOnClickListener(view -> {
            if (input_oldpass.getText().toString().isEmpty()) {
                input_oldpass.requestFocus();
                DialogCustom.showErrorMessage(PasswordChangeNew.this, "Please Enter Old Password");
            } else if (input_newpass.getText().toString().isEmpty()) {
                input_newpass.requestFocus();
                DialogCustom.showErrorMessage(PasswordChangeNew.this, "Please Enter New Password");
            } else if (ValidationUtil.passwordValidaton(input_newpass.getText().toString().trim()).getOut_code().equals("1")) {
                input_newpass.requestFocus();
                DialogCustom.showErrorMessage(PasswordChangeNew.this, ValidationUtil.passwordValidaton(input_newpass.getText().toString().trim()).getOut_message());
            } else if (input_confirmpass.getText().toString().isEmpty()) {
                input_confirmpass.requestFocus();
                DialogCustom.showErrorMessage(PasswordChangeNew.this, "Please Enter Confirm Password");
            } else if (!input_confirmpass.getText().toString().trim().equals(input_newpass.getText().toString().trim())) {
                input_confirmpass.requestFocus();
                DialogCustom.showErrorMessage(PasswordChangeNew.this, "New Password & Confirm Password not Match");
            } else if (input_oldpass.getText().toString().trim().equals(input_newpass.getText().toString().trim())) {
                input_confirmpass.requestFocus();
                DialogCustom.showErrorMessage(PasswordChangeNew.this, "Old Password & New Password are Same");
            } else {
                loadingDialog.startDialoglog();
                PasswordChangeNew();
            }
        });
    }

    private void PasswordChangeNew() {
        AuthCredential credential = EmailAuthProvider.getCredential(check, input_oldpass.getText().toString().trim());

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    user.updatePassword(input_newpass.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()) {
                                loadingDialog.dismisstDialoglog();
                                DialogCustom.showErrorMessage(PasswordChangeNew.this, "Something went wrong. Please try again later");
                            } else {
                                try {
                                    loadingDialog.dismisstDialoglog();
                                    updatedatabase();
                                    DialogCustom.showSuccessMessagePass(PasswordChangeNew.this, "Password Successfully Updated");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                        }
                    });
                } else {
                    loadingDialog.dismisstDialoglog();

                    DialogCustom.showErrorMessage(PasswordChangeNew.this, "Old Password is wrong. Please try again later");

                }
            }
        });
    }

    private void updatedatabase() {
        Query editQuery = databaseReference.orderByChild("email").equalTo(user.getEmail());

        if (!input_newpass.getText().toString().trim().isEmpty()) {
            editQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        for (DataSnapshot edtData : dataSnapshot.getChildren()) {
                            edtData.getRef().child("password").setValue(input_newpass.getText().toString().trim());

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //Toast.makeText(PasswordChangeNew.this, "Password Update Successfully....", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(PasswordChangeNew.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

    }

}

