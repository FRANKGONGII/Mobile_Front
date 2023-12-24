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
import androidx.appcompat.widget.Toolbar;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.MatchResult;

public class DataCenterActivity extends AppCompatActivity {

    private DataService dataService;

    private Toolbar toolbar;
    private TextView nav1;
    private TextView run;
    private TextView ride;
    private TextView walk;
    private TextView swim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_datacenter);

        dataService = DataServiceFactory.getInstance();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nav1 = findViewById(R.id.min_day);
        nav1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DataCenterActivity.this, StatisticActivity.class);
                startActivity(intent);
            }
        });

        run = findViewById(R.id.run);
        ride = findViewById(R.id.ride);
        walk = findViewById(R.id.walk);
        swim = findViewById(R.id.swim);

        onHistoryInit();

        onDataInit();

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

        Calendar nextDay = Calendar.getInstance();

        nextDay.setTime(todayMidnight);
        nextDay.add(Calendar.DAY_OF_MONTH,1);


        List<Record> todayRecords = dataService.queryRecordByBoth(null, todayMidnight, nextDay.getTime());

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
        spannableString3.setSpan(new StyleSpan(Typeface.BOLD), 5, 5+todayKaStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString3.setSpan(new AbsoluteSizeSpan(20, true), 5, 5+todayKaStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString3.setSpan(new ForegroundColorSpan(Color.BLACK), 5, 5+todayKaStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView textView3 = findViewById(R.id.today_ka);
        textView3.setText(spannableString3);

    }



    public void onDataInit() {
        List<Record> runList = dataService.queryRecordByBoth(Record.RecordType.RUNNING, null, null);
        String runDistStr = String.valueOf(sumDistance(runList));
        SpannableString str1 = new SpannableString(runDistStr + "公里");
        str1.setSpan(new StyleSpan(Typeface.BOLD), 0, runDistStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str1.setSpan(new AbsoluteSizeSpan(15, true), 0, runDistStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, runDistStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        run.setText(str1);

        List<Record> rideList = dataService.queryRecordByBoth(Record.RecordType.RIDING, null, null);
        String rideDistStr = String.valueOf(sumDistance(rideList));
        SpannableString str2 = new SpannableString(rideDistStr + "公里");
        str2.setSpan(new StyleSpan(Typeface.BOLD), 0, rideDistStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str2.setSpan(new AbsoluteSizeSpan(15, true), 0, rideDistStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str2.setSpan(new ForegroundColorSpan(Color.BLACK), 0, rideDistStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ride.setText(str2);

        List<Record> walkList = dataService.queryRecordByBoth(Record.RecordType.WALKING, null, null);
        String walkDistStr = String.valueOf(sumDistance(walkList));
        SpannableString str3 = new SpannableString(walkDistStr + "公里");
        str3.setSpan(new StyleSpan(Typeface.BOLD), 0, walkDistStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str3.setSpan(new AbsoluteSizeSpan(15, true), 0, walkDistStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str3.setSpan(new ForegroundColorSpan(Color.BLACK), 0, walkDistStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        walk.setText(str3);

        List<Record> swimList = dataService.queryRecordByBoth(Record.RecordType.SWIMMING, null, null);
        String swimDurStr = String.valueOf((int)(sumDurationBySec(swimList) / 60));
        SpannableString str4 = new SpannableString(swimDurStr + "分钟");
        str4.setSpan(new StyleSpan(Typeface.BOLD), 0, swimDurStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str4.setSpan(new AbsoluteSizeSpan(15, true), 0, swimDurStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str4.setSpan(new ForegroundColorSpan(Color.BLACK), 0, swimDurStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        swim.setText(str4);
    }

    public double sumDistance(List<Record> recordList) {
        double sum = 0;
        for (Record record : recordList) {
            sum += record.getDistance();
        }
        return sum;
    }

    public int sumDurationBySec(List<Record> recordList) {
        int sum = 0;
        for (Record record : recordList) {
            sum += record.getDuration();
        }
        return sum;
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
