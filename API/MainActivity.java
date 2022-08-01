package com.example.roadsaftey;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends ActionBarActivity {

    String number;
    int i=1;
    GPSTracker gps;
    double latitude;
    double longitude;

    protected LocationManager locationManager;
    boolean isGPSEnabled;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        gps = new GPSTracker(MainActivity.this);


        Button rs = (Button) this.findViewById(R.id.roadsafety);
        Button ps = (Button) this.findViewById(R.id.personal);
        rs.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                Intent i1 = new Intent(MainActivity.this,AccelerometerUI.class);
                startActivity(i1);
                Intent i = new Intent(MainActivity.this, MyService.class);
                startService(i);
            }

        });

        ps.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                Intent i1 = new Intent(MainActivity.this,PersonalSafety.class);
                startActivity(i1);
            }

        });

        // Adding Contacts
        Button add = (Button) findViewById(R.id.contacts);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(MainActivity.this,ContactsUI.class);
                startActivity(i1);
            }
        });
        Button read = (Button) findViewById(R.id.location);
        read.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String add;
                // TODO Auto-generated method stub
                gps = new GPSTracker(MainActivity.this);

                // check if GPS enabled
                if(gps.canGetLocation()){

                    latitude = gps.getLatitude();
                    longitude =gps.getLongitude();
                    add= getMyLocationAddress(latitude, longitude);
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude , Toast.LENGTH_LONG).show();
                    Toast.makeText(getBaseContext(),add,Toast.LENGTH_SHORT).show();

                }


                else

                {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
            }
        });

        Button alarm = (Button) findViewById(R.id.alarm);
        alarm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(MainActivity.this,AlarmUI.class);
                startActivity(i);
            }
        });
        locationManager = (LocationManager) getApplicationContext()
                .getSystemService(LOCATION_SERVICE);
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(!isGPSEnabled) {
            gps.showSettingsAlert();
        }


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

}

