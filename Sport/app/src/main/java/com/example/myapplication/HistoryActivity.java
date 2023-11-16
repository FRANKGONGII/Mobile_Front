package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.RunningHistoryAdapter;
import com.example.myapplication.bean.Activity;
import com.example.myapplication.data.DataService;
import com.example.myapplication.data.LocalData;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity{
    private List<Activity> activityList;

    private DataService dataService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dataService = new LocalData();
        activityList = dataService.getActivities();

        RecyclerView recyclerView = findViewById(R.id.runningHistoryList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);//添加布局管理器
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);  //设置为纵向水平滚动
        recyclerView.setLayoutManager(layoutManager);//设置布局管理器
        RunningHistoryAdapter adapter = new RunningHistoryAdapter(activityList);
        recyclerView.setAdapter(adapter);
    }
}
