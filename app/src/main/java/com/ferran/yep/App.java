package com.ferran.yep;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Ferra  on 14/01/2016.
 */
public class App extends Application {

    public static final String YOUR_APPLICATION_ID = "BX4DNKHpKq80K5ORG6wpcZFBkelvRUeZqyDqYmma";
    public static final String YOUR_CLIENT_KEY = "uNpeM7tZVjU79dsyVPB0OcCAJ68ndp2xX9NUNQKe";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("prueba", "onCreate: ");

        // Add your initialization code here
        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);
        ParseInstallation.getCurrentInstallation().saveInBackground();



    }
}
