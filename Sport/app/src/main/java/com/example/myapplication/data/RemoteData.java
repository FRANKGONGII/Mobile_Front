package com.example.myapplication.data;

import android.util.Log;

import com.example.myapplication.bean.Record;
import com.example.myapplication.constant.LinkConstant;
import com.google.gson.Gson;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RemoteData implements DataService {

    //后端启动的地址
    private String url = LinkConstant.url;

    @Override
    public List<Record> getAllRecords(){
        String serviceRecord = "/v1/record";
        List<Record> ret = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();
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
                Log.i("URL_TEST", "result : " + result);

                Gson gson = new Gson();
                Record[] tmp = gson.fromJson(result,Record[].class);
                Log.d("URL_TEST",tmp.length+" "+tmp[1].toString());

                for(Record record : tmp){
                    ret.add(record);
                }
                Log.d("URL_TEST","???" + ret.toString());
            }
        });

        thread.start();
        try{
            thread.join();

            Log.d("URL_TEST","!!!" + ret.toString());
            return ret;
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

        OkHttpClient client = new OkHttpClient();
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
                Log.i("URL_TEST", "result : " + result);

                Gson gson = new Gson();
                record[0] = gson.fromJson(result,Record.class);

            }
        });

        thread.start();
        try{
            thread.join();
            Log.i("URL_TEST", "result11 : " + record[0]);
            return record[0];
        }
        catch (Exception e){
            e.printStackTrace();
            Log.i("URL_TEST", "result???");
            return null;
        }
    }

    @Override
    public void updateRecord(Record record) {
        OkHttpClient client=new OkHttpClient();

        String serviceSave = "/v1/record/save";

        String id = String.valueOf(record.id);
        String duration = record.getDuration();
        String distance = record.getDistance();
        String type = record.getType();
        String startTime = String.valueOf(record.getStartTime());

        //构建表单参数
        FormBody.Builder requestBuild=new FormBody.Builder();
        //添加请求体
        RequestBody requestBody=requestBuild
                .add("id",id)
                .add("duration",duration)
                .add("distance",distance)
                .add("type",type)
                .add("startTime",startTime)
                .build();

        Request request=new Request.Builder()
                .url(url+serviceSave)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("URL_TEST", "连接失败" + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("URL_TEST", result);
                if (response.body() != null) {
                    response.body().close();
                }
            }
        });
    }
}
