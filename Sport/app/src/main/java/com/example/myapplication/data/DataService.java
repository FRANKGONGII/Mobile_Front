package com.example.myapplication.data;

import com.example.myapplication.bean.Record;

import java.util.List;

public interface DataService {
    public List<Record> getActivities();

    public Record getOneActivity(int index);

    public void updateActivity(Record activity);


}
