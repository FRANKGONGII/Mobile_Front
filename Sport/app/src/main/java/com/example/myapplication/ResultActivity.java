package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps2d.model.LatLng;
import com.example.myapplication.bean.Record;
import com.example.myapplication.data.DataService;
import com.example.myapplication.data.LocalData;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;


public class ResultActivity extends AppCompatActivity {
    private DataService dataService = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sportresult);
        Log.d("ID_TEST","jump");
        ToastUtils.init(this.getApplication());
        dataService = new LocalData();


        Intent intent = getIntent();
        int id = intent.getIntExtra("passId",-1);
        Log.d("ID_TEST",""+id);
        if(id==-1) {
            ToastUtils.show("获取运动数据错误");
        }
        Record record = dataService.getRecord(id);
        Log.d("ID_TEST",String.valueOf(record.getStartTime()));
        Log.d("ID_TEST", String.valueOf(id));





    }
}
