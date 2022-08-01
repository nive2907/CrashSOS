package com.example.roadsaftey;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PersonalSafety extends ActionBarActivity {
    GPSTracker gps;
    Button btnShowLocation;
    Button cancelButton;
    Button locationButton;
    Button showButton;
    String add="";
    String latitude;
    String longitude;
    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_safety);
        cancelButton = (Button)findViewById(R.id.cancel);

        locationButton = (Button)findViewById(R.id.location);

        locationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    List<Contact> contacts = db.getAllContacts();

                    for (Contact cn : contacts) {
                        String number =cn.getPhoneNumber();
                        smsManager.sendTextMessage(number, null,latitude+"\n"+longitude+"\n"+add, null, null);
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
        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent( PersonalSafety.this,MainActivity.class);
                startActivity(i);

            }



        });
    }

    public String  getMyLocationAddress(String lat, String lon) {

        Geocoder geocoder= new Geocoder(this, Locale.ENGLISH);
        String ret = "";

        try {

            //Place your latitude and longitude
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lon),1);

            if(addresses != null) {

                Address fetchedAddress = addresses.get(0);
                StringBuilder strAddress = new StringBuilder();

                for(int i=0; i<fetchedAddress.getMaxAddressLineIndex(); i++) {
                    strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
                }

                ret=strAddress.toString();

            }

            else
                ret = "No Address returned!";

        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ret = "Can't get Address!";


        }

        return ret;
    }
}