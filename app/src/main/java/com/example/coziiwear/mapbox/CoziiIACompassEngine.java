package com.example.coziiwear.mapbox;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.mapbox.mapboxsdk.location.CompassEngine;
import com.mapbox.mapboxsdk.location.CompassListener;

import java.util.Vector;

public class CoziiIACompassEngine implements CompassEngine, SensorEventListener {

    //private IALocationManager iaLocationManager;
    private SensorManager sensorManager;

    /**
     * heading sensitivity in degrees
     */
    private double headingSensitivity = 10d;

    /**
     * orientation sensitivity in degrees
     */
    private double orientationsensitivity = 10d;

    private Vector<CompassListener> listeners = new Vector<CompassListener>();


    private double lastHeading = 0;

    public CoziiIACompassEngine(double headingSensitivity, Activity activity){
        //this.iaLocationManager = manager;
        sensorManager = (SensorManager) activity.getSystemService(activity.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        if (sensor != null) {
            sensorManager.registerListener(this, sensor,
                    SensorManager.SENSOR_DELAY_GAME);
            Log.i("Compass MainActivity", "Registerered for ORIENTATION Sensor");
        } else {
            Log.e("Compass MainActivity", "Registerered for ORIENTATION Sensor");
            Toast.makeText(activity, "ORIENTATION Sensor not found",
                    Toast.LENGTH_LONG).show();

        }
        this.headingSensitivity = headingSensitivity;

        // specify sensitivity
//        IAOrientationRequest request = new IAOrientationRequest(headingSensitivity, orientationsensitivity);
//        iaLocationManager.registerOrientationListener(request, this);

    }

    @Override
    public void addCompassListener(@NonNull CompassListener compassListener) {
        listeners.add(compassListener);
    }

    @Override
    public void removeCompassListener(@NonNull CompassListener compassListener) {
        listeners.remove(compassListener);
    }

    @Override
    public float getLastHeading() {
        return (float)lastHeading;
    }

    @Override
    public int getLastAccuracySensorStatus() {
        return 0;
    }



    public double getHeadingSensitivity() {
        return headingSensitivity;
    }



    public double getOrientationsensitivity() {
        return orientationsensitivity;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        float heading = event.values[0];
        if(Math.abs(lastHeading - heading) > headingSensitivity){
            lastHeading = heading;
            for (CompassListener compassListener:listeners) {
                compassListener.onCompassChanged((float)heading);
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {


    }
}
