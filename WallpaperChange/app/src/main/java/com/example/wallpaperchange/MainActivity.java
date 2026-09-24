package com.example.wallpaperchange;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Button changeButton, stopButton;
    TextView status;

    Timer timer;

    Random random = new Random();

    int lastWallpaper = -1;

    int[] wallpapers = {
            R.drawable.wallpaper1,
            R.drawable.wallpaper2,
            R.drawable.wallpaper3,
            R.drawable.wallpaper4
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        changeButton = findViewById(R.id.changeButton);
        stopButton = findViewById(R.id.stopButton);
        status = findViewById(R.id.status);

        // Initial status
        status.setText("Wallpaper changing stopped");

        // CHANGE WALLPAPER BUTTON
        changeButton.setOnClickListener(v -> {

            // Stop previous timer if already running
            stopTimer();

            // Change wallpaper immediately
            changeWallpaper();

            // Update status
            status.setText("Wallpaper will change every 30 seconds");

            // Create new timer
            timer = new Timer();

            timer.schedule(new TimerTask() {

                @Override
                public void run() {

                    changeWallpaper();

                }

            }, 30000, 30000);

            Toast.makeText(
                    MainActivity.this,
                    "Wallpaper changing started",
                    Toast.LENGTH_SHORT
            ).show();
        });


        // STOP BUTTON
        stopButton.setOnClickListener(v -> {

            stopTimer();

            status.setText("Wallpaper changing stopped");

            Toast.makeText(
                    MainActivity.this,
                    "Wallpaper changing stopped",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }


    private void changeWallpaper() {

        // Generate random wallpaper number
        int randomIndex;

        do {
            randomIndex = random.nextInt(wallpapers.length);

        } while (randomIndex == lastWallpaper && wallpapers.length > 1);

        lastWallpaper = randomIndex;

        // Load image
        Bitmap bitmap = BitmapFactory.decodeResource(
                getResources(),
                wallpapers[randomIndex]
        );

        WallpaperManager wallpaperManager =
                WallpaperManager.getInstance(
                        getApplicationContext()
                );

        try {

            wallpaperManager.setBitmap(bitmap);

            // Show which wallpaper was selected
            int finalRandomIndex = randomIndex;
            new Handler(Looper.getMainLooper()).post(() -> {

                Toast.makeText(
                        MainActivity.this,
                        "Wallpaper " + (finalRandomIndex + 1) + " applied",
                        Toast.LENGTH_SHORT
                ).show();

            });

        } catch (IOException e) {

            e.printStackTrace();

            new Handler(Looper.getMainLooper()).post(() -> {

                Toast.makeText(
                        MainActivity.this,
                        "Failed to change wallpaper",
                        Toast.LENGTH_SHORT
                ).show();

            });
        }
    }


    private void stopTimer() {

        if (timer != null) {

            timer.cancel();
            timer = null;
        }
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();

        stopTimer();
    }
}