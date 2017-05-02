package com.zhangjie.trip.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.zhangjie.trip.R;
import com.zhangjie.trip.adapter.RouteLineAdapter;
import com.zhangjie.trip.overlay.MyTransitRouteOverlay;
import com.zhangjie.trip.overlay.TransitRouteOverlay;

/**
 * Created by zhangjie on 2017/5/2.
 */

public class TripRouteActivity extends AppCompatActivity implements OnGetRoutePlanResultListener{
    private String startPoint,endPoint,method;
    private MapView myMap;
    private BaiduMap mBaiduMap;
    private RoutePlanSearch mSearch;
    private PlanNode stNode,enNode;
    private TransitRouteResult nowResultransit;
    private boolean hasShownDialogue=false;
    private RouteLine<TransitRouteLine.TransitStep> route;
    private int nodeIndex=-1;
    private TransitRouteOverlay routeOverlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        startPoint=intent.getStringExtra("startPoint");
        endPoint=intent.getStringExtra("endPoint");
        method=intent.getStringExtra("method");

        setContentView(R.layout.activity_trip_route);
        myMap= (MapView) findViewById(R.id.my_map_view);
        mBaiduMap=myMap.getMap();
        stNode=PlanNode.withCityNameAndPlaceName("马鞍山",startPoint);
        enNode=PlanNode.withCityNameAndPlaceName("马鞍山",endPoint);

        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (method.equals("bus")){
            mSearch.transitSearch((new TransitRoutePlanOption())
                    .from(stNode).city("马鞍山").to(enNode));
        }else if (method.equals("car")){
            mSearch.drivingSearch((new DrivingRoutePlanOption())
                    .from(stNode).to(enNode));
        }else if (method.equals("walk")){
            mSearch.walkingSearch((new WalkingRoutePlanOption())
                    .from(stNode).to(enNode));
        }else if (method.equals("bike")){
            mSearch.bikingSearch((new BikingRoutePlanOption())
                    .from(stNode).to(enNode));
        }
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(TripRouteActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            //mBtnPre.setVisibility(View.VISIBLE);
            //mBtnNext.setVisibility(View.VISIBLE);


            if (result.getRouteLines().size() > 1) {
                nowResultransit = result;
                if (!hasShownDialogue) {
                    MyTransitDlg myTransitDlg = new MyTransitDlg(TripRouteActivity.this,
                            result.getRouteLines(),
                            RouteLineAdapter.Type.TRANSIT_ROUTE);
                    myTransitDlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            hasShownDialogue = false;
                        }
                    });
                    myTransitDlg.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
                        public void onItemClick(int position) {

                            route = nowResultransit.getRouteLines().get(position);
                            TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaiduMap);
                            mBaiduMap.setOnMarkerClickListener(overlay);
                            routeOverlay = overlay;
                            overlay.setData(nowResultransit.getRouteLines().get(position));
                            overlay.addToMap();
                            overlay.zoomToSpan();
                        }

                    });
                    myTransitDlg.show();
                    hasShownDialogue = true;
                }
            } else if (result.getRouteLines().size() == 1) {
                // 直接显示
                route = result.getRouteLines().get(0);
                TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaiduMap);
                mBaiduMap.setOnMarkerClickListener(overlay);
                routeOverlay = overlay;
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();

            } else {
                Log.d("route result", "结果数<0");
                return;
            }
        }
    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }
}
