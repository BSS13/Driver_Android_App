package com.example.hp.bustrackingdriver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class launcherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        Thread t=new Thread(new myjob());
        t.start();



  }

    class myjob implements Runnable {
        @Override
        public void run() {

            try
            {
                Thread.sleep(4000);
                Intent in=new Intent(launcherActivity.this,MainActivity.class);
                startActivity(in);

            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }


        }

    }
}
