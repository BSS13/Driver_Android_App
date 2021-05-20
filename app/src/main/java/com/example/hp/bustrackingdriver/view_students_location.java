package com.example.hp.bustrackingdriver;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import java.util.Calendar;

public class view_students_location extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener

{
    private GoogleMap mMap;
    Double lat, lng, lat2, lng2, distancerounded;
    Marker m;

    JSONArray jsonArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students_location);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);


        ////////   Logic to get CURRENT LOCATIONS /////////////////
        /// //---check if GPS_PROVIDER is enabled---

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

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }



        class mylocationlistener implements LocationListener {

            public void onLocationChanged(Location location) {


                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.busicon6666);

                lat = location.getLatitude();
                lng = location.getLongitude();



                LatLng mylocation = new LatLng(lat, lng);

                if (m != null) {
                    m.remove();
                }
                MarkerOptions markerOptions = new MarkerOptions().position(mylocation).title("Yours Location").icon(icon);
                ;
                m = mMap.addMarker(markerOptions);


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

        googleMap.setOnMarkerClickListener(this);
        mMap = googleMap;
        new Thread(new Runnable()
        {
            @Override
            public void run() {


                try {


                        //Send Request to Website
                        URL url = new URL(GlobalData.host + "/fetch_student_home_location_from_app?phonenumber=" + GlobalData.driverphone);
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
Log.d("MYMSG",datareceived);
                            JSONObject jsonmain = new JSONObject(datareceived);

                            jsonArray = jsonmain.getJSONArray("ans");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                final JSONObject singleobject = (JSONObject) (jsonArray.get(i));

                                final int j=i;
                                final Double lat3 = Double.parseDouble(singleobject.getString("latitude"));
                                final Double lng3 = Double.parseDouble(singleobject.getString("longitude"));
                                final String roll_number=singleobject.getString("roll_no");
                                final LatLng mylocationnew = new LatLng(lat3, lng3);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                         MarkerOptions markerOptionsin = new MarkerOptions().position(mylocationnew).title(j+"");
                                         mMap.addMarker(markerOptionsin);
//                                         mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat3,lng3),15));

                                     }
                                });

                            }





                        } else if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                            // IF 404 NOT FOUND , Show Error
                            Toast.makeText(getApplicationContext(), "404 NOT Found", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Some ERROR Occoured", Toast.LENGTH_SHORT).show();
                        }


                }catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }).start();
        // Add polylines and polygons to the map. This section shows just
        // a single polyline. Read the rest of the tutorial to learn more.

        // Position the map's camera near Alice Springs in the center of Australia,
        // and set the zoom factor so most of Australia shows on the screen.

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.6340, 74.8723), 16));

        // Set listeners for click events.
//            googleMap.setOnPolylineClickListener(this);
//            googleMap.setOnPolygonClickListener(this);

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(view_students_location.this, marker.getTitle(), Toast.LENGTH_SHORT).show();
                if(!marker.getTitle().equals("Yours Location"))
                {

                    try {
                        Intent in=new Intent(getApplicationContext(),student_details.class);
                        Bundle bundle=new Bundle();
                        JSONObject singleobject =(JSONObject) jsonArray.get(Integer.parseInt(marker.getTitle()));



                        bundle.putString("photo",singleobject.getString("student_photo"));
                        bundle.putString("roll",singleobject.getString("roll_no"));
                        bundle.putString("sname",singleobject.getString("student_name"));
                        bundle.putString("fname",singleobject.getString("father_name"));
                        bundle.putString("mname",singleobject.getString("mother_name"));
                        bundle.putString("add",singleobject.getString("address"));
                        bundle.putString("lat",singleobject.getString("latitude"));
                        bundle.putString("lng",singleobject.getString("longitude"));

                        in.putExtras(bundle);
                        startActivity(in);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                return true;
            }
        });


    }




}



