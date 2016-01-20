package com.ferran.yep.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ferran.yep.R;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    protected TextView mSignUpTextView;
    private EditText userField, pwdField;
    private Button sendBtn;
    private final String TAG = SignUpActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mSignUpTextView = (TextView)findViewById(R.id.signupText);
        EditText usuTxtEdit = (EditText)findViewById(R.id.passwordField);


        getSupportActionBar().hide();
        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        userField = (EditText) findViewById(R.id.usernameField);
        pwdField = (EditText) findViewById(R.id.passwordField);
        sendBtn = (Button) findViewById(R.id.loginButton);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPerformed();


            }
        });
    }

    public void clickPerformed() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Login in brah :) ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();

        ParseUser.logInInBackground(String.valueOf(userField.getText()), String.valueOf(pwdField.getText()), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {

                    progress.hide();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    Log.d("prueba", "llego hasta aqui :3 ");
                } else {
                    progress.hide();
                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();

                }
            }

        });


    }
}
