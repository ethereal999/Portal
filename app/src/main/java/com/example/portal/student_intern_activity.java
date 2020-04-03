package com.example.portal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class student_intern_activity extends AppCompatActivity {
    FirebaseAuth mFirebaseAuth;
    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<intern> list;
    internAdapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_intern_activity);
        mFirebaseAuth = FirebaseAuth.getInstance();

        reference =  FirebaseDatabase.getInstance().getReference("Internships");
        recyclerView = findViewById(R.id.myRecycler);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                progressDialog.dismiss();
                list = new ArrayList<>();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    Iterable<DataSnapshot>  snap = dataSnapshot1.getChildren();
                    for(DataSnapshot dataSnapshot2: snap) {
                        intern p = dataSnapshot2.getValue(intern.class);
                        list.add(p);
                    }
                }
                if (list.isEmpty()) {
                    Toast.makeText(student_job_activity.this, "No jobs are available", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(student_job_activity.this, studentLandingPage.class));}
                    adapter = new internAdapter(student_intern_activity.this, list);
                    recyclerView.setAdapter(adapter);

                    adapter.setOnItemClickListener(new internAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            String compID = list.get(position).getCompany_id();
                            String internID = list.get(position).getInterID();

                            startActivity(new Intent(student_intern_activity.this, descriptionActivity.class)
                                    .putExtra("Type", "1").putExtra("Company_ID", compID).putExtra("Intern_ID", internID));
                        }
                    });
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(student_job_activity.this, "No jobs are available", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(student_job_activity.this, studentLandingPage.class));
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(student_intern_activity.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void back_to_student(View view) {
        startActivity(new Intent(student_intern_activity.this, studentLandingPage.class));
        finish();
    }
}