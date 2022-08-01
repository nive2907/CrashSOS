package com.example.roadsaftey;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;


public class AlarmUI extends ActionBarActivity {

    private MediaPlayer mediaPlayer;

    private double timeElapsed = 0, finalTime = 0;

    private Handler durationHandler = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ui);
        //*power button for alarm*

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume (AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),0);

        StartMyServiceAtBootReceiver mReceiver = new StartMyServiceAtBootReceiver(this);
        registerReceiver(mReceiver, filter);

        initializeViews();


    }


    public void initializeViews(){
        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        finalTime = mediaPlayer.getDuration();

    }

    // play mp3 song
    public void play(View view) {

        mediaPlayer.start();
        timeElapsed = mediaPlayer.getCurrentPosition();

    }

    //handler to change seekBarTime
    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            //get current position
            timeElapsed = mediaPlayer.getCurrentPosition();
            //set seekbar progress

            //set time remaing
            double timeRemaining = finalTime - timeElapsed;


            //repeat yourself that again in 100 miliseconds
            durationHandler.postDelayed(this, 100);
        }
    };

    // pause mp3 song
    public void pause(View view) {
        mediaPlayer.pause();
    }




}
