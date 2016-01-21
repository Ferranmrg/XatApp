package com.ferran.yep.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ferran.yep.R;

//CONTROLLER
import com.ferran.yep.controllers.SignUpController;
import com.parse.Parse;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.text.ParseException;

public class SignUpActivity extends AppCompatActivity {
    private EditText userField, pwdField, emailField;
    private TextView backButton;
    private Button sendBtn;
    private final String TAG = SignUpActivity.class.getSimpleName();

    //TEMP de mierda
    private float x1, x2;
    static final int MIN_DISTANCE = 150;
    //---------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);

        userField = (EditText) findViewById(R.id.usernameField);
        pwdField = (EditText) findViewById(R.id.passwordField);
        emailField = (EditText) findViewById(R.id.emailField);

        sendBtn = (Button) findViewById(R.id.signupButton);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickPerformed()) {
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                }
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

                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                    }

                    // Right to left swipe action
                    else {
                        // Toast.makeText(this, "Right to Left swipe [Previous]", Toast.LENGTH_SHORT).show ();
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




    public boolean clickPerformed() {
        boolean complete = true;
        String usu = "", pwd = "", mail = "";
        if (!isEmptyField(userField))
            usu = String.valueOf(userField.getText());
        else {
            userField.setHint("Debe insertar un usuario");
            complete = false;
        }
        if (!isEmptyField(pwdField))
            pwd = String.valueOf(pwdField.getText());
        else {
            pwdField.setHint("Debe insertar una contraseña");
            complete = false;
        }
        if (!isEmptyField(emailField))
            mail = String.valueOf(emailField.getText());
        else {
            emailField.setHint("Debe insertar un email");
            complete = false;
        }
        if (complete) {
            createUser(usu, pwd, mail);
        }
        return complete;
    }

    public boolean isEmptyField(EditText edit) {
        boolean isEmpty = false;
        if (String.valueOf(edit.getText()).equals("")) {
            isEmpty = true;
        }
        return isEmpty;
    }

    public void createUser(String usu, String pwd, String mail) {
        ParseUser user = new ParseUser();
        user.setUsername(usu);
        user.setPassword(pwd);
        user.setEmail(mail);


        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    Log.d("prueba", "createUser: biennn ");
                } else {

                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    Log.d("prueba", "createUser:  malll");
                }
            }
        });
    }
}
