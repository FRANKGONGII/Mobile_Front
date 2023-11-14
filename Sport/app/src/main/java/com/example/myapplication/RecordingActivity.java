package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;


public class RecordingActivity extends AppCompatActivity {
    private MapView mapView;
    private AMap aMap;
    private LocationSource.OnLocationChangedListener mListener = null;//定位监听器
    //private LocationUtil locationUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);
        //TODO: 接入地图，完成运动记录相关功能


        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        //init();


    }



}
