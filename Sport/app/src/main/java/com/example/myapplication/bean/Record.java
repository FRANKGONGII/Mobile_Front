package com.example.myapplication.bean;

import com.amap.api.maps2d.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class Record {
    public enum ActivityType {
        RUNNING,RIDING,WALKING,FITNESS;
        private static final HashMap<ActivityType,String> mp = new HashMap<ActivityType,String>(){{
            put(RUNNING,"跑步");
            put(RIDING,"骑行");
            put(WALKING,"健走");
            put(FITNESS,"健身");
        }};

        public String getStr(){
            return mp.get(this);
        }
    }
    ActivityType activityType;
    String recordTime; // 运动日期
    double distance; // 跑步距离
    int duration; // 跑步时间，秒计

    //public List<LatLng> latLngList = new ArrayList();

    public Record(ActivityType activityType, String time, double dist, int duration){
        this.activityType = activityType;
        this.recordTime = time;
        this.distance = dist;
        this.duration = duration;
    }

    public String getRecordTime(){ return recordTime; }

    public String getDistance(){ return String.valueOf(distance); }

    public String getDuration(){ return parse_duration(duration); }

    public String getType(){ return activityType.getStr(); }

    public String parse_duration(int duration){
        int h = duration / 1440;
        int min = duration % 1440 / 60;
        int s = duration % 60;
        return String.format(Locale.getDefault(), "%2d:%2d:%2d",h,min,s);
    }
}
