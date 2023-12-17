package com.example.myapplication.data;

import android.text.TextUtils;
import android.util.Log;

import com.amap.api.maps2d.model.LatLng;
import com.example.myapplication.bean.Record;
import com.example.myapplication.bean.UserAccount;
import com.example.myapplication.constant.LinkConstant;
import com.example.myapplication.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TransferQueue;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RemoteData implements DataService {

    private Record[] parseJson2RecordArray(String result){

        int startIndex = result.indexOf("data");
        int len = result.length();
        String recordArray = result.substring(startIndex+6,len-1);
        Log.i("URL_TEST", "arr: "+recordArray);

        //Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        //Record[] tmp = gson.fromJson(result,HashMap<String,String>.class);
        //String  map1= gson.fromJson(result,type);
        Gson gson = new Gson();
        return gson.fromJson(recordArray,Record[].class);
    }

    private Record parseJson2Record(String result){

        int startIndex = result.indexOf("data");
        int len = result.length();
        String recordArray = result.substring(startIndex+6,len-1);
        Log.i("URL_TEST", "arr: "+recordArray);
        //Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        //Record[] tmp = gson.fromJson(result,HashMap<String,String>.class);
        //String  map1= gson.fromJson(result,type);
        Gson gson = new Gson();
        return gson.fromJson(recordArray,Record.class);
    }

    private long parseJson2Long(String result){

        int startIndex = result.indexOf("data");
        int len = result.length();
        String recordArray = result.substring(startIndex+6,len-1);
        Log.i("URL_TEST", "arr: "+recordArray);

        //Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        //Record[] tmp = gson.fromJson(result,HashMap<String,String>.class);
        //String  map1= gson.fromJson(result,type);
        Gson gson = new Gson();
        return gson.fromJson(recordArray,Long.class);
    }



    //后端启动的地址
    private String url = LinkConstant.url;

    public RemoteData(){
        Record.id_counter = getAllRecords().size();
    }

    @Override
    public List<Record> getAllRecords(){
        String serviceRecord = "/v1/record";
        List<Record> ret = new ArrayList<>();

        Map<String, String> params = new HashMap<>();
        params.put("userId", String.valueOf(UserAccount.id));

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url+serviceRecord+ Utils.getBodyParams(params))   // 设置请求地址
                .get()                    // 使用 Get 方法
                .build();

        Log.d("URL_TEST",url+serviceRecord+ Utils.getBodyParams(params));
        Type type = new TypeToken<Map<String,String>>(){}.getType();
        final Map<String, String>[] map2 = new Map[]{null};

        // 同步 Get 请求
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    Log.d("URL_TEST","status:"+response.isSuccessful());
                } catch (IOException e) {
                    Log.d("URL_TEST",e.getMessage());
                    e.printStackTrace();
                }

                String result = null;
                try {
                    result = response.body().string();
                    Log.d("URL_TEST","jjjjjjj"+" "+result);
                } catch (IOException e) {
                    Log.i("URL_TEST","fail"+response.isSuccessful());
                    e.printStackTrace();
                }
                Log.i("URL_TEST", "result : " + result+" "+type);
//                int startIndex = result.indexOf("data");
//                int len = result.length();
//                String recordArray = result.substring(startIndex+6,len-1);
//                Log.i("URL_TEST", "arr: "+recordArray);




                //Gson gson = new Gson();
                Record[] tmp = parseJson2RecordArray(result);
                //Log.d("URL_TEST",tmp+"");



//                for(Record record : map2[0].get("data")){
//                    ret.add(record);
//                }
//                Log.d("URL_TEST","???" + ret.toString());
            }
        });

        thread.start();
        try{
            thread.join();


            for(Record r:ret){
                r.setLatLngList();
            }
            for(Record r:ret){
                Log.d("URL_TEST","finals" + r.toString());
            }
            return ret;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public Record getRecord(long index) {
        Log.d("URL_TEST","start get");
        String serviceRecord = "/v1/record"+"/"+ index;
        final Record[] record = new Record[1];

        Map<String, String> params = new HashMap<>();
        params.put("userId", String.valueOf(UserAccount.id));

        Log.d("URL_TEST","get path: "+url+serviceRecord+Utils.getBodyParams(params));
        Log.d("URL_TEST","get record id: "+index);


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url+serviceRecord+Utils.getBodyParams(params))   // 设置请求地址
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
                Log.i("URL_TEST", "result : " + result);

                //Gson gson = new Gson();
                record[0] = parseJson2Record(result);



            }
        });

        thread.start();
        try{
            thread.join();
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
    public long updateRecord(Record record) throws JSONException {
        Log.d("URL_TEST","start save");



        OkHttpClient client=new OkHttpClient();

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
        //jsonObject.put("id", id);
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

        Map<String, String> params = new HashMap<>();
        params.put("userId", String.valueOf(UserAccount.id));

        Log.d("URL_TEST","save path: "+url+serviceSave+Utils.getBodyParams(params));

        Request request=new Request.Builder()
                .url(url+serviceSave+Utils.getBodyParams(params))
                .post(requestBody)
                .build();

        final long[] newId = {-1};

//        Call call = client.newCall(request);
//        try {
//            Response response = call.execute();
//            Log.i("URL_TEST", "postSync: " + response.body().string());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    Log.d("URL_TEST","status: "+response.isSuccessful());
                } catch (IOException e) {
                    Log.d("URL_TEST",e.getMessage());
                    //e.printStackTrace();
                }

                String result = null;
                try {
                    result = response.body().string();
                } catch (IOException e) {
                    Log.i("URL_TEST","fail: "+response.isSuccessful());
                    e.printStackTrace();
                }
                Log.i("URL_TEST", "resultaa : " + result);

                //Gson gson = new Gson();
                if(response.code()==200)newId[0] = parseJson2Long(result);



            }
        });

        thread.start();
        try{
            thread.join();
            //尝试构建坐标List
            return newId[0];
        }
        catch (Exception e){
            e.printStackTrace();
            Log.i("URL_TEST", "result wrong"+e);
        }


//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d("URL_TEST", "连接失败" + e.getLocalizedMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String result = response.body().string();
//                Log.d("URL_TEST", "result: "+result);
//                Log.d("URL_TEST","id before: "+newId[0]+"");
//                if (response.body() != null) {
//                    newId[0] = parseJson2Long(result);
//                    Log.d("URL_TEST","id after: "+newId[0]+"");
//                    response.body().close();
//                }
//            }
//        });

        return newId[0];
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
    public List<Record> queryRecordByTime(Date startTime, Date endTime){
        List<Record> ret = new ArrayList<>();
        OkHttpClient client=new OkHttpClient();
        String serviceQueryInfo = "info";
        Map<String, String>params = new HashMap<>();
        params.put("startDate", String.valueOf(startTime));
        params.put("endDate", String.valueOf(endTime));

        params.put("userId", String.valueOf(UserAccount.id));

        Headers headers = setHeaderParams(params);
        Request request=new Request.Builder()
                .url(url+serviceQueryInfo)
                .get()
                .headers(headers)
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
                Log.i("URL_TEST", "result : " + result);

                Gson gson = new Gson();
                Record[] tmp = parseJson2RecordArray(result);
                Log.d("URL_TEST",tmp.length+" "+tmp[0].toString());

                for(Record record : tmp){
                    ret.add(record);
                }
                Log.d("URL_TEST","???" + ret.toString());
            }
        });

        thread.start();
        try{
            thread.join();


            for(Record r:ret){
                r.setLatLngList();
            }
            for(Record r:ret){
                Log.d("URL_TEST","finals" + r.toString());
            }
            return ret;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Record> queryRecordByType(Record.RecordType type){
        List<Record> ret = new ArrayList<>();
        OkHttpClient client=new OkHttpClient();
        String serviceQueryInfo = "info";
        Map<String, String>params = new HashMap<>();
        params.put("startDate",null);
        params.put("endDate",null);
        params.put("recordType", String.valueOf(type));

        params.put("userId", String.valueOf(UserAccount.id));

        Headers headers = setHeaderParams(params);
        Request request=new Request.Builder()
                .url(url+serviceQueryInfo)
                .get()
                .headers(headers)
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
                Log.i("URL_TEST", "result : " + result);

                Gson gson = new Gson();
                Record[] tmp = parseJson2RecordArray(result);
                Log.d("URL_TEST",tmp.length+" "+tmp[0].toString());

                for(Record record : tmp){
                    ret.add(record);
                }
                Log.d("URL_TEST","???" + ret.toString());
            }
        });

        thread.start();
        try{
            thread.join();
            for(Record r:ret){
                r.setLatLngList();
            }
            for(Record r:ret){
                Log.d("URL_TEST","finals" + r.toString());
            }
            return ret;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
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
        List<Record> ret = new ArrayList<>();
        OkHttpClient client=new OkHttpClient();
        String serviceQueryInfo = "/v1/record/info";
        Map<String, String>params = new HashMap<>();
        params.put("startDate", getFormatTime(startTime,"yyyy-MM-dd"));
        params.put("endDate", getFormatTime(endTime,"yyyy-MM-dd"));
        params.put("recordType", String.valueOf(type));

        params.put("userId", String.valueOf(UserAccount.id));

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
                Log.i("URL_TEST", "result : " + result);

                Gson gson = new Gson();
                Record[] tmp = parseJson2RecordArray(result);
                if(tmp.length != 0) Log.d("URL_TEST",tmp.length+" "+tmp[0].toString());

                for(Record record : tmp){
                    ret.add(record);
                }
                Log.d("URL_TEST","???" + ret.toString());
            }
        });

        thread.start();
        try{
            thread.join();


            for(Record r:ret){
                r.setLatLngList();
            }
            for(Record r:ret){
                Log.d("URL_TEST","finals" + r.toString());
            }
            return ret;
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
