package com.ferran.yep.views;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.ferran.yep.R;
import com.ferran.yep.models.Message;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.yayandroid.parallaxlistview.ParallaxListView;

import java.util.ArrayList;
import java.util.List;

public class AddFriends extends AppCompatActivity {

    ArrayList<String> users;
    ParallaxListView parallaxListView;
    ArrayList<String> userEmail = new ArrayList<String>();
    ArrayList<String> userEmailName = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        getSupportActionBar().hide();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });



        parallaxListView = (ParallaxListView) findViewById(R.id.parallaxListView);
        parallaxListView.setDividerHeight(5);
        parallaxListView.setBackgroundColor(getResources().getColor(R.color.white));

        users = new ArrayList<>();
        loadFriendList();

        parallaxListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParseObject friend = new ParseObject("UserFriends");
                friend.put("user", ParseUser.getCurrentUser().getUsername());
                friend.put("friend", userEmailName.get(position));


                ParseQuery<ParseObject> query = ParseQuery.getQuery("UserFriends");

                query.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());
                query.whereEqualTo("friend", userEmailName.get(position));

                final String friendName = userEmailName.get(position);
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> messageList, ParseException e) {
                        if (e == null) {
                            if (messageList.size() > 0) {
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Already Friends :)", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            } else {
                                ParseObject friend = new ParseObject("UserFriends");
                                friend.put("user", ParseUser.getCurrentUser().getUsername());
                                friend.put("friend", friendName);
                                friend.saveInBackground();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            Log.d("MESSAGE", "Error: " + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    public void loadFriendList() {
        // CONSULTA PARSE
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());

        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, ParseException e) {
                Log.d("Amigos: ", String.valueOf(userList.size()));
                if (e == null) {
                    for (int i = 0; i < userList.size(); i++) {
                        users.add(userList.get(i).getUsername());

                        ParseQuery<ParseUser> query = ParseUser.getQuery();
                        query.whereEqualTo("username", userList.get(i).getUsername());
                        query.orderByAscending("username");
                        query.getFirstInBackground(new GetCallback<ParseUser>() {
                            @Override
                            public void done(ParseUser object, ParseException e) {
                                userEmail.add(object.getEmail());
                                userEmailName.add(object.getUsername());

                                parallaxListView.setAdapter(
                                        new FriendsAdapter(getApplication(), userEmailName, userEmailName.size(), userEmail));
                                // Log.d("email", "done: " + object.getEmail() + "---------------" + object.getUsername());

                            }
                        });


                    }
                } else {
                    Log.d("MESSAGE", "Error: " + e.getMessage());
                }
            }
        });
    }

}
