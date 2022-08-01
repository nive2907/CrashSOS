package com.example.roadsaftey;


import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Process;
import android.widget.Toast;

public class MyService extends Service implements SensorEventListener {
    public static final String TAG = MyService.class.getName();
    public static final int SCREEN_OFF_RECEIVER_DELAY = 500;
    private float x = 0;
    private float y = 0;
    private float z = 0;
    private long lastUpdate = 0;
    private float lastX = 0;
    private float lastY = 0;
    private float lastZ = 0;
    private static float threshold  = 30.0f;
    private static int interval     = 200;
    private long now = 0;
    private long lastShake = 0;
    private long timeDiff = 0;
    private float force = 0;
    private SensorManager mSensorManager = null;
    private WakeLock mWakeLock = null;

    /*
     * Register this as a sensor event listener.
     */
    private void registerListener() {
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    /*
     * Un-register this as a sensor event listener.
     */
    private void unregisterListener() {
        mSensorManager.unregisterListener(this);
    }

    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            if (!intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                return;
            }

            Runnable runnable = new Runnable() {
                public void run() {

                    unregisterListener();
                    registerListener();
                }
            };

            new Handler().postDelayed(runnable, SCREEN_OFF_RECEIVER_DELAY);
        }
    };

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onSensorChanged(SensorEvent event) {

        now = event.timestamp;
        x = event.values[0];
        y = event.values[1];
        z = event.values[2];
        if (lastUpdate == 0) {
            lastUpdate = now;
            lastShake = now;
            lastX = x;
            lastY = y;
            lastZ = z;
            Toast.makeText(this, "No Motion detected",
                    Toast.LENGTH_SHORT).show();

        } else {
            timeDiff = now - lastUpdate;
            if (timeDiff > 0) {

                    /*force = Math.abs(x + y + z - lastX - lastY - lastZ)
                                / timeDiff;*/
                force = Math.abs(x + y + z - lastX - lastY - lastZ);

                if (Float.compare(force, threshold) > 0) {
                    //Toast.makeText(Accelerometer.getContext(),
                    //(now-lastShake)+"  >= "+interval, 1000).show();

                    if (now - lastShake >= interval) {

                        // trigger shake event
                        Toast.makeText(getBaseContext(), "Motion detected",
                                Toast.LENGTH_SHORT).show();


                        stopService(new Intent(this, MyService.class));

                        Intent intent = new Intent(this,ScreenActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    lastShake = now;
                }
                lastX = x;
                lastY = y;
                lastZ = z;
                lastUpdate = now;


            }


        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        PowerManager manager =
                (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);

        registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        unregisterListener();
        mWakeLock.release();
        stopForeground(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        startForeground(Process.myPid(), new Notification());
        registerListener();
        mWakeLock.acquire();

        return START_STICKY;
    }
}

