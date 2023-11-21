package com.example.myapplication.bean;

import android.util.Log;

import com.amap.api.maps2d.model.LatLng;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Record {
    public enum RecordType {
        RUNNING,RIDING,WALKING,FITNESS;
        private static final HashMap<RecordType,String> mp = new HashMap<RecordType,String>(){{
            put(RUNNING,"跑步");
            put(RIDING,"骑行");
            put(WALKING,"健走");
            put(FITNESS,"健身");
        }};

        public static RecordType getValue(String type) {
            for(Map.Entry<RecordType,String> entry : mp.entrySet()){
                if(entry.getValue().equals(type)){
                    return entry.getKey();
                }
            }

            Log.d("Record","Undefined type");
            return null;
        }

        public String getStr(){
            return mp.get(this);
        }
    }

    private static int id_counter = 0;

    public int id;
    RecordType recordType;
    Date startTime;

    Date endTime;
    double distance; // 跑步距离
    int duration; // 跑步时间，秒计

    public List<LatLng> latLngList;

    public Record(RecordType recordType, Date startTime, Date endTime, double dist, int duration, List<LatLng> latLngList){
        this.recordType = recordType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.distance = dist;
        this.duration = duration;
        this.latLngList = latLngList;
        this.id = ++id_counter;
        Log.d("ID_TEST",""+id);
    }

    public Record(RecordType recordType, Date startTime, Date endTime, double dist, int duration){
        this.recordType = recordType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.distance = dist;
        this.duration = duration;
        this.latLngList = null;
        this.id = ++id_counter;
        Log.d("ID_TEST",""+id);
    }

    public Date getStartTime(){ return startTime; }

    public Date getEndTime(){ return endTime; }

    public String getDistance(){ return String.format("%.2f", distance); }

    public String getDuration(){ return parse_duration(duration); }

    public String getType(){ return recordType.getStr(); }

    public String parse_duration(int duration){
        int h = duration / 1440;
        int min = duration % 1440 / 60;
        int s = duration % 60;
        return String.format(Locale.getDefault(), "%02d:%02d:%02d",h,min,s);
    }
}
