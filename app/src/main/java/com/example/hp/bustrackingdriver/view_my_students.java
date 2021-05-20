package com.example.hp.bustrackingdriver;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class view_my_students extends AppCompatActivity {

    ArrayList<Student>  al=new ArrayList<>();

    ListView lv1;
    myadapter ad;
    TextView tv1;

    JSONArray jsonArray;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_students);

        lv1=(ListView)(findViewById(R.id.lv1));
        tv1=(TextView)(findViewById(R.id.tv1));



        toolbar=(Toolbar)(findViewById(R.id.toolbar));

        toolbar.setTitle("");
        //This will replace toolbar with actionbar
        setSupportActionBar(toolbar);

        toolbar.setTitle("");

        ad=new myadapter();

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
                    jsonArray = jsonmain.getJSONArray("ans");

                    al.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject singleobject = (JSONObject) (jsonArray.get(i));

                        String stuname = singleobject.getString("student_name");
                        String address = singleobject.getString("address");
                        String photo = singleobject.getString("student_photo");

                        al.add(new Student(stuname, address, photo,false));
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ad.notifyDataSetChanged();
                        }
                    });


                } else if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                    // IF 404 NOT FOUND , Show Error
                    Toast.makeText(view_my_students.this, "404 NOT Found", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(view_my_students.this, "Some ERROR Occoured", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /////   Inner Class ///////
    class myadapter extends BaseAdapter
    {
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
            return i*10;
        }

        @Override
        public View getView(final int i, View customView, ViewGroup parent) {

            if(customView==null) {
                LayoutInflater inflater = LayoutInflater.from(view_my_students.this);
                customView = inflater.inflate(R.layout.singlerowstudent, parent, false);
            }

            final Student st = al.get(i);

            TextView tv111,tv222;
            ImageView imv111,imv222;


            tv111 = (TextView) (customView.findViewById(R.id.tv111));
            tv222 = (TextView) (customView.findViewById(R.id.tv222));
            imv111 = (ImageView) (customView.findViewById(R.id.imv111));
            imv222 = (ImageView) (customView.findViewById(R.id.imv222));


            tv1.setText(al.size()+"");

            tv111.setText(st.name);
            tv222.setText(st.address);

            Picasso.with(view_my_students.this).load(GlobalData.host+"/"+st.photo).into(imv111);


            imv222.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Intent in=new Intent(getApplicationContext(),student_details_activity.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("photo",st.photo);
                        bundle.putString("studentname",st.name);
                        bundle.putString("fathername",jsonObject.getString("father_name"));
                        bundle.putString("mothername",jsonObject.getString("mother_name"));
                        bundle.putString("fatherphone",jsonObject.getString("father_phone"));
                        bundle.putString("motherphone",jsonObject.getString("mother_phone"));
                        bundle.putString("classs",jsonObject.getString("class"));
                        bundle.putString("section",jsonObject.getString("section"));

                        in.putExtras(bundle);
                        startActivity(in);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });



            return customView;
        }
    }




}




