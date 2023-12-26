package com.example.myapplication.bean;

// Schedule.java

import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Schedule {
    private String title;
    private String time;

    private List<String> dates;

    public Schedule(String title, String time, List<String> dates) {
        this.title = title;
        this.time = time;
        this.dates = dates;
    }

    public Schedule() {
        this.dates = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public Set<String> toSetString(){
        Set<String> set = new LinkedHashSet<>();
        set.add(title);
        set.add(time);
        set.addAll(dates);
        return set;
    }

    public static Schedule parseSet(Set<String> set){
        if(set==null)return null;
        Schedule ret = new Schedule();
        for(String s:set){
            Log.d("SAVE_TEST",s);
            if(s.matches("[0-9]+")){
                Log.d("SAVE_TEST","here1");
                ret.dates.add(s);
            }else if(s.charAt(s.length()-1)=='。'){
                Log.d("SAVE_TEST","here2");
                ret.time = s;
            }else{
                Log.d("SAVE_TEST","here3");
                ret.title = s;
            }
        }
        return ret;
    }

    // 可以根据需要添加其他属性和方法
}
