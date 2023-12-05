package com.example.myapplication.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageButton;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.myapplication.EditUserInfoActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.SportCardAdapter;
import com.example.myapplication.bean.Record;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";

    public static final int layout = R.layout.fragment_home;
    private AppCompatActivity activity;
    private View view;
    private Window window;
    private  DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ImageButton editUserInfoBtn;



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
        editUserInfoBtn = view.findViewById((R.id.editUserInfoButton));

        //        drawerLayout = view.findViewById(R.id.drawer_layout);


        //设置系统状态栏为toolbar颜色
        int toolbarColor = ((ColorDrawable)toolbar.getBackground()).getColor();
        setSysWinColor(toolbarColor);

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

        //设置编辑用户信息按钮
        editUserInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EditUserInfoActivity.class);
                startActivity(intent);
            }
        });



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
        Record fitnessRecord = new Record(Record.RecordType.FITNESS, sdf.parse("2021-10-04 18:00:00"), sdf.parse("2021-10-04 19:30:00"), 0, 5400);
        List<Record> recordList = new ArrayList<>();
        recordList.add(runningRecord);
        recordList.add(ridingRecord);
        recordList.add(walkingRecord);
        recordList.add(fitnessRecord);




        //利用RecyclerView加载
//        SportDataAdapter adapter = new SportDataAdapter(recordList);
//        RecyclerView recyclerView = view.findViewById(R.id.sportsDataRecycler);
//        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
//        recyclerView.setAdapter(adapter);

        //利用GridView加载
        GridView sportCardGridView = view.findViewById(R.id.sportCardGridView);
        SportCardAdapter cardAdapter = new SportCardAdapter(this.getContext(), recordList);
        sportCardGridView.setAdapter(cardAdapter);


        sportCardGridView.setVerticalScrollBarEnabled(false);
        sportCardGridView.setHorizontalScrollBarEnabled(false);
        sportCardGridView.setScrollContainer(false);
        //动态设置gridView高度
//        int itemCount = cardAdapter.getCount();
//        int itemHeight = cardAdapter.getView(0, null, mGridView).getMeasuredHeight();
//        int totalHeight = ((int) Math.ceil(itemCount / 2.0)) * itemHeight;
//        totalHeight += mGridView.getVerticalSpacing() * (itemCount-1) + mGridView.getPaddingTop() + mGridView.getPaddingBottom();
//        ViewGroup.LayoutParams params = mGridView.getLayoutParams();
//        params.height = totalHeight;
//        mGridView.setLayoutParams(params);
    }


}
