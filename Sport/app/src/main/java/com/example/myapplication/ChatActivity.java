package com.example.myapplication;

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
        promptList.add("你是一个运动智能问答机器人，你需要与用户对话，并以尽量专业的内容机器人。被问到你的身份是，请称自己为‘智能运动助手’");
        roleList = new ArrayList<>();
        roleList.add("system");
        chatBeanList = new ArrayList<>();
        ChatBean initChat = new ChatBean(1, "你好，我是你的智能运动助手，有什么需要我帮助的吗？");
        chatBeanList.add(initChat);
        adapter = new ChatAdapter(chatBeanList, this);

        initView();


//        String roles = "assistant#user";
//        String prompts = "Hello, I'm ChatGLM3-6B, can I help you?#please tell me about yourself";
//        PyObject res = pyObject.callAttr("conversation",roles, prompts);
//        Log.println(Log.VERBOSE, "python_test", "This is the return from python: " +
//                res.toString());
    }

    private void initView(){
        listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        textBox = findViewById(R.id.et_send_msg);
        btn = findViewById(R.id.btn_send);

//        textBox.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
//                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() ==
//                        KeyEvent.ACTION_DOWN) {
//                    sendMessage();//点击Enter键也可以发送信息
//                }
//                return false;
//            }
//        });
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

        String res = pyChatObject.callAttr("conversation",roles, prompts).toString().trim();
        ChatBean response = new ChatBean(1, res);

        promptList.add(res);
        roleList.add("assistant");
        chatBeanList.add(response);

        adapter.notifyDataSetChanged(); //更新view
    }

}
