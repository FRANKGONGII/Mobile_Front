package com.example.myapplication.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myapplication.StatisticActivity;

import com.example.myapplication.fragment.MonthlyFragment;
import com.example.myapplication.fragment.WeeklyFragment;
import com.example.myapplication.fragment.YearlyFragment;

public class StatisticPageAdapter extends FragmentPagerAdapter {

    public StatisticPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }



    @Override
    public Fragment getItem(int position) {
        // 返回对应选项卡的Fragment
        switch (position) {
            case 0:
                return new WeeklyFragment();
            case 1:
                return new MonthlyFragment();
            case 2:
                return new YearlyFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // 返回选项卡的数量
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // 返回选项卡的标题
        switch (position) {
            case 0:
                return "本周";
            case 1:
                return "本月";
            case 2:
                return "本年";
            default:
                return null;
        }
    }
}
