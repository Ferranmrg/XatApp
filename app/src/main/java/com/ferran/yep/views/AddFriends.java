package com.ferran.yep.views;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ferran.yep.R;
import com.ferran.yep.models.Message;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class AddFriends extends AppCompatActivity {
    ListView friendList;
    ArrayList<String> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        friendList = (ListView) findViewById(R.id.friendList);
        users = new ArrayList<>();
        loadFriendList();

        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParseObject friend = new ParseObject("UserFriends");
                friend.put("user", ParseUser.getCurrentUser().getUsername());
                friend.put("friend", users.get(position));


                ParseQuery<ParseObject> query = ParseQuery.getQuery("UserFriends");

                query.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());
                query.whereEqualTo("friend", users.get(position));

                final String friendName = users.get(position);
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> messageList, ParseException e) {
                        if (e == null) {
                            if (messageList.size() > 0) {
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Already Friends :(", Snackbar.LENGTH_LONG);
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
                        friendList.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, users));
                    }
                } else {
                    Log.d("MESSAGE", "Error: " + e.getMessage());
                }
            }
        });
    }

}
