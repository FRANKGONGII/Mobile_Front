package com.example.myapplication.data;

import android.util.Log;

import com.example.myapplication.bean.Record;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class LocalData implements DataService {
    private static ArrayList<Record> records = null;

    static{
        generateRecords();
    }

    public List<Record> getAllRecords(){
        Log.d("SAVE_TEST",""+records.size());
        Collections.sort(records, new Comparator<Record>(){
            @Override
            public int compare(Record r1, Record r2) {
                return r2.getStartTime().compareTo(r1.getStartTime());
            }
        });
        return records;
    }

    @Override
    public Record getRecord(int id){
        int index = id-1; // id从1开始计数，需要-1
        if(index >= getAllRecords().size()){
            System.out.println("index out of bound in arr(records)");
            throw new RuntimeException();
        }

        return getAllRecords().get(index);
    }


    @Override
    public void updateRecord(Record record){
        records.add(record);
        Log.d("SAVE_TEST",""+records.size());
    }

    @Override
    public void init(){
        generateRecords();
    }

    private static void generateRecords(){
        records = new ArrayList<>();
        long now = System.currentTimeMillis();
        Record a = new Record(Record.RecordType.RUNNING,new Date(now-1000000),new Date(now-9000000),2.4,1000);
        Record b = new Record(Record.RecordType.RUNNING,new Date(now-30000000),new Date(now-29999200),2.0,800);
        Record c = new Record(Record.RecordType.RIDING,new Date(now-100000000),new Date(now-99998700),5.0,1300);
        records.add(a);
        records.add(b);
        records.add(c);
    }
}
