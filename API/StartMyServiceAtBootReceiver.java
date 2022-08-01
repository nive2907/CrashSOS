package com.example.roadsaftey;

/**
 * Created by Nive on 2/27/2015.
 */
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

public class StartMyServiceAtBootReceiver extends BroadcastReceiver{

    private MediaPlayer mediaPlayer;
    private int a=0;
    private Activity acti = null;
    private long lastTouch = -1;
    private final long DELAY = 2000;
    long k;
    int v=0;
    private long  tmpDelay=0;


    public StartMyServiceAtBootReceiver(Activity act) {

        acti = act;
        initializeViews();
    }

    public void initializeViews(){

        mediaPlayer = MediaPlayer.create(acti, R.raw.alarm);

    }
    @Override
    public void onReceive(Context ctxt, Intent intent) {

        if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){


            if(lastTouch == -1){
                //1st touch
                lastTouch = System.currentTimeMillis();
                a++;
            }else{
                //2ns touch

                tmpDelay = System.currentTimeMillis() - lastTouch;
                k=tmpDelay;
                //if you want to always consider this as second touch, set lastTouch here
                //lastTouch = System.currentTimeMillis(); //[OPTION 1]


                if(tmpDelay >= DELAY){
                    // Toast.makeText(acti,tmpDelay+" "+ a, Toast.LENGTH_SHORT).show();
                    a=0;
                    lastTouch =-1;

                }else{
                    a++;

                    // if it's not within the range, then consider this 2nd touch as 1st touch.
                    //lastTouch = System.currentTimeMillis();//[OPTION 2]
                }
            }

            if(a>1)
            {

                a=0;
                lastTouch =-1;

                // For example to set the volume of played media to maximum.


                mediaPlayer.start();

                // Intent i = new Intent(acti, AlarmPower.class);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // acti.startActivity(i);

            }
            else
            {


            }
        }
    }

}