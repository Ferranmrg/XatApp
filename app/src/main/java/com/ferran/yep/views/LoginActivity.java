package com.ferran.yep.views;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ferran.yep.R;
import com.parse.ParseObject;

public class LoginActivity extends AppCompatActivity {

    protected TextView mSignUpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
        mSignUpTextView = (TextView)findViewById(R.id.signupText);
        EditText usuTxtEdit = (EditText)findViewById(R.id.passwordField);
        usuTxtEdit.requestFocus();
        getSupportActionBar().hide();
        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
