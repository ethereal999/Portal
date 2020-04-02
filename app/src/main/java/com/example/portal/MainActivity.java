package com.example.portal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;




public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
    public void goto_login(View view) {
        Intent gotoLoginPage = new Intent(this, LogInActivity.class);
        startActivity(gotoLoginPage);
        finish();
    }

    public void goto_signup(View view) {
        Intent gotoSignupPage = new Intent(this, SignUpActivity.class);
        startActivity(gotoSignupPage);
        finish();
    }
}
