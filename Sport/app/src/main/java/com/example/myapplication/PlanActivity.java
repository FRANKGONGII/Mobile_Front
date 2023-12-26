package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.ScheduleAdapter;
import com.example.myapplication.bean.Schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlanActivity extends AppCompatActivity {

    private RecyclerView recyclerViewScheduleList;
    private ImageButton buttonAddSchedule;
    private ImageButton buttonBack;

    private static List<Schedule> scheduleList;
    private ScheduleAdapter scheduleAdapter;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        pref = getSharedPreferences("schedule", MODE_PRIVATE);
        editor = pref.edit();
        // 初始化视图
        initView();

        //TODO:取出来
        List<Set<String>> list = new ArrayList<>();
        int numOfSets = pref.getInt("numOfSets", 0);
        for (int i = 0; i < numOfSets; ++i) {
            Set<String> set = pref.getStringSet("item"+i, null);
            if (set != null) {
                list.add(set);
            }
        }

        // 初始化日程列表
        initScheduleList(list);

        // 设置RecyclerView的布局管理器和适配器
        recyclerViewScheduleList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewScheduleList.setAdapter(scheduleAdapter);

        // 添加日程按钮点击事件，添加任务和返回
        buttonAddSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlanActivity.this,AddPlanActivity.class));
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        List<Set<String>> list = new ArrayList<>();
        int numOfSets = pref.getInt("numOfSets", 0);
        for (int i = 0; i < numOfSets; ++i) {
            Set<String> set = pref.getStringSet("item"+i, null);
            if (set != null) {
                list.add(set);
            }
        }
        // 初始化日程列表
        initScheduleList(list);
    }

    public static void addItem(Schedule s){
        scheduleList.add(s);
    }

    // 初始化视图
    private void initView() {
        recyclerViewScheduleList = findViewById(R.id.recyclerViewScheduleList);
        buttonAddSchedule = findViewById(R.id.plan_add_button);
        buttonBack = findViewById(R.id.plan_back_button);
    }

    // 初始化日程列表数据
    private void initScheduleList(List<Set<String>> list) {

        // 创建虚拟的日程数据
        scheduleList = new ArrayList();
        for(Set<String>set:list){
            if(Schedule.parseSet(set)!=null)scheduleList.add(Schedule.parseSet(set));
        }
        // 添加更多日程数据...

        // 初始化适配器
        scheduleAdapter = new ScheduleAdapter(scheduleList);
        recyclerViewScheduleList.setAdapter(scheduleAdapter);
    }
}
