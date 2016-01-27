package com.ferran.yep.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ferran.yep.R;
import com.ferran.yep.models.Message;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

public class ReadMessages extends AppCompatActivity {
    TextView txtFrom;
    TextView txtMessage;
    ImageView imgMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_messages);

        txtFrom = (TextView) findViewById(R.id.txtFromMessage);
        txtMessage = (TextView) findViewById(R.id.txtInMessage);
        imgMessage = (ImageView) findViewById(R.id.imgFromMessage);
        Message message = (Message) getIntent().getExtras().getSerializable("Message");
        if (message.getMessage() != null)
            txtFrom.setText(message.getFrom() + " Says:");
        if (message.getMessage() != null) {
            txtMessage.setText(message.getMessage());
        }
        if (message.getImage() != null) {
            Bitmap bmp = BitmapFactory
                    .decodeByteArray(
                            message.getImage(), 0,
                            message.getImage().length);
            imgMessage.setImageBitmap(bmp);
        }

    }
}
