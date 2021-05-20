package com.example.hp.bustrackingdriver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.bustrackingdriver.GlobalData;
import com.example.hp.bustrackingdriver.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.BatchUpdateException;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class breakdown_alert_message extends AppCompatActivity implements OnMapReadyCallback

{
    private GoogleMap mMap;
    Double lat, lng;
    Marker m;
    EditText etdate,ettime;
    EditText etmsg;
    Button bt1;
    Date date;
    Time time;
    String datareceived;
    JSONArray jsonArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakdown_alert_message);

        etdate=(EditText)(findViewById(R.id.etdate));
        ettime=(EditText)(findViewById(R.id.ettime));
        etmsg=(EditText)(findViewById(R.id.etmsg));
        bt1=(Button)(findViewById(R.id.bt1));

        time=new Time(System.currentTimeMillis());

        long times = System.currentTimeMillis();
        java.sql.Date tdate = new java.sql.Date(times);

        String edate=tdate.toString();
        String etime=time.toString();

        etdate.setText(edate);
        ettime.setText(etime);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new myjob()).start();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);


        ////////   Logic to get CURRENT LOCATIONS /////////////////


        //---check if GPS_PROVIDER is enabled---

        boolean gpsStatus = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);


        //---check if NETWORK_PROVIDER is enabled---

        boolean networkStatus = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


        mylocationlistener mylocationlistenerobj = new mylocationlistener();

        // check which provider is enabled

        if (gpsStatus == false && networkStatus == false) {

            Toast.makeText(this, "Both GPS and Newtork are disabled", Toast.LENGTH_LONG).show();

            //---display the "Location services" settings page---

            Intent in = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);

            startActivity(in);
        }


        if (gpsStatus == true) {

            Toast.makeText(this, "GPS is Enabled, using it", Toast.LENGTH_LONG).show();

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mylocationlistenerobj);

        }


        if (networkStatus == true) {

            Toast.makeText(this, "Network Location is Enabled, using it", Toast.LENGTH_LONG).show();

            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mylocationlistenerobj);

        }

    }

    class myjob implements Runnable {
        @Override
        public void run() {
            try {
                // Code to Send Request to Website and Receive JSON Data

                String cdate,ctime;
                cdate = etdate.getText().toString().trim();
                ctime = ettime.getText().toString().trim();

                Log.d("MYMSG",cdate+" "+ctime);

                String msg = etmsg.getText().toString();

                msg = msg.replace(" ","%20");

                //Send Request to Website
                URL url = new URL(GlobalData.host + "/alert_message_from_app?driverphone="+GlobalData.driverphone+"&lat="+lat+"&lng="+lng+"&date="+cdate+"&time="+ctime+"&message="+msg);

                Log.d("MYMSG",url.toString());

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                Log.d("MYMSG",connection.getResponseCode()+" code");
                //Receive Response from Website
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    // IF 200 OK, Read Incoming Data from Web

                    int size = connection.getContentLength();
                    datareceived = new String();
                    byte[] buffer = new byte[size];
                    int count=0;
                    while ((count = connection.getInputStream().read(buffer, 0, size)) > 0)
                    { datareceived += new String(buffer, 0, count); }


                    Log.d("MYMSG",datareceived+" output");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // / Toast.makeText(Connect_Mobile_With_Web3.this, datareceived, Toast.LENGTH_SHORT).show();

                            if (datareceived.trim().contains("SUCCESS")) {
                                Toast.makeText(breakdown_alert_message.this, "Message Sent", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(breakdown_alert_message.this, "Message Not Sent", Toast.LENGTH_LONG).show();



                            }


                        }
                    });

                } else if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                    // IF 404 NOT FOUND , Show Error
                    Toast.makeText(breakdown_alert_message.this, "404 NOT Found", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(breakdown_alert_message.this, "Some ERROR Occoured", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

        class mylocationlistener implements LocationListener {

            public void onLocationChanged(Location location) {


                lat = location.getLatitude();
                lng = location.getLongitude();


                LatLng mylocation = new LatLng(lat, lng);

                if (m != null) {
                    m.remove();
                }
                MarkerOptions markerOptions = new MarkerOptions().position(mylocation).title("Bus's Location");

                m = mMap.addMarker(markerOptions);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),15));


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        }


        // More code goes here, including the onCreate() method described above.

        @Override
        public void onMapReady(final GoogleMap googleMap) {

            mMap = googleMap;



            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.6340, 74.8723), 15));


        }



}



