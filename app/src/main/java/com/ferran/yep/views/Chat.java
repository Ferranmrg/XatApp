package com.ferran.yep.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Chat extends Activity {
    String toUser;
    EditText chatText;
    ImageView send;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toUser = getIntent().getExtras().getString("To");
        chatText = (EditText) findViewById(R.id.chatEditText);
        send = (ImageView) findViewById(R.id.sendImg);
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
                                PS.setMessage(String.valueOf(chatText.getText()));
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

    }


}
