package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.adapter.StatisticPageAdapter;
import com.example.myapplication.fragment.MonthlyFragment;
import com.example.myapplication.fragment.WeeklyFragment;
import com.example.myapplication.fragment.YearlyFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;


public class StatisticActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        ImageButton back = findViewById(R.id.statistic_back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 获取TabLayout和ViewPager的实例
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);

        // 创建适配器并将其设置给ViewPager
        StatisticPageAdapter pagerAdapter = new StatisticPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        // 将TabLayout与ViewPager关联
        tabLayout.setupWithViewPager(viewPager);

        // 添加滑块动画效果
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));


        // 添加TabLayout选中监听器
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 根据选中的位置切换对应的Fragment
                switch (tab.getPosition()) {
                    case 0:
                        //showDailyFragment();
                        break;
                    case 1:
                        //showMonthlyFragment();
                        break;
                    case 2:
                        //showYearlyFragment();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // 可选，当Tab被取消选中时的操作
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // 可选，当已经选中的Tab再次被点击时的操作
            }
        });
    }


    private void showDailyFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,new WeeklyFragment())
                .commit();
    }

    private void showMonthlyFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new MonthlyFragment())
                .commit();
    }

    private void showYearlyFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new YearlyFragment())
                .commit();
    }
}
