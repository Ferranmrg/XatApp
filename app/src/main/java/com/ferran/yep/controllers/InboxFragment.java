package com.ferran.yep.controllers;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ListFragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ferran.yep.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ferra on 20/01/2016.
 */
public class InboxFragment extends ListFragment {
    Thread t;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);
        ProgressBar spinner = (ProgressBar)
                rootView.findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isHidden())
                    if (ParseUser.getCurrentUser() != null)
                        loadMsg();
            }
        });
        if (ParseUser.getCurrentUser() != null) {
            t.start();
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadMessages();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    public synchronized void loadMsg() {
        try {
            wait(5000);
            LoadMessages();
        } catch (InterruptedException e) {

        }
    }


    public void LoadMessages() {
        final ArrayList<String> messages = new ArrayList<>();

        // CONSULTA PARSE
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");

        query.whereEqualTo("To", ParseUser.getCurrentUser().getUsername());

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> messageList, ParseException e) {
                if (e == null) {
                    Log.d("MESSAGE", "Retrieved " + messageList.size() + " MESSAGES");
                    for (int i = 0; i < messageList.size(); i++) {
                        messages.add(messageList.get(i).get("mText").toString());
                        Log.d("MESSAGE", "done: " + messageList.get(i).getString("From"));
                        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, messages));
                    }
                } else {
                    Log.d("MESSAGE", "Error: " + e.getMessage());
                }
            }
        });


    }

    @Override
    public void onPause() {
        super.onPause();
        t.stop();
    }
}
