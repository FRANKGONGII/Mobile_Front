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
    private String key;
    private List<String> dates;

    public Schedule(String title, String time, List<String> dates) {
        this.title = title;
        this.time = time;
        this.dates = dates;
    }

    public Schedule(String title, String time, List<String> dates, String key) {
        this.title = title;
        this.time = time;
        this.dates = dates;
        this.key = key;
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
    public String getKey() {return key;}

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

    public static Schedule newParseSet(Set<String> set, String key){
//        if(set==null)return null;
        Schedule ret = new Schedule();
        for(String s:set){
            Log.d("SET",s);
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
        ret.key = key;
        return ret;
    }


    public  List<String> getDates(){
        return dates;
    }


    public boolean ifInDates(String s){
        for(String str:dates){
            Log.d("FINISH_TEST",str);
            if(s.equals(str)){
                return true;
            }
        }
        return false;
    }


    // 可以根据需要添加其他属性和方法
}
