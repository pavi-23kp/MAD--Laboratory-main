package com.example.startstopcounter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView counterText;
    Button startButton, stopButton;

    int counter = 0;

    Handler handler = new Handler(Looper.getMainLooper());

    boolean isRunning = false;

    Runnable counterRunnable = new Runnable() {

        @Override
        public void run() {

            if (isRunning) {

                counter++;

                counterText.setText(String.valueOf(counter));

                // Run again after 1 second
                handler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        counterText = findViewById(R.id.counterText);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);

        // START button
        startButton.setOnClickListener(v -> {

            if (!isRunning) {

                isRunning = true;

                handler.post(counterRunnable);
            }
        });

        // STOP button
        stopButton.setOnClickListener(v -> {

            isRunning = false;

            handler.removeCallbacks(counterRunnable);
        });
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        handler.removeCallbacks(counterRunnable);
    }
}