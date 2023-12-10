package com.example.myapplication.data;

import android.text.TextUtils;
import android.util.Log;

import com.amap.api.maps2d.model.LatLng;
import com.example.myapplication.bean.Record;
import com.example.myapplication.constant.LinkConstant;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RemoteData implements DataService {

    boolean has_conn = true;
    //后端启动的地址
    private String url = LinkConstant.url;

    public RemoteData(){
        // TODO: In this stage, we use getAllRecords() to set id_counter,but it is incorrcet
        // TODO: in multi-user situation ,consider create new api get_id_counter()?
        List<Record> ls = getAllRecords();
        if(ls == null){
            Record.id_counter = 0;
        }
        else {
            Record.id_counter = ls.size();
        }
    }

    @Override
    public List<Record> getAllRecords(){
        String serviceRecord = "/v1/record";
        final List<Record>[] ret = new List[]{null};

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS).build();//设置连接超时时间;

        Request request = new Request.Builder()
                .url(url+serviceRecord)   // 设置请求地址
                .get()                    // 使用 Get 方法
                .build();


        // 同步 Get 请求
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    //Log.d("URL_TEST","status:"+response.isSuccessful());
                } catch (IOException e) {
                    Log.d("URL_TEST",e.getMessage());
                    //e.printStackTrace();
                }

                String result = null;
                try {
                    result = response.body().string();
                } catch (IOException e) {
                    Log.i("URL_TEST","fail"+response.isSuccessful());
                    e.printStackTrace();
                }
                catch(NullPointerException e){
                    Log.i("URL_TEST","got null responce(possibly conn error)");
                    return;
                }
                Log.i("URL_TEST", "result : " + result);

                Gson gson = new Gson();
                Record[] tmp = gson.fromJson(result,Record[].class);
                Log.d("URL_TEST",tmp.length+" "+tmp[1].toString());

                ret[0] = new ArrayList<>();
                for(Record record : tmp){
                    ret[0].add(record);
                }
                Log.d("URL_TEST","???" + ret[0].toString());
            }
        });

        thread.start();
        try{
            thread.join();


            for(Record r: ret[0]){
                r.setLatLngList();
            }
            for(Record r: ret[0]){
                Log.d("URL_TEST","finals" + r.toString());
            }
            return ret[0];
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public Record getRecord(int index) {
        String serviceRecord = "/v1/record"+"/"+ index;
        final Record[] record = new Record[1];

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                .url(url+serviceRecord)   // 设置请求地址
                .get()                    // 使用Get方法
                .build();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    //Log.d("URL_TEST","status:"+response.isSuccessful());
                } catch (IOException e) {
                    Log.d("URL_TEST",e.getMessage());
                    //e.printStackTrace();
                }

                String result = null;
                try {
                    result = response.body().string();
                } catch (IOException e) {
                    Log.i("URL_TEST","fail"+response.isSuccessful());
                    e.printStackTrace();
                }
                catch(NullPointerException e){
                    Log.i("URL_TEST","got null responce(possibly conn error)");
                    record[0] = null;
                    return;
                }
                Log.i("URL_TEST", "result : " + result);

                Gson gson = new Gson();
                record[0] = gson.fromJson(result,Record.class);

            }
        });

        thread.start();
        try{
            thread.join();
            if(record[0] == null){
                return null;
            }
            //尝试构建坐标List
            record[0].setLatLngList();
            //Log.i("URL_TEST", "final result : " + record[0].toString());
            return record[0];
        }
        catch (Exception e){
            e.printStackTrace();
            Log.i("URL_TEST", "result wrong"+e);
            return null;
        }

    }

    @Override
    public boolean updateRecord(Record record) throws JSONException {



        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS).build();

        String serviceSave = "/v1/record/save";

        int id = record.getId();
        Log.d("URL_TEST","id:"+id);
        int duration = record.getDuration();
        String distance = record.getDistanceByStr();
        Record.RecordType recordType = record.getRecordType();
        String startTime = record.getStartTimeByStr();
        String endTime = record.getEndTimeByStr();
        //Log.d("URL_TEST",endTime+" "+startTime);


        // 创建json对象
        JSONObject jsonObject = new JSONObject();
        // 1个数组参数
        JSONArray jsonArrayLa = new JSONArray();
        JSONArray jsonArrayLo = new JSONArray();
        for (LatLng latLng : record.getLatLngList()) {
            jsonArrayLa.put(latLng.latitude);
            jsonArrayLo.put(latLng.longitude);
        }
        jsonObject.put("latitudeList", jsonArrayLa);
        jsonObject.put("longitudeList", jsonArrayLo);
        jsonObject.put("id", id);
        jsonObject.put("duration", duration);
        jsonObject.put("distance", distance);
        jsonObject.put("recordType", recordType);
        jsonObject.put("startTime", startTime);
        jsonObject.put("endTime",endTime);
        jsonObject.put("latLngList",null);

        String data = jsonObject.toString();

        // 构造请求体
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), data);

        // 3个字符串参数



        Request request=new Request.Builder()
                .url(url+serviceSave)
                .post(requestBody)
                .build();

        final boolean[] result = {true};
        final boolean[] flag = {false};
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("URL_TEST", "连接失败" + e.getLocalizedMessage());
                result[0] = false;
                flag[0] = true;
                Log.d("URL_TEST","1"+result[0]);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                flag[0] = true;
                String result = response.body().string();
                Log.d("URL_TEST", "3" + result);
                if (response.body() != null) {
                    response.body().close();
                }
            }
        });

        while(!flag[0]){}; // TODO: sb写法(因为不会synchronized)
        Log.d("URL_TEST","2"+result[0]);
        return result[0];
    }

    private Headers setHeaderParams(Map<String, String> headerParams) {
        Headers headers = null;
        Headers.Builder headersbuilder = new Headers.Builder();
        if (headerParams != null && headerParams.size() > 0) {
            for (String key : headerParams.keySet()) {
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(headerParams.get(key))) {
                    //如果参数不是null并且不是""，就拼接起来
                    headersbuilder.add(key, headerParams.get(key));
                }
            }
        }

        headers = headersbuilder.build();
        return headers;
    }


    @Override
    @Deprecated
    public List<Record> queryRecordByTime(Date startTime, Date endTime){
        // You shouldn't use it
        return new ArrayList<>();
//        List<Record> ret = new ArrayList<>();
//        OkHttpClient client=new OkHttpClient();
//        String serviceQueryInfo = "info";
//        Map<String, String>params = new HashMap<>();
//        params.put("startDate", String.valueOf(startTime));
//        params.put("endDate", String.valueOf(endTime));
//
//        Headers headers = setHeaderParams(params);
//        Request request=new Request.Builder()
//                .url(url+serviceQueryInfo)
//                .get()
//                .headers(headers)
//                .build();
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Response response = null;
//                try {
//                    response = client.newCall(request).execute();
//                    //Log.d("URL_TEST","status:"+response.isSuccessful());
//                } catch (IOException e) {
//                    Log.d("URL_TEST",e.getMessage());
//                    //e.printStackTrace();
//                }
//
//                String result = null;
//                try {
//                    result = response.body().string();
//                } catch (IOException e) {
//                    Log.i("URL_TEST","fail"+response.isSuccessful());
//                    e.printStackTrace();
//                }
//                Log.i("URL_TEST", "result : " + result);
//
//                Gson gson = new Gson();
//                Record[] tmp = gson.fromJson(result,Record[].class);
//                Log.d("URL_TEST",tmp.length+" "+tmp[1].toString());
//
//                for(Record record : tmp){
//                    ret.add(record);
//                }
//                Log.d("URL_TEST","???" + ret.toString());
//            }
//        });
//
//        thread.start();
//        try{
//            thread.join();
//
//
//            for(Record r:ret){
//                r.setLatLngList();
//            }
//            for(Record r:ret){
//                Log.d("URL_TEST","finals" + r.toString());
//            }
//            return ret;
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//        return null;
    }

    @Override
    @Deprecated
    public List<Record> queryRecordByType(Record.RecordType type){
        // You shouldn't use it
        return new ArrayList<>();
//        List<Record> ret = new ArrayList<>();
//        OkHttpClient client=new OkHttpClient();
//        String serviceQueryInfo = "info";
//        Map<String, String>params = new HashMap<>();
//        params.put("startDate",null);
//        params.put("endDate",null);
//        params.put("recordType", String.valueOf(type));
//
//        Headers headers = setHeaderParams(params);
//        Request request=new Request.Builder()
//                .url(url+serviceQueryInfo)
//                .get()
//                .headers(headers)
//                .build();
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Response response = null;
//                try {
//                    response = client.newCall(request).execute();
//                    //Log.d("URL_TEST","status:"+response.isSuccessful());
//                } catch (IOException e) {
//                    Log.d("URL_TEST",e.getMessage());
//                    //e.printStackTrace();
//                }
//
//                String result = null;
//                try {
//                    result = response.body().string();
//                } catch (IOException e) {
//                    Log.i("URL_TEST","fail"+response.isSuccessful());
//                    e.printStackTrace();
//                }
//                Log.i("URL_TEST", "result : " + result);
//
//                Gson gson = new Gson();
//                Record[] tmp = gson.fromJson(result,Record[].class);
//                Log.d("URL_TEST",tmp.length+" "+tmp[0].toString());
//
//                for(Record record : tmp){
//                    ret.add(record);
//                }
//                Log.d("URL_TEST","???" + ret.toString());
//            }
//        });
//
//        thread.start();
//        try{
//            thread.join();
//            for(Record r:ret){
//                r.setLatLngList();
//            }
//            for(Record r:ret){
//                Log.d("URL_TEST","finals" + r.toString());
//            }
//            return ret;
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//        return null;
    }

    private String getBodyParams(Map<String, String> bodyParams) {
        //1.添加请求参数
        //遍历map中所有参数到builder
        if (bodyParams != null && bodyParams.size() > 0) {
            StringBuffer temp = new StringBuffer("?");
            StringBuffer stringBuffer = new StringBuffer();
            for (String key : bodyParams.keySet()) {
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(bodyParams.get(key))) {
                    //如果参数不是null并且不是""，就拼接起来
                    stringBuffer.append("&");
                    stringBuffer.append(key);
                    stringBuffer.append("=");
                    stringBuffer.append(bodyParams.get(key));
                }
            }

            temp.append(stringBuffer.toString().substring(1));

            return temp.toString();
        } else {
            return "";
        }
    }


    @Override
    public List<Record> queryRecordByBoth(Record.RecordType type, Date startTime, Date endTime)  {
        final List<Record>[] ret = new List[]{new ArrayList<>()};
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS).build();
        String serviceQueryInfo = "/v1/record/info";
        Map<String, String>params = new HashMap<>();
        params.put("startDate", getFormatTime(startTime,"yyyy-MM-dd"));
        params.put("endDate", getFormatTime(endTime,"yyyy-MM-dd"));
        params.put("recordType", String.valueOf(type));

        Log.d("URL_TEST",url+serviceQueryInfo+getBodyParams(params));
        Request request=new Request.Builder()
                .url(url+serviceQueryInfo+getBodyParams(params))
                .get()
                .build();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    //Log.d("URL_TEST","status:"+response.isSuccessful());
                } catch (IOException e) {
                    Log.d("URL_TEST",e.getMessage());
                    //e.printStackTrace();
                }

                String result = null;
                try {
                    result = response.body().string();
                } catch (IOException e) {
                    Log.i("URL_TEST","fail"+response.isSuccessful());
                    e.printStackTrace();
                }
                catch(NullPointerException e){
                    Log.i("URL_TEST","got null responce(possibly conn error)");
                    ret[0] = null;
                    return;
                }
                Log.i("URL_TEST", "result : " + result);

                Gson gson = new Gson();
                Record[] tmp = gson.fromJson(result,Record[].class);
                if(tmp.length != 0) Log.d("URL_TEST",tmp.length+" "+tmp[0].toString());

                for(Record record : tmp){
                    ret[0].add(record);
                }
                Log.d("URL_TEST","???" + ret[0].toString());
            }
        });

        thread.start();
        try{
            thread.join();


            for(Record r: ret[0]){
                r.setLatLngList();
            }
            for(Record r: ret[0]){
                Log.d("URL_TEST","finals" + r.toString());
            }
            return ret[0];
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String getFormatTime(Date date,String formatStr){
        if(date == null) return "null";
        DateFormat format=new SimpleDateFormat(formatStr);
        format.setTimeZone(TimeZone.getTimeZone("Asia/Macao"));
        return format.format(date);
    }
}
