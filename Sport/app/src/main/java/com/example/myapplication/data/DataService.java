package com.example.myapplication.data;

import com.example.myapplication.bean.Activity;

import java.util.List;

public interface DataService {
    public List<Activity> getActivities();

    public Activity getOneActivity(int index);

    public void updateActivity(Activity activity);


}
