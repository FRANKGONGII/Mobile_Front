package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.RecordAdapter;
import com.example.myapplication.bean.Record;
import com.example.myapplication.data.DataService;
import com.example.myapplication.data.DataServiceFactory;
import com.example.myapplication.utils.Utils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.MatchResult;

public class DataCenterActivity extends AppCompatActivity {

    private DataService dataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_datacenter);

        dataService = DataServiceFactory.getInstance();

//        onHistoryInit();

        onCardClick();
    }

    public void onHistoryInit() {
        List<Record> allRecords = dataService.getAllRecords();

        int totalSec = 0;
        for (Record record : allRecords) {
            totalSec += record.getDuration();
        }
        String totalMinStr = String.valueOf((int)(totalSec/60));

        // 不想算就默认累计6天
        int days = 6;
        String daysStr = String.valueOf(days);

        SpannableString spannableString = new SpannableString(totalMinStr+ " 分钟\n" + "累计 " + daysStr + " 天");
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, totalMinStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(30, true), 0, totalMinStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, totalMinStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView textView1 = findViewById(R.id.min_day);
        textView1.setText(spannableString);


        int todaySec = 0;
        int todayKa = 0;

        Calendar calendar = Calendar.getInstance();
        // 设置为今日凌晨的时间
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // 获取今日凌晨的时间
        Date todayMidnight = calendar.getTime();

        List<Record> todayRecords = dataService.queryRecordByBoth(null, todayMidnight, null);

        for (Record record : todayRecords) {
            todaySec += record.getDuration();
        }

        String todayMinStr = String.valueOf((int) (todaySec/60));
        String todayKaStr = String.valueOf(todayKa);

        SpannableString spannableString2 = new SpannableString("运动时长\n" + todayMinStr + " 分钟");
        spannableString2.setSpan(new StyleSpan(Typeface.BOLD), 5, 5+todayMinStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString2.setSpan(new AbsoluteSizeSpan(20, true), 5, 5+todayMinStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString2.setSpan(new ForegroundColorSpan(Color.BLACK), 5, 5+todayMinStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView textView2 = findViewById(R.id.today_min);
        textView2.setText(spannableString2);

        SpannableString spannableString3 = new SpannableString("运动消耗\n" + todayKa + " 千卡");
        spannableString3.setSpan(new StyleSpan(Typeface.BOLD), 5, todayKaStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString3.setSpan(new AbsoluteSizeSpan(20, true), 0, todayKaStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString3.setSpan(new ForegroundColorSpan(Color.BLACK), 0, todayKaStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView textView3 = findViewById(R.id.today_ka);
        textView3.setText(spannableString);

    }

    public void onDataInit() {

    }

    public void onCardClick() {

        MaterialCardView cardRun = findViewById(R.id.cardRun);
        MaterialCardView cardRide = findViewById(R.id.cardRide);
        MaterialCardView cardWalk = findViewById(R.id.cardWalk);
        MaterialCardView cardSwim = findViewById(R.id.cardSwim);

        cardRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataCenterActivity.this, TestActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("activity_type", Record.RecordType.RUNNING);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        cardRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataCenterActivity.this, TestActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("activity_type", Record.RecordType.RIDING);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        cardWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataCenterActivity.this, TestActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("activity_type", Record.RecordType.WALKING);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        cardSwim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataCenterActivity.this, TestActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("activity_type", Record.RecordType.SWIMMING);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });



    }
}
