package com.zhangjie.trip;

import android.app.Application;
import android.os.Vibrator;

import com.baidu.mapapi.SDKInitializer;
import com.zhangjie.trip.service.LocationService;

/**
 * Created by zhangjie on 2017/4/24.
 */

public class LocationApplication extends Application{
    public LocationService LocationService;
    public Vibrator mVibrator;

    @Override
    public void onCreate() {
        super.onCreate();
        LocationService=new LocationService(getApplicationContext());
        mVibrator= (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
    }
}
