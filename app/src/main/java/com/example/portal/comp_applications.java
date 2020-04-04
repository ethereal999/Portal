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
import java.util.Objects;

public class comp_applications extends AppCompatActivity {
    FirebaseAuth mFirebaseAuth;
    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<application> list;
    application_comadapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_applications);
        mFirebaseAuth = FirebaseAuth.getInstance();

        reference =  FirebaseDatabase.getInstance().getReference("Applicants").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
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
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Iterable<DataSnapshot> snap = dataSnapshot1.getChildren();
                        for (DataSnapshot dataSnapshot2 : snap) {
                            application p = dataSnapshot2.getValue(application.class);
                            list.add(p);
                        }
                        if (list.isEmpty()) {
                            Toast.makeText(comp_applications.this, "No Applications are available", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(comp_applications.this, companyLandingPage.class));
                        }
                        adapter = new application_comadapter(comp_applications.this, list);
                        recyclerView.setAdapter(adapter);


                    }
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(comp_applications.this, "No Applications are available", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(comp_applications.this, companyLandingPage.class));
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(comp_applications.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void back_to_student(View view) {
        startActivity(new Intent(comp_applications.this, companyLandingPage.class));
        finish();
    }
}