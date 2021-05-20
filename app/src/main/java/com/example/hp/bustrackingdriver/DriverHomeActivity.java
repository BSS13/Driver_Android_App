package com.example.hp.bustrackingdriver;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import static android.content.Context.MODE_PRIVATE;


public class DriverHomeActivity extends AppCompatActivity {

    TextView tv1;
    Toolbar toolbar;



    DrawerLayout mDrawerLayout;

    CarouselView carouselView;

    int[] sampleImages = {R.drawable.school, R.drawable.ponee, R.drawable.ptwoo, R.drawable.pthreee,R.drawable.picthree};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_driver_home);



        toolbar=(Toolbar)(findViewById(R.id.toolbar8));
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        //toolbar.setTitle("NEW TITLE");
        //toolbar.setBackgroundColor(Color.YELLOW);
        //toolbar.setNavigationIcon(R.drawable.burger);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

// Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null)
        {
            supportActionBar.setTitle("Drawer Demo");
            supportActionBar.setHomeAsUpIndicator(R.drawable.burger);
            supportActionBar.setDisplayHomeAsUpEnabled(true);


            }
        else
        {
        }


        tv1=(TextView)(findViewById(R.id.tv1));



        TextView marque = (TextView)(findViewById(R.id.marque_scrolling_text));
        marque.setSelected(true);


        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);

        carouselView.setImageListener(imageListener);


        SharedPreferences pref=getSharedPreferences("driverlogin.txt",MODE_PRIVATE);

        String phonenumber=pref.getString("phonenumber",null);
        String drivername=pref.getString("drivername",null);

        GlobalData.drivername=drivername;
        GlobalData.driverphone=phonenumber;



//        toolbar.setTitle("hello..........");


        //This will replace toolbar with actionbar



// Set behavior of Navigation drawer

        navigationView.setNavigationItemSelectedListener
                (
                        new NavigationView.OnNavigationItemSelectedListener()
                        {
                            // This method will trigger on item Click of navigation menu
                            @Override
                            public boolean onNavigationItemSelected(MenuItem menuItem) {
                                // Set item in checked state
                                // menuItem.setChecked(true);
                                // TODO: handle navigation
                                // Closing drawer on item click

                                if(menuItem.getItemId()==R.id.m11)
                                {
                                    // Toast.makeText(MyToolbarWithDrawerLayout.this, "Option 1 Clicked", Toast.LENGTH_SHORT).show();

                                    Intent in=new Intent(DriverHomeActivity.this,view_my_students.class);
                                    startActivity(in);
                                }
                                else  if(menuItem.getItemId()==R.id.m22)
                                {
                                    // Toast.makeText(MyToolbarWithDrawerLayout.this, "Option 1 Clicked", Toast.LENGTH_SHORT).show();
                                    Intent in=new Intent(DriverHomeActivity.this,mark_students_attendance.class);
                                    startActivity(in);

                                }
                                else if(menuItem.getItemId()==R.id.m33)
                                {
                                    // Toast.makeText(MyToolbarWithDrawerLayout.this, "Option 1 Clicked", Toast.LENGTH_SHORT).show();

                                    Intent in=new Intent(DriverHomeActivity.this, MarkSchoolEntryExit.class);
                                    startActivity(in);
                                }

                                else if(menuItem.getItemId()==R.id.m44)
                                {
                                    // Toast.makeText(MyToolbarWithDrawerLayout.this, "Option 1 Clicked", Toast.LENGTH_SHORT).show();

                                    Intent in=new Intent(DriverHomeActivity.this,view_students_location.class);
                                    startActivity(in);
                                }

                                else if(menuItem.getItemId()==R.id.m55)
                                {
                                    // Toast.makeText(MyToolbarWithDrawerLayout.this, "Option 1 Clicked", Toast.LENGTH_SHORT).show();
                                    Intent in=new Intent(DriverHomeActivity.this,breakdown_alert_message.class);
                                    startActivity(in);
                                }

                                else if(menuItem.getItemId()==R.id.m66)
                                {
                                    // Toast.makeText(MyToolbarWithDrawerLayout.this, "Option 1 Clicked", Toast.LENGTH_SHORT).show();
                                    Intent sendIntent = new Intent();
                                    sendIntent.setAction(Intent.ACTION_SEND);
                                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                                            "Hey check out my app at: https://play.WJ.com/store/apps/details?id=com.google.android.apps.plus");
                                    sendIntent.setType("text/plain");
                                    startActivity(sendIntent);
                                }

                                else if(menuItem.getItemId()==R.id.m77)
                                {
                                    // Toast.makeText(MyToolbarWithDrawerLayout.this, "Option 1 Clicked", Toast.LENGTH_SHORT).show();
                                    Intent in=new Intent(DriverHomeActivity.this,MyService.class);
                                    stopService(in);

                                    SharedPreferences sharedPreferences=getSharedPreferences("driverlogin.txt",MODE_PRIVATE);
                                    sharedPreferences.edit().remove("phonenumber").commit();
                                    sharedPreferences.edit().remove("drivernmame").commit();

                                    finish();
                                    Intent innew=new Intent(DriverHomeActivity.this,MainActivity.class);
                                    startActivity(innew);


                                }

                                //Logic
                                mDrawerLayout.closeDrawers();
                                return true;
                            }
                        });




    //now we can set title of toolbar like we do of actionbar
        //getSupportActionBar().setTitle("Welcome "+GlobalData.drivername);

        tv1.setText("Welcome :"+GlobalData.drivername);
        // OR Some Phones
        // toolbar.setTitle("Hello from TOOLBAR");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drivermenustyle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id== android.R.id.home)
        {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }

        if(id==R.id.changepass)
        {
          Intent in4=new Intent(DriverHomeActivity.this,ChangePasswordDriver.class);
          startActivity(in4);

        }

        if(id==R.id.logout)
        {
            Intent in=new Intent(DriverHomeActivity.this,MyService.class);
            stopService(in);

            SharedPreferences sharedPreferences=getSharedPreferences("driverlogin.txt",MODE_PRIVATE);
            sharedPreferences.edit().remove("phonenumber").commit();
            sharedPreferences.edit().remove("drivernmame").commit();

            finish();
            Intent innew=new Intent(DriverHomeActivity.this,MainActivity.class);
            startActivity(innew);


        }



        return super.onOptionsItemSelected(item);
    }







    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };





}
