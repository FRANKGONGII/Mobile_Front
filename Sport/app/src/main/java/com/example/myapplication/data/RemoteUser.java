package com.example.myapplication.data;

import android.text.TextUtils;
import android.util.Log;

import com.example.myapplication.bean.Record;
import com.example.myapplication.bean.UserAccount;
import com.example.myapplication.constant.LinkConstant;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class RemoteUser implements UserService{
    String URL= LinkConstant.url;

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

            if(stringBuffer.length()>=1)temp.append(stringBuffer.toString().substring(1));

            return temp.toString();
        } else {
            return "";
        }
    }


    @Override
    public int LoginByPwd(String str, String pwd) {
        String url2 = "/v1/login";
        OkHttpClient client=new OkHttpClient();
        Map<String, String> params = new HashMap<>();
        params.put("username", String.valueOf(str));
        params.put("password", String.valueOf(pwd));

        Log.d("URL_TEST",URL+url2+getBodyParams(params));
        RequestBody empBody = new FormBody.Builder().build();

        Request request=new Request.Builder()
                .url(URL+url2+getBodyParams(params))
                .post(empBody)
                .build();

        final int[] result = {400};
        final String[] data = {null};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    Log.d("URL_TEST","status:"+response.isSuccessful());
                    //Log.d("URL_TEST","result:"+response.body().string());
                    data[0] = response.body().string();
                } catch (IOException e) {
                    Log.d("URL_TEST",e.getMessage());
                    //e.printStackTrace();
                }

                result[0] = 404;
                if(response!=null)result[0] = response.code();
                Log.i("URL_TEST", "res : " + result[0]);

            }
        });

        thread.start();
        try{
            thread.join();
            int len = data[0].length();
            int startIndex = data[0].indexOf("data");
            UserAccount.id = Integer.parseInt(data[0].substring(startIndex+6,len-1));
            //Log.d("URL_TEST","code: " + result[0]+" data: "+data[0]+" "+data[0].substring(startIndex+6,len-1));

            if(result[0]==200){
                return Integer.parseInt(data[0].substring(startIndex+6,len-1));
            }
//            for(Record r:ret){
//                r.setLatLngList();
//            }
//            for(Record r:ret){
//                Log.d("URL_TEST","finals" + r.toString());
//            }
//            return ret;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean LoginByCheckCode(String phone, String code) {
        return false;
    }

    @Override
    public boolean Register(String name, String phone, String pwd) {
        return false;
    }
}
