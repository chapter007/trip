package com.zhangjie.trip.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.baidu.mapapi.model.LatLng;
import com.zhangjie.trip.R;

/**
 * Created by zhangjie on 2017/5/2.
 */

public class TripPlanActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "TripActivity";
    private EditText startPoint,endPoint;
    private String stPoint,edPoint;
    private Button bus,bike,walk,car;
    private Intent intent;
    private double mLocation_x,mLocation_y;
    private LatLng choosePt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent=getIntent();
        mLocation_x= intent.getDoubleExtra("Location_x",0);
        mLocation_y= intent.getDoubleExtra("Location_y",0);//获得当前经纬度
        choosePt=intent.getParcelableExtra("choosePt");//获得选中点

        setContentView(R.layout.activity_trip_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startPoint= (EditText) findViewById(R.id.start_point);
        endPoint= (EditText) findViewById(R.id.end_point);
        bus= (Button) findViewById(R.id.bus);
        bike= (Button) findViewById(R.id.bike);
        walk= (Button) findViewById(R.id.walk);
        car= (Button) findViewById(R.id.car);

        if(choosePt!=null){
            endPoint.setText("地图选中点");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intent=new Intent(TripPlanActivity.this,TripRouteActivity.class);

        bus.setOnClickListener(this);
        car.setOnClickListener(this);
        walk.setOnClickListener(this);
        bike.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        stPoint=startPoint.getText().toString();
        edPoint=endPoint.getText().toString();

        intent.putExtra("Location_x",mLocation_x);
        intent.putExtra("Location_y",mLocation_y);
        intent.putExtra("choosePt",choosePt);
        intent.putExtra("startPoint",stPoint);
        intent.putExtra("endPoint",edPoint);
        switch (view.getId()){
            case R.id.bus:
                intent.putExtra("method","bus");
                startActivity(intent);
                break;
            case R.id.car:
                intent.putExtra("method","car");
                startActivity(intent);
                break;
            case R.id.walk:
                intent.putExtra("method","walk");
                startActivity(intent);
                break;
            case R.id.bike:
                intent.putExtra("method","bike");
                startActivity(intent);
                break;
        }
    }
}
