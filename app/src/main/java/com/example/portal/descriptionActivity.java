package com.example.portal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class descriptionActivity extends AppCompatActivity {
    TextView type, company_name, position, description, branches, cpi, date;
    ImageView company_icon;
    String typeOpp, compID, typeID;

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
            type.setText("INTERNSHIP @");

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("Internships").child(compID).child(typeID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    intern intern = Objects.requireNonNull(dataSnapshot.getValue(intern.class));
                    company_name.setText(intern.getCompany_name());
                    position.setText(intern.getPosition());
                    description.setText(intern.getDescription());
                    branches.setText(intern.getBranches_allowed());
                    cpi.setText(intern.getCpi_cutoff());
                    date.setText(intern.getLast_day_to_apply());
                    Glide.with(descriptionActivity.this).load(intern.getImageURL()).into(company_icon);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        }
        else{
            typeID = getIntent().getStringExtra("Job_ID");
            type.setText("JOB @");

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("Jobs").child(compID).child(typeID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    job job = Objects.requireNonNull(dataSnapshot.getValue(job.class));
                    company_name.setText(job.getCompany_name());
                    position.setText(job.getOff_position());
                    description.setText(job.getDescription());
                    branches.setText(job.getBranches_allowed());
                    cpi.setText(job.getCpi_cutoff());
                    date.setText(job.getLast_day_to_apply());
                    Glide.with(descriptionActivity.this).load(job.getImageURL()).into(company_icon);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        }

    }

    public void back_to_interns(View view) {
        startActivity(new Intent(descriptionActivity.this, student_intern_activity.class));
        finish();
    }
}
