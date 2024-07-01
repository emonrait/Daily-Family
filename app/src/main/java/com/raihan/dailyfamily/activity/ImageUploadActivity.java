package com.raihan.dailyfamily.activity;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.raihan.dailyfamily.R;
import com.raihan.dailyfamily.model.AutoLogout;
import com.raihan.dailyfamily.model.DialogCustom;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.raihan.dailyfamily.model.GlobalVariable;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.UUID;

public class ImageUploadActivity extends AutoLogout {
    GlobalVariable globalVariable;
    private ImageView ivLogout;
    private ImageView userPhoto;
    private ImageView img_plusNew;
    private ImageView userPhotoNew;
    private ImageView img_plus;
    private ImageView ivBack;
    private TextView tv_genereal_header_title;
    private Button btnUpload;
    private ConstraintLayout image_layout_new;
    private ConstraintLayout image_layout;
    LoadingDialog loadingDialog = new LoadingDialog(this);
    private final int PICK_IMAGE_REQUEST = 71;
    String generatedFilePath;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase fbd;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        globalVariable = ((GlobalVariable) getApplicationContext());

        image_layout = findViewById(R.id.image_layout);
        img_plusNew = findViewById(R.id.img_plusNew);
        image_layout_new = findViewById(R.id.image_layout_new);
        userPhoto = findViewById(R.id.userPhoto);
        userPhotoNew = findViewById(R.id.userPhotoNew);
        img_plus = findViewById(R.id.img_plus);
        ivLogout = findViewById(R.id.ivLogout);
        ivBack = findViewById(R.id.ivBack);
        tv_genereal_header_title = findViewById(R.id.tv_genereal_header_title);
        btnUpload = findViewById(R.id.btnUpload);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        fbd = FirebaseDatabase.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        //databaseReference = firebaseDatabase.getReference("Members");


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageUploadActivity.this, DashboardActivity.class);
                DialogCustom.doClearActivity(intent, ImageUploadActivity.this);
            }
        });

        tv_genereal_header_title.setText(R.string.iamge_upload);

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCustom.englishcustomLogout(ImageUploadActivity.this);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filePath == null) {
                    DialogCustom.showErrorMessage(ImageUploadActivity.this, "Please Select Image to Upload");
                } else {
                    uploadImage();

                }
            }
        });

        img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        img_plusNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        Picasso.get().load(globalVariable.getUrl()).placeholder(R.drawable.image_placeholder).into(userPhoto);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            CropImage.activity(filePath).setGuidelines(CropImageView.Guidelines.ON)
                    //.setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                image_layout_new.setVisibility(View.VISIBLE);
                image_layout.setVisibility(View.GONE);
                //Uri resulturi = result.getUri();
                filePath = result.getUri();
                userPhotoNew.setImageURI(filePath);


//                Bitmap original = BitmapFactory.decodeStream(get);
//                ByteArrayOutputStream out = new ByteArrayOutputStream();
//                original.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                DialogCustom.showErrorMessage(this, error.toString());

            }

        }

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
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
                                    try {
                                        updatedatabase();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    // Snackbar.make(scrollView, "Image Upload Successful!", Snackbar.LENGTH_LONG).show();
                                    //Toast.makeText(getApplicationContext(), generatedFilePath, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            DialogCustom.showErrorMessage(ImageUploadActivity.this, e.toString());

                            //   Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            //progressDialog.setCancelable(false);
                        }
                    });
        }
    }

    private void updatedatabase() {
        databaseReference = firebaseDatabase.getReference("Members");
        Query editQuery = databaseReference.orderByChild("email").equalTo(user.getEmail());

        try {
            if (!generatedFilePath.trim().isEmpty()) {
                editQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            for (DataSnapshot edtData : dataSnapshot.getChildren()) {
                                edtData.getRef().child("prolink").setValue(generatedFilePath.trim());

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //DialogCustom.showSuccessMessage(ImageUpload.this, "Image Upload Successful!");

                        //Toast.makeText(PasswordChangeNew.this, "Password Update Successfully....", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ImageUploadActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}