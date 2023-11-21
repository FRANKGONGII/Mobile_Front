package com.example.myapplication.data;

import android.util.Log;

import com.example.myapplication.bean.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocalData implements DataService {
    private static ArrayList<Record> records = null;
    public List<Record> getAllRecords(){
        if(records == null){
            Log.d("SAVE_TEST","?");
            generateRecords();
        }
        Log.d("SAVE_TEST",""+records.size());
        return records;
    }

    @Override
    public Record getRecord(int index){
        if(getAllRecords().size() <= index){
            System.out.println("index out of bound in arr(records)");
            throw new RuntimeException();
        }

        return getAllRecords().get(index);
    }


    @Override
    public void updateRecord(Record record){
        if(records == null) generateRecords();
        Log.d("SAVE_TEST",record.toString());
        records.add(record);
        Log.d("SAVE_TEST",""+records.size());
    }

    private void generateRecords(){
        records = new ArrayList<>();
        long now = System.currentTimeMillis();
        Record a = new Record(Record.RecordType.RUNNING,new Date(now-1000000),new Date(now-9000000),2.4,1000);
        Record b = new Record(Record.RecordType.RUNNING,new Date(now-30000000),new Date(now-29999200),2.0,800);
        Record c = new Record(Record.RecordType.RIDING,new Date(now-100000000),new Date(now-99998700),5.0,1300);
        records.add(a);
        records.add(b);
        records.add(c);
        records.add(b);
        records.add(a);
        records.add(c);
        records.add(a);
        records.add(c);
    }
}
