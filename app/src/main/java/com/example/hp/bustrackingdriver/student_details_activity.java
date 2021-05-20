package com.example.hp.bustrackingdriver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class student_details_activity extends AppCompatActivity {

    TextView tv2,tv4,tv6,tv8,tv10,tv12,tv14;
    ImageView imv1;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_student_details_activity);

        tv2=(TextView)(findViewById(R.id.tv2));
        tv4=(TextView)(findViewById(R.id.tv4));
        tv6=(TextView)(findViewById(R.id.tv6));
        tv8=(TextView)(findViewById(R.id.tv8));
        tv10=(TextView)(findViewById(R.id.tv10));
        tv12=(TextView)(findViewById(R.id.tv12));
        tv14=(TextView)(findViewById(R.id.tv14));
        imv1=(ImageView)(findViewById(R.id.imv1));

        toolbar=(Toolbar)(findViewById(R.id.toolbar));

        toolbar.setTitle("");
        //This will replace toolbar with actionbar
        setSupportActionBar(toolbar);


        Intent incomingintent = getIntent();

        String photo,name,fathername,mothername,fatherphone,motherphone,classs,section;

        Bundle incomingbundle=incomingintent.getExtras();

        photo = incomingbundle.getString("photo");
        name = incomingbundle.getString("studentname");
        fathername = incomingbundle.getString("fathername");
        mothername = incomingbundle.getString("mothername");
        fatherphone = incomingbundle.getString("fatherphone");
        motherphone= incomingbundle.getString("motherphone");
        classs = incomingbundle.getString("classs");
        section = incomingbundle.getString("section");


        Picasso.with(student_details_activity.this).load(GlobalData.host+"/"+photo).into(imv1);

        tv2.setText(name);
        tv4.setText(fathername);
        tv6.setText(mothername);
        tv8.setText(fatherphone);
        tv10.setText(motherphone);
        tv12.setText(classs);
        tv14.setText(section);

    }
}
