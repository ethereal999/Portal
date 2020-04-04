package com.example.portal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    TextView sName,sEmail,sCPI,sBranch;
    String name,email,cpi,branch,pf,img;
    ImageView profile_photo;
    Button choosebtn, uploadbtn;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("Students");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sName = (TextView) findViewById(R.id.name);
        sEmail = (TextView) findViewById(R.id.email);
        sCPI = (TextView) findViewById(R.id.cpi);
        sBranch = (TextView) findViewById(R.id.branch);
        profile_photo = (ImageView) findViewById(R.id.profile_photo);
        choosebtn = (Button) findViewById(R.id.choose);
        uploadbtn = (Button) findViewById(R.id.upload);

        choosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
        mFirebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Students");
        ref.child(Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name = Objects.requireNonNull(dataSnapshot.getValue(student.class)).student_getName();
                sName.setText(name);
                email = Objects.requireNonNull(dataSnapshot.getValue(student.class)).student_getEmail();
                sEmail.setText(email);
                cpi = Objects.requireNonNull(dataSnapshot.getValue(student.class)).student_getCpi();
                sCPI.setText(cpi);
                branch = Objects.requireNonNull(dataSnapshot.getValue(student.class)).student_getBranch();
                sBranch.setText(branch);
                pf = Objects.requireNonNull(dataSnapshot.getValue(student.class)).getProfileImg();

                Picasso.get().load(pf).into(profile_photo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void back_to_main(View view) {
        Intent gotoMainPage = new Intent(this, studentLandingPage.class);
        startActivity(gotoMainPage);
        finish();
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
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profile_photo.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child(Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            Toast.makeText(ProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    img = uri.toString();
                                    databaseReference =  FirebaseDatabase.getInstance().getReference("Students");
                                    databaseReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("profileImg").setValue(img);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ProfileActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }
    public void change_password(View view) {
        startActivity(new Intent(ProfileActivity.this, change_password.class).putExtra("Type","1"));
        finish();
    }

    public void update_details(View view) {
        startActivity(new Intent(ProfileActivity.this, profile_update.class).putExtra("Type","1"));
        finish();
    }
}