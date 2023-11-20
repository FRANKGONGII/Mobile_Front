package com.example.myapplication.data;

import com.example.myapplication.bean.Record;

import java.util.ArrayList;
import java.util.List;

public class LocalData implements DataService {
    private ArrayList<Record> activities = null;
    @Override
    public List<Record> getActivities(){
        if(activities == null){
            generateActvities();
        }
        return activities;
    }

    @Override
    public Record getOneActivity(int index){
        if(getActivities().size() <= index){
            System.out.println("index out of bound in arr(activities)");
            throw new RuntimeException();
        }

        return getActivities().get(index);
    }


    @Override
    public void updateActivity(Record activity){
        if(activities == null) generateActvities();
        activities.add(activity);
    }

    private void generateActvities(){
        activities = new ArrayList<>();
        Record a = new Record(Record.ActivityType.RUNNING,"12月2日",2.4,1000);
        Record b = new Record(Record.ActivityType.RUNNING,"11月5日",2.0,800);
        Record c = new Record(Record.ActivityType.RIDING,"11月20日",5.0,1300);
        activities.add(a);
        activities.add(b);
        activities.add(c);
        activities.add(b);
        activities.add(a);
        activities.add(c);
        activities.add(a);
        activities.add(c);
    }
}
