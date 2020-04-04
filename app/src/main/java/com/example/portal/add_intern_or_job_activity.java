package com.example.portal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
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
    EditText opPosition, opDescription, opCpi, opBranchesAllowed;
    TextView dateView, pdfSelect, pdfPath;
    Uri pdfUri;
    String date, url,internID,jobID;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    FirebaseAuth mFirebaseAuth;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    Button registerButton;
    AppCompatRadioButton internButton, jobButton;
    ProgressDialog progressDialog;

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
        pdfSelect = findViewById(R.id.pdfAddTextView);
        pdfPath = findViewById(R.id.pdfTextView);
        progressDialog = new ProgressDialog(add_intern_or_job_activity.this);

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(add_intern_or_job_activity.this, R.style.Theme_AppCompat_DayNight_Dialog, onDateSetListener, year, month, day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date_str;
                if (dayOfMonth < 10) {
                    if (month < 9) {
                        date_str = "0" + dayOfMonth + "/0" + (month + 1) + "/" + year;
                    } else {
                        date_str = "0" + dayOfMonth + "/" + (month + 1) + "/" + year;
                    }
                } else {
                    if (month < 9) {
                        date_str = dayOfMonth + "/0" + (month + 1) + "/" + year;
                    } else {
                        date_str = dayOfMonth + "/" + (month + 1) + "/" + year;
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
                final String companyName = getIntent().getStringExtra("Company_Name");
                final String companyPic = getIntent().getStringExtra("Profile_Pic");
                final String position = opPosition.getText().toString();
                final String description = opDescription.getText().toString();
                final String branch = opBranchesAllowed.getText().toString();
                final String cpi = opCpi.getText().toString();

                if (position.isEmpty() || description.isEmpty() || date.isEmpty() || branch.isEmpty() || cpi.isEmpty()) {
                    Toast.makeText(add_intern_or_job_activity.this, "Fields are Empty!", Toast.LENGTH_SHORT).show();
                }
//                    else if(Integer.valueOf(cpi)>10){
//                        Toast.makeText(add_intern_or_job_activity.this, "CPI can't be more than 10 !",Toast.LENGTH_SHORT).show();
//                    }
                else if (pdfUri == null) {
                    Toast.makeText(add_intern_or_job_activity.this, "Select a File", Toast.LENGTH_SHORT).show();
                } else if (url == null) {
                    Toast.makeText(add_intern_or_job_activity.this, "Wait, File is Uploading", Toast.LENGTH_SHORT).show();
                } else {
                    if (internButton.isChecked()) {
                        intern intern = new intern(companyName, Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid(), position, description, cpi, date, branch, companyPic, url,internID);
                        databaseReference.setValue(intern).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(add_intern_or_job_activity.this, "Intern Registration Successful !!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(add_intern_or_job_activity.this, companyLandingPage.class));
                                    finish();
                                } else {
                                    Toast.makeText(add_intern_or_job_activity.this, "Registration Unsuccessful, Please try Again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else if (jobButton.isChecked()) {
                        job job = new job(companyName, Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid(), position, description, cpi, date, branch, companyPic,url,jobID);
                        databaseReference.setValue(job).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(add_intern_or_job_activity.this, "Job Registration Successful !!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(add_intern_or_job_activity.this, companyLandingPage.class));
                                    finish();
                                } else {
                                    Toast.makeText(add_intern_or_job_activity.this, "Registration Unsuccessful, Please try Again", Toast.LENGTH_SHORT).show();
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
                if (internButton.isChecked()) {
                    jobButton.setChecked(false);
                    databaseReference = FirebaseDatabase.getInstance().getReference("Internships")
                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                    internID = Objects.requireNonNull(databaseReference.push().getKey());
                    storageReference = FirebaseStorage.getInstance().getReference("Internships")
                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                            .child(internID);
                    databaseReference = databaseReference.child(internID);
                }
            }
        });

        jobButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (jobButton.isChecked()) {
                    internButton.setChecked(false);
                    databaseReference = FirebaseDatabase.getInstance().getReference("Jobs")
                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                    jobID = Objects.requireNonNull(databaseReference.push().getKey());
                    storageReference = FirebaseStorage.getInstance().getReference("Jobs")
                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                            .child(internID);
                    databaseReference = databaseReference.child(jobID);
                }
            }
        });

        pdfSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(add_intern_or_job_activity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectPdf();
                } else {
                    ActivityCompat.requestPermissions(add_intern_or_job_activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 4);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 4 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectPdf();
        } else {
            Toast.makeText(add_intern_or_job_activity.this, "Please Provide Permission", Toast.LENGTH_SHORT).show();
        }
    }

    public void selectPdf() {
        startActivityForResult(new Intent().setType("application/pdf").setAction(Intent.ACTION_GET_CONTENT), 13);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 13 && data != null && resultCode == RESULT_OK) {
            pdfUri = data.getData();
            String filePath = "File Selected: " + Objects.requireNonNull(data.getData()).getPath();

            if (jobButton.isChecked() || internButton.isChecked()) {
                progressDialog.setTitle("File is Uploading...");
                progressDialog.show();

                storageReference.putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(add_intern_or_job_activity.this, "Upload Successful", Toast.LENGTH_SHORT).show();

                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                url = uri.toString();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(add_intern_or_job_activity.this, "Upload Unsuccessful, Please try Again", Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                });
            } else {
                Toast.makeText(add_intern_or_job_activity.this, "Please select a type, choose file again", Toast.LENGTH_SHORT).show();
            }
            pdfPath.setText(filePath);
        } else {
            Toast.makeText(add_intern_or_job_activity.this, "Please select a file", Toast.LENGTH_SHORT).show();
        }
    }

    public void back_to_company_page(View view) {
        startActivity(new Intent(add_intern_or_job_activity.this, companyLandingPage.class));
        finish();
    }
}
