package com.example.myapplication.data;

import com.example.myapplication.bean.Record;

import org.json.JSONException;

import java.util.List;

public interface DataService {
    public List<Record> getAllRecords();

    public Record getRecord(int index);

    public void updateRecord(Record record) throws JSONException;

}
