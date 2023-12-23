package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import github.com.st235.lib_expandablebottombar.ExpandableBottomBar;
import github.com.st235.lib_expandablebottombar.navigation.ExpandableBottomBarNavigationUI;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
//    BottomNavigationView bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        viewPager = findViewById(R.id.viewPager);
//        bottomNavigation = findViewById(R.id.bottom_navigation);

        //获取navController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //通过setupWithNavController将底部导航和导航控制器进行绑定
//        NavigationUI.setupWithNavController(bottomNavigation,navController);

        ExpandableBottomBar expandableBottomBar = findViewById(R.id.expandable_bottom_bar);

        ExpandableBottomBarNavigationUI.setupWithNavController(expandableBottomBar, navController);






    }
}