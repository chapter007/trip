package com.zhangjie.trip.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        startPoint= (EditText) findViewById(R.id.start_point);
        endPoint= (EditText) findViewById(R.id.end_point);
        bus= (Button) findViewById(R.id.bus);
        bike= (Button) findViewById(R.id.bike);
        walk= (Button) findViewById(R.id.walk);
        car= (Button) findViewById(R.id.car);


    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        stPoint=startPoint.getText().toString();
        edPoint=endPoint.getText().toString();

        intent=new Intent(TripPlanActivity.this,TripRouteActivity.class);
        intent.putExtra("startPoint",stPoint);
        intent.putExtra("endPoint",edPoint);

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
