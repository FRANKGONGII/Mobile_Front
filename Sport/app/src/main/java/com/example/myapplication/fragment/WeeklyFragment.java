package com.example.myapplication.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.bean.Record;
import com.example.myapplication.data.DataService;
import com.example.myapplication.data.DataServiceFactory;
import com.example.myapplication.layout.WeekRangeView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class WeeklyFragment extends Fragment {
    View thisView;
    // 创建运动类型的数组
    String[] sportTypes = {"跑步", "健走", "游泳", "骑行"};
    String[] sportTypesEn = {"RUNNING", "WALKING", "SWIMMING", "RIDING"};
    int chosen_type = 0;
    int chosen_week = 0;
    // 创建月份的数组
    String[] weeks = {"第一周","第二周","第三周","第四周"};

    double[][] duration = new double[sportTypes.length][7];
    double[][] distance = new double[sportTypes.length][7];
    double[][] frequency = new double[sportTypes.length][7];

    //默认是选里程
    int radio = 0;

    DataService dataService;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("FRA_TEST","make");
        // 使用布局加载器加载布局文件
        View view = inflater.inflate(R.layout.fragment_week, container, false);
        thisView = view;

        //记录服务实例初始化
        dataService = DataServiceFactory.getInstance();

        // 在这里可以初始化和设置 MonthlyFragment 的视图内容
        // 这里可以设置适配器和监听器等
        //运动类型 Spinner
        Spinner sportTypeSpinner = view.findViewById(R.id.spinnerSportType2);
        //创建适配器并设置给 Spinner
        ArrayAdapter<String> sportTypeAdapter = new ArrayAdapter<>(getContext(), R.layout.custom_spinner_item, sportTypes);
        sportTypeAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
        sportTypeSpinner.setAdapter(sportTypeAdapter);

        //默认选择跑步
        sportTypeSpinner.setSelection(0);
        chosen_type = 0;

//        sportTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                // 在这里处理选中项的逻辑
//                String selectedSportType = sportTypes[position];
//                chosen_type = position;
//                // 打印选中项
//                Log.d("TYPE_TEST",selectedSportType+"");
//                // 查询
//                //TextView textView = thisView.findViewById(R.id.textViewRunning2);
//                //textView.setText("累计"+selectedSportType);
//
//                // 查询
//                List<Record> result = dataService.queryRecordByBoth(Record.RecordType.valueOf(sportTypesEn[chosen_type]),
//                        get_this_week(),get_next_week(get_this_week()));
//                if(result==null){
//                    //没找到
//                    Toast.makeText(getContext(),"查询失败",Toast.LENGTH_SHORT);
//                }else{
//                    generateData(result);
//                    if(radio==0){
//                        //Log.d("DATE_TEST","choose distance");
//                        drawChartDis(distance[chosen_type]);
//                    }else if(radio==1){
//                        //cnt = drawTableByCnt(result);
//                        drawChartDis(frequency[chosen_type]);
//                    }else{
//                        //cnt = drawTableByTime(result);
//                        drawChartTime(duration[chosen_type]);
//                    }
//                    changePanelData();
//                }
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // 未选中任何项时的逻辑
//            }
//        });




        //TODO:周的处理
        //找到 WeekRangeView
        WeekRangeView weekRangeView = view.findViewById(R.id.weekRangeView);
        // Set button click listeners



        return  view;
    }

    public Date get_this_week(){
        return null;
    }

    public Date get_next_week(Date date){
        return null;
    }

    public void generateData(List<Record> records){

    }
    public void drawChartDis(double[] cnt){

    }
    public void drawChartTime(double[] cnt){

    }
    public void changePanelData(){

    }


}
