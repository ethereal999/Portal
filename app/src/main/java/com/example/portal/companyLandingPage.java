package com.example.portal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class companyLandingPage extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    GoogleSignInClient mGoogleSignInClient;
    TextView companyName;
    String cName;
    String image="https://firebasestorage.googleapis.com/v0/b/p-portal-1caec.appspot.com/o/Internships%2FiStock-476085198-300x300.jpg?alt=media&token=59d78f78-3baf-4a84-9b80-7191a962fc7c";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_landing_page);

        mFirebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        companyName = findViewById(R.id.company_name_text_view);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Companies").child(Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cName = Objects.requireNonNull(dataSnapshot.getValue(company.class)).company_getName();
                image = Objects.requireNonNull(dataSnapshot.getValue(company.class)).getLogo();
                companyName.setText(cName);
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
                Toast.makeText(companyLandingPage.this, "Signed Out Successfully !", Toast.LENGTH_SHORT).show();
            }
        });
        startActivity(new Intent(companyLandingPage.this,MainActivity.class));
        finish();
    }

    public void goto_add_page(View view) {
        Bundle extras = new Bundle();
        extras.putString("Company_Name",cName);
        extras.putString("Image_URL",image);
        startActivity(new Intent(companyLandingPage.this, add_intern_or_job_activity.class).putExtras(extras));
        finish();
    }
}
