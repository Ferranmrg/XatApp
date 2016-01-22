package com.ferran.yep.controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ferran.yep.R;
import com.ferran.yep.views.Chat;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ferra on 20/01/2016.
 */
public class FriendsFragment extends ListFragment  {
    final ArrayList<String> friends = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        ProgressBar spinner = (ProgressBar)
                rootView.findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
        if(ParseUser.getCurrentUser() != null) {
            getFriendList();
        }
        return rootView;
    }

    private void getFriendList() {

        // CONSULTA PARSE
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserFriends");

        query.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> messageList, ParseException e) {
                if (e == null) {
                    Log.d("Friends", "Retrieved " + messageList.size() + " Friends");
                    for (int i = 0; i < messageList.size(); i++) {
                        friends.add(messageList.get(i).get("friend").toString());
                        Log.d("MESSAGE", "done: " + messageList.get(i).getString("friend"));
                        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, friends));
                    }
                } else {
                    Log.d("MESSAGE", "Error: " + e.getMessage());
                }
            }
        });
    }
    private String m_Text = "";
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent intent = new Intent(this.getContext(), Chat.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("To", friends.get(position));
        this.startActivity(intent);

    }


}
