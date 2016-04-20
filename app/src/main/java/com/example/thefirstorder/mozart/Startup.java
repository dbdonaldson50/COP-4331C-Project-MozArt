package com.example.thefirstorder.mozart;
import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;


public class Startup extends Activity {


    private static final int SPLASH_DISPLAY_LENGTH = 5000;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);



        new Handler().postDelayed(new Runnable() {

            @Override

            public void run() {
                /* Create an Intent that will start the Menu-Activity. */

                Intent mainIntent = new Intent(Startup.this, HomePage.class);

                Startup.this.startActivity(mainIntent);

                Startup.this.finish();

            }

        }, SPLASH_DISPLAY_LENGTH);

        //Creating the MozArt Folder
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures";
        final File MozArt = new File(rootPath, "MozArt");
        if(!MozArt.exists())
            MozArt.mkdir();

    }

};


