package com.example.myapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.EditUserInfoActivity;
import com.example.myapplication.R;
import com.example.myapplication.StatisticActivity;
import com.example.myapplication.adapter.SportDataAdapter;
import com.example.myapplication.bean.Record;
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
    private Toolbar toolbar;
    private CircleImageView avatarView;
    private TextView nicknameView;
    private ImageButton editUserInfoBtn;

    private DataService dataService;

    private String[] sportType = {"跑步","骑行","游泳","快走"};

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (AppCompatActivity) getActivity();
        dataService = DataServiceFactory.getInstance();
        window = activity.getWindow();
        pref = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = pref.edit();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(layout, container, false);
        appBarLayout = view.findViewById(R.id.appBar);
        toolbar = view.findViewById(R.id.toolbar);
        avatarView = view.findViewById(R.id.userAvatar);
        nicknameView = view.findViewById(R.id.userNickname);
        editUserInfoBtn = view.findViewById((R.id.editUserInfoButton));



        ImageView imageView = view.findViewById(R.id.statistic);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getActivity(), StatisticActivity.class);
                startActivity(intent);
            }
        });





        // 设置系统状态栏为toolbar颜色
        int toolbarColor = ((ColorDrawable)toolbar.getBackground()).getColor();
        setSysWinColor(toolbarColor);

        // 用toolbar取代actionBar
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        // AppBar向上滑动渐变透明，同时toolbar逐渐显现
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                RelativeLayout relativeLayout = view.findViewById(R.id.head);

                float alpha_toolbar = (float) Math.abs(verticalOffset) / appBarLayout.getTotalScrollRange();
                float alpha = 1 - alpha_toolbar;

                relativeLayout.setAlpha(alpha);
                toolbar.setAlpha(alpha_toolbar);
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
        try {
            onSportCardInit();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        List<Record> records = dataService.getAllRecords();
        if(records==null){
            Toast.makeText(getActivity(), "获取历史运动记录失败", Toast.LENGTH_SHORT).show();
        }else{
            //设置图表
//            GraphView graph = (GraphView) view.findViewById(R.id.graph);
//           DataPoint[] dataPointArray = new DataPoint[4];//默认按照类型显示
//            int[] cnt = new int[4];
//            for(Record record:records){
//                Record.RecordType recordType = record.getRecordType();
//                if(recordType== Record.RecordType.RUNNING){
//                    cnt[0]++;
//                }else if(recordType== Record.RecordType.RIDING){
//                    cnt[1]++;
//                }else if(recordType== Record.RecordType.SWIMMING){
//                    cnt[2]++;
//                }else if(recordType== Record.RecordType.WALKING){
//                    cnt[3]++;
//                }
//            }
////
//            for(int i = 0;i<4;i++){
//                dataPointArray[i] = new DataPoint(i,cnt[i]);
//            }
////
////            BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(dataPointArray);
////            graph.addSeries(series);
//
//
//
//            GraphView graph = (GraphView) view.findViewById(R.id.graph);
//            BarGraphSeries<DataPoint> series = new BarGraphSeries<>(dataPointArray);
//            graph.addSeries(series);
//
//
//            series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
//                @Override
//                public int get(DataPoint data) {
//                    return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
//                }
//            });
//
//            series.setSpacing(50);
//
//
//            //series.setDrawValuesOnTop(true);
//            //series.setValuesOnTopColor(Color.RED);
//
//
//            graph.getGridLabelRenderer().setNumHorizontalLabels(4);
//
//
//
//            graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
//                @Override
//                public String formatLabel(double value, boolean isValueX) {
//                    if (isValueX) {
//                        Log.d("NUM_TEST",value+"");
//                       if(0<=value&&value<=3){
//                           return sportType[(int) value];
//                       }
//                        return super.formatLabel(value, isValueX);
//
//                    } else {
//                        // show currency for y values
//                        return super.formatLabel(value, isValueX)+"次";
//                    }
//
//                }
//            });
        }




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

    public void setSysWinColor(int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
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

    private RecyclerView recyclerView;

    public void onSportCardInit() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Record runningRecord = new Record(Record.RecordType.RUNNING, sdf.parse("2021-10-01 08:00:00"), sdf.parse("2021-10-01 08:30:00"), 5.2, 1800);
        Record ridingRecord = new Record(Record.RecordType.RIDING, sdf.parse("2021-10-02 10:00:00"), sdf.parse("2021-10-02 11:30:00"), 12.7, 5400);
        Record walkingRecord = new Record(Record.RecordType.WALKING, sdf.parse("2021-10-03 15:30:00"), sdf.parse("2021-10-03 16:00:00"), 3.1, 1800);
        Record swimmingRecord = new Record(Record.RecordType.SWIMMING, sdf.parse("2021-10-04 18:00:00"), sdf.parse("2021-10-04 19:30:00"), 0, 5400);
        List<Record> recordList = new ArrayList<>();
        recordList.add(runningRecord);
        recordList.add(ridingRecord);
        recordList.add(walkingRecord);
        recordList.add(swimmingRecord);



        //利用RecyclerView加载
        SportDataAdapter adapter = new SportDataAdapter(recordList);
        recyclerView = view.findViewById(R.id.sportCardRecyclerView);
        int columnCount = 2; // 每行的列数
        int itemCount = adapter.getItemCount(); // item 的数量
        int rowCount = (int) Math.ceil((double) itemCount / columnCount); // 计算行数
        GridLayoutManager layoutManager = new GridLayoutManager(this.getContext(), 2, RecyclerView.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

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
            avatarView.setImageResource(R.drawable.user_bkg_test);
        } else {
            // 转化为bitmap
            avatarView.setImageBitmap(PhotoUtil.base64Str2Bitmap(avatarBase64Str));
        }

        nicknameView.setText(nickname);
    }
}
