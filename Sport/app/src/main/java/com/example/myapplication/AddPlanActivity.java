package com.example.myapplication;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.bean.Schedule;
import com.github.airsaid.calendarview.widget.CalendarView;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.github.airsaid.calendarview.util.DateUtils;

public class AddPlanActivity extends AppCompatActivity {

    private CheckBox checkBoxWeekly;

    private boolean ifWeek = false;
    private CheckBox checkBoxMonthly;

    private boolean ifMonth = false;
    private CalendarView calendarView;
    private ImageButton buttonSaveSchedule;

    private EditText planName;
    private EditText planDetail;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private ImageButton back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_addplan);
        pref = getSharedPreferences("schedule", MODE_PRIVATE);
        editor = pref.edit();
        back = findViewById(R.id.add_plan_back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 初始化视图
        initView();
    }

    // 初始化视图
    private void initView() {
        checkBoxWeekly = findViewById(R.id.checkBoxWeekly);
        checkBoxMonthly = findViewById(R.id.checkBoxMonthly);
        planDetail = findViewById(R.id.editTextTime);
        planName = findViewById(R.id.editTextTitle);


        checkBoxWeekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ifWeek = !ifWeek;
            }
        });

        checkBoxMonthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ifMonth = !ifMonth;
            }
        });



        buttonSaveSchedule = findViewById(R.id.add_plan_save_button);
        calendarView = findViewById(R.id.calendarView);


        ImageButton month_back = findViewById(R.id.add_month_back);
        ImageButton month_add = findViewById(R.id.add_month_forward);
        TextView month_show= findViewById(R.id.add_plan_date);


        month_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("cal_test","before: "+calendarView.getYear()+" "+calendarView.getMonth());
                calendarView.lastMonth();
                Log.d("cal_test","after: "+calendarView.getYear()+" "+calendarView.getMonth());
                month_show.setText(calendarView.getYear()+"-"+(calendarView.getMonth()+1));
            }
        });

        month_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("cal_test","before: "+calendarView.getYear()+" "+calendarView.getMonth());
                calendarView.nextMonth();
                Log.d("cal_test","after: "+calendarView.getYear()+" "+calendarView.getMonth());
                month_show.setText(calendarView.getYear()+"-"+(calendarView.getMonth()+1));
            }
        });

        calendarView.setClickable(true);
        calendarView.setChangeDateStatus(true);



        calendarView.setOnDataClickListener(new CalendarView.OnDataClickListener() {
            @Override
            public void onDataClick(@NonNull CalendarView view, int year, int month, int day) {
                Log.e("CAL_TEST", "year: " + year+" "+calendarView.getYear());
                Log.e("CAL_TEST", "month,: " + (month + 1));//获取的少1月
                Log.e("CAL_TEST", "day: " + day);
                Log.e("CAL_TEST", "format: "+ calendarView.getDateFormatPattern());
                //这里我要把最近的4个周，本月内的，加入
                List<String> mSelectDate = calendarView.getSelectDate();

                int tmpYear = year;int tmpMonth = month;int tmpDay = day;

                if(ifWeek) {
                    //如果选中了周
                    for (int i = 0; i < 4; i++) {
                        day += 7;
                        Log.d("CAL_TEST",day+" this month size: "+DateUtils.getMonthDays(year, month+1));
                        if (day > DateUtils.getMonthDays(year, month+1)) {
                            //如果现在超过了当月
                            day -= DateUtils.getMonthDays(year, month+1);
                            month += 1;
                            if (month > 11) {
                                //超过了当年
                                year += 1;
                                month = 0;
                            }
                        }
                        Log.d("CAL_TEST",day+" "+(month+1)+" "+year);
                        mSelectDate.add(calendarView.getFormatDate(year, month, day));
                    }
                    calendarView.setSelectDate(mSelectDate);
                }

                year = tmpYear; month = tmpMonth; day=tmpDay;
                if(ifMonth){
                    //选中了月
                    for (int i = 0; i < 4; i++) {
                        month+=1;
                        //Log.d("CAL_TEST",day+" this month size: "+DateUtils.getMonthDays(year, month+1));
                        if (month > 11) {
                            //超过了当年
                            year += 1;
                            month = 0;

                        }
                        Log.d("CAL_TEST",day+" "+(month+1)+" "+year);
                        mSelectDate.add(calendarView.getFormatDate(year, month, day));
                    }
                    calendarView.setSelectDate(mSelectDate);
                }



            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, boolean select, int year, int month, int day) {
                if(select){
                    Toast.makeText(getApplicationContext()
                            , "选中了：" + year + "年" + (month + 1) + "月" + day + "日", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext()
                            , "取消选中了：" + year + "年" + (month + 1) + "月" + day + "日", Toast.LENGTH_SHORT).show();
                }
            }
        });


        buttonSaveSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SAVE_TEST",planDetail.getText().toString().length()+"");
                if(planDetail.getText().toString().length()==0||
                        planDetail.getText().toString().length()==0||
                        calendarView.getSelectDate().size()==0
                ){
                    Toast.makeText(getApplicationContext(),"请输入完整信息",Toast.LENGTH_LONG).show();
                }else {

                    Toast.makeText(getApplicationContext(),"成功添加计划",Toast.LENGTH_LONG).show();
                    int numOfSets = pref.getInt("numOfSets", 0);
                    Set<String> set = new Schedule(planName.getText().toString(),
                            planDetail.getText().toString()+'。',calendarView.getSelectDate(), "item"+numOfSets).toSetString();
                    editor.putStringSet("item"+numOfSets, set);
                    editor.putInt("numOfSets", numOfSets+1);
                    editor.putInt("item"+numOfSets+"Size", calendarView.getSelectDate().size());
                    editor.apply();


                    finish();
                }
            }
        });



    }

}



//    /**
//     * 设置选中的日期数据.
//     *
//     * @param days 日期数据, 日期格式为 {@link #setDateFormatPattern(String)} 方法所指定,
//     * 如果没有设置则以默认的格式 {@link #DATE_FORMAT_PATTERN} 进行格式化.
//     */
//    public void setSelectDate(List<String> days){
//        this.mSelectDate = days;
//        invalidate();
//    }

//    /**
//     * 获取选中的日期数据.
//     *
//     * @return 日期数据.
//     */
//    public List<String> getSelectDate(){
//        return mSelectDate;
//    }

//    /**
//     * 获取日期格式化格式.
//     *
//     * @return 格式化格式.
//     */
//    public String getDateFormatPattern(){
//        return mDateFormatPattern;
//    }
