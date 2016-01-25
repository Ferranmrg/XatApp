package com.ferran.yep.controllers;

import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ListFragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ferran.yep.R;
import com.ferran.yep.models.Message;
import com.ferran.yep.views.Chat;
import com.ferran.yep.views.ReadMessages;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ferra on 20/01/2016.
 */
public class InboxFragment extends ListFragment {
    Thread t;
    ProgressBar pb;
    ArrayList<Message> messages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);
        pb = (ProgressBar)
                rootView.findViewById(R.id.progressBar);
        messages = new ArrayList<>();
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isHidden())
                    if (ParseUser.getCurrentUser() != null)
                        loadMsg();
            }
        });
        loadMsg();
        if (ParseUser.getCurrentUser() != null) {
            //     t.start();
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadMessages();
    }

    public synchronized void loadMsg() {
        try {
            wait(5000);
            LoadMessages();
        } catch (InterruptedException e) {

        }
    }


    public void LoadMessages() {

        pb.setMax(100);
        pb.setVisibility(ProgressBar.VISIBLE);
        pb.setProgress(20);
        // CONSULTA PARSE
        if (ParseUser.getCurrentUser() != null) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
            query.orderByDescending("createdAt");
            query.whereEqualTo("To", ParseUser.getCurrentUser().getUsername());

            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> messageList, ParseException e) {
                    if (e == null) {
                        Log.d("MESSAGE", "Retrieved " + messageList.size() + " MESSAGES");
                        ArrayList<String> Aux = new ArrayList<String>();
                        for (int i = 0; i < messageList.size(); i++) {
                            Message M = new Message(messageList.get(i).get("From").toString(),
                                    messageList.get(i).get("To").toString(),
                                    messageList.get(i).get("mText").toString());
                            messages.add(M);
                            pb.setProgress(60);
                            Aux.add(i, "Messsage From: " + M.getFrom());
                        }
                        if (getActivity() != null)
                            setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Aux));
                        pb.setProgress(80);
                    } else {
                        Log.d("MESSAGE", "Error: " + e.getMessage());
                    }
                }
            });
        }
        pb.setProgress(100);
        pb.setVisibility(ProgressBar.INVISIBLE);

    }

    @Override
    public void onPause() {
        super.onPause();
        //t.interrupt();
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent intent = new Intent(this.getContext(), ReadMessages.class);
        intent.putExtra("Message", messages.get(position));
        this.startActivity(intent);

    }

}
