package com.ferran.yep.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.ferran.yep.R;
import com.ferran.yep.models.Message;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.io.UnsupportedEncodingException;
import java.net.URI;

public class ReadMessages extends AppCompatActivity {
    TextView txtFrom;
    TextView txtMessage;
    ImageView imgMessage;
    TextView txtCountDown;
    VideoView videoView;
    final static int TIME = 12000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_messages);


      /*  Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }, TIME);*/
        txtCountDown = (TextView) findViewById(R.id.countDownText);
        new CountDownTimer(TIME, 1000) {
            public void onTick(long milisToFinish) {
                txtCountDown.setText(String.valueOf(milisToFinish / 1000));
            }

            public void onFinish() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.unshade, R.anim.slide_out_right);
            }
        }.start();

        txtFrom = (TextView) findViewById(R.id.txtFromMessage);
        txtMessage = (TextView) findViewById(R.id.txtInMessage);
        imgMessage = (ImageView) findViewById(R.id.imgFromMessage);
        videoView = (VideoView) findViewById(R.id.videoView);
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
        if (message.getVideo() != null) {
            Uri uri = Uri.fromFile(message.getVideo());
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(uri);
            videoView.resume();
        }

    }
}
