package com.example.thefirstorder.mozart;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class HomePage extends AppCompatActivity {

    static AnimationDrawable backgroundAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_page);

        //ImageView backgroundImage = (ImageView) findViewById(R.id.background_anim);
        //backgroundImage.setBackgroundResource(R.drawable.background_animation);
        //backgroundAnimation = (AnimationDrawable) backgroundImage.getBackground();

    }

    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);

        //if(hasFocus)
            //backgroundAnimation.start();
    }


    public void jumpToMusicLibrary(View view)
    {
        Intent intent = new Intent(HomePage.this, MusicLibrary.class);
        startActivity(intent);
    }

    public void jumpToImages(View view)
    {
        Intent intent = new Intent(HomePage.this, images.class);
        startActivity(intent);
    }

    public void jumpToOptions(View view)
    {
        Intent intent = new Intent(HomePage.this, options.class);
        startActivity(intent);
    }

    public void jumpToAbout(View view)

    {
        Intent intent = new Intent(HomePage.this, About.class);
        startActivity(intent);
    }

}
