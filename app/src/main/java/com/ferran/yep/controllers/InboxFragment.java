package com.ferran.yep.controllers;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.ferran.yep.R;


/**
 * Created by Ferra on 20/01/2016.
 */
public class InboxFragment extends ListFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);
        ProgressBar spinner = (ProgressBar)
                rootView.findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
        return rootView;
    }
}
