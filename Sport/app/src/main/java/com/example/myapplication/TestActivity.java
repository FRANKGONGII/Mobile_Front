package com.example.myapplication;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.TypedValue;


import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



import androidx.annotation.MenuRes;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.Resource;
import com.example.myapplication.adapter.RecordAdapter;
import com.example.myapplication.bean.Record;
import com.example.myapplication.data.DataService;
import com.example.myapplication.data.DataServiceFactory;
import com.example.myapplication.utils.Utils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.w3c.dom.Text;

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

    private CoordinatorLayout coordinatorLayout;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private View customToolbar;

    //Record.RecordType recordType = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test2);

        //列表初始化
        dataService = DataServiceFactory.getInstance();
        recordList = dataService.getAllRecords();
        if(recordList == null){
            Toast.makeText(this, "network error: 408", Toast.LENGTH_SHORT).show();
            recordList = new ArrayList<>();
        }

        coordinatorLayout = findViewById(R.id.root_layout);
        appBarLayout = findViewById(R.id.appBar);
        // 用toolbar取代actionBar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        customToolbar = getLayoutInflater().inflate(R.layout.custom_toolbar2, toolbar, false);
        toolbar.addView(customToolbar);


        TextView textView = customToolbar.findViewById(R.id.title);
        ImageButton imageButton = findViewById(R.id.history_back_button);
        Drawable drawable = imageButton.getDrawable();

        setSysWinColor(Color.WHITE, 1);

        // AppBar向上滑动渐变透明，同时toolbar逐渐显现
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                LinearLayout linearLayout = findViewById(R.id.head);

                float alpha_toolbar = (float) Math.abs(verticalOffset) / appBarLayout.getTotalScrollRange();
                float alpha = 1 - alpha_toolbar;

                linearLayout.setAlpha(alpha);
                if (alpha < alpha_toolbar) {
                    coordinatorLayout.setBackgroundColor(Color.WHITE);
                    toolbar.setBackgroundColor(Color.WHITE);
//                    TextView textView = customToolbar.findViewById(R.id.title);
                    textView.setTextColor(ContextCompat.getColor(TestActivity.this, R.color.black));
//                    ImageButton imageButton = findViewById(R.id.history_back_button);
//                    Drawable drawable = imageButton.getDrawable();
                    drawable.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    getWindow().setStatusBarColor(Color.WHITE);

                } else {
                    coordinatorLayout.setBackgroundColor(Color.parseColor("#87CEEB"));
                    toolbar.setBackgroundColor(Color.parseColor("#87CEEB"));
//                    TextView textView = customToolbar.findViewById(R.id.title);
                    textView.setTextColor(ContextCompat.getColor(TestActivity.this, R.color.white));
//                    ImageButton imageButton = findViewById(R.id.history_back_button);
//                    Drawable drawable = imageButton.getDrawable();
                    drawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
                    setSysWinColor(Color.WHITE, 1);
                }

//                customToolbar.setAlpha(alpha);

                // 根据alpha_toolbar的值来设置状态栏颜色
                int sysWinAlpha = (int) (alpha_toolbar * 255);
//                setSysWinColor(Color.WHITE, sysWinAlpha);
//                window.setStatusBarColor();
            }
        });






        Log.d("CHECK","1");

        recyclerView = findViewById(R.id.history_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);//添加布局管理器
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);  //设置为纵向水平滚动
        recyclerView.setLayoutManager(layoutManager);//设置布局管理器
        adapter = new RecordAdapter(recordList);
        recyclerView.setAdapter(adapter);
        Log.d("CHECK","2");


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

        Log.d("CHECK","3");

        //时间选择按钮
        Button sortByTime = findViewById(R.id.history_time);
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

        Log.d("CHECK","4");

        Button sortByDis = findViewById(R.id.history_distance);
        sortByDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Log.d("CHECK","5");

        //返回按钮
        ImageButton backButton  =  customToolbar.findViewById(R.id.history_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        Log.d("CHECK","6");

    }

    public void setSysWinColor(int color, int alpha) {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(color);
            getWindow().setStatusBarColor(ColorUtils.setAlphaComponent(color, alpha));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle bundle = getIntent().getExtras();



        if(nowType!=null)Log.d("DATE_TEST",nowType.getStr());

        if(bundle!=null){
            Log.d("DATE_TEST","here!");
            Record.RecordType recordType = (Record.RecordType) bundle.getSerializable("activity_type");
            nowType = recordType == null ? nowType : recordType;

        }

        if(nowType!=null)Log.d("DATE_TEST",nowType.getStr());
//        String typeStr = intent.getStringExtra("activity_type");
//        switch (typeStr) {
//            case "RUNNING":
//                reFreshView(dataService.queryRecordByBoth(Record.RecordType.RUNNING,start,end));
//                break;
//            default:
//        }
        //选择类型按钮
//        Button listPopupWindowButton = findViewById(R.id.history_popup_button);
//        listPopupWindowButton.setText();






        reFreshView(dataService.queryRecordByBoth(nowType, start, end));
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
                    nowType=null;
                    Log.d("DATE_TEST","change to null");
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
            Button type = findViewById(R.id.history_popup_button);
            type.setText(nowType==null?"全部":nowType.getStr());

            //修改长度
            String newSumDistance = getSumDistance(records);
            SpannableString spannableString = new SpannableString("累计里程\n"+ newSumDistance + "公里");
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 5, 5+newSumDistance.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new AbsoluteSizeSpan(40, true), 5, 5+newSumDistance.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 5, 5+newSumDistance.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            TextView distance = findViewById(R.id.history_show_distance);
            distance.setText(spannableString);



            String newDuration = getSumTime(records);
            SpannableString spannableString2 = new SpannableString("累计时间\n"+ newDuration + "小时");
            spannableString2.setSpan(new StyleSpan(Typeface.BOLD), 5, 5+newDuration.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString2.setSpan(new AbsoluteSizeSpan(40, true), 5, 5+newDuration.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString2.setSpan(new ForegroundColorSpan(Color.WHITE), 5, 5+newDuration.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            TextView duration = findViewById(R.id.history_show_duration);
            duration.setText(spannableString2);

//            String newCalorie = getCalorie();
//            SpannableString spannableString3 = new SpannableString("累计消耗\n"+ newCalorie + "千卡");
//            spannableString3.setSpan(new StyleSpan(Typeface.BOLD), 5, 5+newCalorie.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            spannableString3.setSpan(new AbsoluteSizeSpan(30, true), 5, 5+newCalorie.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            spannableString3.setSpan(new ForegroundColorSpan(Color.WHITE), 5, 5+newCalorie.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            TextView calorie = findViewById(R.id.history_show_calorie);
//            calorie.setText(spannableString3);


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
