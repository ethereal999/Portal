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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;



public class studentLandingPage extends AppCompatActivity {

    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<intern> list;
    MyAdapter adapter;
    FirebaseAuth mFirebaseAuth;
    private ProgressDialog progressDialog;
    GoogleSignInClient mGoogleSignInClient;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_landing_page);

        recyclerView = findViewById(R.id.myRecycler);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        mFirebaseAuth = FirebaseAuth.getInstance();


        reference =  FirebaseDatabase.getInstance().getReference("Internships");


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        list = new ArrayList<>();


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        DatabaseReference temp;
                        temp= dataSnapshot1.getRef();
                        temp.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshotp) {
                                for (DataSnapshot dataSnapshot2 : dataSnapshotp.getChildren()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(studentLandingPage.this, "Good", Toast.LENGTH_SHORT).show();
                                    intern p = dataSnapshot2.getValue(intern.class);
                                    list.add(p);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Toast.makeText(studentLandingPage.this, "LALALALA", Toast.LENGTH_SHORT).show();
                        adapter = new MyAdapter(studentLandingPage.this, list);
                        recyclerView.setAdapter(adapter);

                    }








            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(studentLandingPage.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void signOut(View view) {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(studentLandingPage.this, "Signed Out Successfully !", Toast.LENGTH_SHORT).show();
            }
        });
        startActivity(new Intent(studentLandingPage.this,LogInActivity.class));
        finish();
    }
}