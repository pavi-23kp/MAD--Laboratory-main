package com.example.sensorvalues;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
        implements SensorEventListener {

    TextView xValue, yValue, zValue;

    SensorManager sensorManager;
    Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Connect TextViews
        xValue = findViewById(R.id.xValue);
        yValue = findViewById(R.id.yValue);
        zValue = findViewById(R.id.zValue);

        // Get Sensor Service
        sensorManager = (SensorManager)
                getSystemService(SENSOR_SERVICE);

        // Get Accelerometer
        accelerometer = sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (accelerometer != null) {

            sensorManager.registerListener(
                    this,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL
            );
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Stop receiving sensor data
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() ==
                Sensor.TYPE_ACCELEROMETER) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            xValue.setText(
                    String.format("X : %.2f", x)
            );

            yValue.setText(
                    String.format("Y : %.2f", y)
            );

            zValue.setText(
                    String.format("Z : %.2f", z)
            );
        }
    }

    @Override
    public void onAccuracyChanged(
            Sensor sensor,
            int accuracy) {

        // Not required for this application
    }
}