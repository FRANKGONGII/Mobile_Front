package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ThirdPartyAdapter;
import com.example.myapplication.bean.ThirdPartyBean;

import java.util.ArrayList;
import java.util.List;

public class EquipmentActivity extends AppCompatActivity {
    private ListView listView;
    private List<ThirdPartyBean> contentList;
    private ThirdPartyAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_equipment);
        initView();
    }

    public void initView(){
        contentList = new ArrayList<>();
        adapter = new ThirdPartyAdapter(contentList, this);
        listView = findViewById(R.id.equipment_listView);
        listView.setAdapter(adapter);

        setConstantList();
    }

    //直接加载预制的特定数据，没有实际数据库时用
    public void setConstantList(){
        ThirdPartyBean bean = new ThirdPartyBean("华为运动健康", R.drawable.app_1);
        contentList.add(bean);
        bean = new ThirdPartyBean("Garmin", R.drawable.app_2);
        contentList.add(bean);
        bean = new ThirdPartyBean("COROS", R.drawable.app_3);
        contentList.add(bean);
        bean = new ThirdPartyBean("如骏ROOZYM", R.drawable.app_4);
        contentList.add(bean);
        bean = new ThirdPartyBean("欢太健康", R.drawable.app_5);
        contentList.add(bean);
        bean = new ThirdPartyBean("vivo健康", R.drawable.app_6);
        contentList.add(bean);
        bean = new ThirdPartyBean("Zepp", R.drawable.app_7);
        contentList.add(bean);
        bean = new ThirdPartyBean("Suunto", R.drawable.app_8);
        contentList.add(bean);

        adapter.notifyDataSetChanged();
    }

    // 所有显示未完成的功能的onClick事件
    public void undoneClick(View view){
        Toast.makeText(view.getContext(), "功能开发中....", Toast.LENGTH_SHORT).show();
    }
}
