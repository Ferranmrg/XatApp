package com.ferran.yep.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ferran.yep.R;
import com.ferran.yep.views.Chat;
import com.ferran.yep.views.DrawActivity;
import com.ferran.yep.views.MainActivity;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ferra on 20/01/2016.
 */
public class FriendsFragment extends ListFragment {
    final ArrayList<String> friends = new ArrayList<>();
    Chat chatRes = new Chat();
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_DRAW = 2;
    String toUser = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        ProgressBar spinner = (ProgressBar)
                rootView.findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
        if (ParseUser.getCurrentUser() != null) {
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
    public void onListItemClick(ListView l, View v, final int position, long id) {
        super.onListItemClick(l, v, position, id);

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_menu_friend);
        dialog.setTitle("Pick One");

        //TEXT
        Button text = (Button) dialog.findViewById(R.id.TextBtn);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog textDiag = new Dialog(getActivity());
                textDiag.setTitle("Your Message to " + friends.get(position));
                textDiag.setContentView(R.layout.custom_text_float_menu);
                final EditText textContent = (EditText) textDiag.findViewById(R.id.editText);
                Button send = (Button) textDiag.findViewById(R.id.sendtxtMenuBtn);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ParseObject message = new ParseObject("Messages");
                        toUser = friends.get(position);
                        message.put("From", ParseUser.getCurrentUser().getUsername());
                        message.put("To", toUser);
                        if (!String.valueOf(textContent.getText()).isEmpty()) {
                            message.put("mText", String.valueOf(textContent.getText()));
                        }

                        ParseQuery<ParseUser> query = ParseUser.getQuery();
                        query.whereEqualTo("username", toUser);


                        query.findInBackground(new FindCallback<ParseUser>() {
                            public void done(List<ParseUser> userList, ParseException e) {
                                if (e == null) {
                                    ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
                                    pushQuery.whereMatches("username", toUser);
                                    ParsePush PS = new ParsePush();
                                    PS.setQuery(pushQuery);
                                    //PS.setMessage(String.valueOf(chatText.getText()));
                                    JSONObject fromUsuJson = new JSONObject();
                                    try {
                                        fromUsuJson.put("From", ParseUser.getCurrentUser().getUsername());
                                        fromUsuJson.put("Message", String.valueOf(textContent.getText()));
                                    } catch (JSONException ex) {

                                    }

                                    PS.setData(fromUsuJson);
                                    PS.sendInBackground();

                                } else {
                                    Log.d("MESSAGE", "Error: " + e.getMessage());
                                }
                            }
                        });
                        textDiag.dismiss();
                        dialog.dismiss();
                        message.saveInBackground();
                    }
                });
                textDiag.show();

            }
        });

        //IMAGES
        Button image = (Button) dialog.findViewById(R.id.imgBtn);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                toUser = friends.get(position);
                dialog.dismiss();
            }
        });

        //DRAW
        Button draw = (Button) dialog.findViewById(R.id.drawBtn);
        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent drawIntent = new Intent(getContext(), DrawActivity.class);
                startActivityForResult(drawIntent, REQUEST_DRAW);
                dialog.dismiss();
            }
        });

        //DELETE
        Button delete = (Button) dialog.findViewById(R.id.deleteBtn);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("UserFriends");
                //"objectId" the column name in parse.com
                //objectID is the content ID in the table objectId
                query.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());
                query.whereEqualTo("friend", friends.get(position));
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject friend, ParseException e) {
                        try {
                            friend.delete();
                            friend.saveInBackground();
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    //Activity Results
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Log.d("vuelvo", "onActivityResult: ------- ");
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            ParseObject message = new ParseObject("Messages");
            message.put("From", ParseUser.getCurrentUser().getUsername());
            message.put("To", toUser);
            ParseFile file = new ParseFile("foto.png", byteArray);
            file.saveInBackground();
            message.put("mIMG", file);

            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("username", toUser);

            // TODO revisar esto

            query.findInBackground(new FindCallback<ParseUser>() {
                public void done(List<ParseUser> userList, ParseException e) {
                    if (e == null) {
                        ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
                        pushQuery.whereMatches("username", userList.get(0).getUsername());
                        ParsePush PS = new ParsePush();
                        PS.setQuery(pushQuery);
                        JSONObject fromUsuJson = new JSONObject();
                        try {
                            fromUsuJson.put("From", ParseUser.getCurrentUser().getUsername());
                            fromUsuJson.put("Message", "Image");
                        } catch (JSONException ex) {

                        }
                        PS.setData(fromUsuJson);
                        PS.sendInBackground();

                    } else {
                        Log.d("MESSAGE", "Error: " + e.getMessage());
                    }
                }
            });
            message.saveInBackground();


        } else


            //DRAW RESULT ACTIVITY


            if (requestCode == REQUEST_DRAW && resultCode == Activity.RESULT_OK) {


                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                ParseObject message = new ParseObject("Messages");
                message.put("From", ParseUser.getCurrentUser().getUsername());
                message.put("To", toUser);
                ParseFile file = new ParseFile("foto.png", byteArray);
                file.saveInBackground();
                message.put("mIMG", file);

                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("username", toUser);

                // TODO revisar esto

                query.findInBackground(new FindCallback<ParseUser>() {
                    public void done(List<ParseUser> userList, ParseException e) {
                        if (e == null) {
                            ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
                            pushQuery.whereMatches("username", toUser);
                            ParsePush PS = new ParsePush();
                            PS.setQuery(pushQuery);
                            JSONObject fromUsuJson = new JSONObject();
                            try {
                                fromUsuJson.put("From", ParseUser.getCurrentUser().getUsername());
                                fromUsuJson.put("Message", "Draw");
                            } catch (JSONException ex) {

                            }
                            PS.setData(fromUsuJson);
                            PS.sendInBackground();

                        } else {
                            Log.d("MESSAGE", "Error: " + e.getMessage());
                        }
                    }
                });
                message.saveInBackground();


            }
    }

}
