package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    private DataService dataService = null;
    private AMap aMap;
    TextView tvDistance;
    TextView tvDuration;
    TextView tvSpeed;
    TextView tvDistribution;
    TextView tvCalorie;
    Button chat_btn;


//    private LineChart chart;
//
//
//
//
//
//    private void setData(List<Record> records) {
//
//        ArrayList<Entry> values = new ArrayList<>();
//        int count = records.size();
//
//        for (int i = 0; i < count; i++) {
//
//            float val = (float) records.get(i).getDistance();
//            //这里的star可以替换为你自己项目的图标
//            values.add(new Entry(i, val));
//        }
//
//        LineDataSet set1;
//
//        if (chart.getData() != null &&
//                chart.getData().getDataSetCount() > 0) {
//            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
//            set1.setValues(values);
//            set1.notifyDataSetChanged();
//            chart.getData().notifyDataChanged();
//            chart.notifyDataSetChanged();
//        } else {
//            // create a dataset and give it a type
//            set1 = new LineDataSet(values, "DataSet 1");
//
//            set1.setDrawIcons(false);
//
//            // draw dashed line
//            set1.enableDashedLine(10f, 5f, 0f);
//
//            // black lines and points
//            set1.setColor(Color.BLACK);
//            set1.setCircleColor(Color.BLACK);
//
//            // line thickness and point size
//            set1.setLineWidth(1f);
//            set1.setCircleRadius(3f);
//
//            // draw points as solid circles
//            set1.setDrawCircleHole(false);
//
//            // customize legend entry
//            set1.setFormLineWidth(1f);
//            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
//            set1.setFormSize(15.f);
//
//            // text size of values
//            set1.setValueTextSize(9f);
//
//            // draw selection line as dashed
//            set1.enableDashedHighlightLine(10f, 5f, 0f);
//
//            // set the filled area
//            set1.setDrawFilled(true);
//            set1.setFillFormatter(new IFillFormatter() {
//                @Override
//                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
//                    return chart.getAxisLeft().getAxisMinimum();
//                }
//            });
//
//            // set color of filled area
//            if (Utils.getSDKInt() >= 18) {
//                // drawables only supported on api level 18 and above
//
//                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.baseline_male_blue_24);
//                set1.setFillDrawable(drawable);
//            } else {
//                set1.setFillColor(Color.BLACK);
//            }
//
//            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//            dataSets.add(set1); // add the data sets
//
//            // create a data object with the data sets
//            LineData data = new LineData(dataSets);
//
//            // set data
//            chart.setData(data);
//        }
//    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sportresult);
        Log.d("ID_TEST","jump");
        ToastUtils.init(this.getApplication());
        dataService = DataServiceFactory.getInstance();


        Intent intent = getIntent();

        long id = intent.getLongExtra("passId",-1);
        Log.d("URL_TEST","need id:"+id);
        String act = intent.getStringExtra("formActivity");
        Log.d("JUMP_TEST","need id:"+id+" from: "+act);

        if(id==-1) {
            ToastUtils.show("获取运动数据错误");
        }


        //setData(dataService.getAllRecords());


        ImageView back = findViewById(R.id.re_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(act.equals("history")){
                    finish();
                }else if(act.equals("record")){
                    Intent intent1 = new Intent(ResultActivity.this,MainActivity.class);
                    startActivity(intent1);
                }
            }
        });



        Record record = dataService.getRecord(id);
        if(record == null){
            // 正常不会到这里，除非你真的是故意找茬
            Toast.makeText(this, "Record not exists (errorNo:404)", Toast.LENGTH_SHORT).show();
            record = Record.getDefaultOne();
        }

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
        chat_btn = findViewById(R.id.btn_chat);

        tvDistance.setText(record.getDistanceByStr());
        tvDuration.setText(record.getDurationByStr());
        tvSpeed.setText(record.getSpeed1());
        tvDistribution.setText(record.getSpeed2());
        tvCalorie.setText(record.getCalorie());

        String type_name = record.getRecordTypeByStr();

        chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChatActivity.class);
                intent.putExtra("TaskType", "EvalRecord");
                String data = "我进行了一次" + type_name + "运动，总距离为" +
                        tvDistance.getText() + "公里， 用时" + tvDuration.getText() +
                        "。我的性别为男，年龄为20岁，身高、体重等身体数据假定为同年龄人群中的平均值。请你根据" +
                        "上述数据对我本次运动的表现给出意见和建议。省略计算过程，忽略其他条件的影响，如果需要其他数据，" +
                        "可以挑最重要的在最后提出，不超过2条。结尾加上'如果您还有什么其他问题，欢迎随时向我提出'";

                intent.putExtra("RecordData", data);

                view.getContext().startActivity(intent);
            }
        });



        if(list!=null){
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
}
