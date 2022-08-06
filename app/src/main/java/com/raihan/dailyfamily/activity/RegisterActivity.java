package com.raihan.dailyfamily.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.raihan.dailyfamily.BuildConfig;
import com.raihan.dailyfamily.R;
import com.raihan.dailyfamily.model.AutoLogout;
import com.raihan.dailyfamily.model.DialogCustom;
import com.raihan.dailyfamily.model.GlobalVariable;
import com.raihan.dailyfamily.model.LogoutService;
import com.raihan.dailyfamily.model.Members;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.raihan.dailyfamily.model.SendMailMessage;
import com.raihan.dailyfamily.model.ValidationUtil;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AutoLogout {
    GlobalVariable globalVariable;
    String member_name;
    String father_name;
    String mobile;
    String email;
    String nid;
    String address;
    String password;
    String balance;
    String notify;
    String occupation;
    String nick;
    String version;
    String role;
    String message;
    String prolink;
    TextView textView;
    private Button register, addiamge;
    //private DatabaseReference mDatabase;
    private CircleImageView adPhoto;
    private TextView linksign;
    private EditText mDisplayName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mAddress;
    private EditText mFname;
    private EditText mMobile;
    private EditText mNid;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private ProgressDialog mRegProgress;
    // private ScrollView scrollView;
    final int REQUEST_GALLERY_CODE = 999;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    String generatedFilePath;

    FirebaseStorage storage;
    StorageReference storageReference;

    private ImageView ivLogout;
    private ImageView ivBack;
    private TextView tv_genereal_header_title;

    DatabaseReference reference;

    FirebaseDatabase fbd;


    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //getSupportActionBar().hide();
        globalVariable = ((GlobalVariable) getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        mDisplayName = findViewById(R.id.input_name);
        mFname = findViewById(R.id.input_fname);
        mMobile = findViewById(R.id.input_mobile);
        mAddress = findViewById(R.id.input_address);
        mPassword = findViewById(R.id.input_password);
        mNid = findViewById(R.id.input_nid);
        mEmail = findViewById(R.id.input_email);
        register = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressbar);
        linksign = findViewById(R.id.link_login);
        // scrollView = findViewById(R.id.scrollView);
        adPhoto = findViewById(R.id.adPhoto);
        addiamge = findViewById(R.id.addiamge);
        ivLogout = findViewById(R.id.ivLogout);
        ivBack = findViewById(R.id.ivBack);
        tv_genereal_header_title = findViewById(R.id.tv_genereal_header_title);
        fbd = FirebaseDatabase.getInstance();


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
                DialogCustom.doClearActivity(intent, RegisterActivity.this);
            }
        });

        //DialogCustom.showErrorMessage(RegisterActivity.this, getSaltString());

        tv_genereal_header_title.setText(R.string.new_member_registration);

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCustom.englishcustomLogout(RegisterActivity.this);
            }
        });

        addiamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();

            }
        });

        adPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
                //uploadImage();
                //ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_GALLERY_CODE);
            }
        });

        linksign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(i);
                // finish();
                DialogCustom.doClearActivity(i, RegisterActivity.this);
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fieldRequire();

            }
        });

        LogoutService.logout(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
        }
    }

    private String getMessage() {

        message = "Dear " + member_name + "," + "\n" + "\n" + "A warm welcome and lots of good wishes on becoming part of 'Plan of Establishing Together (POET)'. Congratulations and on behalf of all the members. We are all happy and excited about your inputs and contribution to our company & Unity. "
                + "\n" + "Your User Id is: " + email + ". "
                + "\n" + "Your Password is: " + password + ". "
                + "\n" + "Your Mobile No is: " + mobile + ". "
                + "\n" + "Thank you so much for staying with us. Any query? Please notify us by this email. Please preserve your login information for future."
                + "\n" + "\n" + "Thanks & Regards" + "\n" + "Plan of Establishing Together (POET)" + "\n" + "Email: poetol22@gmail.com";

        return message;
    }

    private void sendEmail() {
        String email1 = mEmail.getText().toString().trim();
        String subject = "Welcome Message to " + member_name;

        String message = "Dear " + member_name + "," + "\n" + "\n" + "A warm welcome and lots of good wishes on becoming part of 'Plan of Establishing Together (POET)'. Congratulations and on behalf of all the members. We are all happy and excited about your inputs and contribution to our company & Unity. "
                + "\n" + "Your User Id is: " + email + ". "
                + "\n" + "Your Password is: " + password + ". "
                + "\n" + "Your Mobile No is: " + mobile + ". "
                + "\n" + "Thank you so much for staying with us. Any query? Please notify us by this email. Please preserve your login information for future."
                + "\n" + "\n" + "Thanks & Regards" + "\n" + "Plan of Establishing Together (POET)" + "\n" + "Email: poetol22@gmail.com";

        SendMailMessage sm = new SendMailMessage(this, email1, subject, message);
        sm.execute();
        // Snackbar.make(scrollView, "Email Send to all Memmer Successfully", Snackbar.LENGTH_LONG).show();
        //DialogCustom.showErrorMessage(RegisterActivity.this, "Email Send to all Memmer Successfully");

    }

    public void addMember() {

        member_name = mDisplayName.getText().toString();
        email = mEmail.getText().toString();
        //password = mPassword.getText().toString();
        //password = UUID.randomUUID().toString();
        password = getSaltString();
        mobile = mMobile.getText().toString();
        father_name = mFname.getText().toString();
        nid = mNid.getText().toString();
        address = mAddress.getText().toString();
        balance = "0";
        notify = "Welcome To The Plan of Establishing Together (POET). Thanks stay connected with us.";
        occupation = "Private Service";
        version = String.valueOf(BuildConfig.VERSION_CODE);
        role = "user";
        //prolink = "https://1.bp.blogspot.com/-az1l3GyfOwc/XhIwp5RUTEI/AAAAAAAAAE4/TLdhPNfDQpkCvqYGIxnf7FlSzZvjA1p3QCLcBGAsYHQ/s320/logo.png";
        //prolink = generatedFilePath;
        nick = "";


        if (!TextUtils.isEmpty(member_name) ||
                !TextUtils.isEmpty(email) &&
                        !TextUtils.isEmpty(password) ||
                !TextUtils.isEmpty(father_name) ||
                !TextUtils.isEmpty(nid) ||
                !TextUtils.isEmpty(address) ||
                !TextUtils.isEmpty(mobile)) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                final LoadingDialog loadingDialog = new LoadingDialog(RegisterActivity.this);
                                loadingDialog.startDialoglog();
                                Members members = new Members(member_name, father_name, mobile, email, nid, address, password, balance, notify, occupation, generatedFilePath, nick, version, role);

                                FirebaseDatabase.getInstance().getReference("Members")
                                        .child(mAuth.getCurrentUser().getUid())
                                        .setValue(members)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    //DialogCustom.showErrorMessage(RegisterActivity.this, "Registration Success");

                                                    // Snackbar.make(scrollView, "Registration Success", Snackbar.LENGTH_LONG).show();

                                                    sendEmail();
                                                    // message=getMessage();
                                                    //sendMessgae(email);
                                                    loadingDialog.dismisstDialoglog();


                                                } else {
                                                    DialogCustom.showErrorMessage(RegisterActivity.this, "Registration Unsuccessful");
                                                    loadingDialog.dismisstDialoglog();

                                                    // Snackbar.make(scrollView, "Registration Unsuccessful", Snackbar.LENGTH_LONG).show();
                                                }
                                                loadingDialog.dismisstDialoglog();
                                            }
                                        });

                            } else {
                                DialogCustom.showErrorMessage(RegisterActivity.this, "Authentication failed");
                                // Snackbar.make(scrollView, "Authentication failed", Snackbar.LENGTH_LONG).show();
                                //Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                //Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

        } else {
            // Snackbar.make(scrollView, "Registration Unsuccessful", Snackbar.LENGTH_LONG).show();
            DialogCustom.showErrorMessage(RegisterActivity.this, "Registration Unsuccessful");


            //Toast.makeText(RegisterActivity.this, "Registration Unsuccessful, Please try again", Toast.LENGTH_LONG).show();
            // progressBar.setVisibility(View.GONE);
        }

    }

    private void sendMessgae(String email) {
        String subject = "Welcome Message to " + member_name;

        Intent intent = new Intent(Intent.ACTION_SEND, Uri.fromParts("mailto", email, null));
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, getMessage());
        //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, "Send email..."));
        }
        //startActivity(Intent.createChooser(intent, "Send Email..."));


    }

    public void fieldRequire() {
        if (generatedFilePath == null) {
            DialogCustom.showErrorMessage(this, "Please Upload a Image");
        } else if (TextUtils.isEmpty(mDisplayName.getText())) {
            DialogCustom.showErrorMessage(this, "Please Enter Your Name");
            mDisplayName.requestFocus();
            mDisplayName.setError("Name is Required!");
        } else if (TextUtils.isEmpty(mAddress.getText())) {
            DialogCustom.showErrorMessage(this, "Please Enter Your Address");
            mAddress.requestFocus();
            mAddress.setError("Address is Required!");
        } else if (TextUtils.isEmpty(mEmail.getText())) {
            DialogCustom.showErrorMessage(this, "Please Enter Your Email");
            mEmail.requestFocus();
            mEmail.setError("Email is Required!");
        } else if (ValidationUtil.getEmailValidate(mEmail.getText().toString()).equals("1")) {
            DialogCustom.showErrorMessage(this, "Please Enter Valid Email (example@mail.com).");
            mEmail.requestFocus();
            mEmail.setError("Email is not Valid Format!");
        } else if (TextUtils.isEmpty(mFname.getText())) {
            DialogCustom.showErrorMessage(this, "Please Enter Your Father Name");
            mFname.requestFocus();
            mFname.setError("Father Name is Required!");
        } else if (TextUtils.isEmpty(mMobile.getText())) {
            DialogCustom.showErrorMessage(this, "Please Enter Your Mobile Number");
            mMobile.requestFocus();
            mMobile.setError("Mobile no is Required!");
        } else if (TextUtils.isEmpty(mNid.getText())) {
            DialogCustom.showErrorMessage(this, "Please Enter Your NID Number");
            mNid.requestFocus();
            mNid.setError("NID no is Required!");

        } else if (mNid.getText().toString().length() < 10 || mNid.getText().toString().length() > 10 && mNid.getText().toString().length() < 13 || mNid.getText().toString().length() > 13 && mNid.getText().toString().length() < 17 || mNid.getText().toString().length() > 17) {
            DialogCustom.showErrorMessage(this, "Please Enter 10 or 13 or 17 digit NID Number.");
            mNid.requestFocus();
            mNid.setError("NID no is not Valid Format");
        } else if (!DialogCustom.isOnline(RegisterActivity.this)) {
            DialogCustom.showInternetConnectionMessage(RegisterActivity.this);
        } else {
            addMember();
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            CropImage.activity(filePath).setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //Uri resulturi = result.getUri();
                filePath = result.getUri();
                adPhoto.setImageURI(filePath);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                DialogCustom.showErrorMessage(this, error.toString());

            }

        }

    }


    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_LONG).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    generatedFilePath = String.valueOf(uri);
                                    DialogCustom.showSuccessMessage(RegisterActivity.this, "Image Upload Successful!");

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("error", e.toString());
                            progressDialog.dismiss();
                            DialogCustom.showErrorMessage(RegisterActivity.this, e.toString());

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890@!#$%&*";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    private String getId() {
        String id = "";
        final int[] value = {0};
        LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.startDialoglog();
        reference = fbd.getReference().child("Members");
        ArrayList<String> emailList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String id = ds.child("nick").getValue(String.class);

                    int newval = ValidationUtil.replacePoet(id);
                    if (newval > value[0]) {
                        value[0] = newval;

                    }

                    //id="POET"+"";


                    loadingDialog.dismisstDialoglog();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                DialogCustom.showErrorMessage(RegisterActivity.this, databaseError.getMessage());

            }
        });
        return id;
    }

}

