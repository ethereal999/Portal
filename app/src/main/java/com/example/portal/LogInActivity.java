package com.example.portal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LogInActivity extends AppCompatActivity {
    private static final String TAG ="Failed" ;
    EditText userId, password;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    AppCompatRadioButton stud, comp;
    Button loginButton;
    TextView dontHaveAnAccount,mOption,randOption;
    SignInButton googleLogin;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 1;

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);

    }

    public void back_to_main(View view) {
        Intent gotoMainPage = new Intent(this, MainActivity.class);
        startActivity(gotoMainPage);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_log_in);

        userId = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginpaswd);
        dontHaveAnAccount = findViewById(R.id.TVSignIn);
        stud = findViewById(R.id.student_selector_2);
        comp = findViewById(R.id.company_selector_2);
        mOption = findViewById(R.id.TVSignIn);
       randOption= findViewById(R.id.randv);



        loginButton = findViewById(R.id.btnLogIn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userId.getText().toString();
                String pwd = password.getText().toString();
                if(username.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(LogInActivity.this,"Fields are Empty!",Toast.LENGTH_SHORT).show();
                }
                else if(username.isEmpty()){
                    userId.setError("Please enter Username");
                    userId.requestFocus();
                }
                else if(pwd.isEmpty()){
                    password.setError("Please enter a Password");
                    password.requestFocus();
                }
                else{
                    mFirebaseAuth.signInWithEmailAndPassword(username,pwd).addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(! task.isSuccessful()){
                                Toast.makeText(LogInActivity.this,"Login Error, Please try Again",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                if(stud.isChecked()){
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Students");
                                    ref.child(Objects.requireNonNull(mFirebaseAuth.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){
                                                Toast.makeText(LogInActivity.this, "Logged in!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(LogInActivity.this, studentLandingPage.class));
                                                finish();
                                            } else{
                                                Toast.makeText(LogInActivity.this, "Username Not in Students !", Toast.LENGTH_SHORT).show();
                                                FirebaseAuth.getInstance().signOut();
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                                    });
                                } else if(comp.isChecked()){
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Companies");
                                    ref.child(Objects.requireNonNull(mFirebaseAuth.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){
                                                Toast.makeText(LogInActivity.this, "Logged in!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(LogInActivity.this, companyLandingPage.class));
                                                finish();
                                            } else{
                                                Toast.makeText(LogInActivity.this, "Username Not in Companies !", Toast.LENGTH_SHORT).show();
                                                FirebaseAuth.getInstance().signOut();
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                                    });
                                }else{
                                    Toast.makeText(LogInActivity.this, "Please Check in your Type !", Toast.LENGTH_SHORT).show();
                                    FirebaseAuth.getInstance().signOut();
                                }
                            }
                        }
                    });
                }
            }
        });

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (stud.isChecked()) {
                    if (mFirebaseUser != null) {
                        Toast.makeText(LogInActivity.this, "You are Logged in !", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LogInActivity.this, studentLandingPage.class));
                        finish();
                    } else {
                        Toast.makeText(LogInActivity.this, "Please fill Correct Details", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (comp.isChecked()) {
                    if (mFirebaseUser != null) {

                        startActivity(new Intent(LogInActivity.this, companyLandingPage.class));
                        finish();
                    } else {
                        Toast.makeText(LogInActivity.this, "Please fill Correct Details", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(LogInActivity.this, "Choose the user type", Toast.LENGTH_SHORT).show();
                }
            }
        };
        dontHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, SignUpActivity.class));
                finish();
            }
        });
        stud.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(stud.isChecked()){
                    comp.setChecked(false);
                    mOption.setVisibility(View.VISIBLE);
                    randOption.setVisibility(View.VISIBLE);
                    googleLogin.setVisibility(View.VISIBLE);
                }
            }
        });

        comp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(comp.isChecked()){
                    stud.setChecked(false);
                    mOption.setVisibility(View.GONE);
                    randOption.setVisibility(View.GONE);
                    googleLogin.setVisibility(View.GONE);
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googleLogin = findViewById(R.id.google_login_button);
        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.google_login_button:
                        googleLogIn();
                        break;
                }
            }
        });
    }

    private void googleLogIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                startActivity(new Intent(LogInActivity.this,  HomeActivity.class));
                finish();

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }

        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LogInActivity.this,"Login Error, Please try Again",Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            FirebaseAuth.getInstance().signOut();
            mGoogleSignInClient.signOut();
        }
        else{

        }
    }
}