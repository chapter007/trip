package com.zhangjie.trip.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baidu.mapapi.search.core.RouteLine;
import com.zhangjie.trip.R;
import com.zhangjie.trip.adapter.RouteLineAdapter;

import java.util.List;

/**
 * Created by zhangjie on 2017/5/2.
 */

interface OnItemInDlgClickListener {
    public void onItemClick(int position);
}

class MyTransitDlg extends Dialog{
    private List<? extends RouteLine> mtransitRouteLines;
    private ListView transitRouteList;
    private RouteLineAdapter mTransitAdapter;

    OnItemInDlgClickListener onItemInDlgClickListener;

    public MyTransitDlg(Context context, int theme) {
        super(context, theme);
    }

    public MyTransitDlg(Context context, List<? extends RouteLine> transitRouteLines, RouteLineAdapter.Type
            type) {
        this(context, 0);
        mtransitRouteLines = transitRouteLines;
        mTransitAdapter = new RouteLineAdapter(context, mtransitRouteLines, type);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void setOnDismissListener(OnDismissListener listener) {
        super.setOnDismissListener(listener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit_dialog);

        transitRouteList = (ListView) findViewById(R.id.transitList);
        transitRouteList.setAdapter(mTransitAdapter);

        transitRouteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemInDlgClickListener.onItemClick(position);
                //mBtnPre.setVisibility(View.VISIBLE);
                //mBtnNext.setVisibility(View.VISIBLE);
                dismiss();
                //hasShownDialogue = false;
            }
        });
    }

    public void setOnItemInDlgClickLinster(OnItemInDlgClickListener itemListener) {
        onItemInDlgClickListener = itemListener;
    }
}
