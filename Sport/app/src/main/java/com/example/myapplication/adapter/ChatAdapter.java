package com.example.myapplication.adapter;

import com.example.myapplication.R;
import com.example.myapplication.bean.ChatBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends BaseAdapter {
    private List<ChatBean> chatList;
    private  LayoutInflater inflater;

    public ChatAdapter(List<ChatBean> chatList, Context context){
        this.chatList = chatList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Object getItem(int i) {
        return chatList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ChatBean message = chatList.get(i);
        if(message.getType()==0){
            convertView = inflater.inflate(R.layout.left_chat_item, null);
        }
        else{
            convertView = inflater.inflate(R.layout.right_chat_item, null);
        }

        TextView textView = convertView.findViewById(R.id.tv_chat_content);
        textView.setText(message.getMessage());
        return convertView;
    }
}
