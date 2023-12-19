package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
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
    private Button send_btn;
    private Button back_btn;
    private EditText textBox;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
        initView();


        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        Python python = Python.getInstance();
        pyChatObject = python.getModule("AIConversation");


        Intent intent = getIntent();
        String taskType = intent.getStringExtra("TaskType");
        if(taskType == null) {
            Log.e("ChatTaskType", "No Chat TaskType passed");

            ChatBean initChat = new ChatBean(1, "抱歉，系统出现错误，请退出重试");
            chatBeanList.add(initChat);

            send_btn.setEnabled(false);
        }
        else{
            Log.i("ChatTaskType", taskType);

            if(taskType.equals("NormalChat")){
                promptList.add("你是一个运动智能问答机器人，你需要与用户对话，并以尽量专业的内容进行回答。被问到你的身份时，请称自己为‘智能运动助手’");
                roleList.add("system");
                ChatBean initChat = new ChatBean(1, "你好，我是你的智能运动助手，有什么需要我帮助的吗？");
                chatBeanList.add(initChat);
            }
            else if(taskType.equals("EvalRecord")){
                String data = intent.getStringExtra("RecordData");
                if(data == null){
                    Log.e("ChatRecordData", "RecordData to ChatActivity missing");
                    ChatBean initChat = new ChatBean(1, "获取运动数据时出现错误，请重试");
                    chatBeanList.add(initChat);

                    send_btn.setEnabled(false);
                }
                else{
                    promptList.add("你是一个运动智能问答机器人。你需要分析用户发送的运动数据，从专业的视角给出意见，并回答用户的后续疑问。");
                    roleList.add("system");
                    ChatBean initChat = new ChatBean(1, "已收到您的运动数据，正全力分析，请稍后...");
                    chatBeanList.add(initChat);
                    adapter.notifyDataSetChanged();

                    promptList.add(data);
                    roleList.add("user");
                    LLM_Post();
                }
            }
            else{
                Log.e("ChatTaskType", "Undefined TaskType in ChatActivity");

                ChatBean initChat = new ChatBean(1, "抱歉，系统出现错误，请退出重试");
                chatBeanList.add(initChat);

                send_btn.setEnabled(false);
            }
        }
        //显示更新的对话内容
        adapter.notifyDataSetChanged();
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(ChatActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


    }

    private void initView(){
        promptList = new ArrayList<>();
        roleList = new ArrayList<>();
        chatBeanList = new ArrayList<>();
        adapter = new ChatAdapter(chatBeanList, this);

        listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        textBox = findViewById(R.id.et_send_msg);
        send_btn = findViewById(R.id.btn_send);
        back_btn = findViewById(R.id.btn_back);
        progressDialog = new ProgressDialog(ChatActivity.this);
    }

    //onClick 对应的函数必须要public
    public void sendMessage(View view){
        String message = textBox.getText().toString();
        textBox.setText("");
        ChatBean request = new ChatBean(0, message);
        promptList.add(message);
        roleList.add("user");
        chatBeanList.add(request);

        LLM_Post();
    }

    // LLM调用入口，将promptList和roleList中的内容组合发送
    private void LLM_Post(){
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
    }

}
