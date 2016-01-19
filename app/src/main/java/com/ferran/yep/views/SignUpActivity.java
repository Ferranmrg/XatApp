package com.ferran.yep.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ferran.yep.R;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);
    }
}