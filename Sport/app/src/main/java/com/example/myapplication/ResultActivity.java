package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolylineOptions;
import com.example.myapplication.bean.Record;
import com.example.myapplication.data.DataService;
import com.example.myapplication.data.DataServiceFactory;
import com.hjq.toast.ToastUtils;

import java.util.List;

public class ResultActivity extends AppCompatActivity {
    private DataService dataService = null;
    private AMap aMap;
    TextView tvDistance;
    TextView tvDuration;
    TextView tvSpeed;
    TextView tvDistribution;
    TextView tvCalorie;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sportresult);
        Log.d("ID_TEST","jump");
        ToastUtils.init(this.getApplication());
        dataService = DataServiceFactory.getInstance();


        Intent intent = getIntent();
        long id = intent.getLongExtra("passId",11);
        Log.d("URL_TEST","need id:"+id);
        if(id==-1) {
            ToastUtils.show("获取运动数据错误");
        }

        Record record = dataService.getRecord(id);
        //Log.d("ID_TEST","after find"+record);
        List<LatLng> list = record.getLatLngList();
        //Log.d("ID_TEST", list.get(0).latitude+" "+list.get(0).longitude);
        Log.d("ID_TEST",String.valueOf(record.getId()));

        MapView mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mapView.getMap();

        tvDistance = findViewById(R.id.tvDistance);
        tvDuration = findViewById(R.id.tvDuration);
        tvSpeed = findViewById(R.id.tvSpeed);
        tvDistribution = findViewById(R.id.tvDistribution);
        tvCalorie = findViewById(R.id.tvCalorie);

        tvDistance.setText(record.getDistanceByStr());
        tvDuration.setText(record.getDurationByStr());
        tvSpeed.setText(record.getSpeed1());
        tvDistribution.setText(record.getSpeed2());
        tvCalorie.setText(record.getCalorie());

        CameraPosition cameraPosition = new CameraPosition(list.get(0), 64, 0, 0);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        aMap.moveCamera(cameraUpdate);

        for(int i = 3;i<list.size();i++){
            LatLng l1 = list.get(i-1);
            LatLng l2 = list.get(i);
            double speed = AMapUtils.calculateLineDistance(l1,l2);
            speed = speed*3600/1000;

            PolylineOptions options = new PolylineOptions();
            options.add(l1);
            options.add(l2);

            if(speed>6) {
                aMap.addPolyline(options.width(10).color(Color.argb(255, 204, 0, 51)));
            } else if(speed<=6&&speed>=4){
                aMap.addPolyline(options.width(10).color(Color.argb(255, 255, 255, 0)));
            }else{
                aMap.addPolyline(options.width(10).color(Color.argb(255, 36, 164, 255)));
            }
        }


        Marker marker1 = aMap.addMarker(new MarkerOptions().
                position(list.get(2)).title("北京").snippet("DefaultMarker"));
        Marker marker2 = aMap.addMarker(new MarkerOptions().
                position(list.get(list.size()-1)).title("北京").snippet("DefaultMarker"));






    }
}
