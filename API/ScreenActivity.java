package com.example.roadsaftey;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//import android.widget.TextView;

public class ScreenActivity extends  Activity{
    GPSTracker gps;
    Button btnShowLocation;
    Button cancelButton;
    Button locationButton;
    Button showButton;
    String addressText="";
    String address="";
    double latitude=0.0;
    double longitude=0.0;
    int i;
    private Toast toast = null;
    DatabaseHandler db = new DatabaseHandler(this);
    //Handler that gets latitude and longitude


    @Override
    public void onCreate(Bundle savedInstanceState) {

       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);


        cancelButton = (Button)findViewById(R.id.cancel);
        locationButton = (Button)findViewById(R.id.location);
        //     showButton  = (Button)findViewById(R.id.button1);
        gps = new GPSTracker(this);

        for(i=0;i<5;i++){
            latitude=gps.getLatitude();
            longitude=gps.getLongitude();
            Toast.makeText(getApplicationContext(), "Latitude:"+latitude+"\nLongitude:"+longitude, Toast.LENGTH_LONG).show();
            if(latitude!=0.0)
                break;
            if(i==4)
                break;
        }

        if(latitude!=0.0 && longitude!=0.0) {

                addressText = getMyLocationAddress(latitude, longitude);
               Toast.makeText(getApplicationContext(), "Address:"+addressText, Toast.LENGTH_LONG).show();

        }

        //Sends SMS after 30 seconds
        final CountDownTimer start1=new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {

                if(latitude!=0.0 && longitude!=0.0) {
                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        List<Contact> contacts = db.getAllContacts();


                        for (Contact cn : contacts) {
                            String number = cn.getPhoneNumber();

                            if(addressText!=null && !addressText.isEmpty()) {

                                smsManager.sendTextMessage(number, null, latitude + "\n" + longitude + "\n" + addressText, null, null);
                                Toast.makeText(getApplicationContext(), "Latitude:" + latitude + "\nLongitude:" + longitude, Toast.LENGTH_LONG).show();
                                Toast.makeText(getApplicationContext(), "Address:" + addressText, Toast.LENGTH_LONG).show();
                                Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_SHORT).show();
                            }
                            else{

                                String uri = "http://maps.google.com/maps?saddr=" +latitude+","+longitude;
                                smsManager.sendTextMessage(number, null,uri, null, null);
                                Toast.makeText(getApplicationContext(),uri, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(),
                                ex.getMessage().toString(),
                                Toast.LENGTH_LONG).show();
                        ex.printStackTrace();
                    }
                }

            }

        }.start();



        locationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    List<Contact> contacts = db.getAllContacts();

                    for (Contact cn : contacts) {
                        String number =cn.getPhoneNumber();
                        smsManager.sendTextMessage(number, null,latitude+"\n"+longitude+"ressText\n"+addressText, null, null);
                        Toast.makeText(getApplicationContext(), "Address:" + addressText, Toast.LENGTH_LONG).show();
                        // Writing Contacts to log
                        Toast.makeText(getApplicationContext(), "Message Sent",Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception ex) {
                    Toast.makeText(getApplicationContext(),
                            ex.getMessage().toString(),
                            Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }

            }


        });




        // show cancel button click event
        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent( ScreenActivity.this,MainActivity.class);
                startActivity(i);
                ScreenActivity.this.finish();
                start1.cancel();

            }



        });

    }
    public String  getMyLocationAddress(double lat, double lon) {

        Geocoder geocoder= new Geocoder(this, Locale.ENGLISH);
        String ret = "";

        try {

            //Place your latitude and longitude
            List<Address> addresses = geocoder.getFromLocation(lat, lon,1);

            if(addresses != null) {

                Address fetchedAddress = addresses.get(0);
                StringBuilder strAddress = new StringBuilder();

                for(int i=0; i<fetchedAddress.getMaxAddressLineIndex(); i++) {
                    strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
                }

                ret=strAddress.toString();

            }

            else
                ret = "";

        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();



        }

        return ret;
    }

    public void onAttachedToWindow() {
        Window window = getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onAttachedToWindow();
    }
    @Override
    protected void onPause() {

        super.onPause();
    }

    //to resume that runnable
    @Override
    protected void onResume()
    {

        super.onResume();
    }

    @Override
    protected void onStop() {

        super.onStop();

    }


}
	





