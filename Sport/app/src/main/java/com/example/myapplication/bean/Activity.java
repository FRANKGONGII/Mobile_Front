package com.example.myapplication.bean;

import java.util.Locale;

enum SportsType{
    RUNNING,RIDING,WALKING,FITNESS
}
public class Activity {
    SportsType sportsType;
    String recordTime; // 运动日期
    double distance; // 跑步距离
    int duration; // 跑步时间，秒计

    Activity(String time,double dist,int duration){
        this.recordTime = time;
        this.distance = dist;
        this.duration = duration;
    }

    public String getRecordTime(){ return recordTime; }

    public String getDistance(){ return String.valueOf(distance); }

    public String getDuration(){ return parse_duration(duration); }

    public String parse_duration(int duration){
        int h = duration / 1440;
        int min = duration % 1440 / 60;
        int s = duration % 60;
        return String.format(Locale.getDefault(), "%2d:%2d:%2d",h,min,s);
    }
}
