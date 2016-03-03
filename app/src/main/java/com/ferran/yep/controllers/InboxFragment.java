package com.ferran.yep.controllers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.GpsStatus;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ferran.yep.R;
import com.ferran.yep.models.Message;
import com.ferran.yep.views.Chat;
import com.ferran.yep.views.InboxAdapter;
import com.ferran.yep.views.ReadMessages;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.GetFileCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


/**
 * Created by Ferra on 20/01/2016.
 */
public class InboxFragment extends ListFragment {
    ProgressBar pb;
    ArrayList<Message> messages;
    protected SwipeRefreshLayout mSwipeRefleshLayout;
    ListAdapter customAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);

        mSwipeRefleshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.SwipeRefreshLayout);

        mSwipeRefleshLayout.setOnRefreshListener(mOnRefreshListener);

        messages = new ArrayList<>();

        pb = (ProgressBar)
                rootView.findViewById(R.id.progressBar);



        Log.d("content in msg", "done: " + messages.size());

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadMessages();
    }


    public synchronized void LoadMessages() {
        messages = new ArrayList<>();
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
                                               final ArrayList<String> Aux = new ArrayList<String>();
                                               Log.d("MESSAGE", "Retrieved " + messageList.size() + " MESSAGES");
                                               for (int i = 0; i < messageList.size(); i++) {
                                                   String inMsg;
                                                   Object text = messageList.get(i).get("mText");
                                                   if (text != null) {
                                                       inMsg = text.toString();
                                                   } else {
                                                       inMsg = null;
                                                   }
                                                   Date fecha = messageList.get(i).getCreatedAt();
                                                   final Message M = new Message(messageList.get(i).get("From").toString(),
                                                           messageList.get(i).get("To").toString(), inMsg, fecha);

                                                   ParseFile fileObject = (ParseFile) messageList.get(i).get("mIMG");
                                                   ParseFile drawObject = (ParseFile) messageList.get(i).get("mDRAW");
                                                   ParseFile videoObject = (ParseFile) messageList.get(i).get("mVID");
                                                   if (fileObject != null) {
                                                       fileObject.getDataInBackground(new GetDataCallback() {
                                                           public void done(byte[] data, ParseException e) {
                                                               if (e == null) {
                                                                   Log.d("test",
                                                                           "We've got data in data.");
                                                                   // Decode the Byte[] into
                                                                   // Bitmap
                                                                   M.setImage(data);
                                                                   messages.add(M);
                                                                   pb.setProgress(60);
                                                                   customAdapter = new InboxAdapter(getActivity(), R.layout.custom_inbox_row, messages);
                                                                   setListAdapter(customAdapter);
                                                               }
                                                           }

                                                       });
                                                       Aux.add(i, "Image From:" + M.getFrom());
                                                   } else if (drawObject != null) {
                                                       drawObject.getDataInBackground(new GetDataCallback() {
                                                           public void done(byte[] data, ParseException e) {
                                                               if (e == null) {
                                                                   Log.d("test",
                                                                           "We've got data in data.");
                                                                   // Decode the Byte[] into
                                                                   // Bitmap
                                                                   M.setImage(data);
                                                                   messages.add(M);
                                                                   pb.setProgress(60);
                                                                   customAdapter = new InboxAdapter(getActivity(), R.layout.custom_inbox_row, messages);
                                                                   setListAdapter(customAdapter);
                                                               }
                                                           }
                                                       });
                                                   } else if (videoObject != null) {

                                                       videoObject.getFileInBackground(new GetFileCallback() {
                                                           @Override
                                                           public void done(File file, ParseException e) {

                                                               M.setVideo(file);
                                                               messages.add(M);
                                                               customAdapter = new InboxAdapter(getActivity(), R.layout.custom_inbox_row, messages);
                                                               setListAdapter(customAdapter);
                                                           }
                                                       });
                                                       Aux.add(i, "Video From:" + M.getFrom());
                                                   } else {
                                                       messages.add(M);
                                                       Aux.add(i, "Message From:" + M.getFrom());

                                                   }
                                                   if (getActivity() != null) {
                                                       customAdapter = new InboxAdapter(getActivity(), R.layout.custom_inbox_row, messages);
                                                       setListAdapter(customAdapter);
                                                   }
                                               }
                                               pb.setProgress(80);
                                           } else

                                           {
                                               Log.d("MESSAGE", "Error: " + e.getMessage());
                                           }

                                       }
                                   }

            );
        }
        //PARA EL SIMBOLO DEL SWIPE REFRESH
        if (mSwipeRefleshLayout.isRefreshing()){
            mSwipeRefleshLayout.setRefreshing(false);
        }

        pb.setProgress(100);
        pb.setVisibility(ProgressBar.INVISIBLE);

    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message m1, Message m2) {

                return m1.getFecha().compareTo(m2.getFecha());
            }
        });
        Collections.reverse(messages);
        Intent intent = new Intent(this.getContext(), ReadMessages.class);
        intent.putExtra("Message", messages.get(position));
        //DELETE SELECTED MESSAGE
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
        query.whereEqualTo("To", messages.get(position).getTo());
        query.whereEqualTo("createdAt", messages.get(position).getFecha());
        query.getFirstInBackground(new GetCallback<ParseObject>() {

                                       @Override
                                       public void done(ParseObject object, com.parse.ParseException arg0) {
                                           // TODO Auto-generated method stub

                                           final ParseObject deleteOb = object;

                                           try {
                                               deleteOb.delete();
                                               deleteOb.saveInBackground();
                                               LoadMessages();
                                           } catch (ParseException e) {

                                           }
                                       }
                                   }
        );

        this.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.shade);

    }

    protected SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            LoadMessages();
        }
    };




}

