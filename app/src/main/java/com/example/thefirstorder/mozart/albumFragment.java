package com.example.thefirstorder.mozart;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Juan on 4/12/2016.
 */
public class albumFragment extends Fragment
{
    //Creates an object of text. This will refer to the text box in the xml file
    TextView text;

    //When called from MusicLibrary
    public static albumFragment newInstance()
    {
        albumFragment fragment = new albumFragment();
        return fragment;
    }

    //When this activity is created, the user will be able to see the following upon creation
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.albums_fragment, container, false);
        text = (TextView) rootView.findViewById(R.id.albumsView);
        return rootView;
    }

    //constructor
    public albumFragment() {
    }
}
