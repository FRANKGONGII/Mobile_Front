package com.example.myapplication.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WeekRangeView extends LinearLayout {

    private TextView weekRangeTextView;
    public ImageButton prevWeekButton;
    public ImageButton nextWeekButton;

    public Calendar currentStartDate;

    public WeekRangeView(Context context) {
        super(context);
        init(context);
    }

    public WeekRangeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WeekRangeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // 使用布局文件week_range_view.xml初始化视图
        LayoutInflater.from(context).inflate(R.layout.week_range_view, this, true);

        // 找到视图中的各个组件
        weekRangeTextView = findViewById(R.id.weekRangeTextView);
        prevWeekButton = findViewById(R.id.prevWeekButton);
        nextWeekButton = findViewById(R.id.nextWeekButton);

        // 初始化当前周的起始日期
        currentStartDate = Calendar.getInstance();
        updateWeekRangeText();

        // 设置按钮点击事件监听器
        setButtonClickListeners();
    }

    private void setButtonClickListeners() {
        // 上一周按钮点击事件
        prevWeekButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentStartDate.add(Calendar.DAY_OF_MONTH, -7);
                Log.d("PRESH_TEST","hh");
                updateWeekRangeText();
            }
        });

        // 下一周按钮点击事件
        nextWeekButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentStartDate.add(Calendar.DAY_OF_MONTH, 7);
                updateWeekRangeText();
            }
        });
    }

    public void updateWeekRangeText() {
        // 更新周范围的显示文本
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日");

        // 获取当前日期是一周的第几天
        int dayOfWeek = currentStartDate.get(Calendar.DAY_OF_WEEK);

        // 计算与周一相差的天数
        int daysToMonday = (dayOfWeek - Calendar.MONDAY + 7) % 7;

        // 将当前日期设置为本周的周一
        currentStartDate.add(Calendar.DAY_OF_MONTH, -daysToMonday);

        // 计算本周的结束日期（周日）
        Calendar currentEndDate = (Calendar) currentStartDate.clone();
        currentEndDate.add(Calendar.DAY_OF_MONTH, 6);

        Date startDate = currentStartDate.getTime();
        Date endDate = currentEndDate.getTime();

        String weekRangeText = dateFormat.format(startDate) + " - " + dateFormat.format(endDate);
        weekRangeTextView.setText(weekRangeText);
    }

}
