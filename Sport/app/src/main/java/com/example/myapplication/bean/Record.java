package com.example.myapplication.bean;

import android.util.Log;

import com.amap.api.maps2d.model.LatLng;

import java.io.Serializable;

import java.util.ArrayList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;


public class Record implements Serializable {
    public enum RecordType {
        RUNNING,RIDING,WALKING,SWIMMING;
        private static final HashMap<RecordType,String> mp = new HashMap<RecordType,String>(){{
            put(RUNNING,"跑步");
            put(RIDING,"骑行");
            put(WALKING,"健走");
            put(SWIMMING,"游泳");
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

    public List<Double> latitudeList;

    public List<Double> longitudeList;

    public Record(RecordType recordType, Date startTime, Date endTime, double dist, int duration, List<LatLng> latLngList){
        this.recordType = recordType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.distance = dist;
        this.duration = duration;
        this.latLngList = latLngList;
        this.id = ++id_counter;
    }

    public Record(RecordType recordType, Date startTime, Date endTime, double dist, int duration){
        this.recordType = recordType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.distance = dist;
        this.duration = duration;
        this.latLngList = null;
        this.id = ++id_counter;
    }

    public String getStartTimeByStr(){
        Date d = getStartTime();
        DateFormat format=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Macao"));
        return format.format(d);
    }
    public Date getStartTime(){ return startTime; }

    public Date getEndTime(){ return endTime; }

    public String getDistance(){ return String.format("%.2f", distance); }

    public String getDuration(){ return parse_duration(duration); }

    public String getType(){ return recordType.getStr(); }

    public String toString(){
        return "" + getId()+" "+duration+" "+getDistance()+" "+getType()+" "+getStartTimeByStr()+" "+getEndTime()+" "+getLatLngList();
    }
    // km per hour
    public String getSpeed1(){ return String.format("%.2f", distance * 3600 / duration); }

    // minutes per km
    public String getSpeed2(){
        int s = (int) (duration / distance);
        return String.format("%02d'%02d''", s/60,s%60);
    }

    public String getCalorie(){
        int WEIGHT = 60;
        return String.format("%.1f",1.036*distance*WEIGHT);
    }

    public String parse_duration(int duration){
        int h = duration / 3600;
        int min = duration % 3600 / 60;
        int s = duration % 60;
        return String.format(Locale.getDefault(), "%02d:%02d:%02d",h,min,s);
    }


    public int getId(){
        return id;
    }

    public List<LatLng> getLatLngList(){return latLngList;}

    public void setLatLngList(){
        if(this.longitudeList!=null&&this.latitudeList!=null){
            this.latLngList = new ArrayList<>();
            for(int i = 0;i<longitudeList.size();i++){
                this.latLngList.add(new LatLng(latitudeList.get(i),longitudeList.get(i)));
            }
        }
    }

    public void setLatLngList(List<Double> la,List<Double>lo){
        this.latitudeList = la;
        this.longitudeList = lo;
        this.setLatLngList();

    }


}
