package com.example.hp.bustrackingdriver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class mark_students_attendance extends AppCompatActivity {

    ArrayList<Student> al = new ArrayList<>();

    ListView lv1;
    myadapter ad;
    TextView tv2;
    JSONArray jsonArray;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_students_attendance);

        lv1 = (ListView) (findViewById(R.id.lv1));

        tv2 = (TextView) (findViewById(R.id.tv2));


        tv2 = (TextView) findViewById(R.id.tv2);
        final Calendar cal = Calendar.getInstance();
        int dd = cal.get(Calendar.DAY_OF_MONTH);
        int mm = cal.get(Calendar.MONTH);
        int yy = cal.get(Calendar.YEAR);

        tv2.setText(new StringBuilder()
                .append(dd).append(" ").append("-").append(mm + 1).append("-")
                .append(yy));

        toolbar=(Toolbar)(findViewById(R.id.toolbar));

        toolbar.setTitle("");
        //This will replace toolbar with actionbar
        setSupportActionBar(toolbar);

        ad = new myadapter();


        lv1.setAdapter(ad);

        new Thread(new myjob()).start();

    }

    class myjob implements Runnable {
        @Override
        public void run() {
            try {
                // Code to Send Request to Website and Receive JSON Data

                String driverphone;
                driverphone = GlobalData.driverphone;

                //Send Request to Website
                URL url = new URL(GlobalData.host + "/fetch_all_students_from_app?phonenumber=" + driverphone + "");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                //Receive Response from Website
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    // IF 200 OK, Read Incoming Data from Web

                    int size = connection.getContentLength();
                    String datareceived = new String();
                    byte[] buffer = new byte[size];
                    int count=0;
                    while ((count = connection.getInputStream().read(buffer, 0, size)) > 0)
                    { datareceived += new String(buffer, 0, count); }



                    //Parse JSON Data
                    JSONObject jsonmain = new JSONObject(datareceived);
                    jsonArray = null;
                    jsonArray = jsonmain.getJSONArray("ans");



                    java.sql.Date date=new java.sql.Date(System.currentTimeMillis());
                    al.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject singleobject = (JSONObject) (jsonArray.get(i));

                        String stuname = singleobject.getString("student_name");
                        String address = singleobject.getString("address");
                        String photo = singleobject.getString("student_photo");

                        boolean b1=singleobject.getBoolean("b");


                        al.add(new Student(stuname, address, photo,b1));
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ad.notifyDataSetChanged();
                        }
                    });


                } else if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                    // IF 404 NOT FOUND , Show Error
                    Toast.makeText(mark_students_attendance.this, "404 NOT Found", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(mark_students_attendance.this, "Some ERROR Occoured", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /////   Inner Class ///////
    class myadapter extends BaseAdapter {
        @Override
        public int getCount() {
            return al.size();
        }

        @Override
        public Object getItem(int i) {
            return al.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i * 10;
        }

        @Override
        public View getView(final int i, View customView, ViewGroup parent) {

            if (customView == null) {
                LayoutInflater inflater = LayoutInflater.from(mark_students_attendance.this);
                customView = inflater.inflate(R.layout.single_row_for_student_attendance, parent, false);
            }

            final Student st = al.get(i);

            TextView tv111, tv222;
            ImageView imv111;


            tv111 = (TextView) (customView.findViewById(R.id.tv111));
            tv222 = (TextView) (customView.findViewById(R.id.tv222));
            imv111 = (ImageView) (customView.findViewById(R.id.imv111));
            final CheckBox rb = (CheckBox) (customView.findViewById(R.id.rb));

            tv111.setText(st.name);
            tv222.setText(st.address);

            Picasso.with(mark_students_attendance.this).load(GlobalData.host + "/" + st.photo).into(imv111);


                                 Log.d("MYMSG","state "+st.b);
            rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (rb.isChecked()) {

                        try {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    // Code to Send Request to Website and Receive JSON Data
                                      try {
                                          String driverphone;
                                          driverphone = GlobalData.driverphone;

                                          JSONObject jsonObject = jsonArray.getJSONObject(i);
                                          String roll_no=jsonObject.getString("roll_no");
                                          //Send Request to Website
                                          URL url = new URL(GlobalData.host +"/mark_student_attendance_from_app?phonenumber="+ driverphone+"&rollnum="+roll_no);
                                          HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                                          //Receive Response from Website
                                          if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
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

                                                      if(datareceived.trim().equals("SUCCESS"))
                                                      {
                                                          al.get(i).b=true;
                                                          runOnUiThread(new Runnable() {
                                                              @Override
                                                              public void run() {
                                                                  Toast.makeText(mark_students_attendance.this, "Marked Present Successfully", Toast.LENGTH_LONG).show();

                                                              }
                                                          });

                                                      }

                                                  }
                                              });


                                          } else if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                                              // IF 404 NOT FOUND , Show Error
                                              Toast.makeText(mark_students_attendance.this, "404 NOT Found", Toast.LENGTH_SHORT).show();

                                          } else {
                                              Toast.makeText(mark_students_attendance.this, "Some ERROR Occoured", Toast.LENGTH_SHORT).show();
                                          }

                                      }
                                      catch(Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }



                    else{

                        try {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    // Code to Send Request to Website and Receive JSON Data
                                    try {

                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String roll_no=jsonObject.getString("roll_no");
                                        //Send Request to Website
                                        URL url = new URL(GlobalData.host +"/delete_marked_student_attendance_from_app?rollnum="+roll_no);
                                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                                        //Receive Response from Website
                                        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
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

                                                    if(datareceived.trim().equals("SUCCESS"))
                                                    {
                                                        al.get(i).b = false;
                                                        Toast.makeText(mark_students_attendance.this, "Marked Absent Successfully", Toast.LENGTH_LONG).show();

                                                    }

                                                }
                                            });


                                        } else if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                                            // IF 404 NOT FOUND , Show Error
                                            Toast.makeText(mark_students_attendance.this, "404 NOT Found", Toast.LENGTH_SHORT).show();

                                        } else {
                                            Toast.makeText(mark_students_attendance.this, "Some ERROR Occoured", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                    catch(Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                }
            });
            rb.setChecked(st.b);
            return customView;
        }
    }

}