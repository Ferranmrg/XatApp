package com.ferran.yep.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
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
import com.ferran.yep.views.FriendsAdapter;
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
import com.yayandroid.parallaxlistview.ParallaxListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Ferra on 20/01/2016.
 */
public class FriendsFragment extends ListFragment {
    final ArrayList<String> friends = new ArrayList<>();
     ArrayList<String> friendEmailName = new ArrayList<String>();
      ArrayList<String> friendEmail = new ArrayList<String>();
    Chat chatRes = new Chat();
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_DRAW = 2;
    static final int REQUEST_VIDEO_CAPTURE = 3;
    String toUser = "";
    static ParallaxListView parallaxListView = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);

        parallaxListView = (ParallaxListView) rootView.findViewById(R.id.parallaxListView);
        parallaxListView.setDividerHeight(5);
        parallaxListView.setBackgroundColor(rootView.getResources().getColor(R.color.white));




       // ProgressBar spinner = (ProgressBar)
       //         rootView.findViewById(R.id.progressBar);
       //     spinner.setVisibility(View.GONE);
        if (ParseUser.getCurrentUser() != null) {
            getFriendList();
        }
        return rootView;
    }



    private void getFriendList() {

        // CONSULTA PARSE
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserFriends");

        query.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());

        //query.orderByAscending("friend");

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> messageList, ParseException e) {
                if (e == null) {
                    Log.d("Friends", "Retrieved " + messageList.size() + " Friends");
                    for (int i = 0; i < messageList.size(); i++) {
                        friends.add(messageList.get(i).get("friend").toString());


                        ParseQuery<ParseUser> query = ParseUser.getQuery();
                        query.whereEqualTo("username", messageList.get(i).get("friend").toString());
                        //query.orderByAscending("username");
                        query.getFirstInBackground(new GetCallback<ParseUser>() {
                            @Override
                            public void done(ParseUser object, ParseException e) {
                                friendEmail.add(object.getEmail());
                                friendEmailName.add(object.getUsername());


                                parallaxListView.setAdapter(
                                        new FriendsAdapter(getContext(), friendEmailName, friendEmailName.size(), friendEmail));
                               // Log.d("email", "done: " + object.getEmail() + "---------------" + object.getUsername());

                            }
                        });

                        Log.d("MESSAGE", "done: " + messageList.get(i).getString("friend"));



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
        dialog.setTitle(getString(R.string.pickone));

        //TEXT
        Button text = (Button) dialog.findViewById(R.id.TextBtn);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog textDiag = new Dialog(getActivity());
                textDiag.setTitle(getString(R.string.yourmessageto) +" "+ friendEmailName.get(position));
                textDiag.setContentView(R.layout.custom_text_float_menu);
                final EditText textContent = (EditText) textDiag.findViewById(R.id.editText);
                Button send = (Button) textDiag.findViewById(R.id.sendtxtMenuBtn);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ParseObject message = new ParseObject("Messages");
                        toUser = friendEmailName.get(position);
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
                toUser = friendEmailName.get(position);
                dialog.dismiss();
            }
        });

        //VIDEO
        Button video = (Button) dialog.findViewById(R.id.videBtn);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                }
                toUser = friendEmailName.get(position);
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
                query.whereEqualTo("friend", friendEmailName.get(position));
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
                friendEmailName.remove(position);
                parallaxListView.setAdapter(new FriendsAdapter(getActivity(), new ArrayList<String>(), 0, new ArrayList<String>()));
                parallaxListView.setAdapter(new FriendsAdapter(getActivity(),friendEmailName , friendEmail.size(),null));
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
                message.put("mDRAW", file);

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

        //VIDEO RESULT ACTIVITY


        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK) {

            Intent intent = getActivity().getIntent();
            Uri videoUri = data.getData();
            InputStream iStream;
            byte[] inputData = null;

            try {
                iStream = getActivity().getContentResolver().openInputStream(videoUri);
                inputData = getBytes(iStream);
            }catch (FileNotFoundException e){

            }catch (IOException e){

            }

            ParseObject message = new ParseObject("Messages");
            message.put("From", ParseUser.getCurrentUser().getUsername());
            message.put("To", toUser);
            ParseFile file = new ParseFile("video.mp4", inputData);
            file.saveInBackground();
            message.put("mVID", file);

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
                            fromUsuJson.put("Message", "Video");
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
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024*1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

}
