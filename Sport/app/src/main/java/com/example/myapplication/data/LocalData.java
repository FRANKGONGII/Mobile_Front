package com.example.myapplication.data;

import com.example.myapplication.bean.Activity;

import java.util.ArrayList;
import java.util.List;

public class LocalData implements DataService {
    private ArrayList<Activity> activities = null;
    @Override
    public List<Activity> getActivities(){
        if(activities == null){
            generateActvities();
        }
        return activities;
    }

    @Override
    public Activity getOneActivity(int index){
        if(getActivities().size() <= index){
            System.out.println("index out of bound in arr(activities)");
            throw new RuntimeException();
        }

        return getActivities().get(index);
    }


    @Override
    public void updateActivity(Activity activity){
        if(activities == null) generateActvities();
        activities.add(activity);
    }

    private void generateActvities(){
        activities = new ArrayList<>();
        Activity a = new Activity(Activity.SportsType.RUNNING,"12月2日",2.4,1000);
        Activity b = new Activity(Activity.SportsType.RUNNING,"11月5日",2.0,800);
        activities.add(a);
        activities.add(b);
        activities.add(a);
        activities.add(b);
        activities.add(a);
        activities.add(b);
        activities.add(a);
        activities.add(b);
    }
}
