package com.example.myapplication.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.bean.Record;
import com.example.myapplication.data.DataService;
import com.example.myapplication.data.DataServiceFactory;

import java.util.List;

public class Medal1Fragment extends Fragment {

    DataService dataService = DataServiceFactory.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Log.d("MEDAL_TEST","here!!");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_medal1, container, false);

        // 获取布局中的组件
        TextView itemText1 = view.findViewById(R.id.itemText1_1);
        TextView itemText2 = view.findViewById(R.id.itemText2_1);
        TextView itemText3 = view.findViewById(R.id.itemText3_1);
        TextView itemText4 = view.findViewById(R.id.itemText4_1);
        TextView itemText5 = view.findViewById(R.id.itemText5_1);
        TextView itemText6 = view.findViewById(R.id.itemText6_1);

        List<Record> results = dataService.getAllRecords();
        if(results==null){
            Toast.makeText(getContext(),"网络错误",Toast.LENGTH_LONG);
        }else{
            if(ifHaveRun(results))itemText1.setText("已获得！");
            if(ifHaveCycle(results))itemText2.setText("已获得！");
            if(ifHaveSwim(results))itemText3.setText("已获得！");
            if(ifHaveWalk(results))itemText4.setText("已获得！");
            itemText5.setText("已获得！");
            if(ifHaveMara(results))itemText6.setText("已获得！");
        }





        return view;
    }

    private boolean ifHaveRun(List<Record> list){
        for(Record record:list){
            if(record.getRecordType().equals(Record.RecordType.RUNNING)){
                return true;
            }
        }
        return false;
    }

    private boolean ifHaveSwim(List<Record> list){
        for(Record record:list){
            if(record.getRecordType().equals(Record.RecordType.RUNNING)){
                return true;
            }
        }
        return false;
    }

    private boolean ifHaveWalk(List<Record> list){
        for(Record record:list){
            if(record.getRecordType().equals(Record.RecordType.RUNNING)){
                return true;
            }
        }
        return false;
    }

    private boolean ifHaveCycle(List<Record> list){
        for(Record record:list){
            if(record.getRecordType().equals(Record.RecordType.RUNNING)){
                return true;
            }
        }
        return false;
    }

    private boolean ifHaveMara(List<Record> list){
        for(Record record:list){
            if(record.getRecordType().equals(Record.RecordType.RUNNING)
            &&record.getDistance()>42.12){
                return true;
            }
        }
        return false;
    }
}
