package com.ferran.yep;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Ferra on 14/01/2016.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);

        Parse.initialize(this);
    }
}
