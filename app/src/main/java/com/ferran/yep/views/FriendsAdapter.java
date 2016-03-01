package com.ferran.yep.views;

/**
 * Created by pedrovelasco on 29/2/16.
 */
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ferran.yep.R;
import com.squareup.picasso.Picasso;
import com.yayandroid.parallaxlistview.ParallaxImageView;
import com.yayandroid.parallaxlistview.ParallaxViewHolder;

import java.util.ArrayList;

import fr.tkeunebr.gravatar.Gravatar;


/**
 * Created by yahyabayramoglu on 14/04/15.
 */
public class FriendsAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<String> friendsNames;
    private ArrayList<String> friendsEmails;
    private int totalFriends;



    public FriendsAdapter(Context context, ArrayList<String> friendsNames,int totalFriends, ArrayList<String> friendsEmails) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.friendsNames = friendsNames;
        this.totalFriends = totalFriends;
        this.friendsEmails = friendsEmails;

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_friends, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.testText);
            viewHolder.itemView = convertView;
            viewHolder.setBackgroundImage((ParallaxImageView) convertView.findViewById(R.id.parallaxImageView));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


         String gravatarUrl = Gravatar.init().with(friendsEmails.get(position)).defaultImage("retro").size(500).build();
        //Log.d("foto", "getView: "+gravatarUrl);


        // mi foto
        //"http://www.gravatar.com/avatar/f627cf51b38965a3105945484fcd4f33?s=500"

        Picasso.with(context).load(gravatarUrl).into(viewHolder.getBackgroundImage());
        viewHolder.textView.setText(friendsNames.get(position));

        // # CAUTION:
        // Important to call this method
        viewHolder.getBackgroundImage().reuse();




        return convertView;
    }


    @Override
    public int getCount() {
        return totalFriends;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * # CAUTION:
     * ViewHolder must extend from ParallaxViewHolder
     */
    static class ViewHolder extends ParallaxViewHolder {

        private TextView textView;

    }

}

