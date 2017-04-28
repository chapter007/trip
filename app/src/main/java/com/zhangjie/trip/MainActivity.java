package com.zhangjie.trip;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.zhangjie.trip.service.LocationService;
import com.zhangjie.trip.utils.Utils;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "zhangjie map";
    private static final int UPDATE_LOCATE =0;
    private static final int BAIDU_READ_PHONE_STATE =100;
    private MapView myMap;
    private FloatingActionButton fab;
    private LocationClient mLocationClient = null;
    private LocationService mLocationService;
    private LocationManager locationManager;
    private Context mContext;
    private BaiduMap mBaiduMap;


    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATE_LOCATE:
                    Utils.makeToast(getApplicationContext(),msg.getData().getString("location"));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myMap = (MapView) findViewById(R.id.bmapView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        mContext=getApplicationContext();
        mBaiduMap=myMap.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient=new LocationClient(mContext);
        mLocationClient.registerLocationListener(mListener);
        checkPermission();

        //开始定位
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
        mLocationClient.start();

    }

    private void checkPermission(){
        // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(mContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED&&mContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {

                requestPermissions( new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
                        ,Manifest.permission.ACCESS_FINE_LOCATION},BAIDU_READ_PHONE_STATE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //获取到权限
                    Utils.makeToast(mContext,"获得了权限");
                }else{
                    Utils.makeToast(mContext,"未获取到权限");
                    finish();
                }
                break;
            default:
                break;
        }
    }

    private void initLocate(){
        //这个服务有用到吗？并没有
        mLocationService= ((LocationApplication) getApplication()).LocationService;
        mLocationService.registerListener(mListener);
        mLocationService.setLocationOption(mLocationService.getDefaultLocationClientOption());
    }

    @Override
    protected void onStart() {
        super.onStart();
        fab.setVisibility(View.GONE);
        //initLocate();
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mLocationService.start();
                Toast.makeText(MainActivity.this,"start locate",Toast.LENGTH_SHORT).show();
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //mLocationService.stop();
                Toast.makeText(MainActivity.this,"stop locate",Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/
    }

    public void sendLocation(final String location){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg=new Message();
                msg.what=UPDATE_LOCATE;
                Bundle bundle=new Bundle();
                bundle.putString("location",location);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }
        }).start();
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

    private double mCurrentLat=0.0;
    private double mCurrentLon=0.0;
    private float mCurrentAccracy;
    private MyLocationData locData;
    private float mCurrentDirection=0;
    private boolean isFirstLoc=true;

    private BDLocationListener mListener=new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || myMap == null) {
                return;
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }

            //getLocationData(location);
        }
    };

    private void getLocationData(BDLocation location) {
        StringBuffer sb=new StringBuffer(256);
        sb.append("time:");
        sb.append(location.getTime());
        sb.append("\nerror code : ");
        sb.append(location.getLocType());    //获取类型类型

        sb.append("\nlatitude : ");
        sb.append(location.getLatitude());    //获取纬度信息

        sb.append("\nlontitude : ");
        sb.append(location.getLongitude());    //获取经度信息

        sb.append("\nradius : ");
        sb.append(location.getRadius());    //获取定位精准度

        if (location.getLocType() == BDLocation.TypeGpsLocation){

            // GPS定位结果
            sb.append("\nspeed : ");
            sb.append(location.getSpeed());    // 单位：公里每小时

            sb.append("\nsatellite : ");
            sb.append(location.getSatelliteNumber());    //获取卫星数

            sb.append("\nheight : ");
            sb.append(location.getAltitude());    //获取海拔高度信息，单位米

            sb.append("\ndirection : ");
            sb.append(location.getDirection());    //获取方向信息，单位度

            sb.append("\naddr : ");
            sb.append(location.getAddrStr());    //获取地址信息

            sb.append("\ndescribe : ");
            sb.append("gps定位成功");

        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){

            // 网络定位结果
            sb.append("\naddr : ");
            sb.append(location.getAddrStr());    //获取地址信息

            sb.append("\noperationers : ");
            sb.append(location.getOperators());    //获取运营商信息

            sb.append("\ndescribe : ");
            sb.append("网络定位成功");

        } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

            // 离线定位结果
            sb.append("\ndescribe : ");
            sb.append("离线定位成功，离线定位结果也是有效的");

        } else if (location.getLocType() == BDLocation.TypeServerError) {

            sb.append("\ndescribe : ");
            sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

        } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

            sb.append("\ndescribe : ");
            sb.append("网络不同导致定位失败，请检查网络是否通畅");

        } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

            sb.append("\ndescribe : ");
            sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

        }

        sb.append("\nlocationdescribe : ");
        sb.append(location.getLocationDescribe());    //位置语义化信息

        List<Poi> list = location.getPoiList();    // POI数据
        if (list != null) {
            sb.append("\npoilist size = : ");
            sb.append(list.size());
            for (Poi p : list) {
                sb.append("\npoi= : ");
                sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
            }
        }
        Log.i("BaiduLocationApiDem", sb.toString());
        sendLocation(sb.toString());
    }
}


