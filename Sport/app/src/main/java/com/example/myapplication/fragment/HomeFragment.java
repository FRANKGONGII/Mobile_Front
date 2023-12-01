package com.example.myapplication.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.SportDataAdapter;
import com.example.myapplication.bean.Record;
import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";

    public static final int layout = R.layout.fragment_home;
    private AppCompatActivity activity;
    private View view;
    private Window window;
    private  DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (AppCompatActivity) getActivity();
        window = activity.getWindow();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(layout, container, false);
        toolbar = view.findViewById(R.id.toolbar);
//        drawerLayout = view.findViewById(R.id.drawer_layout);


        //设置系统状态栏为透明
        setSysWinColor(Color.TRANSPARENT);

        //用toolbar取代actionBar
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(true);

        //开启隐藏菜单
//        setOptMenu();

        //响应菜单项点击事件
//        onMenuItemClick();

        //设置背景图,更换背景图
//        ImageView imageView = view.findViewById(R.id.userBkgImg);
//        imageView.setImageResource(R.drawable.user_bkg_test);

        //初始化运动卡片
        try {
            onSportCardInit();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }



        return view;
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

        SportDataAdapter adapter = new SportDataAdapter(recordList);


        //利用RecyclerView加载
        RecyclerView recyclerView = view.findViewById(R.id.sportsDataRecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        recyclerView.setAdapter(adapter);

        //利用GridView加载

//        GridView mGridView = view.findViewById(R.id.gridView);
//        mGridView.setAdapter((ListAdapter) adapter);
    }


}
