package com.example.myapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.chaquo.python.Python;
import com.chaquo.python.PyObject;
import com.chaquo.python.android.AndroidPlatform;
import com.example.myapplication.adapter.ChatAdapter;
import com.example.myapplication.bean.ChatBean;
import com.example.myapplication.task.ChatTask;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity{
    private PyObject pyChatObject;
    private ListView listView;
    private ChatAdapter adapter;
    private List<String> promptList; //这是传给LLM的prompt的内容，与应用显示的对话内容并不相同
    private List<String> roleList; //传给LLM的role的列表，与上面的prompt一一对应
    private List<ChatBean> chatBeanList; //实际显示的对话内容列表，与prompt相区别
    private Button btn;
    private EditText textBox;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);

        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        Python python = Python.getInstance();
        pyChatObject = python.getModule("AIConversation");

        promptList = new ArrayList<>();
        promptList.add("你是一个运动智能问答机器人，你需要与用户对话，并以尽量专业的内容进行回答。被问到你的身份时，请称自己为‘智能运动助手’");
        roleList = new ArrayList<>();
        roleList.add("system");
        chatBeanList = new ArrayList<>();
        ChatBean initChat = new ChatBean(1, "你好，我是你的智能运动助手，有什么需要我帮助的吗？");
        chatBeanList.add(initChat);
        adapter = new ChatAdapter(chatBeanList, this);

        progressDialog = new ProgressDialog(ChatActivity.this);

        initView();
    }

    private void initView(){
        listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        textBox = findViewById(R.id.et_send_msg);
        btn = findViewById(R.id.btn_send);
    }

    //onClick 对应的函数必须要public
    public void sendMessage(View view){
        String message = textBox.getText().toString();
        textBox.setText("");
        ChatBean request = new ChatBean(0, message);
        promptList.add(message);
        roleList.add("user");
        chatBeanList.add(request);

        StringBuilder promptsBuilder = new StringBuilder();
        StringBuilder rolesBuilder = new StringBuilder();
        int len = promptList.size();
        for(int i=0;i<len;i++){
            promptsBuilder.append(promptList.get(i));
            rolesBuilder.append(roleList.get(i));
            if(i < len-1){
                promptsBuilder.append('#');
                rolesBuilder.append('#');
            }
        }
        String prompts = promptsBuilder.toString();
        String roles = rolesBuilder.toString();
        //用于执行与LLM交流的线程，注意每个AsyncTask只能执行一次，所以一定要new
        ChatTask chatTask = new ChatTask(pyChatObject, progressDialog, chatBeanList, adapter);

        chatTask.execute(roles, prompts, roleList, promptList);
//        String res = chatBeanList.get(chatBeanList.size()-1).getMessage();

//        if(res.equals("Internet Error")){
//            promptList.remove(promptList.size()-1);
//            roleList.remove(roleList.size()-1);
//        }
//        else{
//            promptList.add(res);
//            roleList.add("assistant");
//        }
    }

}
