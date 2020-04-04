package com.example.portal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;



public class studentLandingPage extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    GoogleSignInClient mGoogleSignInClient;
    TextView studentName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_landing_page);

        mFirebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        studentName = findViewById(R.id.student_name_text_view);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();


                ref.child("Students").child(Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String[] cName = Objects.requireNonNull(dataSnapshot.getValue(company.class)).company_getName().split(" ");
                String s = "HI " + cName[0] + " !";
                studentName.setText(cName[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
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
        startActivity(new Intent(studentLandingPage.this, MainActivity.class));
        finish();
    }

    public void to_intern(View view) {
        startActivity(new Intent(studentLandingPage.this, student_intern_activity.class));
        finish();
    }
    public void profile(View view) {
        startActivity(new Intent(studentLandingPage.this, ProfileActivity.class));
        finish();
    }

    public void to_job(View view) {
        startActivity(new Intent(studentLandingPage.this, student_job_activity.class));
        finish();
    }
    public void to_applications_page(View view) {
        startActivity(new Intent(studentLandingPage.this, stud_applications.class));
        finish();
    }


}