package com.example.myapplication.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.android.material.tabs.TabLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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

public class YearlyFragment extends Fragment {

    View thisView;
    // 创建运动类型的数组
    String[] sportTypes = {"跑步", "健走", "游泳", "骑行"};
    String[] sportTypesEn = {"RUNNING", "WALKING", "SWIMMING", "RIDING"};
    int chosen_type = 0;
    //默认选择2023年
    int chosen_year = 0;
    // 创建月份的数组
    String[] years = {"2023","2024"};

    double[][] duration = new double[sportTypes.length][12];
    double[][] distance = new double[sportTypes.length][12];
    double[][] frequency = new double[sportTypes.length][12];

    //默认是选里程
    int radio = 0;

    DataService dataService;

    private final static int START_YEAR = 2023;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("FRA_TEST","make");
        // 使用布局加载器加载布局文件
        View view = inflater.inflate(R.layout.fragment_year, container, false);
        thisView = view;

        //记录服务实例初始化
        dataService = DataServiceFactory.getInstance();

        // 在这里可以初始化和设置 MonthlyFragment 的视图内容
        // 这里可以设置适配器和监听器等
        //运动类型 Spinner
        Spinner sportTypeSpinner = view.findViewById(R.id.spinnerSportType3);
        //创建适配器并设置给 Spinner
        ArrayAdapter<String> sportTypeAdapter = new ArrayAdapter<>(getContext(), R.layout.custom_spinner_item, sportTypes);
        sportTypeAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
        sportTypeSpinner.setAdapter(sportTypeAdapter);

        //默认选择跑步
        sportTypeSpinner.setSelection(0);
        chosen_type = 0;

        // 设置选中监听器
        sportTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // 在这里处理选中项的逻辑
                String selectedSportType = sportTypes[position];
                chosen_type = position;
                // 打印选中项
                Log.d("TYPE_TEST",selectedSportType+"");
                // 查询
                TextView textView = thisView.findViewById(R.id.textViewRunning3);
                textView.setText("累计"+selectedSportType);

                // 查询
                List<Record> result = dataService.queryRecordByBoth(Record.RecordType.valueOf(sportTypesEn[chosen_type]),
                        get_this_year(), get_next_year(get_this_year()));
                if(result==null){
                    //没找到
                    Toast.makeText(getContext(),"查询失败",Toast.LENGTH_SHORT);
                }else{
                    //构建根据另一个选项图表
                    //double[] cnt; // 记录次数或者距离
                    //String[] timeCnt; // 记录时间
                    generateData(result);
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


        // 在活动的onCreate或Fragment的onCreateView中
        Spinner yearSpinner = view.findViewById(R.id.spinnerYear);
        // 创建适配器并设置给 Spinner
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(getContext(), R.layout.custom_spinner_item_black, years);
        monthAdapter.setDropDownViewResource(R.layout.custom_spinner_item_black);
        yearSpinner.setAdapter(monthAdapter);
        //获取现在的月份
        //TODO: 改成获取年
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR); // 月份从0开始计数，所以需要加1
        Log.d("CAL_TEST3",currentYear+"");
        //默认选择当前月份
        yearSpinner.setSelection(currentYear-2023);
        chosen_year = currentYear;

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // 在这里处理选中项的逻辑
                String selectedYear = years[position];
                // 打印选中项
                Log.d("DATE_TEST3",selectedYear+"");
                chosen_year = Integer.parseInt(years[position]);
                // 查询
                List<Record> result = dataService.queryRecordByBoth(Record.RecordType.valueOf(sportTypesEn[chosen_type]),
                        get_this_year(), get_next_year(get_this_year()));
                Log.d("CAL_TEST3",get_this_year()+" "+get_next_year(get_this_year()));
                if(result==null){
                    //没找到
                    Toast.makeText(getContext(),"查询失败",Toast.LENGTH_SHORT);
                }else{
                    //构建根据另一个选项图表
                    generateData(result);
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
//        RadioGroup radioGroupOptions = view.findViewById(R.id.radioGroupOptions3);
//        // 设置 RadioGroup 监听
//        radioGroupOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                List<Record> result = dataService.queryRecordByBoth(Record.RecordType.valueOf(sportTypesEn[chosen_type]),
//                        get_this_year(), get_next_year(get_this_year()));
//                // 处理选中变化的逻辑
//                if(result==null){
//                    Toast.makeText(getContext(),"获取数据错误",Toast.LENGTH_LONG);
//                    return;
//                }
//                generateData(result);
//                if (checkedId == R.id.radioButtonDistance3) {
//                    // 选中了里程
//                    Log.d("RADIO_TEST","distance");
//                    // 处理相关逻辑
//                    radio = 0;
//                    drawChartDis(distance[chosen_type]);
//                } else if (checkedId == R.id.radioButtonDays3) {
//                    // 选中了次数
//                    radio = 1;
//                    Log.d("RADIO_TEST","days");
//                    drawChartDis(frequency[chosen_type]);
//                } else if (checkedId == R.id.radioButtonDuration3) {
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
//        RadioButton radioButtonDistance = view.findViewById(R.id.radioButtonDistance3);
//        radioButtonDistance.setChecked(true);


        //尝试另一个写法
        TabLayout tabLayout = view.findViewById(R.id.tabLayout2);

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
                List<Record> results = dataService.queryRecordByBoth(Record.RecordType.valueOf(sportTypesEn[chosen_type]),
                       get_this_year(), get_next_year(get_this_year()));
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

        return view;
    }



    private Date get_this_year() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, chosen_year);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private Date get_next_year(Date now){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.YEAR, 1);         // 年份加1
        calendar.set(Calendar.DAY_OF_YEAR, 1); // 将日设置为1
        calendar.set(Calendar.HOUR_OF_DAY, 0); // 将小时设置为0
        calendar.set(Calendar.MINUTE, 0);      // 将分钟设置为0
        calendar.set(Calendar.SECOND, 0);      // 将秒设置为0
        calendar.set(Calendar.MILLISECOND, 0); // 将毫秒设置为0
        return calendar.getTime();
    }

    private void generateData(List<Record> records){
        reSet2Array(distance);
        reSet2Array(duration);
        reSet2Array(frequency);
        Log.d("DATE_TEST3","draw by type:"+sportTypesEn[chosen_type]);
        //double[][] cnt = new double[4][31];
        for(Record record:records){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(record.getStartTime());

            int month = calendar.get(Calendar.MONTH);
            Log.d("DATE_TEST3",record.getStartTime()+" "+month);
            Log.d("DATE_TEST3","chosen_year: "+chosen_year);

            distance[getTypeIndex(record)][month]+=record.getDistance();
            frequency[getTypeIndex(record)][month]++;
            duration[getTypeIndex(record)][month]+=record.getDuration();
        }
    }



    //画次数和距离都是这个
    private void drawChartDis(double[] cnt){
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        List<AxisValue> axisValues = new ArrayList(); // X轴的标注

        int columnColor = Color.parseColor("#2196F3");

        for (int i = 0; i < 12; ++i) {
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
            Log.d("DATA_TEST",ifZero+"");
            columns.add(column);
            //给x轴坐标设置描述
            axisValues.add(new AxisValue(i).setLabel((i+1)+"月"));
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

        for (int i = 0; i < 12; ++i) {
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
            Log.d("DATA_TEST",ifZero+"");
            columns.add(column);
            //给x轴坐标设置描述
            axisValues.add(new AxisValue(i).setLabel((i+1)+"月"));
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

    private void reSet2Array(double[][] array){
        for(int i = 0;i<array.length;i++){
            for(int j = 0;j<array[0].length;j++){
                array[i][j] = 0;
            }
        }
    }

    private void changePanelData(){
        double sumDistance = 0;
        int days = 0;
        int sumDuration = 0;
        for(int i = 0;i<12;i++){
            sumDistance+=distance[chosen_type][i];
            sumDuration+=duration[chosen_type][i];
            if(distance[chosen_type][i]!=0)days++;
        }
        TextView headDistance = thisView.findViewById(R.id.headDistanceShow3);
        headDistance.setText(sumDistance+"km");
        TextView textViewTotalDistance = thisView.findViewById(R.id.textViewTotalDistance3);
        textViewTotalDistance.setText(sumDistance+"km");

        TextView textViewTotalDays = thisView.findViewById(R.id.textViewTotalDays3);
        textViewTotalDays.setText(days+"天");

        TextView textViewTotalDuration = thisView.findViewById(R.id.textViewTotalDuration3);
        textViewTotalDuration.setText(Record.parse_duration(sumDuration));

        TextView textViewTotalCalories = thisView.findViewById(R.id.textViewTotalCalories3);
        textViewTotalCalories.setText(Record.getCalorie((int) sumDistance)+"ka");

    }



}
