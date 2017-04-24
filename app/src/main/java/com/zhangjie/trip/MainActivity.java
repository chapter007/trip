package com.zhangjie.trip;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.zhangjie.trip.service.LocationService;

import static android.R.attr.type;

public class MainActivity extends AppCompatActivity {
    private static final String TAG ="zhangjie map";
    private MapView myMap;
    private FloatingActionButton fab;
    private LocationClient mLocationClient=null;
    private BDLocationListener mListener=new MyLocationListener();
    private LocationService mLocationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myMap= (MapView) findViewById(R.id.bmapView);

        //fab
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
        mLocationService= ((LocationApplication) getApplication()).LocationService;
        mLocationService.registerListener(mListener);
        if (type == 0) {
            mLocationService.setLocationOption(mLocationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            mLocationService.setLocationOption(mLocationService.getOption());
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLocationService.start();
                Toast.makeText(MainActivity.this,"start locate",Toast.LENGTH_SHORT).show();
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mLocationService.stop();
                Toast.makeText(MainActivity.this,"stop locate",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myMap.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        myMap.onResume();
        Log.i(TAG, "onResume: ");
    }
}
