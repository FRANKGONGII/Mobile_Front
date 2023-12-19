package com.example.myapplication.data;

import com.example.myapplication.bean.Record;

import org.json.JSONException;

import java.util.Date;
import java.util.List;

public interface DataService {

    public List<Record> getAllRecords();

    public Record getRecord(long index);


    public long updateRecord(Record record) throws JSONException;


    public List<Record> queryRecordByTime(Date startTime, Date endTime);

    public List<Record> queryRecordByType(Record.RecordType type);

    public List<Record> queryRecordByBoth(Record.RecordType type,Date startTime, Date endTime) ;

}
