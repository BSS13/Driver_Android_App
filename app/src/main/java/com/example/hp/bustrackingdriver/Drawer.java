package com.example.hp.bustrackingdriver;

import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

public class Drawer extends AppCompatActivity
{
    Toolbar toolbar;

    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drawer);

        // setup toolbar with Tabs
        toolbar=(Toolbar)(findViewById(R.id.toolbar));
        setSupportActionBar(toolbar);


        //getSupportActionBar().setTitle("Hello World");

        //toolbar.setTitle("Hello World");
        toolbar.setSubtitle("How are you");

        // Create Navigation drawer and inflate layout
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

// Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null)
        {
            supportActionBar.setTitle("Drawer Demo");
            supportActionBar.setHomeAsUpIndicator(R.drawable.hamburger);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

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
                                    FragmentManager frm = getSupportFragmentManager();
                                    FragmentTransaction frt = frm.beginTransaction();



                                    frt.commit();
                                }
                                else  if(menuItem.getItemId()==R.id.m22)
                                {
                                    // Toast.makeText(MyToolbarWithDrawerLayout.this, "Option 1 Clicked", Toast.LENGTH_SHORT).show();
                                    FragmentManager frm = getSupportFragmentManager();
                                    FragmentTransaction frt = frm.beginTransaction();



                                    frt.commit();
                                }
                                else if(menuItem.getItemId()==R.id.m33)
                                {
                                    // Toast.makeText(MyToolbarWithDrawerLayout.this, "Option 1 Clicked", Toast.LENGTH_SHORT).show();
                                    FragmentManager frm = getSupportFragmentManager();
                                    FragmentTransaction frt = frm.beginTransaction();



                                    frt.commit();
                                }

                                //Logic
                                mDrawerLayout.closeDrawers();
                                return true;
                            }
                        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mytoolbarmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(item.getTitle()!=null)
            Log.d("MYMESSAGE",item.getTitle().toString());

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            Toast.makeText(this, "Setting Clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(id== android.R.id.home)
        {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }


        return super.onOptionsItemSelected(item);
    }



}
