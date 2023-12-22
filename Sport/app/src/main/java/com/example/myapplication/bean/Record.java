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
            put(WALKING,"快走");
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

    public static int id_counter = 0;

    public int id;
    RecordType recordType;
    Date startTime;

    Date endTime;
    double distance; // 跑步距离
    int duration; // 跑步时间，秒计

    public List<LatLng> latLngList;

    public List<Double> latitudeList;

    public List<Double> longitudeList;

    public long userId;

    public Record(RecordType recordType, Date startTime, Date endTime, double dist, int duration, List<LatLng> latLngList){
        this.recordType = recordType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.distance = dist;
        this.duration = duration;
        this.latLngList = latLngList;
        this.userId = 0;
        this.id_counter =
        this.id = ++id_counter;
    }

    public Record(RecordType recordType, Date startTime, Date endTime, double dist, int duration){
        this.recordType = recordType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.distance = dist;
        this.duration = duration;
        this.latLngList = null;
        this.userId = 0;
        this.id = ++id_counter;
    }

    public Record(){}

    public static Record getDefaultOne(){
        Record record = new Record();
        record.recordType = RecordType.RUNNING;
        record.startTime = new Date();
        record.endTime = new Date();
        record.distance = 0;
        record.duration = 0;
        record.latLngList = null;
        record.id = 0;
        return record;
    }


    public String getRecordTime(){
        // 仅用于HistoryActivity中，获取用于展示的时间
        Date d = getStartTime();
        DateFormat format=new SimpleDateFormat("EEE, MMM dd HH:mm",Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("Asia/Macao"));
        return format.format(d);
    }
    public String getStartTimeByStr(){
        Date d = getStartTime();
        DateFormat format=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz",Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("Asia/Macao"));
        return format.format(d);
    }

    public String getEndTimeByStr(){
        Date d = getEndTime();
        DateFormat format=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz",Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("Asia/Macao"));
        return format.format(d);
    }
    public Date getStartTime(){ return startTime; }

    public Date getEndTime(){ return endTime; }

    public String getDistanceByStr(){ return String.format("%.2f", distance); }

    public double getDistance(){return distance;}

    public String getDurationByStr(){ return parse_duration(duration); }

    public int getDuration(){ return duration; }

    public String getRecordTypeByStr(){ return recordType.getStr(); }

    public RecordType getRecordType(){ return recordType; }

    public String toString(){
        return getId()+" "+duration+" "+ getDistanceByStr() + " " +recordType;
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

    public static String getCalorie(int dis){
        int WEIGHT = 60;
        return String.format("%.1f",1.036*dis*WEIGHT);
    }


    public static String parse_duration(int duration){
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
