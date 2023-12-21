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
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

public class MonthlyFragment extends Fragment {

    View thisView;
    // 创建运动类型的数组
    String[] sportTypes = {"跑步", "健走", "游泳", "骑行"};
    String[] sportTypesEn = {"RUNNING", "WALKING", "SWIMMING", "RIDING"};
    int chosen_type = 0;
    int chosen_month = -1;
    // 创建月份的数组
    String[] months = {"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};

    double[][] duration = new double[sportTypes.length][31];
    double[][] distance = new double[sportTypes.length][31];
    double[][] frequency = new double[sportTypes.length][31];

    //默认是选里程
    int radio = 0;

    DataService dataService;


    public static MonthlyFragment newInstance() {
        return new MonthlyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("FRA_TEST","make");
        // 使用布局加载器加载布局文件
        View view = inflater.inflate(R.layout.fragment_month, container, false);
        thisView = view;

        //记录服务实例初始化
        dataService = DataServiceFactory.getInstance();

        // 在这里可以初始化和设置 MonthlyFragment 的视图内容
        // 这里可以设置适配器和监听器等
        //运动类型 Spinner
        Spinner sportTypeSpinner = view.findViewById(R.id.spinnerSportType);
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
                TextView textView = thisView.findViewById(R.id.textViewRunning);
                textView.setText("累计"+selectedSportType);

                // 查询
                List<Record> result = dataService.queryRecordByBoth(Record.RecordType.valueOf(sportTypesEn[chosen_type]),
                        get_this_month(),get_next_month(get_this_month()));
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
        Spinner monthSpinner = view.findViewById(R.id.spinnerMonth);
        // 创建适配器并设置给 Spinner
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(getContext(), R.layout.custom_spinner_item_black, months);
        monthAdapter.setDropDownViewResource(R.layout.custom_spinner_item_black);
        monthSpinner.setAdapter(monthAdapter);
        //获取现在的月份
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // 月份从0开始计数，所以需要加1
        //Log.d("CAL_TEST",currentMonth+"");
        //默认选择当前月份
        monthSpinner.setSelection(currentMonth-1);
        chosen_month = currentMonth;

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // 在这里处理选中项的逻辑
                String selectedMonth = months[position];
                // 打印选中项
                Log.d("DATE_TEST",selectedMonth+"");
                chosen_month = position+1;
                // 查询
                List<Record> result = dataService.queryRecordByBoth(Record.RecordType.valueOf(sportTypesEn[chosen_type]),
                        get_this_month(),get_next_month(get_this_month()));
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


        //选择器
        RadioGroup radioGroupOptions = view.findViewById(R.id.radioGroupOptions);
        // 设置 RadioGroup 监听
        radioGroupOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                List<Record> result = dataService.queryRecordByBoth(Record.RecordType.valueOf(sportTypesEn[chosen_type]),
                        get_this_month(),get_next_month(get_this_month()));
                // 处理选中变化的逻辑
                if(result==null){
                    Toast.makeText(getContext(),"获取数据错误",Toast.LENGTH_LONG);
                    return;
                }
                generateData(result);
                if (checkedId == R.id.radioButtonDistance) {
                    // 选中了里程
                    Log.d("RADIO_TEST","distance");
                    // 处理相关逻辑
                    radio = 0;
                    drawChartDis(distance[chosen_type]);
                } else if (checkedId == R.id.radioButtonDays) {
                    // 选中了次数
                    radio = 1;
                    Log.d("RADIO_TEST","days");
                    drawChartDis(frequency[chosen_type]);
                } else if (checkedId == R.id.radioButtonDuration) {
                    // 选中了时长
                    radio = 2;
                    drawChartTime(duration[chosen_type]);
                }
                changePanelData();


            }
        });

        //默认选中里程
        RadioButton radioButtonDistance = view.findViewById(R.id.radioButtonDistance);
        radioButtonDistance.setChecked(true);

        return view;
    }

    private Date get_this_month() {
        Log.d("DATE_TEST","chosen_month: "+chosen_month);
        if(chosen_month == -1) return null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Macao"));
        try{
            Date dt = sdf.parse(String.format("2023-%02d-01", chosen_month));

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dt);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            dt = calendar.getTime();
            Log.d("DATE_TEST",dt.toString());
            return dt;
        }
        catch(ParseException e){
            e.printStackTrace();
        }

        return null;
    }

    private Date get_next_month(Date now){
        if(now == null) return null;

        Calendar rightNow = Calendar.getInstance();
        rightNow.setTimeZone(TimeZone.getTimeZone("Asia/Macao"));
        rightNow.setTime(now);
        rightNow.add(Calendar.MONTH, 1);
        Log.d("DATE_TEST",now+" "+rightNow.getTime());
        return rightNow.getTime();
    }

    private void generateData(List<Record> records){
        reSet2Array(distance);
        reSet2Array(duration);
        reSet2Array(frequency);
        Log.d("DATE_TEST","draw by type:"+sportTypesEn[chosen_type]);
        //double[][] cnt = new double[4][31];
        for(Record record:records){
            SimpleDateFormat sdf = new SimpleDateFormat("d",Locale.ENGLISH);
            String dayStr = sdf.format(record.getStartTime());
            int day = Integer.parseInt(dayStr);

            sdf = new SimpleDateFormat("MM",Locale.ENGLISH);
            dayStr = sdf.format(record.getStartTime());
            int month = Integer.parseInt(dayStr);

            Log.d("DATE_TEST",record.getStartTime()+" "+day+" " +month);
            Log.d("DATE_TEST","chosen_month: "+chosen_month);
            if(month==chosen_month){
                distance[getTypeIndex(record)][day-1]+=record.getDistance();
                frequency[getTypeIndex(record)][day-1]++;
                duration[getTypeIndex(record)][day-1]+=record.getDuration();
            }
        }
//        for(int i = 0;i<31;i++){
//            Log.d("DATE_TEST",+(i+1)+" : "+cnt[chosen_type][i]);
//        }
        return;
    }

//    private double[] drawTableByCnt(List<Record> records){
//        Log.d("DATE_TEST","draw by type:"+sportTypesEn[chosen_type]);
//        double[][] cnt = new double[4][31];
//        for(Record record:records){
//            SimpleDateFormat sdf = new SimpleDateFormat("d",Locale.ENGLISH);
//            String dayStr = sdf.format(record.getStartTime());
//            int day = Integer.parseInt(dayStr);
//
//            sdf = new SimpleDateFormat("MM",Locale.ENGLISH);
//            dayStr = sdf.format(record.getStartTime());
//            int month = Integer.parseInt(dayStr);
//            Log.d("DATE_TEST",record.getStartTime()+" "+day+" " +month);
//            Log.d("DATE_TEST","chosen_month: "+chosen_month);
//            if(month==chosen_month)cnt[getTypeIndex(record)][day-1]++;
//        }
//        for(int i = 0;i<31;i++){
//            Log.d("DATE_TEST",+(i+1)+" : "+cnt[chosen_type][i]);
//        }
//        return cnt[chosen_type];
//    }

//    private double[] drawTableByTime(List<Record> records){
//        Log.d("DATE_TEST","draw by type:"+sportTypesEn[chosen_type]);
//        double[][] cnt = new double[4][31];
//        for(Record record:records){
//            SimpleDateFormat sdf = new SimpleDateFormat("d",Locale.ENGLISH);
//            String dayStr = sdf.format(record.getStartTime());
//            int day = Integer.parseInt(dayStr);
//
//            sdf = new SimpleDateFormat("MM",Locale.ENGLISH);
//            dayStr = sdf.format(record.getStartTime());
//            int month = Integer.parseInt(dayStr);
//
//            Log.d("DATE_TEST",record.getStartTime()+" "+day+" " +month);
//            Log.d("DATE_TEST","chosen_month: "+chosen_month);
//            if(month==chosen_month)cnt[getTypeIndex(record)][day-1]+=record.getDuration();
//        }
//        for(int i = 0;i<31;i++){
//            Log.d("DATE_TEST",+(i+1)+" : "+cnt[chosen_type][i]);
//        }
//        return cnt[chosen_type];
//    }


    //画次数和距离都是这个
    private void drawChartDis(double[] cnt){
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        List<AxisValue> axisValues = new ArrayList(); // X轴的标注

        int columnColor = Color.parseColor("#2196F3");

        for (int i = 0; i < 31; ++i) {
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
            axisValues.add(new AxisValue(i).setLabel((i+1)+"日"));
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

        for (int i = 0; i < 31; ++i) {
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
            axisValues.add(new AxisValue(i).setLabel((i+1)+"日"));
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
        for(int i = 0;i<31;i++){
            sumDistance+=distance[chosen_type][i];
            sumDuration+=duration[chosen_type][i];
            if(distance[chosen_type][i]!=0)days++;
        }
        TextView headDistance = thisView.findViewById(R.id.headDistanceShow);
        headDistance.setText(sumDistance+"km");
        TextView textViewTotalDistance = thisView.findViewById(R.id.textViewTotalDistance);
        textViewTotalDistance.setText(sumDistance+"km");

        TextView textViewTotalDays = thisView.findViewById(R.id.textViewTotalDays);
        textViewTotalDays.setText(days+"天");

        TextView textViewTotalDuration = thisView.findViewById(R.id.textViewTotalDuration);
        textViewTotalDuration.setText(Record.parse_duration(sumDuration));

        TextView textViewTotalCalories = thisView.findViewById(R.id.textViewTotalCalories);
        textViewTotalCalories.setText(Record.getCalorie((int) sumDistance)+"ka");

    }

}
