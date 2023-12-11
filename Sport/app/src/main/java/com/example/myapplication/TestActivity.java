package com.example.myapplication;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.PopupMenu;


import androidx.annotation.MenuRes;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.ListPopupWindow;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private static final int ICON_MARGIN_DP = 0; // 用你希望的dp值替换8


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);

        Button listPopupWindowButton = findViewById(R.id.history_popup_button);




        listPopupWindowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v,R.menu.history_type_menu);
            }
        });
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


//                if(id==R.id.option_1){
//
//                }else if(id==R.id.option_2){
//
//                }else if(id==R.id.option_3){
//
//                }else if(id==R.id.option_4){
//
//                }else if(id==R.id.option_5){
//
//                }
                return true;
            }
        });
        popup.show();
    }

}
