package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.RecordAdapter;
import com.example.myapplication.bean.Record;
import com.example.myapplication.data.DataService;
import com.example.myapplication.data.DataServiceFactory;
import com.example.myapplication.data.LocalData;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HistoryActivity extends AppCompatActivity{
    private RecordAdapter adapter;

    private RecyclerView recyclerView;
    private List<Record> recordList;

    private final int[] record_type_ids = {R.id.record_type2,R.id.record_type3,R.id.record_type4,R.id.record_type5};
    private final Record.RecordType[] recordTypes = {Record.RecordType.RUNNING, Record.RecordType.RIDING, Record.RecordType.WALKING, Record.RecordType.SWIMMING};
    private DataService dataService;

    private Record.RecordType type = null;

    private int month = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dataService = DataServiceFactory.getInstance();
        recordList = dataService.getAllRecords();

        recyclerView = findViewById(R.id.runningHistoryList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);//添加布局管理器
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);  //设置为纵向水平滚动
        recyclerView.setLayoutManager(layoutManager);//设置布局管理器
        adapter = new RecordAdapter(recordList);
        recyclerView.setAdapter(adapter);

        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    recordList = dataService.queryRecordByTime(sdf.parse("2023-11-01 00:00:00"),sdf.parse("2023-12-01 00:00:00"));
                    recordList = dataService.queryRecordByType(Record.RecordType.RUNNING);
                    adapter = new RecordAdapter(recordList);
                    recyclerView.setAdapter(adapter);
                    Log.d("Data_test","!"+recordList.size());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        TextView temp = findViewById(R.id.record_type1);
        temp.setText("全部");
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                type = null;
                update_list();
            }
        });

        for(int i=0;i<4;i++){
            Record.RecordType record_type = recordTypes[i];
            temp = findViewById(record_type_ids[i]);
            temp.setText(record_type.getStr());
            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    type = record_type;
                    update_list();
                }
            });
        }
    }

    private void update_list() {
        Date startTime = get_this_month();
        Date endTime = get_next_month(startTime);

        recordList = dataService.queryRecordByBoth(type,startTime,endTime);
        adapter = new RecordAdapter(recordList);
        recyclerView.setAdapter(adapter);
    }

    private Date get_this_month() {
        if(month == -1) return null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date dt = sdf.parse(String.format("2023-%02d-01",month));
            Log.d("Date_test",dt.toString());
            return dt;
        }
        catch(ParseException e){
            e.printStackTrace();
        }

        return null;
    }

    private Date get_next_month(Date now){
        if(now == null) return null;

        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(now);
        rightNow.add(Calendar.MONTH, 1);
        return rightNow.getTime();
    }

}
