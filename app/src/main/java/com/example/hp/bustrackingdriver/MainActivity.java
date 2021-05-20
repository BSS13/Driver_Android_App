package com.example.hp.bustrackingdriver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText et1,et2;
    Button bt1;
    Toolbar toolbar;
    TextView pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bt1=(Button)(findViewById(R.id.bt1));
        et1=(EditText) (findViewById(R.id.et1));
        et2=(EditText) (findViewById(R.id.et2));
        pass=(TextView)(findViewById(R.id.pass));

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent();
                in.setClass(MainActivity.this,Forgot_Password_Driver.class);
                startActivity(in);
            }
        });

        toolbar=(Toolbar)(findViewById(R.id.toolbar));

        toolbar.setTitle("");



        //This will replace toolbar with actionbar
        setSupportActionBar(toolbar);


        SharedPreferences pref=getSharedPreferences("driverlogin.txt",MODE_PRIVATE);

        String phonenumber=pref.getString("phonenumber",null);

        if(phonenumber==null || phonenumber.equals("") ){

        }

        else{
            Intent in=new Intent(this,DriverHomeActivity.class);
            startActivity(in);
            finish();
        }

       go6();

    }




    public void go6()

    {

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M)

        {

            //Check If Permissions are already granted, otherwise show Ask Permission Dialog

            if(checkPermission())

            {

                Toast.makeText(this, "All Permissions Already Granted", Toast.LENGTH_SHORT).show();

            }

            else

            {

                Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();

                requestPermission();

            }

        }

    }



    public boolean checkPermission()

    {

        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED;

        boolean result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;

        boolean result3 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED;

        boolean result4 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED;

        boolean result5 = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)== PackageManager.PERMISSION_GRANTED;


        return result1 && result2 && result3 && result4 && result5;

    }



    public void requestPermission()

    {

        //Show ASK FOR PERSMISSION DIALOG (passing array of permissions that u want to ask)

        ActivityCompat.requestPermissions(this,

                new String[]{Manifest.permission.CALL_PHONE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.RECEIVE_SMS}, 1);

    }



    // After User Selects Desired Permissions, thid method is automatically called

    // It has request code, permissions array and corresponding grantresults array

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)

    {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);



        if(requestCode==1)

        {

            if(grantResults.length>0)

            {



                if(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED && grantResults[2]==PackageManager.PERMISSION_GRANTED && grantResults[3]==PackageManager.PERMISSION_GRANTED && grantResults[4]==PackageManager.PERMISSION_GRANTED)

                {

                    Toast.makeText(this, "All PERMISSON GRANTED", Toast.LENGTH_SHORT).show();

                }

                if(grantResults[0]==PackageManager.PERMISSION_DENIED && grantResults[1]==PackageManager.PERMISSION_DENIED)

                {

                    Toast.makeText(this, "All Permission Denied", Toast.LENGTH_SHORT).show();

                }

            }

        }



    }






    public void go(View v) {

           if(et1.getText().toString().equals("") ||  et1.getText().toString().equals(""))
           {
              Toast.makeText(this,"Please Enter Complete Details",Toast.LENGTH_LONG).show();
           }
          else {
               new Thread(new myjob()).start();
           }
    }
        /////  Inner Class //////
        class myjob implements Runnable
        {
            @Override
            public void run()
            {
                try
                {
                    // Code to Send Request to Website and Receive JSON Data

                    String m,p;
                    m=et1.getText().toString().trim();
                    p=et2.getText().toString().trim();

                    //Send Request to Website
                    URL url = new URL(GlobalData.host+"/driver_login_from_app?phonenumber="+m+"&password="+p+"");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    //Receive Response from Website
                    if(connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                    {
                        // IF 200 OK, Read Incoming Data from Web

                        int size =  connection.getContentLength() ;
                        byte b[]=new byte[size];

                        // Read all data and fill in byte array
                        connection.getInputStream().read(b,0,size);

                        // Convert to String
                        final String datareceived = new String(b);


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // / Toast.makeText(Connect_Mobile_With_Web3.this, datareceived, Toast.LENGTH_SHORT).show();

                                if(datareceived.trim().equals("FAILURE"))
                                {
                                    Toast.makeText(MainActivity.this, "Wrong Credentials", Toast.LENGTH_LONG).show();

                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this, "Login Successfull", Toast.LENGTH_LONG).show();

                                    SharedPreferences pref=getSharedPreferences("driverlogin.txt",MODE_PRIVATE);
                                    SharedPreferences.Editor editor=pref.edit();

                                    editor.putString("phonenumber",et1.getText().toString());
                                    editor.putString("drivername",datareceived.trim());

                                    editor.commit();

                                    Toast.makeText(MainActivity.this, "Details Saved", Toast.LENGTH_SHORT).show();

                                    Intent in=new Intent(MainActivity.this,DriverHomeActivity.class);
                                    startActivity(in);

                                    Intent in1=new Intent(MainActivity.this,MyService.class);
                                    startService(in1);

                                }


                            }
                        });

                    }
                    else  if(connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND)
                    {
                        // IF 404 NOT FOUND , Show Error
                        Toast.makeText(MainActivity.this, "404 NOT Found", Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Some ERROR Occoured", Toast.LENGTH_SHORT).show();
                    }

                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }
        ////////////////////////
    }

