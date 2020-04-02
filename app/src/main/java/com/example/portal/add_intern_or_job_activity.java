package com.example.portal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;



public class add_intern_or_job_activity extends AppCompatActivity {
    EditText opPosition;
    EditText opDescription;
    EditText opCpi;
    EditText opBranchesAllowed;
    String img;
    TextView dateView;

    //    LocalDate date;
    String date;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    FirebaseAuth mFirebaseAuth;
    Button registerButton;
    Button btnbrowse, btnupload;
    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference,database1;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog ;

    AppCompatRadioButton internButton, jobButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_intern_or_job_activity);
        mFirebaseAuth = FirebaseAuth.getInstance();

        opPosition = findViewById(R.id.position_edit_text);
        opDescription = findViewById(R.id.description_edit_text);
        opCpi = findViewById(R.id.cpi_edit_text);
        dateView = findViewById(R.id.dateView);
        opBranchesAllowed = findViewById(R.id.branch_edit_text);
        registerButton = findViewById(R.id.register_button);
        internButton = findViewById(R.id.intern_selector);
        jobButton = findViewById(R.id.job_selector);
        btnbrowse = (Button)findViewById(R.id.btnbrowse);
        btnupload= (Button)findViewById(R.id.btnupload);;

        progressDialog = new ProgressDialog(add_intern_or_job_activity.this);


        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(add_intern_or_job_activity.this,R.style.Theme_AppCompat_DayNight_Dialog,onDateSetListener,year,month,day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date_str;
                if(dayOfMonth < 10){
                    if(month < 9){
                        date_str = "0" + dayOfMonth + "/0" + (month+1) + "/" + year;
                    } else{
                        date_str = "0" + dayOfMonth + "/" + (month+1) + "/" + year;
                    }
                } else{
                    if(month < 9){
                        date_str = dayOfMonth + "/0" + (month+1) + "/" + year;
                    } else {
                        date_str = dayOfMonth + "/" + (month+1) + "/" + year;
                    }
                }
                dateView.setText(date_str);
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);
//                date = LocalDate.parse(date_str, formatter);
                date = date_str;
            }
        };

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(internButton.isChecked()){
                    final String companyName = getIntent().getStringExtra("Company_Name");
                    final String position = opPosition.getText().toString();
                    final String description = opDescription.getText().toString();
                    final String branch = opBranchesAllowed.getText().toString();
                    final String cpi = opCpi.getText().toString();
                    final String urli = img;


                    if(position.isEmpty() || description.isEmpty() || date.isEmpty() || branch.isEmpty() || cpi.isEmpty()){
                        Toast.makeText(add_intern_or_job_activity.this,"Fields are Empty!",Toast.LENGTH_SHORT).show();
                    }
//                    else if(Integer.valueOf(cpi)>10){
//                        Toast.makeText(add_intern_or_job_activity.this, "CPI can't be more than 10 !",Toast.LENGTH_SHORT).show();
//                    }
                    else {
                        intern intern = new intern(companyName, Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid(),position,description,cpi,date,branch,urli);

                        database1 = FirebaseDatabase.getInstance().getReference("Internships");


                                       database1= database1.child(Objects.requireNonNull(database1.push().getKey()));
                                                database1.child(Objects.requireNonNull(database1.push().getKey())).setValue(intern).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(add_intern_or_job_activity.this, "Intern Registration Successful !!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(add_intern_or_job_activity.this, "Registration Unsuccessful, Please try Again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }
                else if(jobButton.isChecked()){
                    final String companyName = getIntent().getStringExtra("Company_Name");
                    final String position = opPosition.getText().toString();
                    final String description = opDescription.getText().toString();
                    final String branch = opBranchesAllowed.getText().toString();
                    final String cpi = opCpi.getText().toString();
                    final String urli = img;

                    if(position.isEmpty() || description.isEmpty() || date.isEmpty() || branch.isEmpty() || cpi.isEmpty()){
                        Toast.makeText(add_intern_or_job_activity.this,"Fields are Empty!",Toast.LENGTH_SHORT).show();
                    }
//                    else if(Integer.valueOf(cpi)>10){
//                        Toast.makeText(add_intern_or_job_activity.this, "CPI can't be more than 10 !",Toast.LENGTH_SHORT).show();
//                    }
                    else {
                        job job = new job(companyName, Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid(),position,description,cpi,date,branch);
                        FirebaseDatabase.getInstance().getReference("Jobs")
                                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                .setValue(job).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(add_intern_or_job_activity.this, "Intern Registration Successful !!", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(add_intern_or_job_activity.this,"Registration Unsuccessful, Please try Again",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });


        internButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(internButton.isChecked()){
                    jobButton.setChecked(false);
                    databaseReference = FirebaseDatabase.getInstance().getReference("Internships");

                    DatabaseReference dr,dr1;
                    dr = databaseReference;
                    databaseReference = databaseReference.child(Objects.requireNonNull(databaseReference.push().getKey()));
                    dr1 = databaseReference;
                    databaseReference = databaseReference.child(Objects.requireNonNull(databaseReference.push().getKey()));
                    storageReference = FirebaseStorage.getInstance().getReference("Internships");


                    storageReference = storageReference.child(Objects.requireNonNull(dr.push().getKey()));
                    storageReference = storageReference.child(Objects.requireNonNull(dr1.push().getKey()));



                }
            }
        });

        jobButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(jobButton.isChecked()){
                    internButton.setChecked(false);
                    storageReference = FirebaseStorage.getInstance().getReference("Jobs");
                    databaseReference = FirebaseDatabase.getInstance().getReference("Jobs");
                }
            }
        });

        btnbrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);

            }
        });
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                UploadImage();

            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();
            Toast.makeText(add_intern_or_job_activity.this, "Testing !!", Toast.LENGTH_SHORT).show();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);

            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }


    public void UploadImage() {

        if (FilePathUri != null) {
            Toast.makeText(add_intern_or_job_activity.this, "Testing1 !!", Toast.LENGTH_SHORT).show();
            progressDialog.setTitle("Image is Uploading...");
            progressDialog.show();
            final StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            storageReference2.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {


                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                            storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    img = uri.toString();
                                }
                            });



                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        else {

            Toast.makeText(add_intern_or_job_activity.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }






    public void back_to_company_page(View view) {
        startActivity(new Intent(add_intern_or_job_activity.this, companyLandingPage.class));
        finish();
    }
}
