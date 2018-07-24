package com.palabs.polaroiddiary;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class Splash extends AppCompatActivity {

    ImageView i1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();


        setContentView(R.layout.activity_splash);

        i1=(ImageView)findViewById(R.id.imageViewSplash1);
        final Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein);
        final Animation animation2= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadeout);
        i1.setAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                MediaPlayer ring= MediaPlayer.create(Splash.this,R.raw.palabsaudio);
                ring.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                i1.setAnimation(animation2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation2.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                i1.setVisibility(View.GONE);
                startActivity(new Intent(getApplicationContext(),Splash2.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }
}

