package com.example.myapplication.request;


import java.util.Date;


public class RequestRecordInfo {
    private Date startTime;
    private Date endTime;
    private String type;

    public Date getStartTime(){
        return startTime;
    }

    public Date getEndTime(){
        return endTime;
    }

    public String getType(){
        return type;
    }
}
