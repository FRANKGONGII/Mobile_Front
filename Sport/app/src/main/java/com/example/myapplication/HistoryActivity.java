package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.RecordAdapter;
import com.example.myapplication.bean.Record;
import com.example.myapplication.data.DataService;
import com.example.myapplication.data.DataServiceFactory;
import com.example.myapplication.data.LocalData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class HistoryActivity extends AppCompatActivity{
    private List<Record> recordList;

    private DataService dataService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dataService = DataServiceFactory.getInstance();
        recordList = dataService.getAllRecords();

        RecyclerView recyclerView = findViewById(R.id.runningHistoryList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);//添加布局管理器
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);  //设置为纵向水平滚动
        recyclerView.setLayoutManager(layoutManager);//设置布局管理器
        final RecordAdapter[] adapter = {new RecordAdapter(recordList)};
        recyclerView.setAdapter(adapter[0]);

        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    recordList = dataService.queryRecordByTime(sdf.parse("2023-11-01 00:00:00"),sdf.parse("2023-12-01 00:00:00"));
                    recordList = dataService.queryRecordByType(Record.RecordType.RUNNING);
                    adapter[0] = new RecordAdapter(recordList);
                    recyclerView.setAdapter(adapter[0]);
                    Log.d("Data_test","!"+recordList.size());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
