package com.example.hp.bustrackingdriver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import android.util.Log;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;

public class MyService extends Service {
    int i;

    boolean RUNNINGFLAG = false;

    public MyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

                LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

                /////////////////   GET LAST GPS AND NW LOCTIONS if Available  ///////////


                //---check if GPS_PROVIDER is enabled---

                boolean gpsStatus = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);


                //---check if NETWORK_PROVIDER is enabled---

                boolean networkStatus = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


                mylocationlistener mylocationlistenerobj = new mylocationlistener();


                // check which provider is enabled

                if (gpsStatus == false && networkStatus == false)

                {

                    Toast.makeText(getApplicationContext(), "Both GPS and Newtork are disabled", Toast.LENGTH_LONG).show();


                    //---display the "Location services" settings page---

                    Intent in = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                    startActivity(in);

                }
                if (gpsStatus == true)
                {

                    Toast.makeText(getApplicationContext(), "GPS is Enabled, using it", Toast.LENGTH_LONG).show();

                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 120000, 0, mylocationlistenerobj);

                }


                if (networkStatus == true)

                {

                    Toast.makeText(getApplicationContext(), "Network Location is Enabled, using it", Toast.LENGTH_LONG).show();

                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 120000, 0, mylocationlistenerobj);

                }

                return START_STICKY;
            }


            ///// Inner Class  //////////////////

            class mylocationlistener implements LocationListener {

                public void onLocationChanged(Location location) {

                    final double lat, lon;

                     lat = location.getLatitude();

                    lon = location.getLongitude();

                    SharedPreferences pref=getSharedPreferences("driverlogin.txt",MODE_PRIVATE);

                    final String phonenumber=pref.getString("phonenumber",null);



                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try
                            {
                                // Code to Send Request to Website and Receive JSON Data

                                //Send Request to Website
                                URL url = new URL(GlobalData.host+"/driver_location_save_from_app?phonenumber="+phonenumber+"&latitude="+lat+"&longitude="+lon);
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


                                    Handler h = new Handler(getApplicationContext().getMainLooper());
                                // Although you need to pass an appropriate context
                                    h.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(datareceived.trim().equals("SUCCESS"))
                                            {
                                                Toast.makeText(MyService.this, "SAVED", Toast.LENGTH_LONG).show();

                                            }
                                            else
                                            {
                                                Toast.makeText(MyService.this, "Not Saved", Toast.LENGTH_LONG).show();


                                            }

                                        }
                                    });



                                }
                                else  if(connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND)
                                {
                                    // IF 404 NOT FOUND , Show Error
                                    Toast.makeText(MyService.this, "404 NOT Found", Toast.LENGTH_SHORT).show();

                                }
                                else
                                {
                                    Toast.makeText(MyService.this, "Some ERROR Occoured", Toast.LENGTH_SHORT).show();
                                }

                            }
                            catch(Exception ex)
                            {
                                ex.printStackTrace();
                            }
                        }
                    }).start();



                }


                public void onProviderDisabled(String provider)

                {


                }


                public void onProviderEnabled(String provider)

                {


                }


                public void onStatusChanged(String provider, int status, Bundle extras)

                {


                }

            }

}




