package com.example.myapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ChatActivity;
import com.example.myapplication.EditUserInfoActivity;
import com.example.myapplication.EquipmentActivity;
import com.example.myapplication.R;
import com.example.myapplication.StatisticActivity;
import com.example.myapplication.adapter.SportDataAdapter;
import com.example.myapplication.bean.Record;
import com.example.myapplication.data.DataService;
import com.example.myapplication.data.DataServiceFactory;
import com.example.myapplication.data.LocalData;
import com.example.myapplication.utils.PhotoUtil;
import com.example.myapplication.data.DataService;
import com.example.myapplication.data.DataServiceFactory;
import com.google.android.material.appbar.AppBarLayout;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";

    public static final int layout = R.layout.fragment_home;
    private AppCompatActivity activity;
    private View view;
    private Window window;
    private DrawerLayout drawerLayout;
    private AppBarLayout appBarLayout;
    private NestedScrollView nestedScrollView;
    private Toolbar toolbar;
    private CircleImageView avatarView;
    private TextView nicknameView;
    private ImageButton editUserInfoBtn;
    private String[] sportType = {"跑步","骑行","游泳","快走"};

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private DataService dataService;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (AppCompatActivity) getActivity();
        window = activity.getWindow();
        pref = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = pref.edit();
        dataService = DataServiceFactory.getInstance();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(layout, container, false);
        appBarLayout = view.findViewById(R.id.appBar);
        toolbar = view.findViewById(R.id.toolbar);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        avatarView = view.findViewById(R.id.userAvatar);
        nicknameView = view.findViewById(R.id.userNickname);
        editUserInfoBtn = view.findViewById((R.id.editUserInfoButton));


        // 创建一个渐变色的Drawable对象
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM, // 渐变方向，这里设置为从上到下
                new int[] {Color.parseColor("#2196F3"), Color.parseColor("#F5F5F5")} // 渐变色数组
        );

        // 设置渐变色背景
        appBarLayout.setBackground(gradientDrawable);

//        GradientDrawable gradientDrawable2 = new GradientDrawable(
//                GradientDrawable.Orientation.TOP_BOTTOM, // 渐变方向，这里设置为从上到下
//                new int[] {Color.parseColor("#87CEEB"), Color.parseColor("#F5F5F5")} // 渐变色数组
//        );
//
//        // 设置渐变色背景
//        nestedScrollView.setBackground(gradientDrawable2);

        //跳转统计数据
        ImageView statistic = view.findViewById(R.id.home_icon1);
        statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getActivity(), StatisticActivity.class);
                startActivity(intent);
            }
        });

        //跳转ai助手
        ImageView assistant = view.findViewById(R.id.home_icon3);
        assistant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("TaskType", "NormalChat");
                startActivity(intent);
            }
        });


        ImageView example_shoes = view.findViewById(R.id.imageViewCard1);
        example_shoes.setImageResource(R.drawable.shoes_example);
        TextView example_shoes_name = view.findViewById(R.id.textViewCard1Top);
        example_shoes_name.setText("Li-Ning Crazy Run X 减震跑鞋");
        TextView example_shoes_dist_text = view.findViewById(R.id.textViewCard1Bottom);
        List<Record> running_record_list = dataService.queryRecordByType(Record.RecordType.RUNNING);
        double example_shoes_dist = 0;
        for(Record record : running_record_list){
            example_shoes_dist += record.getDistance();
        }
        String dist_shoes = String.valueOf(example_shoes_dist) + "km";
        example_shoes_dist_text.setText(dist_shoes);

        View equipmentCard = view.findViewById(R.id.card2);
        equipmentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getActivity(), EquipmentActivity.class);
                startActivity(intent);
            }
        });










        // 设置系统状态栏为toolbar颜色
        int toolbarColor = ((ColorDrawable)toolbar.getBackground()).getColor();
        setSysWinColor(Color.WHITE, 1);


        // 用toolbar取代actionBar
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        View customToolbar = getLayoutInflater().inflate(R.layout.custom_toolbar, toolbar, false);
        toolbar.addView(customToolbar);

        // AppBar向上滑动渐变透明，同时toolbar逐渐显现
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                RelativeLayout relativeLayout = view.findViewById(R.id.head);

                float alpha_toolbar = (float) Math.abs(verticalOffset) / appBarLayout.getTotalScrollRange();
                float alpha = 1 - alpha_toolbar;

                relativeLayout.setAlpha(alpha);
                toolbar.setAlpha(alpha_toolbar);

                // 根据alpha_toolbar的值来设置状态栏颜色
                int sysWinAlpha = (int) (alpha_toolbar * 255);
                setSysWinColor(Color.WHITE, sysWinAlpha);
//                window.setStatusBarColor();
            }
        });


        //开启隐藏菜单
//        setOptMenu();

        //响应菜单项点击事件
//        onMenuItemClick();

        //设置背景图,更换背景图
//        ImageView imageView = view.findViewById(R.id.userBkgImg);
//        imageView.setImageResource(R.drawable.user_bkg_test);

        // 设置编辑用户信息按钮
        editUserInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EditUserInfoActivity.class);
                startActivity(intent);
            }
        });

        // 初始化运动卡片
//        try {
//            onSportCardInit();
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
        

        onTotalSportCardInit();




        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        refreshUserCard();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return true;
    }


    public void setSysWinColor(int color, int alpha) {
        if (Build.VERSION.SDK_INT >= 21) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(color);
            window.setStatusBarColor(ColorUtils.setAlphaComponent(color, alpha));
        }
    }

    public void setOptMenu() {
        setHasOptionsMenu(true);

//        if (drawerLayout == null) activity.getSupportActionBar().setTitle("OOPS");
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_format_list_bulleted_24);
        }
    }

//    public void onMenuItemClick() {
//        NavigationView navView = view.findViewById(R.id.nav_view);
//        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                //后续添加处理逻辑
//                drawerLayout.closeDrawers();
//                return true;
//            }
//        });
//    }


    public void onTotalSportCardInit() {
        List<Record> recordList = dataService.getAllRecords();

        int totalMin = 0;
        for (Record record : recordList) {
            totalMin += record.getDuration();
        }
        String totalMinStr = String.valueOf(totalMin);

        SpannableString spannableString = new SpannableString(totalMinStr+" 分钟");
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, totalMinStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(18, true), 0, totalMinStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, totalMinStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView textView = view.findViewById(R.id.minutes);
        textView.setText(spannableString);
    }

    private RecyclerView recyclerView;

    public void onSportCardInit() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        // 从数据库加载最近一次运动记录

        List<Record> runningRecords = dataService.queryRecordByBoth(Record.RecordType.RUNNING,null,null);
        List<Record> ridingRecords = dataService.queryRecordByBoth(Record.RecordType.RIDING, null, null);
        List<Record> walkingRecords = dataService.queryRecordByBoth(Record.RecordType.WALKING, null, null);
        List<Record> swimmingRecords = dataService.queryRecordByBoth(Record.RecordType.SWIMMING, null, null);

        Record runningRecord = runningRecords.isEmpty() ? new Record(Record.RecordType.RUNNING, null, null, 0.0, 0) : runningRecords.get(runningRecords.size()-1);
        Record ridingRecord = ridingRecords.isEmpty() ? new Record(Record.RecordType.RIDING, null, null, 0.0, 0) : ridingRecords.get(ridingRecords.size()-1);
        Record walkingRecord = walkingRecords.isEmpty() ? new Record(Record.RecordType.WALKING, null, null, 0.0, 0) : walkingRecords.get(walkingRecords.size()-1);
        Record swimmingRecord = swimmingRecords.isEmpty() ? new Record(Record.RecordType.SWIMMING, null, null, 0.0, 0) :swimmingRecords.get(swimmingRecords.size()-1);

//        Record runningRecord = new Record(Record.RecordType.RUNNING, sdf.parse("2021-10-01 08:00:00"), sdf.parse("2021-10-01 08:30:00"), 5.2, 1800);
//        Record ridingRecord = new Record(Record.RecordType.RIDING, sdf.parse("2021-10-02 10:00:00"), sdf.parse("2021-10-02 11:30:00"), 12.7, 5400);
//        Record walkingRecord = new Record(Record.RecordType.WALKING, sdf.parse("2021-10-03 15:30:00"), sdf.parse("2021-10-03 16:00:00"), 3.1, 1800);
//        Record swimmingRecord = new Record(Record.RecordType.SWIMMING, sdf.parse("2021-10-04 18:00:00"), sdf.parse("2021-10-04 19:30:00"), 0, 5400);
        List<Record> recordList = new ArrayList<>();
        recordList.add(runningRecord);
        recordList.add(ridingRecord);
        recordList.add(walkingRecord);
        recordList.add(swimmingRecord);

        
        //利用RecyclerView加载
        SportDataAdapter adapter = new SportDataAdapter(recordList);
//        recyclerView = view.findViewById(R.id.sportCardRecyclerView);
//        int columnCount = 2; // 每行的列数
//        int itemCount = adapter.getItemCount(); // item 的数量
//        int rowCount = (int) Math.ceil((double) itemCount / columnCount); // 计算行数
//        GridLayoutManager layoutManager = new GridLayoutManager(this.getContext(), 2, RecyclerView.VERTICAL, false) {
//            @Override
//            public boolean canScrollVertically() {
//                return false;
//            }
//        };

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);

        //利用GridView加载
//        GridView sportCardGridView = view.findViewById(R.id.sportCardGridView);
//        SportCardAdapter cardAdapter = new SportCardAdapter(this.getContext(), recordList);
//        sportCardGridView.setAdapter(cardAdapter);
//
//
//        sportCardGridView.setVerticalScrollBarEnabled(false);
//        sportCardGridView.setHorizontalScrollBarEnabled(false);
//        sportCardGridView.setScrollContainer(false);
        //动态设置gridView高度
//        int itemCount = cardAdapter.getCount();
//        int itemHeight = cardAdapter.getView(0, null, mGridView).getMeasuredHeight();
//        int totalHeight = ((int) Math.ceil(itemCount / 2.0)) * itemHeight;
//        totalHeight += mGridView.getVerticalSpacing() * (itemCount-1) + mGridView.getPaddingTop() + mGridView.getPaddingBottom();
//        ViewGroup.LayoutParams params = mGridView.getLayoutParams();
//        params.height = totalHeight;
//        mGridView.setLayoutParams(params);
    }

    public void refreshUserCard() {
        String avatarBase64Str = pref.getString("avatar", "");
        String nickname = pref.getString("nickname", "Runner");

        if (avatarBase64Str.equals("")) {
            // 设置默认头像
            avatarView.setImageResource(R.drawable.baseline_avatar_default1);
        } else {
            // 转化为bitmap
            avatarView.setImageBitmap(PhotoUtil.base64Str2Bitmap(avatarBase64Str));
        }

        nicknameView.setText(nickname);
    }
}
