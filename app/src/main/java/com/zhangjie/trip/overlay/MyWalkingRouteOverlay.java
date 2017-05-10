package com.zhangjie.trip.overlay;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.overlayutil.*;
import com.zhangjie.trip.R;

/**
 * Created by zhangjie on 2017/5/10.
 */

public class MyWalkingRouteOverlay extends com.baidu.mapapi.overlayutil.WalkingRouteOverlay {
    private boolean useDefaultIcon=false;

    public MyWalkingRouteOverlay(BaiduMap baiduMap) {
        super(baiduMap);
    }

    @Override
    public BitmapDescriptor getStartMarker() {
        if (useDefaultIcon) {
            return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
        }
        return null;
    }

    @Override
    public BitmapDescriptor getTerminalMarker() {
        if (useDefaultIcon) {
            return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
        }
        return null;
    }
}
