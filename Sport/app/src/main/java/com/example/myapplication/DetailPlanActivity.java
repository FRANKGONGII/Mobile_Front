package com.example.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.bean.Schedule;
import com.github.airsaid.calendarview.widget.CalendarView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class DetailPlanActivity extends AppCompatActivity {

    private TextView textViewDetailScheduleTitle;
    private TextView textViewDetailScheduleDetail;

    private TextView textViewDetailToday;

    private ImageButton button;
    private CalendarView calendarView;
    private ImageButton back;
    private ImageButton del;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

//    private int index = -1;

    private Schedule schedule;

    private boolean ifToday = false;

    String format;

    private int size = 0;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_plan_detail);

        pref = getSharedPreferences("schedule", MODE_PRIVATE);
        editor = pref.edit();
        key = getIntent().getStringExtra("key");
        back = findViewById(R.id.detail_plan_back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        del = findViewById(R.id.plan_del_button);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建builder
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(DetailPlanActivity.this);

                // 设置builder属性
//                builder.setTitle("修改昵称");
                builder.setMessage("相信您一定可以坚持完成计划哦!\n确定要删除这个计划吗?");

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (key != null) {
                            editor.putStringSet(key, null);
                            editor.apply();
                            finish();
                        }
                    }
                });

                builder.show();
            }
        });


        // 获取从总览页面传递过来的日程数据
//        index = getIntent().getIntExtra("index",-1);
        String key = getIntent().getStringExtra("key");
        Log.d("SET", "onCreate: "+key);
        if(key==null){
            Toast.makeText(getApplicationContext(),"读取数据出错了",Toast.LENGTH_LONG);
        }else{
            schedule = new Schedule();
            Set<String> set = pref.getStringSet(key, null);
            size = pref.getInt(key+"Size",0);
            if(set!=null){
                // 初始化视图
                schedule = Schedule.newParseSet(set, key);
                initView();
            }else{
                Toast.makeText(getApplicationContext(),"读取数据出错了",Toast.LENGTH_LONG);
            }

        }

        // 设置日程详情
    }

    // 初始化视图
    private void initView() {
        textViewDetailScheduleTitle = findViewById(R.id.textViewTitle);
        textViewDetailScheduleDetail = findViewById(R.id.textViewTime);
        textViewDetailToday = findViewById(R.id.textViewIfPlanToday);



        calendarView = findViewById(R.id.calendarViewDetail);

        //检测今天有没有任务
        java.util.Calendar calendar =java.util.Calendar.getInstance();
        // 获取年、月、日的整数值
        int year = calendar.get(java.util.Calendar.YEAR);
        int month = calendar.get(java.util.Calendar.MONTH)+1; // 注意：Calendar.MONTH 是从 0 开始的，显示的话需要加 1
        int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);
        format = String.valueOf(year)+String.valueOf(month)+String.valueOf(day);
        Log.d("FINISH_TEST",format);
        if(schedule.ifInDates(format)){
            //有
            textViewDetailToday.setText("今天有任务！");
            textViewDetailToday.setTextColor(Color.RED);
            ifToday = true;
        }else{
            textViewDetailToday.setText("今天没任务~ ");
            textViewDetailToday.setTextColor(Color.BLUE);
            ifToday = false;
        }



        ImageButton month_back = findViewById(R.id.detail_month_back);
        ImageButton month_add = findViewById(R.id.detail_month_forward);
        TextView month_show= findViewById(R.id.detail_plan_date);


        month_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.lastMonth();
                month_show.setText(calendarView.getYear()+"-"+(calendarView.getMonth()+1));
            }
        });

        month_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.nextMonth();
                month_show.setText(calendarView.getYear()+"-"+(calendarView.getMonth()+1));
            }
        });

        calendarView.setClickable(false);
        calendarView.setChangeDateStatus(true);

        setScheduleDetails();

        Button finish = findViewById(R.id.detail_finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ifToday){
                    textViewDetailToday.setText("今天任务完成了！");
                    textViewDetailToday.setTextColor(Color.GREEN);
                    List<String> newDate = schedule.getDates();
                    newDate.remove(format);

                    Set<String> set = new Schedule(schedule.getTitle(),
                            schedule.getTime(),newDate, schedule.getKey()).toSetString();
                    editor.putStringSet(key, set);
                    editor.apply();

                }
            }
        });


        TextView haveFinish = findViewById(R.id.textViewCntFinish);
        haveFinish.setText("任务已完成 "+(size-schedule.getDates().size())+"/"+size);

        if(schedule.getDates().size()==0){
            textViewDetailToday.setText("计划已经全部完成！");
            textViewDetailToday.setTextColor(Color.DKGRAY);
        }



    }

    // 设置日程详情
    private void setScheduleDetails() {
        textViewDetailScheduleTitle.setText("计划标题：" + schedule.getTitle());
        textViewDetailScheduleDetail.setText("计划详情：" + schedule.getTime());
        calendarView.setSelectDate(schedule.getDates());
    }
}
