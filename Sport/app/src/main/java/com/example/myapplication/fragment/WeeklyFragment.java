package com.example.myapplication.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.bean.Record;
import com.example.myapplication.data.DataService;
import com.example.myapplication.data.DataServiceFactory;
import com.example.myapplication.layout.WeekRangeView;
import com.example.myapplication.utils.Utils;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lecho.lib.hellocharts.formatter.ColumnChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleColumnChartValueFormatter;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;


public class WeeklyFragment extends Fragment {
    View thisView;
    // 创建运动类型的数组
    String[] sportTypes = {"跑步", "健走", "游泳", "骑行"};
    String[] sportTypesEn = {"RUNNING", "WALKING", "SWIMMING", "RIDING"};
    int chosen_type = 0;
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

        //找到 WeekRangeView
        WeekRangeView weekRangeView = view.findViewById(R.id.weekRangeView);

        //TODO:周的处理
        //Set button click listeners
        //初始化，也就是进来要先找一次
        List<Record> results = dataService.queryRecordByBoth(
                Record.RecordType.valueOf(sportTypesEn[chosen_type]),
                weekRangeView.startDate,weekRangeView.endDate);
        if(results==null){
            Toast.makeText(getContext(),"查询失败",Toast.LENGTH_SHORT);
        }else{
            generateData(results);
            Log.d("DATE2_TEST","choose distance");
            drawChartDis(distance[chosen_type]);
            changePanelData();
        }

        ImageButton buttonNext = weekRangeView.nextWeekButton;
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weekRangeView.currentStartDate.add(Calendar.DAY_OF_MONTH, 7);
                weekRangeView.updateWeekRangeText();
                List<Record> results = dataService.queryRecordByBoth(
                        Record.RecordType.valueOf(sportTypesEn[chosen_type]),
                        weekRangeView.startDate,weekRangeView.endDate);
                if(results==null){
                    //没找到
                    Toast.makeText(getContext(),"查询失败",Toast.LENGTH_SHORT);
                }else {
                    generateData(results);
                    if (radio == 0) {
                        //Log.d("DATE_TEST","choose distance");
                        drawChartDis(distance[chosen_type]);
                    } else if (radio == 1) {
                        //cnt = drawTableByCnt(result);
                        drawChartDis(frequency[chosen_type]);
                    } else {
                        //cnt = drawTableByTime(result);
                        drawChartTime(duration[chosen_type]);
                    }
                    changePanelData();
                }
            }
        });

        ImageButton buttonPre = weekRangeView.prevWeekButton;
        buttonPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weekRangeView.currentStartDate.add(Calendar.DAY_OF_MONTH, -7);
                weekRangeView.updateWeekRangeText();
                List<Record> results = dataService.queryRecordByBoth(
                        Record.RecordType.valueOf(sportTypesEn[chosen_type]),
                        weekRangeView.startDate,weekRangeView.endDate);
                if(results==null){
                    //没找到
                    Toast.makeText(getContext(),"查询失败",Toast.LENGTH_SHORT);
                }else {
                    generateData(results);
                    if (radio == 0) {
                        //Log.d("DATE_TEST","choose distance");
                        drawChartDis(distance[chosen_type]);
                    } else if (radio == 1) {
                        //cnt = drawTableByCnt(result);
                        drawChartDis(frequency[chosen_type]);
                    } else {
                        //cnt = drawTableByTime(result);
                        drawChartTime(duration[chosen_type]);
                    }
                    changePanelData();
                }
            }
        });








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

        sportTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // 在这里处理选中项的逻辑
                String selectedSportType = sportTypes[position];
                chosen_type = position;
                // 打印选中项
                Log.d("TYPE_TEST",selectedSportType+"");
                // 查询
                TextView textView = thisView.findViewById(R.id.textViewRunning2);
                textView.setText("累计"+selectedSportType);

                // 查询
                List<Record> results = dataService.queryRecordByBoth(
                        Record.RecordType.valueOf(sportTypesEn[chosen_type]),
                        weekRangeView.startDate,weekRangeView.endDate);
                if(results==null){
                    //没找到
                    Toast.makeText(getContext(),"查询失败",Toast.LENGTH_SHORT);
                }else{
                    generateData(results);
                    if(radio==0){
                        //Log.d("DATE_TEST","choose distance");
                        drawChartDis(distance[chosen_type]);
                    }else if(radio==1){
                        //cnt = drawTableByCnt(result);
                        drawChartDis(frequency[chosen_type]);
                    }else{
                        //cnt = drawTableByTime(result);
                        drawChartTime(duration[chosen_type]);
                    }
                    changePanelData();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 未选中任何项时的逻辑
            }
        });







//        //选择器
//        RadioGroup radioGroupOptions = view.findViewById(R.id.radioGroupOptions2);
//        // 设置 RadioGroup 监听
//        radioGroupOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                List<Record> results = dataService.queryRecordByBoth(
//                        Record.RecordType.valueOf(sportTypesEn[chosen_type]),
//                        weekRangeView.startDate,weekRangeView.endDate);
//                // 处理选中变化的逻辑
//                if(results==null){
//                    Toast.makeText(getContext(),"获取数据错误",Toast.LENGTH_LONG);
//                    return;
//                }
//                generateData(results);
//                if (checkedId == R.id.radioButtonDistance2) {
//                    // 选中了里程
//                    Log.d("RADIO_TEST","distance");
//                    // 处理相关逻辑
//                    radio = 0;
//                    drawChartDis(distance[chosen_type]);
//                } else if (checkedId == R.id.radioButtonDays2) {
//                    // 选中了次数
//                    radio = 1;
//                    Log.d("RADIO_TEST","days");
//                    drawChartDis(frequency[chosen_type]);
//                } else if (checkedId == R.id.radioButtonDuration2) {
//                    // 选中了时长
//                    radio = 2;
//                    drawChartTime(duration[chosen_type]);
//                }
//                changePanelData();
//
//
//            }
//        });
//
//        //默认选中里程
//        RadioButton radioButtonDistance = view.findViewById(R.id.radioButtonDistance2);
//        radioButtonDistance.setChecked(true);





        //尝试另一个写法
        TabLayout tabLayout = view.findViewById(R.id.tabLayout1);

        // 添加选项卡
        tabLayout.addTab(tabLayout.newTab().setText("里程"));
        tabLayout.addTab(tabLayout.newTab().setText("次数"));
        tabLayout.addTab(tabLayout.newTab().setText("时长"));

        // 设置指示器高度和颜色
        tabLayout.setSelectedTabIndicatorHeight(getResources().getDimensionPixelSize(R.dimen.tab_indicator_height));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));

        // 设置选项卡选择监听器
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 选中时的操作
                List<Record> results = dataService.queryRecordByBoth(
                        Record.RecordType.valueOf(sportTypesEn[chosen_type]),
                        weekRangeView.startDate,weekRangeView.endDate);
                if(results==null){
                    Toast.makeText(getContext(),"获取数据错误",Toast.LENGTH_LONG);
                    return;
                }
                generateData(results);
                switch (tab.getPosition()) {
                    case 0:
                        // 处理"里程"选项卡被选中的情况
                        radio = 0;
                        drawChartDis(distance[chosen_type]);
                        Log.d("RADIO3_TEST","distance");
                        break;
                    case 1:
                        // 处理"次数"选项卡被选中的情况
                        radio = 1;
                        drawChartDis(frequency[chosen_type]);
                        Log.d("RADIO3_TEST","days");
                        break;
                    case 2:
                        // 处理"时长"选项卡被选中的情况
                        radio = 2;
                        drawChartTime(duration[chosen_type]);
                        Log.d("RADIO3_TEST","time");
                        break;
                    default:
                        break;
                }
                changePanelData();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // 未选中时的操作
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // 再次选中时的操作
            }
        });



        return  view;
    }

    private void generateData(List<Record> records){
        Utils.reSet2Array(distance);
        Utils.reSet2Array(duration);
        Utils.reSet2Array(frequency);
        Log.d("DATE_TEST","draw by type:"+sportTypesEn[chosen_type]);

        for(Record record:records){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(record.getStartTime());

            int day = calendar.get(Calendar.DAY_OF_WEEK);
            Log.d("DATE2_TEST",day+" "+record.getStartTime());
            day = (day==1)?7:day-1;
            Log.d("DATE2_TEST",day+" "+record.getStartTime());
            distance[getTypeIndex(record)][day-1]+=record.getDistance();
            frequency[getTypeIndex(record)][day-1]++;
            duration[getTypeIndex(record)][day-1]+=record.getDuration();

        }
    }
    private void drawChartDis(double[] cnt){
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        List<AxisValue> axisValues = new ArrayList(); // X轴的标注

        int columnColor = Color.parseColor("#2196F3");

        for (int i = 0; i < 7; ++i) {
            values = new ArrayList<SubcolumnValue>();
            //遍历每一列的每一个子列
            boolean ifZero = true;
            for (int j = 0; j < 1; ++j) {
                //为每一柱图添加颜色和数值

                float f = (float) cnt[i];
                if(f>0)ifZero = false;
                values.add(new SubcolumnValue(f, columnColor));
            }

            //创建Column对象
            Column column = new Column(values);
            ColumnChartValueFormatter chartValueFormatter = new SimpleColumnChartValueFormatter(1);
            column.setFormatter(chartValueFormatter);
            //是否有数据标注
            column.setHasLabels(true);
            //是否是点击圆柱才显示数据标注
            if(ifZero)column.setHasLabelsOnlyForSelected(true);
            else column.setHasLabelsOnlyForSelected(false);
            columns.add(column);
            //给x轴坐标设置描述
            axisValues.add(new AxisValue(i).setLabel(Utils.getDayInWeek(i)));
        }

        ColumnChartData data = new ColumnChartData();
        data.setColumns(columns);


        ColumnChartView chart = thisView.findViewById(R.id.chart);
        //允许交互
        chart.setInteractive(true);
        //设置缩放的轴
        chart.setZoomType(ZoomType.HORIZONTAL);
        //设置缩放的倍数
        chart.setMaxZoom(2);


        Axis axisX = new Axis(); //X轴
        axisX.setValues(axisValues);
        axisX.setMaxLabelChars(5);
        data.setAxisXBottom(axisX);//注入X轴

        chart.setColumnChartData(data);
        chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        chart.setVisibility(View.VISIBLE);
    }

    private void drawChartTime(double[] cnt){
        String[] times = new String[cnt.length];
        for(int i = 0;i<cnt.length;i++){
            times[i] = Record.parse_duration((int) cnt[i]);
        }
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        List<AxisValue> axisValues = new ArrayList(); // X轴的标注

        int columnColor = Color.parseColor("#2196F3");

        for (int i = 0; i < 7; ++i) {
            values = new ArrayList<>();
            //遍历每一列的每一个子列
            boolean ifZero = true;
            for (int j = 0; j < 1; ++j) {
                //为每一柱图添加颜色和数值
                float f = (float) cnt[i];
                if(f>0)ifZero = false;
                values.add(new SubcolumnValue(f, columnColor).setLabel(times[i]));
            }
            //创建Column对象
            Column column = new Column(values);
            ColumnChartValueFormatter chartValueFormatter = new SimpleColumnChartValueFormatter(1);
            column.setFormatter(chartValueFormatter);
            //是否有数据标注
            column.setHasLabels(true);
            //是否是点击圆柱才显示数据标注
            if(ifZero)column.setHasLabelsOnlyForSelected(true);
            else column.setHasLabelsOnlyForSelected(false);
            columns.add(column);
            //给x轴坐标设置描述
            axisValues.add(new AxisValue(i).setLabel(Utils.getDayInWeek(i)));
        }

        ColumnChartData data = new ColumnChartData();
        data.setColumns(columns);


        ColumnChartView chart = thisView.findViewById(R.id.chart);

        //允许交互
        chart.setInteractive(true);
        //设置缩放的轴
        chart.setZoomType(ZoomType.HORIZONTAL);
        //设置缩放的倍数
        chart.setMaxZoom(2);


        Axis axisX = new Axis(); //X轴
        axisX.setValues(axisValues);
        axisX.setMaxLabelChars(5);
        data.setAxisXBottom(axisX);//注入X轴

        chart.setColumnChartData(data);
        chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        chart.setVisibility(View.VISIBLE);
    }

    private int getTypeIndex(Record record){
        if(record.getRecordType().equals(Record.RecordType.RUNNING)){
            return 0;
        }else if(record.getRecordType().equals(Record.RecordType.WALKING)){
            return 1;
        }else if(record.getRecordType().equals(Record.RecordType.SWIMMING)){
            return 2;
        }else {
            return 3;
        }
    }

    private void changePanelData(){
        double sumDistance = 0;
        int days = 0;
        int sumDuration = 0;
        for(int i = 0;i<7;i++){
            sumDistance+=distance[chosen_type][i];
            sumDuration+=duration[chosen_type][i];
            if(distance[chosen_type][i]!=0)days++;
        }
        TextView headDistance = thisView.findViewById(R.id.headDistanceShow2);
        headDistance.setText(sumDistance+"km");
        TextView textViewTotalDistance = thisView.findViewById(R.id.textViewTotalDistance2);
        textViewTotalDistance.setText(sumDistance+"km");

        TextView textViewTotalDays = thisView.findViewById(R.id.textViewTotalDays2);
        textViewTotalDays.setText(days+"天");

        TextView textViewTotalDuration = thisView.findViewById(R.id.textViewTotalDuration2);
        textViewTotalDuration.setText(Record.parse_duration(sumDuration));

        TextView textViewTotalCalories = thisView.findViewById(R.id.textViewTotalCalories2);
        textViewTotalCalories.setText(Record.getCalorie((int) sumDistance)+"ka");

    }





}
