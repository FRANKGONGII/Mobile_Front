package com.example.myapplication;



import static android.app.PendingIntent.getActivity;
import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapException;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
import com.example.myapplication.R;
import com.example.myapplication.data.DataService;
import com.example.myapplication.data.LocalData;
import com.hjq.toast.ToastUtils;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.example.myapplication.bean.Record;



public class RecordingActivity extends Activity {

    public double latitude;
    public double longitude;
    public AMapLocationClient mLocationClient = null;
    AMap aMap = null;
    public List<LatLng> latLngList = new ArrayList();

    public float speed = 0;
    public float distance = 0;
    public int seconds = 0;

    public int timeCnt = 0;

    TextView speedVal = null;
    TextView distanceVal = null;
    Chronometer passtime = null;

    TextView finish = null;
    TextView stop = null;
    TextView goon = null;

    boolean ifStart = false;

    private DataService dataService = null;
    private MyRunnable mRunnable = null;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    MyLocationStyle myLocationStyle;

    boolean ifStartPoint = true;

    private long startTime = 0;
    private long endTime = 0;


    public void Update(){
        Log.d("CHANGE_UI",speedVal+" "+distanceVal);
        speedVal.setText(String.format("%.2f", speed));
        distanceVal.setText(String.format("%.2f", distance));

        Log.d("CHANGE_UI",String.format("%.2f", speed)+" "+String.format("%.2f", distance));
        Log.d("CHANGE_UI",distanceVal.getText()+" "+distanceVal.getText());
    }

    private class MyRunnable implements Runnable {
        @Override
        public void run() {
            Log.d("TIME_TEST",String.valueOf(seconds));
            passtime.setText(formatseconds());
            mHandler.postDelayed(this, 1000);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);
        speedVal = findViewById(R.id.speed);
        distanceVal = findViewById(R.id.distance);
        passtime = findViewById(R.id.cm_passtime);
        finish = findViewById(R.id.tv1);
        stop = findViewById(R.id.tv2);
        goon = findViewById(R.id.tv3);
        startTime = System.currentTimeMillis();
        ToastUtils.init(this.getApplication());

        dataService = new LocalData();


        stop.setOnClickListener(new View.OnClickListener() {
            //运动暂停
            @Override
            public void onClick(View v) {
                ifStart = false;
                if (null != mRunnable) {
                    mHandler.removeCallbacks(mRunnable);
                    mRunnable = null;
                    myLocationStyle.interval(Long.MAX_VALUE);
                    aMap.setMyLocationStyle(myLocationStyle);
                }
            }
        });

        goon.setOnClickListener(new View.OnClickListener() {
            //运动继续
            @Override
            public void onClick(View v) {
                ifStart = true;
                if (mRunnable == null){
                    mRunnable = new MyRunnable();
                    mHandler.postDelayed(mRunnable, 0);
                    myLocationStyle.interval(1000);
                    aMap.setMyLocationStyle(myLocationStyle);
                    ifStartPoint = true;
                }
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            //运动完成
            @Override
            public void onClick(View v) {
                endTime = System.currentTimeMillis();
                Log.d("SAVE_TEST",String.valueOf(new Date(startTime)));
                Log.d("SAVE_TEST",String.valueOf(new Date(endTime)));
                if(false && (distance<0.1||latLngList.size()<3)){
                    //TODO:时间太短的结束可能还要完善一下
                    ToastUtils.show("运动时间或距离太短啦");
                    //Log.d("SAVE_TEST",String.valueOf(ToastUtils.isInit()));
                    finish();
                } else{
                    ToastUtils.show("保存运动记录");
                    ifStart = false;
                    if (null != mRunnable) {
                        mHandler.removeCallbacks(mRunnable);
                        mRunnable = null;
                        myLocationStyle.interval(Long.MAX_VALUE);
                        aMap.setMyLocationStyle(myLocationStyle);
                    }
                    save();
                }
            }
        });





        if(mRunnable==null)mRunnable=new MyRunnable();
        mHandler.postDelayed(mRunnable, 0);



        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        //setContentView(R.layout.activity_recording);//设置对应的XML布局文件




        // 打开权限，否则我的手机无法定位 --q1w2e3r4
        AMapLocationClient.updatePrivacyShow(this.getApplicationContext(),true,true);
        AMapLocationClient.updatePrivacyAgree(this.getApplicationContext(),true);

        MapView mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mapView.getMap();
        //aMap.setOnMyLocationChangeListener(mapLocationListener);
        CameraUpdate cameraUpdate = CameraUpdateFactory.zoomTo(64);
        aMap.moveCamera(cameraUpdate);


        startLocation();
    }

    public String formatseconds() {
        Log.d("TIME_TEST",String.valueOf(seconds));
        String hh = seconds / 3600 > 9 ? seconds / 3600 + "" : "0" + seconds
                / 3600;
        String mm = (seconds % 3600) / 60 > 9 ? (seconds % 3600) / 60 + ""
                : "0" + (seconds % 3600) / 60;
        String ss = (seconds % 3600) % 60 > 9 ? (seconds % 3600) % 60 + ""
                : "0" + (seconds % 3600) % 60;

        seconds++;

        return hh + ":" + mm + ":" + ss;
    }

    public void startLocation(){
        Handler handler = new Handler(Looper.getMainLooper());

        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
        //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(1000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。

        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                latitude = location.getLatitude();
                longitude = location.getLongitude();

                Log.d("LOC_TEST",String.valueOf(latitude)+" "+String.valueOf(longitude));

                //trick, 避免定位不到直插非洲的大黑线
                if(latitude!=0&& longitude!=0)latLngList.add(new LatLng(latitude,longitude));

                PolylineOptions options = new PolylineOptions();
                //Log.d("LOC_TEST", latLngList.toString());
                if(latLngList.size()>=3&&!ifStartPoint){
                    options.add(new LatLng(latLngList.get(latLngList.size()-2).latitude,
                            latLngList.get(latLngList.size()-2).longitude));
                    //当前的经纬度
                    options.add(new LatLng(latitude, longitude));
                    speed =  AMapUtils.calculateLineDistance(
                            new LatLng(latLngList.get(latLngList.size()-2).latitude,
                                    latLngList.get(latLngList.size()-2).longitude),
                            new LatLng(latitude, longitude));
                    distance += speed/1000;
                    speed = speed*3600/1000;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // 在UI线程更新UI
                                    Update();
                                }
                            });
                        }
                    }).start();


                    Log.d("LOC_TEST",latLngList.toString());
                }
                //Polyline polyline =aMap.addPolyline(options.
                //addAll(latLngList).width(10).color(Color.argb(255, 36, 164, 255)));
                aMap.addPolyline(options);

                if(ifStartPoint)ifStartPoint = false;
            }
        });
    }

    public void save(){
        Intent intent = getIntent();
        String sport_type = intent.getStringExtra("sport_type");

        Record record = new Record(Record.RecordType.getValue(sport_type),new Date(startTime),new Date(endTime),distance,seconds,latLngList);
        dataService.updateRecord(record);
        

        Intent intent2 = new Intent(this, ResultActivity.class);
        startActivity(intent2);
    }


}
