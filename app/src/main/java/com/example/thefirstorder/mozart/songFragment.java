package com.example.thefirstorder.mozart;


import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Juan on 4/12/2016.
 */
public class songFragment extends Fragment
{
    //Creates an object of text. This will refer to the text box in the xml file
    ListView lv;
    String[] items;

    //When called from MusicLibrary
    public static songFragment newInstance()
    {
        songFragment fragment = new songFragment();
        return fragment;
    }

    //When this activity is created, the user will be able to see the following upon creation
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.songs_fragment, container, false);
        lv = (ListView) rootView.findViewById(R.id.lvPlaylist);

        final ArrayList<File> mySongs = findSongs(Environment.getExternalStorageDirectory());
        items = new String[mySongs.size()];
        for(int i = 0; i < mySongs.size(); i++)
        {
            items[i] = mySongs.get(i).getName().toString().replace(".mp3","");
        }

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.song_layout, R.id.textView, items);
        lv.setAdapter(adp);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(songFragment.this.getActivity(), Player.class).putExtra("pos", position).putExtra("songList", mySongs));
            }
        });

        return rootView;

    }

    //findSongs Method
    public ArrayList<File> findSongs(File root)
    {
        ArrayList<File> al = new ArrayList<File>();
        File[] files = root.listFiles();
        File[] test = Environment.getRootDirectory().listFiles();

        if(test == null) {
            al.add(new File("dummy.mp3"));
            return al;
        }

        for(File singleFile : test)
        {
            System.out.println(singleFile.toString());
        }

        if(files == null) {
            al.add(new File("dummy.mp3"));
            return al;
        }

        for(File singleFile : files)
        {
            if(singleFile.isDirectory() && !singleFile.isHidden() && singleFile.toString().equals(root.toString() + "/Music"))
                al = findSongs(singleFile);
            else if(singleFile.toString().contains(root.toString()) && singleFile.getName().endsWith(".mp3"))
                al.add(singleFile);
        }
        return al;
    }

    //constructor
    public songFragment() {
    }




}
