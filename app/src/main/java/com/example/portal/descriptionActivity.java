package com.example.portal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class descriptionActivity extends AppCompatActivity {
    TextView type, company_name, position, description, branches, cpi, date;
    ImageView company_icon;
    String typeOpp, compID, typeID;
    String c_name,c_email,s_name,s_email,offtype,offpos,studCPI,studBranch;
    String combranch;
    String cutcpi;
    String[] branchList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        type = findViewById(R.id.textView15);
        company_name = findViewById(R.id.textView6);
        position = findViewById(R.id.textView8);
        description = findViewById(R.id.textView10);
        branches = findViewById(R.id.textView12);
        cpi = findViewById(R.id.textView14);
        company_icon = findViewById(R.id.imageView4);
        date = findViewById(R.id.textView17);
        typeOpp = getIntent().getStringExtra("Type");
        compID = getIntent().getStringExtra("Company_ID");

        if(typeOpp.equals("1")){
            typeID = getIntent().getStringExtra("Intern_ID");
            type.setText("INTERNSHIP ");
            offtype="INTERNSHIP";

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("Internships").child(compID).child(typeID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    intern intern = Objects.requireNonNull(dataSnapshot.getValue(intern.class));
                    company_name.setText(intern.getCompany_name());
                    c_name=intern.getCompany_name().toString();

                    position.setText(intern.getPosition());
                    offpos=intern.getPosition().toString();
                    description.setText(intern.getDescription());
                    branches.setText(intern.getBranches_allowed());
                    combranch=intern.getBranches_allowed().toString();
                    cpi.setText(intern.getCpi_cutoff());
                    cutcpi=intern.getCpi_cutoff().toString();
                    date.setText(intern.getLast_day_to_apply());
                   branchList=combranch.split(",");
                    Glide.with(descriptionActivity.this).load(intern.getImageURL()).into(company_icon);

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        }
        else{
            typeID = getIntent().getStringExtra("Job_ID");
            type.setText("JOB ");
            offtype = "JOBS";
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("Jobs").child(compID).child(typeID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    job job = Objects.requireNonNull(dataSnapshot.getValue(job.class));
                    company_name.setText(job.getCompany_name());
                    c_name=job.getCompany_name().toString();
                    position.setText(job.getOff_position());
                    offpos=job.getOff_position().toString();
                    combranch= job.getBranches_allowed().toString();
                    cutcpi=job.getCpi_cutoff().toString();
                    description.setText(job.getDescription());
                    branches.setText(job.getBranches_allowed());
                    cpi.setText(job.getCpi_cutoff());
                    branchList=combranch.split(",");
                    date.setText(job.getLast_day_to_apply());
                    Glide.with(descriptionActivity.this).load(job.getImageURL()).into(company_icon);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        }
        DatabaseReference refs = FirebaseDatabase.getInstance().getReference();
        refs.child("Companies").child(compID).child("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c_email=dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        refs.child("Students").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                student student =Objects.requireNonNull(dataSnapshot.getValue(student.class));
                s_email=student.student_getEmail().toString();
                s_name=student.student_getName().toString();
                studBranch = student.student_getBranch();
                studCPI = student.student_getCpi();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public  void apply(View view){


        if(Double.parseDouble(String.valueOf(studCPI))>=Double.parseDouble(String.valueOf(cutcpi)) && Arrays.asList(branchList).contains(studBranch)){
            final application application = new application(s_name,s_email,offtype,c_name,c_email,offpos);
            DatabaseReference refer = FirebaseDatabase.getInstance().getReference();
            refer.child("Applicants").child(compID).child(typeID).child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(application).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        DatabaseReference ds = FirebaseDatabase.getInstance().getReference("Applications").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                        ds.child(typeID).setValue(application).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(descriptionActivity.this, "Congratulations, you have successfully applied for this offer", Toast.LENGTH_SHORT).show();
                                } else{
                                    Toast.makeText(descriptionActivity.this, "Error while adding into user's applications", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(descriptionActivity.this, "Sorry,something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(descriptionActivity.this, "Sorry, You are Ineligible to Apply", Toast.LENGTH_SHORT).show();
        }


    }
    public void back_to_interns(View view) {
        startActivity(new Intent(descriptionActivity.this, student_intern_activity.class));
        finish();
    }
}
