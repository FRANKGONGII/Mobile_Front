package com.example.myapplication;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;


import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.MenuRes;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.Resource;
import com.example.myapplication.adapter.RecordAdapter;
import com.example.myapplication.bean.Record;
import com.example.myapplication.data.DataService;
import com.example.myapplication.data.DataServiceFactory;
import com.example.myapplication.utils.Utils;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TestActivity extends AppCompatActivity {

    private RecordAdapter adapter;

    private RecyclerView recyclerView;
    private List<Record> recordList;

    private DataService dataService;
    private boolean pressTime = false;
    private Record.RecordType nowType = null;

    private Date start = null;
    private Date end = null;

    private double nowDistance = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);

        //列表初始化
        dataService = DataServiceFactory.getInstance();
        recordList = dataService.getAllRecords();
        if(recordList == null){
            Toast.makeText(this, "network error: 408", Toast.LENGTH_SHORT).show();
            recordList = new ArrayList<>();
        }

        recyclerView = findViewById(R.id.history_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);//添加布局管理器
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);  //设置为纵向水平滚动
        recyclerView.setLayoutManager(layoutManager);//设置布局管理器
        adapter = new RecordAdapter(recordList);
        recyclerView.setAdapter(adapter);


        //初始化数据，主要是为了显示那些数值
        reFreshView(dataService.getAllRecords());

        //选择类型按钮
        Button listPopupWindowButton = findViewById(R.id.history_popup_button);
        listPopupWindowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v,R.menu.history_type_menu);
            }
        });

        //时间选择按钮
        ImageButton sortByTime = findViewById(R.id.history_sort_by_time);
        sortByTime.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                ImageButton sortByTime = findViewById(R.id.history_sort_by_time);

                MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
                builder.setTitleText("选择搜索时间范围");
                MaterialDatePicker<Pair<Long,Long>> datePicker = builder.build();
                datePicker.show(getSupportFragmentManager(),"DATE_RANGE_PICKER");

                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {

                        start = Utils.getDateFromLong(selection.first);
                        end = Utils.getDateFromLong(selection.second);
                        //Log.d("TIME_TEST",start+" "+end);

                        reFreshView(dataService.queryRecordByBoth(nowType,start,end));
                    }
                });
            }
        });

        ImageButton sortByDis = findViewById(R.id.history_sort_by_distance);
        sortByDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //返回按钮
        ImageButton backButton  =  findViewById(R.id.history_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle bundle = getIntent().getExtras();
        Record.RecordType recordType = (Record.RecordType) bundle.getSerializable("activity_type");
//        String typeStr = intent.getStringExtra("activity_type");
//        switch (typeStr) {
//            case "RUNNING":
//                reFreshView(dataService.queryRecordByBoth(Record.RecordType.RUNNING,start,end));
//                break;
//            default:
//        }
        reFreshView(dataService.queryRecordByBoth(recordType,start,end));
    }

    private Context requireContext() {
        return this;
    }

    @SuppressLint("RestrictedApi")
    private void showMenu(View v, @MenuRes int menuRes) {
        PopupMenu popup = new PopupMenu(requireContext(), v);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popup.setForceShowIcon(true);
        }

        popup.getMenuInflater().inflate(menuRes, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 处理菜单项点击事件
                int id = item.getItemId();
                Button button = v.findViewById(R.id.history_popup_button);
                int len = item.getTitle().length();
                button.setText(item.getTitle().subSequence(len-2,len));
                if(id==R.id.option_1){
                    //搜索全部
                    reFreshView(dataService.getAllRecords());
                }else if(id==R.id.option_2){
                    nowType = Record.RecordType.RUNNING;
                    reFreshView(dataService.queryRecordByBoth(Record.RecordType.RUNNING,start,end));
                }else if(id==R.id.option_3){
                    nowType = Record.RecordType.RIDING;
                    reFreshView(dataService.queryRecordByBoth(Record.RecordType.RIDING,start,end));
                }else if(id==R.id.option_4){
                    nowType = Record.RecordType.SWIMMING;
                    reFreshView(dataService.queryRecordByBoth(Record.RecordType.SWIMMING,start,end));
                }else if(id==R.id.option_5){
                    nowType = Record.RecordType.WALKING;
                    reFreshView(dataService.queryRecordByBoth(Record.RecordType.WALKING,start,end));
                }
                return true;
            }
        });
        popup.show();
    }


    /**
     * @params: records, 需要更新的到视图上的数据，以dataService.getAllRecord()这样的形式传入参数
     */
    public void reFreshView(List<Record> records){
        if(records==null){
            //如果是空，说明没找到，网络错误
            Toast.makeText(TestActivity.this, "network error: 408", Toast.LENGTH_SHORT).show();
        }else{
            recordList = records;
            adapter = new RecordAdapter(recordList);
            recyclerView.setAdapter(adapter);

            //修改长度
            String newSumDistance = getSumDistance(records);
            TextView distance = findViewById(R.id.history_show_distance);
            distance.setText(newSumDistance);

            String newDuration = getSumTime(records);
            TextView duration = findViewById(R.id.history_show_duration);
            duration.setText(newDuration);

            String newCalorie = getSumTime(records);
            TextView calorie = findViewById(R.id.history_show_calorie);
            calorie.setText(newCalorie);


        }
    }

    public String getSumDistance(List<Record> list){
        double sum = 0;
        for(Record r:list){
            sum+=r.getDistance();
        }
        nowDistance = sum;
        return String.format("%.2f", sum);
    }

    public String getSumTime(List<Record> list){
        int duration = 0;
        for(Record r:list){
            duration+=r.getDuration();
        }


        int h = duration / 3600;
        int min = duration % 3600 / 60;
        //int s = duration % 60;
        return String.format(Locale.getDefault(), "%02d:%02d",h,min);
    }

    public String getCalorie(){
        int WEIGHT = 60;
        return String.format("%.1f",1.036*nowDistance*WEIGHT);
    }

}
