package com.example.myapplication;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.example.myapplication.R;






public class RecordingActivity extends Activity {


//    public AMapLocationClient mLocationClient = null;
//    public AMapLocationListener mapLocationListener = new AMapLocationListener() {
//        @Override
//        public void onLocationChanged(AMapLocation aMapLocation) {
//            Log.d("LOC_TEST",String.valueOf(aMapLocation==null));
//            if (aMapLocation != null) {
//                if (aMapLocation.getErrorCode() == 0) {
//                    double latitude = aMapLocation.getLatitude();
//                    double Longitude = aMapLocation.getLongitude();
//                    String province = aMapLocation.getProvince();
//                    String city = aMapLocation.getCity();
//                    String district = aMapLocation.getDistrict();
//                    String streetNumber = aMapLocation.getStreetNum();
//                    String text = "经度: " + Longitude + "\n"
//                            + "纬度: " + latitude + "\n"
//                            + "详细位置: " + province + city + district + streetNumber;
//                    position.setText(text);
//                } else {
//                    Log.d("LOC_TEST", "location Error, ErrCode:"
//                            + aMapLocation.getErrorCode() + ", errInfo:"
//                            + aMapLocation.getErrorInfo());
//                    position.setText("定位失败");
//                }
//            }
//        }
//    };

//    private TextView position;
//    public AMapLocationClientOption mLocationOption=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);//设置对应的XML布局文件

        MapView mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        AMap aMap = mapView.getMap();


        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
        //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中
   }
//    @SuppressLint("MissingInflatedId")
//    protected void onCreate(Bundle savedInstanceState) {
//        Log.d("LOC_TEST","1");
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_recording);
//        position=findViewById(R.id.position_text);
//        Log.d("LOC_TEST","2");
//        AMapLocationClient.updatePrivacyShow(getApplicationContext(),true,true);
//        AMapLocationClient.updatePrivacyAgree(getApplicationContext(),true);
//        try {
//            mLocationClient=new AMapLocationClient(getApplicationContext());
//            Log.d("LOC_TEST","1");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        mLocationClient.setLocationListener(mapLocationListener);
//        mLocationOption=new AMapLocationClientOption();
//        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        Log.d("LOC_TEST",String.valueOf(mLocationClient==null));
//        if(null!=mLocationClient)
//        {
//            mLocationClient.setLocationOption(mLocationOption);
//            mLocationClient.stopLocation();
//            mLocationClient.startLocation();
//        }
//    }
//
//    public void onDestroy() {
//        super.onDestroy();
//        mLocationClient.onDestroy();
//    }





}
