package com.example.hp.bustrackingdriver;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;


import android.content.Intent;
import android.content.SharedPreferences;



import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MarkSchoolEntryExit extends AppCompatActivity {

    String type;

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_school_entry_exit);

        toolbar=(Toolbar)(findViewById(R.id.toolbar));

        toolbar.setTitle("");

        setSupportActionBar(toolbar);


    }

    public void go(View v) {
        type = "ENTERED";


        AlertDialog.Builder adb=new AlertDialog.Builder(this);
        adb.setTitle("School Entry Exit Alert Dialog");
        adb.setIcon(R.drawable.logo2);
        adb.setMessage("Are You Sure You Want To Mark Entry For School Entry?");
        adb.setCancelable(false);

        //Buttons
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new Thread(new job()).start();
            }
        });

        adb.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });


        AlertDialog ad=adb.create();
        ad.show();



    }


    public void go2(View v)
    {
        type="EXIT";
        AlertDialog.Builder adb=new AlertDialog.Builder(this);
        adb.setTitle("School Entry Exit Alert Dialog");
        adb.setIcon(R.drawable.logo2);
        adb.setMessage("Are You Sure You Want To Mark Entry For School Exit?");
        adb.setCancelable(false);

        //Buttons
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new Thread(new job()).start();
            }
        });

        adb.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });


        AlertDialog ad=adb.create();
        ad.show();



    }



    class job implements Runnable
    {

        @Override
        public void run() {

            try {


                URL url = new URL(GlobalData.host + "/mark_entry_exit_from_app?phonenumber=" + GlobalData.driverphone + "&type=" + type);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                //Receive Response from Website
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    // IF 200 OK, Read Incoming Data from Web

                    int size = connection.getContentLength();
                    byte b[] = new byte[size];

                    // Read all data and fill in byte array
                    connection.getInputStream().read(b, 0, size);

                    // Convert to String
                    final String datareceived = new String(b);


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // / Toast.makeText(Connect_Mobile_With_Web3.this, datareceived, Toast.LENGTH_SHORT).show();

                            if (datareceived.trim().equals("FAILURE")) {
                                Toast.makeText(MarkSchoolEntryExit.this, "Already Marked", Toast.LENGTH_LONG).show();

                            }
                            if (datareceived.trim().equals("SUCCESS"))
                                {
                                Toast.makeText(MarkSchoolEntryExit.this, "Status Recorded", Toast.LENGTH_LONG).show();
                               }


                        }
                    });

                } else if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                    // IF 404 NOT FOUND , Show Error
                    Toast.makeText(MarkSchoolEntryExit.this, "404 NOT Found", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MarkSchoolEntryExit.this, "Some ERROR Occoured", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }
}

