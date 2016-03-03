package com.ferran.yep.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ferran.yep.R;
import com.ferran.yep.models.Message;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class InboxAdapter extends ArrayAdapter<Message> {
    Context context;
    List<Message> messages;


    public InboxAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public InboxAdapter(Context context, int resource, List<Message> items) {
        super(context, resource, items);
        messages = items;
        this.context = context;
        // Sorting
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message fruit2, Message fruit1) {

                return fruit1.getFecha().compareTo(fruit2.getFecha());
            }
        });
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.custom_inbox_row, null);
        }

        Message p = getItem(position);

        if (p != null) {
            TextView senderTxt = (TextView) v.findViewById(R.id.senderMsg);
            TextView timeTxt = (TextView) v.findViewById(R.id.timeMsg);
            ImageView icon = (ImageView) v.findViewById(R.id.iconMsg);

            if (senderTxt != null) {
                senderTxt.setText(p.getFrom());
            }

            if (timeTxt != null) {
                 timeTxt.setText(new SimpleDateFormat("hh:mm:ss").format(p.getFecha()));

            }

            if (icon != null) {
                Icon icono;
                if (p.getVideo() != null) {
                    Drawable myIcon = context.getResources().getDrawable(R.drawable.ic_videomsg);
                    icon.setImageDrawable(myIcon);
                } else if (p.getImage() != null) {
                    Drawable myIcon = context.getResources().getDrawable(R.drawable.ic_imgmsg);
                    icon.setImageDrawable(myIcon);
                } else {
                    Drawable myIcon = context.getResources().getDrawable(R.drawable.ic_txtmsg);
                    icon.setImageDrawable(myIcon);
                }
            }


        }

        return v;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}