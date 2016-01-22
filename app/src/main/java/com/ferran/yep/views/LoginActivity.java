package com.ferran.yep.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ferran.yep.App;
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

    //TEMP de mierda
    private float x1, x2;
    static final int MIN_DISTANCE = 150;
    //---------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        mSignUpTextView = (TextView)findViewById(R.id.signupText);
        EditText usuTxtEdit = (EditText)findViewById(R.id.passwordField);


        getSupportActionBar().hide();

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



    //------------------------------------------PROBAR MIERDA-----------------------------
    //------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;

                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    // Left to Right swipe action
                    if (x2 > x1) {
                        // Toast.makeText(this, "Left to Right swipe [Next]", Toast.LENGTH_SHORT).show ();

                    }

                    // Right to left swipe action
                    else {
                        // Toast.makeText(this, "Right to Left swipe [Previous]", Toast.LENGTH_SHORT).show ();


                        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    }

                } else {
                    // consider as something else - a screen tap for example
                }
                break;
        }
        return super.onTouchEvent(event);
    }


    //------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------











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
                    App.installation.put("username", user.getUsername());
                    App.installation.saveInBackground();



                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } else {
                    progress.hide();
                    //Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"Login Failed :)",Snackbar.LENGTH_LONG);
                    snackbar.show();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(pwdField.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                }
            }

        });


    }
}
