package com.example.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
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

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.myapplication.bean.ChatBean;
import com.example.myapplication.bean.Schedule;
import com.example.myapplication.task.ChatEleTask;
import com.example.myapplication.task.ChatTask;
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
    private Button magicEle;
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
                setMagicEleEnabled(getIntent().getBooleanExtra("isMagic?", false));
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
        String str_m = month>=10?String.valueOf(month) : "0"+String.valueOf(month);
        String str_d = month>=10?String.valueOf(day) : "0"+String.valueOf(day);
        format = String.valueOf(year)+str_m+str_d;
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

        magicEle = findViewById(R.id.magic_elephant);
        if (getIntent().getBooleanExtra("isMagic?", false)) {
            magicEle.setVisibility(View.VISIBLE);
        } else {
            magicEle.setVisibility(View.INVISIBLE);
        }
    }

    // 设置日程详情
    private void setScheduleDetails() {
        textViewDetailScheduleTitle.setText("计划标题：" + schedule.getTitle());
        textViewDetailScheduleDetail.setText("计划详情：" + schedule.getTime());
        calendarView.setSelectDate(schedule.getDates());
    }



    // 🤔

    private List<String> promptList;
    private List<String> roleList;
    private List<ChatBean> chatBeanList;
    private ProgressDialog progressDialog;
    private PyObject pyChatObject;

    private void setMagicEleEnabled(boolean enabled) {
        if (enabled) {
            promptList = new ArrayList<>();
            roleList = new ArrayList<>();
            chatBeanList = new ArrayList<>();
            progressDialog = new ProgressDialog(DetailPlanActivity.this);

            if (! Python.isStarted()) {
                Python.start(new AndroidPlatform(this));
            }

            Python python = Python.getInstance();
            pyChatObject = python.getModule("AIConversation");

            magicEle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (magicEle.getVisibility() == View.INVISIBLE) {
                        magicEle.setVisibility(View.VISIBLE);
                    } else {
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(v.getContext());
                        builder.setMessage("我是可爱的运动大象，一起玩吗?");
                        builder.setNegativeButton("算啦", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });


                        builder.setPositiveButton("如何评价我的计划", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                promptList.add("我想让你对我的计划作出评价，但是你的评价必须遵守以下规则：以”我的评价是X“开头，X必须是”寄、棒“中的一个字，”寄“表示你认为我这个计划不太好或很难实现，”棒“表示你认为我的计划很有希望顺利进行，说完这句话后再给出解释。");
                                roleList.add("system");
                                String data = "我的计划是" + schedule.getTime() + "目前我完成的进度是" + ((size-schedule.getDates().size())+"/"+size) + "如何评价我的计划";
                                promptList.add(data);
                                roleList.add("user");
                                LLM_Post();
                            }
                        });

                        builder.show();
                    }

                }
            });
        } else {

        }
    }

    private void LLM_Post(){
        StringBuilder promptsBuilder = new StringBuilder();
        StringBuilder rolesBuilder = new StringBuilder();
        int len = promptList.size();
        for(int i=0;i<len;i++){
            promptsBuilder.append(promptList.get(i));
            rolesBuilder.append(roleList.get(i));
            if(i < len-1){
                promptsBuilder.append('#');
                rolesBuilder.append('#');
            }
        }
        String prompts = promptsBuilder.toString();
        String roles = rolesBuilder.toString();
        //用于执行与LLM交流的线程，注意每个AsyncTask只能执行一次，所以一定要new
        ChatEleTask chatEleTask = new ChatEleTask(pyChatObject, progressDialog, chatBeanList, DetailPlanActivity.this);

        chatEleTask.execute(roles, prompts, roleList, promptList);
    }

}
