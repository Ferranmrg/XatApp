package com.ferran.yep.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.ferran.yep.R;
import com.ferran.yep.models.Message;

public class ReadMessages extends AppCompatActivity {
    TextView txtFrom;
    TextView txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_messages);

        txtFrom = (TextView) findViewById(R.id.txtFromMessage);
        txtMessage = (TextView) findViewById(R.id.txtInMessage);
        Message message = (Message) getIntent().getExtras().getSerializable("Message");
        txtFrom.setText(message.getFrom() + " Says:");
        txtMessage.setText(message.getMessage());

    }
}
