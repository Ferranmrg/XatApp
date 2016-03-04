package com.ferran.yep.views;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ferran.yep.App;
import com.ferran.yep.R;
import com.ferran.yep.controllers.FriendsFragment;
import com.ferran.yep.controllers.InboxFragment;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;
import za.co.riggaroo.materialhelptutorial.TutorialItem;
import za.co.riggaroo.materialhelptutorial.tutorial.MaterialTutorialActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private FABToolbarLayout layout;
    private View one, two,three, four;
    private View fab;
    private static final int REQUEST_CODE = 1234;
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

        /*
        FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.flotatin_button);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                // Do something with yout menu items, or return false if you don't want to show them
                return true;
            }
        });


        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                // Start some activity
                Log.d("menu", "onMenuItemSelected: "+menuItem.getTitle());
                //Add Friend
                //Log Out
                if(menuItem.getTitle().equals(getString(R.string.action_logOut))){
                    ParseUser.logOut();
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    Intent intent = new Intent(getApplication(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    return true;

                }

                if(menuItem.getTitle().equals(getString(R.string.action_add_friend))){
                    Intent intent = new Intent(getApplication(), AddFriends.class);
                    startActivity(intent);
                    return true;
                }

                return false;
            }
        }); */

        layout = (FABToolbarLayout) findViewById(R.id.fabtoolbar);
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        fab = findViewById(R.id.fabtoolbar_fab);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.show();
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





    }
    //LISTENER FAB
    @Override
    public void onClick(View v) {

       //Toast.makeText(this, "Element clicked" + v.getContentDescription().toString(), Toast.LENGTH_SHORT).show();
        if(v.getContentDescription().toString().equals("2")){
            ParseUser.logOut();
            ParseUser currentUser = ParseUser.getCurrentUser();
            Intent intent = new Intent(getApplication(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);


        }

        if(v.getContentDescription().toString().equals("1")){
            Intent intent = new Intent(getApplication(), AddFriends.class);
            startActivity(intent);
        }

        if(v.getContentDescription().toString().equals("3")){
            layout.hide();
        }

        if(v.getContentDescription().toString().equals("4")){
            loadTutorial();
        }

    }


    public void loadTutorial() {
        Intent mainAct = new Intent(this, MaterialTutorialActivity.class);
        mainAct.putParcelableArrayListExtra(MaterialTutorialActivity.MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS, getTutorialItems(this));
        startActivityForResult(mainAct, REQUEST_CODE);

    }

    private ArrayList<TutorialItem> getTutorialItems(Context context) {
        TutorialItem tutorialItem1 = new TutorialItem(R.string.tutorial1t, R.string.tutorial1s,
                R.color.colorAccent, R.drawable.ic_pajarot, R.drawable.ic_pajarot);

        TutorialItem tutorialItem2 = new TutorialItem(R.string.tutorial2t, R.string.tutorial2s,
                R.color.orange,  R.drawable.t1t,R.drawable.t1t);

        TutorialItem tutorialItem3 = new TutorialItem(R.string.tutorial3t, R.string.tutorial3s,
                R.color.colorPrimary,  R.drawable.t3t,R.drawable.t3t);

        TutorialItem tutorialItem4 = new TutorialItem(R.string.tutorial4t, R.string.tutorial4s,
                R.color.colorPrimaryDark,  R.drawable.pajarot2png,  R.drawable.pajarot2png);

        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();
        tutorialItems.add(tutorialItem1);
        tutorialItems.add(tutorialItem2);
        tutorialItems.add(tutorialItem3);
        tutorialItems.add(tutorialItem4);

        return tutorialItems;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //    super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            //Toast.makeText(this, "Tutorial finished", Toast.LENGTH_LONG).show();
            Log.d("tutorial", "onActivityResult: Tutorial terminado");

        }
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