package com.ferran.yep.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.ferran.yep.R;
import com.ferran.yep.models.Message;
import com.parse.FindCallback;
import com.parse.Parse;
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

public class Chat extends Activity {
    String toUser;
    EditText chatText;
    ImageView send;
    ImageView camera;
    ImageView draw;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_DRAW = 2;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toUser = getIntent().getExtras().getString("To");
        chatText = (EditText) findViewById(R.id.chatEditText);
        send = (ImageView) findViewById(R.id.sendImg);
        draw = (ImageView) findViewById(R.id.drawBtn);
        camera = (ImageView) findViewById(R.id.cameraBtn);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isMessage = false;
                ParseObject message = new ParseObject("Messages");
                message.put("From", ParseUser.getCurrentUser().getUsername());
                message.put("To", toUser);
                if (!String.valueOf(chatText.getText()).isEmpty()) {
                    message.put("mText", String.valueOf(chatText.getText()));
                    isMessage = true;
                }

                if (isMessage) {

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
                                //PS.setMessage(String.valueOf(chatText.getText()));
                                JSONObject fromUsuJson = new JSONObject();
                                try {
                                    fromUsuJson.put("From", ParseUser.getCurrentUser().getUsername());
                                    fromUsuJson.put("Message", String.valueOf(chatText.getText()));
                                } catch (JSONException ex) {

                                }

                                PS.setData(fromUsuJson);
                                PS.sendInBackground();

                            } else {
                                Log.d("MESSAGE", "Error: " + e.getMessage());
                            }
                        }
                    });
                }
                message.saveInBackground();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }


        });


        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // INTENT PARA IR A DIBUJAR
                Intent drawIntent = new Intent(getApplicationContext(), DrawActivity.class);
                startActivityForResult(drawIntent, REQUEST_DRAW);


            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
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
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);


        } else


            //DRAW RESULT ACTIVITY


            if (requestCode == REQUEST_DRAW && resultCode == RESULT_OK) {


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
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);


        }
    }
}
