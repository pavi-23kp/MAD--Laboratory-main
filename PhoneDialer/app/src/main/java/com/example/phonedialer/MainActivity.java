package com.example.phonedialer;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    EditText contactName, phoneNumber;
    Button callButton, saveButton;
    TextView status;

    private static final int CALL_PERMISSION_CODE = 100;
    private static final int CONTACT_PERMISSION_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        contactName = findViewById(R.id.contactName);
        phoneNumber = findViewById(R.id.phoneNumber);

        callButton = findViewById(R.id.callButton);
        saveButton = findViewById(R.id.saveButton);

        status = findViewById(R.id.status);


        // ==================================================
        // CALL BUTTON
        // ==================================================

        callButton.setOnClickListener(v -> {

            String number =
                    phoneNumber.getText()
                            .toString()
                            .trim();

            if (number.isEmpty()) {

                phoneNumber.setError(
                        "Enter phone number"
                );

                phoneNumber.requestFocus();

                return;
            }

            makeCall(number);
        });


        // ==================================================
        // SAVE BUTTON
        // ==================================================

        saveButton.setOnClickListener(v -> {

            String name =
                    contactName.getText()
                            .toString()
                            .trim();

            String number =
                    phoneNumber.getText()
                            .toString()
                            .trim();


            // Check name
            if (name.isEmpty()) {

                contactName.setError(
                        "Enter contact name"
                );

                contactName.requestFocus();

                return;
            }


            // Check number
            if (number.isEmpty()) {

                phoneNumber.setError(
                        "Enter phone number"
                );

                phoneNumber.requestFocus();

                return;
            }


            saveContact(name, number);
        });
    }


    // ======================================================
    // MAKE PHONE CALL
    // ======================================================

    private void makeCall(String number) {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
        ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.CALL_PHONE
                    },
                    CALL_PERMISSION_CODE
            );

            return;
        }


        Uri phoneUri = Uri.parse(
                "tel:" + number
        );


        android.content.Intent callIntent =
                new android.content.Intent(
                        android.content.Intent.ACTION_CALL,
                        phoneUri
                );

        startActivity(callIntent);
    }


    // ======================================================
    // SAVE CONTACT
    // ======================================================

    private void saveContact(
            String name,
            String number) {

        // Check CONTACT permission

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_CONTACTS
        ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.WRITE_CONTACTS
                    },
                    CONTACT_PERMISSION_CODE
            );

            return;
        }


        try {

            // ----------------------------------------------
            // Create a new raw contact
            // ----------------------------------------------

            ContentValues rawContactValues =
                    new ContentValues();

            rawContactValues.put(
                    ContactsContract.RawContacts.ACCOUNT_TYPE,
                    (String) null
            );

            rawContactValues.put(
                    ContactsContract.RawContacts.ACCOUNT_NAME,
                    (String) null
            );


            Uri rawContactUri =
                    getContentResolver().insert(
                            ContactsContract.RawContacts.CONTENT_URI,
                            rawContactValues
                    );


            if (rawContactUri == null) {

                Toast.makeText(
                        this,
                        "Unable to create contact",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }


            long rawContactId =
                    ContentUris.parseId(
                            rawContactUri
                    );


            // ----------------------------------------------
            // Add contact name
            // ----------------------------------------------

            ContentValues nameValues =
                    new ContentValues();

            nameValues.put(
                    ContactsContract.Data.RAW_CONTACT_ID,
                    rawContactId
            );

            nameValues.put(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
            );

            nameValues.put(
                    ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                    name
            );


            getContentResolver().insert(
                    ContactsContract.Data.CONTENT_URI,
                    nameValues
            );


            // ----------------------------------------------
            // Add phone number
            // ----------------------------------------------

            ContentValues phoneValues =
                    new ContentValues();

            phoneValues.put(
                    ContactsContract.Data.RAW_CONTACT_ID,
                    rawContactId
            );

            phoneValues.put(
                    ContactsContract.Data.MIMETYPE,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
            );

            phoneValues.put(
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    number
            );

            phoneValues.put(
                    ContactsContract.CommonDataKinds.Phone.TYPE,
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
            );


            getContentResolver().insert(
                    ContactsContract.Data.CONTENT_URI,
                    phoneValues
            );


            // ----------------------------------------------
            // Success
            // ----------------------------------------------

            status.setText(
                    "Contact saved successfully!"
            );

            Toast.makeText(
                    this,
                    name + " saved to Contacts",
                    Toast.LENGTH_SHORT
            ).show();


        } catch (Exception e) {

            e.printStackTrace();

            Toast.makeText(
                    this,
                    "Failed to save contact",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }


    // ======================================================
    // PERMISSION RESULT
    // ======================================================

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );


        // ----------------------------------------------
        // CALL permission
        // ----------------------------------------------

        if (requestCode == CALL_PERMISSION_CODE) {

            if (grantResults.length > 0
                    && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {

                String number =
                        phoneNumber.getText()
                                .toString()
                                .trim();

                if (!number.isEmpty()) {

                    makeCall(number);
                }

            } else {

                Toast.makeText(
                        this,
                        "Call permission denied",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }


        // ----------------------------------------------
        // CONTACT permission
        // ----------------------------------------------

        if (requestCode == CONTACT_PERMISSION_CODE) {

            if (grantResults.length > 0
                    && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {

                String name =
                        contactName.getText()
                                .toString()
                                .trim();

                String number =
                        phoneNumber.getText()
                                .toString()
                                .trim();

                if (!name.isEmpty()
                        && !number.isEmpty()) {

                    saveContact(
                            name,
                            number
                    );
                }

            } else {

                Toast.makeText(
                        this,
                        "Contact permission denied",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }
}