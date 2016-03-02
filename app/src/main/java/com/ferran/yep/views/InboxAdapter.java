package com.ferran.yep.views;

import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ferran.yep.R;
import com.ferran.yep.models.Message;

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
               // timeTxt.setText(p.getFecha().toString());
            }


        }

        return v;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}