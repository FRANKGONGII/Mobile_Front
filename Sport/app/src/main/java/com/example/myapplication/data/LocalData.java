package com.example.myapplication.data;

import android.util.Log;

import com.example.myapplication.bean.Record;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class LocalData implements DataService {
    private ArrayList<Record> records = null;

    public LocalData(){
        generateRecords();
        Record.id_counter = getAllRecords().size();
    }

    public List<Record> getAllRecords(){
        records.sort(new Comparator<Record>() {
            @Override
            public int compare(Record r1, Record r2) {
                return r2.getStartTime().compareTo(r1.getStartTime());
            }
        });
        return records;
    }


    @Override
    public Record getRecord(long id){
        for(Record record: getAllRecords()){
            if(record.getId() == id){
                return record;
            }
        }

        Log.e("Record","NOT_FOUND");
        return null;
    }


    @Override
    public long updateRecord(Record record){
        records.add(record);
        return record.id+1;
    }

    @Override
    public List<Record> queryRecordByTime(Date startTime, Date endTime) {
        List<Record> ret = new ArrayList<>();
        for(Record record: getAllRecords()){
            if(startTime.compareTo(record.getStartTime()) < 0 && record.getEndTime().compareTo(endTime) < 0){
                ret.add(record);
            }
        }
        return ret;
    }

    @Override
    public List<Record> queryRecordByType(Record.RecordType type) {
        List<Record> ret = new ArrayList<>();
        for(Record record: getAllRecords()){
            if(record.getRecordTypeByStr().equals(type.getStr())){
                ret.add(record);
            }
        }
        return ret;
    }

    @Override
    public List<Record> queryRecordByBoth(Record.RecordType type, Date startTime, Date endTime) {
        if(type == null && startTime == null){
            return getAllRecords();
        }
        else if(type == null){
            return queryRecordByTime(startTime,endTime);
        }
        else if(startTime == null){
            return queryRecordByType(type);
        }
        else{
            List<Record> ret = new ArrayList<>();
            for(Record record: getAllRecords()){
                if(record.getRecordTypeByStr().equals(type.getStr()) && startTime.compareTo(record.getStartTime()) < 0 && record.getEndTime().compareTo(endTime) < 0){
                    ret.add(record);
                }
            }
            return ret;
        }
    }

    private void generateRecords(){
        records = new ArrayList<>();
        long now = System.currentTimeMillis();
        Record a = new Record(Record.RecordType.RUNNING,new Date(now-1000000),new Date(now-9000000),2.4,1000);
        Record b = new Record(Record.RecordType.RUNNING,new Date(now-30000000),new Date(now-29999200),2.0,800);
        Record c = new Record(Record.RecordType.SWIMMING,new Date(now-1000000000),new Date(now-999000000),0.6,600);
        Record d = new Record(Record.RecordType.RIDING,new Date(now-5000000000L),new Date(now-4999998700L),5.0,1300);
        Record e = new Record(Record.RecordType.RIDING,new Date(now),new Date(now),5.0,1300);
        records.add(a);
        records.add(b);
        records.add(c);
        records.add(d);
        records.add(e);
    }
}
