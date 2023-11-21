package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.RecordAdapter;
import com.example.myapplication.bean.Record;
import com.example.myapplication.data.DataService;
import com.example.myapplication.data.LocalData;

import java.util.List;

public class HistoryActivity extends AppCompatActivity{
    private List<Record> recordList;

    private DataService dataService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dataService = new LocalData();
        recordList = dataService.getAllRecords();

        RecyclerView recyclerView = findViewById(R.id.runningHistoryList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);//添加布局管理器
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);  //设置为纵向水平滚动
        recyclerView.setLayoutManager(layoutManager);//设置布局管理器
        RecordAdapter adapter = new RecordAdapter(recordList);
        recyclerView.setAdapter(adapter);
    }
}
