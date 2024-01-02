package com.example.myapplication.task;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.chaquo.python.PyObject;
import com.example.myapplication.ChatActivity;
import com.example.myapplication.adapter.ChatAdapter;
import com.example.myapplication.bean.ChatBean;

import java.util.List;

public class ChatTask extends AsyncTask<Object, Void, String> {
    private PyObject pyChatObject;
    private ProgressDialog progressDialog;
    private List<ChatBean> chatBeanList;
    private ChatAdapter adapter;

    public ChatTask(PyObject pyChatObject, ProgressDialog progressDialog, List<ChatBean> chatBeanList
    , ChatAdapter adapter){
        this.pyChatObject = pyChatObject;
        this.progressDialog = progressDialog;
        this.chatBeanList = chatBeanList;
        this.adapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Loading...");
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
        if(res.equals("Internet Error")){
            response = new ChatBean(1, "网络错误，请检查网络连接，并退出重试");
            promptList.remove(promptList.size()-1);
            roleList.remove(roleList.size()-1);
        }
        else {
            response = new ChatBean(1, res);
            promptList.add(res);
            roleList.add("assistant");
        }
//        chatBeanList.add(response);
        return res;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
        adapter.notifyDataSetChanged();
    }
}
