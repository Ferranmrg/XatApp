package com.ferran.yep.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.ferran.yep.R;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

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

                message.saveInBackground();
                message.put("From", ParseUser.getCurrentUser().getUsername());
                message.put("To", toUser);
                if (!String.valueOf(chatText.getText()).isEmpty()) {
                    message.put("mText", String.valueOf(chatText.getText()));
                    isMessage = true;
                }

                if (isMessage) {
                    message.saveInBackground();
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }


}
