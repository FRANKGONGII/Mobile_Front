package com.example.myapplication.fragment;

import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.PolylineOptions;
import com.example.myapplication.HistoryActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.RecordingActivity;
import com.example.myapplication.TestActivity;
import com.example.myapplication.bean.Record;
import com.example.myapplication.data.DataService;
import com.example.myapplication.data.DataServiceFactory;

import org.w3c.dom.Text;

import java.util.List;

public class SportFragment extends Fragment {

    String sport_type = "跑步";//默认运动是跑步
    Record.RecordType recordType = Record.RecordType.RUNNING;
    private AlertDialog alertDialog2;
    private DataService dataService;

    private View v;

    AMap aMap = null;


    Bundle bundle = null;

    Activity activity;
    Window window;
    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_sport, container, false);

        dataService = DataServiceFactory.getInstance();

        bundle = savedInstanceState;


        //TODO：这里应该要去获取上面面板的数据

        activity = (AppCompatActivity)getActivity();
        window = activity.getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.parseColor("#FFB3DEE5"));

        return mView;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setButtonGO(view);
        setButtonChoose(view);
        set_nav1(view);

        List<Record> list = dataService.queryRecordByBoth(Record.RecordType.RUNNING,null,null);
        if(list==null){
            Toast.makeText(getContext(), "network error: 408", Toast.LENGTH_SHORT).show();
        }else{
            TextView textView = view.findViewById(R.id.sum_distance);
            textView.setText(getSumDistance(list));
        }
        v = view;



    }

    @Override
    public void onStart() {
        super.onStart();
        Button button = v.findViewById(R.id.ButtonGo);
        button.setText("GO");

        if(ActivityCompat.checkSelfPermission(getContext(),android.Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        //setContentView(R.layout.activity_recording);//设置对应的XML布局文件

        // 打开权限，否则我的手机无法定位 --q1w2e3r4
        AMapLocationClient.updatePrivacyShow(getContext(),true,true);
        AMapLocationClient.updatePrivacyAgree(getContext(),true);

        MapView mapView = (MapView) v.findViewById(R.id.map2);
        mapView.onCreate(bundle);// 此方法必须重写
        aMap = mapView.getMap();
        //aMap.setOnMyLocationChangeListener(mapLocationListener);
        CameraUpdate cameraUpdate = CameraUpdateFactory.zoomTo(64);
        aMap.moveCamera(cameraUpdate);


        startLocation();
    }

    public void startLocation(){
        Handler handler = new Handler(Looper.getMainLooper());
        MyLocationStyle myLocationStyle;

        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
        //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(1000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。

        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }

    boolean isCounting = false;
    CountDownTimer countDownTimer = null;
    public void setButtonGO(@NonNull View view) {
        Button button = view.findViewById(R.id.ButtonGo);
//        String word = (String) button.getText();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCounting) {
                    countDownTimer.cancel();
                    button.setText("GO");
                    isCounting = false;
                } else {
                    countDownTimer = new CountDownTimer(3000, 1000) {
                        int count = 3;
                        @Override
                        public void onTick(long millisUntilFinished) {
                            button.setText("" + Math.max(0, count--));
                        }

                        @Override
                        public void onFinish() {
                            isCounting = false;
                            button.setText("GO!");
                            Intent intent = new Intent(getActivity(), RecordingActivity.class);
                            intent.putExtra("sport_type", sport_type);
//
                            // 揭露动画
                            int posX = (int) (button.getX() + button.getWidth() / 2);
                            int posY = (int) (button.getY() + button.getHeight() / 2);
                            intent.putExtra("x", posX);
                            intent.putExtra("y", posY);
//                                        Log.d("POS", String.valueOf(button.getX()));
//                                        Log.d("POS", String.valueOf(button.getY()));

                            startActivity(intent);
                        }
                    }.start();

                    isCounting = true;
                }
            }
        });

//        final boolean[] ifStart = {false};
//        button.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.d("BUTTON_TEST","press go");
//                        if(!ifStart[0])  ifStart[0] = true;
//                        else ifStart[0] = false;
//                        if(ifStart[0]){
//                            final int[] cntTime = {3};
//                            Handler handler = new Handler();
//                            Runnable runnable = new Runnable(){
//                                @Override
//                                public void run(){
//                                    //数字自增
//                                    // 创建文本
//                                    if(!ifStart[0])return;
//                                    Log.d("CNT_TEST",String.valueOf(cntTime[0]));
//                                    if(cntTime[0]>0)button.setText(String.valueOf(cntTime[0]));
//                                    else if(cntTime[0]==0){
//                                        button.setText("GO!");
//                                        Intent intent = new Intent(getActivity(), RecordingActivity.class);
//                                        intent.putExtra("sport_type", sport_type);
////
//                                        // 揭露动画
//                                        int posX = (int) (button.getX() + button.getWidth() / 2);
//                                        int posY = (int) (button.getY() + button.getHeight() / 2);
//                                        intent.putExtra("x", posX);
//                                        intent.putExtra("y", posY);
////                                        Log.d("POS", String.valueOf(button.getX()));
////                                        Log.d("POS", String.valueOf(button.getY()));
//
//                                        startActivity(intent);
//                                        return;
//                                    }
//
//                                    cntTime[0]--;
//                                    if(cntTime[0]<0)return;
//                                    handler.postDelayed(this, 1000);
//
//                                }
//                            };
//                            handler.post(runnable);
//                            if(!ifStart[0])return;
//
////                        button.setBackgroundResource(R.drawable.animation_go);//把Drawable设置为button的背景
////                        //拿到这个我们定义的Drawable，实际也就是AnimationDrawable
////                        AnimationDrawable animationDrawable = (AnimationDrawable) button.getBackground();
////                        animationDrawable.setOneShot(true);// 播放一次
////                        animationDrawable.start();//开启动画
////
////                        Log.d("ANI_TEST","end playing");
//                            //Button jumpFragment = view.findViewById(R.id.start_sport_fragment);
//
//                            // 需要另外启动一个线程 在新线程里面去sleep
//                            // 不要在UI线程中sleep UI被sleep会暂停刷新
////                            Thread t = new Thread(new Runnable() {
////                                @Override
////                                public void run() {
//////                                    try {
//////                                        //sleep(3200);
//////                                        //注意，这里设置成3500是为了让GO！能显示一下
//////                                    } catch (InterruptedException e) {
//////                                        // TODO Auto-generated catch block
//////                                        e.printStackTrace();
//////                                    }
////                                    Intent intent = new Intent(getActivity(), RecordingActivity.class);
////
////                                    intent.putExtra("sport_type", sport_type);
////                                    startActivity(intent);
////                                }
////                            });
//                            // 开启线程
//
////                            Intent intent = new Intent(getActivity(), RecordingActivity.class);
////
////                            intent.putExtra("sport_type", sport_type);
////                            startActivity(intent);
//                        }else{
//                            button.setText(sport_type+"GO");
//                        }
//                    }
//                }
//        );
    }

    public void setButtonChoose(@NonNull View view){
        Button button = view.findViewById(R.id.ButtonChooseType);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleAlertDialog(view);
            }
        });
    }

    //为累计跑步控件设置跳转，跳转到历史记录界面
    //TODO:我修改了跳转的位置
    public void set_nav1(@NonNull View view){
        View nav = view.findViewById(R.id.sum_distance_linear);
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TestActivity.class);
                startActivity(intent);
            }
        });
    }

    public void showSingleAlertDialog(View view){
        final String[] items = {"跑步", "骑行", "游泳", "健走"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle("请选择运动");

        final int[] choose = {-1};
        alertBuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(), items[i], Toast.LENGTH_SHORT).show();
                choose[0] =i;
                Log.d("CHOOSE_TEST","CHOOSE:"+String.valueOf(choose[0]));
            }
        });

        alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("CHOOSE_TEST",String.valueOf(choose[0]));
                if(choose[0]!=-1)sport_type = items[choose[0]];
                Button button = view.findViewById(R.id.ButtonChooseType);
                button.setText(sport_type);
                Button button2 = view.findViewById(R.id.ButtonGo);
                button2.setText("GO");

                TextView sumType = view.findViewById(R.id.sum_type);
                sumType.setText("累计"+sport_type);
                List<Record> list;
                if(sport_type.equals("跑步")){
                    //sport_type = "跑步";
                    list = dataService.queryRecordByBoth(Record.RecordType.RUNNING,null,null);
                }else if(sport_type.equals("健走")){
                    //sport_type = "健走";
                    list = dataService.queryRecordByBoth(Record.RecordType.WALKING,null,null);
                }else if(sport_type.equals("游泳")){
                    //sport_type = "游泳";
                    list = dataService.queryRecordByBoth(Record.RecordType.SWIMMING,null,null);
                }else{
                    //sport_type = "骑行";
                    list = dataService.queryRecordByBoth(Record.RecordType.RIDING,null,null);
                }
                if(list==null){
                    Toast.makeText(getContext(), "network error: 408", Toast.LENGTH_SHORT).show();
                }else{
                    TextView textView = view.findViewById(R.id.sum_distance);
                    textView.setText(getSumDistance(list));
                }


                alertDialog2.dismiss();
            }
        });

        alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog2.dismiss();
            }
        });
        alertDialog2 = alertBuilder.create();
        alertDialog2.show();
    }

    public String getSumDistance(List<Record> list){
        double sum = 0;
        for(Record r:list){
            sum+=r.getDistance();
        }
        return String.format("%.2f", sum);
    }

}