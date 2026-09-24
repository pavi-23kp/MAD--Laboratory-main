package com.example.texttospeech;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements TextToSpeech.OnInitListener {

    EditText textInput;
    Button speakButton;

    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        textInput = findViewById(R.id.textInput);
        speakButton = findViewById(R.id.speakButton);

        // Initialize TextToSpeech
        textToSpeech = new TextToSpeech(
                this,
                this
        );

        // Speak button
        speakButton.setOnClickListener(v -> {

            String text =
                    textInput.getText()
                            .toString()
                            .trim();

            if (text.isEmpty()) {

                textInput.setError(
                        "Enter some text"
                );

                textInput.requestFocus();

                return;
            }

            speak(text);
        });
    }


    // TextToSpeech initialization
    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result =
                    textToSpeech.setLanguage(
                            Locale.US
                    );

            if (result ==
                    TextToSpeech.LANG_MISSING_DATA
                    || result ==
                    TextToSpeech.LANG_NOT_SUPPORTED) {

                Toast.makeText(
                        this,
                        "Language not supported",
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                speakButton.setEnabled(true);
            }

        } else {

            Toast.makeText(
                    this,
                    "TextToSpeech initialization failed",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }


    // Convert text to speech
    private void speak(String text) {

        textToSpeech.speak(
                text,
                TextToSpeech.QUEUE_FLUSH,
                null,
                null
        );
    }


    // Release TextToSpeech resources
    @Override
    protected void onDestroy() {

        if (textToSpeech != null) {

            textToSpeech.stop();

            textToSpeech.shutdown();
        }

        super.onDestroy();
    }
}