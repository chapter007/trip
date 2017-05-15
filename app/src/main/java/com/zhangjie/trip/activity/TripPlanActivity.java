package com.zhangjie.trip.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.zhangjie.trip.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjie on 2017/5/2.
 */

public class TripPlanActivity extends AppCompatActivity implements View.OnClickListener,
        OnGetSuggestionResultListener {

    private static final String TAG = "TripActivity";
    private AutoCompleteTextView startPoint,endPoint;
    private String stPoint,edPoint,city="马鞍山";
    private Button bus,bike,walk,car;
    private Intent intent;
    private double mLocation_x,mLocation_y;
    private LatLng choosePt;
    private List<String> suggest;
    private SuggestionSearch mSuggestionSearch;
    private ArrayAdapter<String> stAdapter = null,edAdapter=null;

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

        startPoint= (AutoCompleteTextView) findViewById(R.id.start_point);
        endPoint= (AutoCompleteTextView) findViewById(R.id.end_point);
        bus= (Button) findViewById(R.id.bus);
        bike= (Button) findViewById(R.id.bike);
        walk= (Button) findViewById(R.id.walk);
        car= (Button) findViewById(R.id.car);

        // 初始化建议搜索模块，注册建议搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

        startPoint.setAdapter(stAdapter);
        endPoint.setAdapter(stAdapter);
        startPoint.setThreshold(1);
        endPoint.setThreshold(1);

        if(choosePt!=null){
            endPoint.setText("地图选中点");
        }

        startPoint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()<0){
                    return;
                }
                /**
                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                 */
                mSuggestionSearch
                        .requestSuggestion((new SuggestionSearchOption())
                                .keyword(charSequence.toString()).city(city));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        endPoint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()<0){
                    return;
                }
                /**
                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                 */
                mSuggestionSearch
                        .requestSuggestion((new SuggestionSearchOption())
                                .keyword(charSequence.toString()).city(city));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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

    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        if (res == null || res.getAllSuggestions() == null) {
            return;
        }
        suggest = new ArrayList<String>();
        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
            if (info.key != null) {
                suggest.add(info.key);
            }
        }
        stAdapter = new ArrayAdapter<String>(TripPlanActivity.this,
                android.R.layout.simple_dropdown_item_1line, suggest);
        startPoint.setAdapter(stAdapter);


        //edAdapter = new ArrayAdapter<String>(TripPlanActivity.this,
        //        android.R.layout.simple_dropdown_item_1line, suggest);
        endPoint.setAdapter(stAdapter);
        //edAdapter.notifyDataSetChanged();
        stAdapter.notifyDataSetChanged();
    }
}
