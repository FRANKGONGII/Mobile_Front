package com.example.myapplication.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.chaquo.python.PyObject;
import com.example.myapplication.ChatActivity;
import com.example.myapplication.adapter.ChatAdapter;
import com.example.myapplication.bean.ChatBean;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class ChatEleTask extends AsyncTask<Object, Void, String> {
    private PyObject pyChatObject;
    private ProgressDialog progressDialog;
    private List<ChatBean> chatBeanList;
    private Context context;
    private String res;
    private Handler handler;

    public ChatEleTask(PyObject pyChatObject, ProgressDialog progressDialog, List<ChatBean> chatBeanList, Context context){
        this.pyChatObject = pyChatObject;
        this.progressDialog = progressDialog;
        this.chatBeanList = chatBeanList;
        this.context = context;
        handler = new Handler();
        res = "";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("可爱的大象正在分析");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(Object... objects) {
        String roles = (String) objects[0];
        String prompts = (String) objects[1];

        List<String> roleList = (List<String>) objects[2];
        List<String> promptList = (List<String>) objects[3];

        String res = pyChatObject.callAttr("conversation",roles, prompts).toString().trim();
        ChatBean response;
        Log.d("ELE", "doInBackground: ");
        if(res.equals("Internet Error")){
            Log.d("ELE", "NO");
            response = new ChatBean(1, "网络错误，请检查网络连接，并退出重试");
            promptList.remove(promptList.size()-1);
            roleList.remove(roleList.size()-1);
        }
        else {
            response = new ChatBean(1, res);
            promptList.add(res);
            roleList.add("assistant");
        }
        chatBeanList.add(response);
        this.res = res;

        handler.post(new Runnable() {
            @Override
            public void run() {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
                builder.setTitle("神奇大象的评价:");
                builder.setMessage(res);
                builder.setPositiveButton("谢谢大象", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });


        return res;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
//        adapter.notifyDataSetChanged();

    }

    public String getRes() {return res;}
}
