package com.example.myapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.RecordAdapter;
import com.example.myapplication.bean.Record;
import com.example.myapplication.data.DataService;
import com.example.myapplication.data.DataServiceFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class HistoryActivity extends AppCompatActivity{
    private RecordAdapter adapter;

    private RecyclerView recyclerView;
    private List<Record> recordList;

    private final int[] record_type_layout_ids = {R.id.record_type_layout1,R.id.record_type_layout2,R.id.record_type_layout3,R.id.record_type_layout4,R.id.record_type_layout5};

    private final int[] time_layout_ids = {R.id.time_layout1,R.id.time_layout2,R.id.time_layout3,R.id.time_layout4,R.id.time_layout5};
    private final int[] month_list = {-1,12,11,10,9};
    private final Record.RecordType[] recordTypes = {null,Record.RecordType.RUNNING, Record.RecordType.RIDING, Record.RecordType.WALKING, Record.RecordType.SWIMMING};
    private DataService dataService;

    private Record.RecordType chosen_type = null;

    private int chosen_month = -1;

    private int record_type_layout_idx = 0;
    private int month_layout_idx = 0;


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
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                try {
//                    recordList = dataService.queryRecordByTime(sdf.parse("2023-11-01 00:00:00"),sdf.parse("2023-12-01 00:00:00"));
//                    recordList = dataService.queryRecordByType(Record.RecordType.RUNNING);
//                    adapter = new RecordAdapter(recordList);
//                    recyclerView.setAdapter(adapter);
//                    Log.d("Data_test","!"+recordList.size());
//                } catch (ParseException e) {
//                    throw new RuntimeException(e);
//                }
            }
        });

        ImageButton back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        init_type();
        init_time();
    }

    private void init_type(){
        View temp = findViewById(record_type_layout_ids[0]);
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                chosen_type = null;
                update_type(0);
                update_list();
            }
        });

        for(int i=1;i<=4;i++){
            Record.RecordType record_type = recordTypes[i];
            temp = findViewById(record_type_layout_ids[i]);
            int finalI = i;
            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    chosen_type = record_type;
                    update_type(finalI);
                    update_list();
                }
            });
        }
    }

    private void update_type(int idx){
        View old_view = findViewById(record_type_layout_ids[record_type_layout_idx]);
        Drawable gray = AppCompatResources.getDrawable(old_view.getContext(),R.drawable.frame_gray);
        old_view.setBackground(gray);

        View new_view = findViewById(record_type_layout_ids[idx]);
        Drawable blue = AppCompatResources.getDrawable(new_view.getContext(),R.drawable.frame2);
        new_view.setBackground(blue);

        record_type_layout_idx = idx;
    }

    private void init_time(){
        View temp = findViewById(time_layout_ids[0]);
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                chosen_month = -1;
                update_time(0);
                update_list();
            }
        });

        for(int i=1;i<=4;i++){
            int month = month_list[i];
            temp = findViewById(time_layout_ids[i]);
            int finalI = i;
            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    chosen_month = month;
                    update_time(finalI);
                    update_list();
                }
            });
        }
    }

    private void update_time(int idx){
        View old_view = findViewById(time_layout_ids[month_layout_idx]);
        Drawable gray = AppCompatResources.getDrawable(old_view.getContext(),R.drawable.frame_gray);
        old_view.setBackground(gray);

        View new_view = findViewById(time_layout_ids[idx]);
        Drawable blue = AppCompatResources.getDrawable(new_view.getContext(),R.drawable.frame2);
        new_view.setBackground(blue);

        month_layout_idx = idx;
    }

    private void update_list() {
        Date startTime = get_this_month();
        Date endTime = get_next_month(startTime);

        recordList = dataService.queryRecordByBoth(chosen_type,startTime,endTime);
        adapter = new RecordAdapter(recordList);
        recyclerView.setAdapter(adapter);
    }

    private Date get_this_month() {
        if(chosen_month == -1) return null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Macao"));
        try{
            Date dt = sdf.parse(String.format("2023-%02d-01", chosen_month));
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
        rightNow.setTimeZone(TimeZone.getTimeZone("Asia/Macao"));
        rightNow.setTime(now);
        rightNow.add(Calendar.MONTH, 1);
        return rightNow.getTime();
    }

}
