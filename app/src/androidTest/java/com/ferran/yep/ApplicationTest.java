package com.ferran.yep;


import android.content.Intent;
import android.os.CountDownTimer;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ferran.yep.views.LoginActivity;

import com.ferran.yep.views.MainActivity;
import com.parse.ParseUser;


public class ApplicationTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private EditText user;
    private Button login;
    private EditText password;
    private LoginActivity actividad;

    public ApplicationTest() {
//		super("com.example.calc", MainActivity.class);
        super(LoginActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        actividad = getActivity();
        user = (EditText) actividad.findViewById(R.id.usernameField);
        password = (EditText) actividad.findViewById(R.id.passwordField);
        login = (Button) actividad.findViewById(R.id.loginButton);

    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private static final String USER = "Ferran";
    private static final String PASSWORD = "0 0 0 0";

    public void testLogin() {

        ParseUser.logOut();
        //on value 1 entry
        TouchUtils.tapView(this, user);
        getInstrumentation().sendStringSync(USER);

        // now on value2 entry
        TouchUtils.tapView(this, password);
        sendKeys(PASSWORD);
        // now on Add button

        //  TouchUtils.clickView(this, login);

        // ParseUser.logInInBackground(String.valueOf(user.getText()), String.valueOf(password.getText()));

        // assertTrue("Loged in...", ParseUser.getCurrentUser() != null);
    }


}
