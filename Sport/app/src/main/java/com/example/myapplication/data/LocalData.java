package com.example.myapplication.data;

import android.provider.ContactsContract;
import android.util.Log;

import com.example.myapplication.bean.Record;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class LocalData implements DataService {
    private ArrayList<Record> records = null;

    public LocalData(){
        generateRecords();
    }

    public List<Record> getAllRecords(){
        Log.d("SAVE_TEST",""+records.size());
        records.sort(new Comparator<Record>() {
            @Override
            public int compare(Record r1, Record r2) {
                return r2.getStartTime().compareTo(r1.getStartTime());
            }
        });
        return records;
    }


    @Override
    public Record getRecord(int id){
        for(Record record: getAllRecords()){
            if(record.getId() == id){
                return record;
            }
        }

        Log.e("ID_TEST","NOT_FOUND");
        return null;
    }


    @Override
    public void updateRecord(Record record){
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
    }
}
