package com.ferran.yep.views;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ferran.yep.App;
import com.ferran.yep.R;
import com.ferran.yep.controllers.FriendsFragment;
import com.ferran.yep.controllers.InboxFragment;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class MainActivity extends AppCompatActivity {

    // Contains the actual Fragment
    Fragment fragment = null;

    // Inbox Fragment
    InboxFragment iFrag = new InboxFragment();


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    Drawable icon1;
    Drawable icon2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            App.installation.put("username", currentUser.getUsername());
            App.installation.saveInBackground();
        } else {

            Intent i = new Intent(this, SplashScreen.class);
            startActivity(i);
        }
        //BOTON FLOTANTE

        FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.flotatin_button);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                // Do something with yout menu items, or return false if you don't want to show them
                return true;
            }
        });

        //FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.fab_speed_dial);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                // Start some activity
                Log.d("menu", "onMenuItemSelected: "+menuItem.getTitle());
                //Add Friend
                //Log Out
                if(menuItem.getTitle().equals("Log Out")){
                    ParseUser.logOut();
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    Intent intent = new Intent(getApplication(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    return true;

                }

                if(menuItem.getTitle().equals("Add Friend")){
                    Intent intent = new Intent(getApplication(), AddFriends.class);
                    startActivity(intent);
                    return true;
                }

                return false;
            }
        });



        //---------------


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_archive_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_people_24dp);

     /*   tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0)
                    icon1.setColorFilter(Color.rgb(0, 150, 136), PorterDuff.Mode.MULTIPLY);
                if (tab.getPosition() == 1) {
                    icon2.setColorFilter(Color.rgb(0, 150, 136), PorterDuff.Mode.MULTIPLY);
                }
                Log.d("tabPressed", "onTabSelected: " + tab.getText().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0)
                    icon1.setColorFilter(Color.rgb(250, 250, 250), PorterDuff.Mode.MULTIPLY);
                if (tab.getPosition() == 1)
                    icon2.setColorFilter(Color.rgb(250, 250, 250), PorterDuff.Mode.MULTIPLY);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/




    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) {
                fragment = iFrag;

            } else if (position == 1) {
                fragment = new FriendsFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {

            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab1);
                case 1:
                    return getString(R.string.tab2);
            }
            return null;
        }
    }
}