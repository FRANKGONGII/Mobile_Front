package com.example.myapplication.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class MonthlyFragment extends Fragment {


    // 创建运动类型的数组
    String[] sportTypes = {"跑步", "健走", "游泳", "骑行"};

    public static MonthlyFragment newInstance() {
        return new MonthlyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("FRA_TEST","make");
        // 使用布局加载器加载布局文件
        View view = inflater.inflate(R.layout.fragment_month, container, false);

        // 在这里可以初始化和设置 MonthlyFragment 的视图内容

        // 例如，你可以获取 Spinner 控件的实例并设置适配器
        Spinner spinnerSportType = view.findViewById(R.id.spinnerSportType);

        // 这里可以设置适配器和监听器等
        //运动类型 Spinner
        Spinner sportTypeSpinner = view.findViewById(R.id.spinnerSportType);
        //创建适配器并设置给 Spinner
        ArrayAdapter<String> sportTypeAdapter = new ArrayAdapter<>(getContext(), R.layout.custom_spinner_item, sportTypes);
        sportTypeAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
        sportTypeSpinner.setAdapter(sportTypeAdapter);


        //默认选择跑步
        spinnerSportType.setSelection(0);

        // 设置选中监听器
        spinnerSportType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // 在这里处理选中项的逻辑
                String selectedSportType = sportTypes[position];
                // 打印选中项
                System.out.println("Selected Sport Type: " + selectedSportType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 未选中任何项时的逻辑
            }
        });


        // 在活动的onCreate或Fragment的onCreateView中
        Spinner monthSpinner = view.findViewById(R.id.spinnerMonth);

        // 创建月份的数组
        String[] months = {"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};

        // 创建适配器并设置给 Spinner
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(getContext(), R.layout.custom_spinner_item, months);
        monthAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
        monthSpinner.setAdapter(monthAdapter);





        return view;
    }
}
