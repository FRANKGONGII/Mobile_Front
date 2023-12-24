package com.example.myapplication.adapter;

import com.example.myapplication.R;
import com.example.myapplication.bean.ChatBean;
import com.example.myapplication.utils.PhotoUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends BaseAdapter {
    private List<ChatBean> chatList;
    private  LayoutInflater inflater;
    private SharedPreferences pref;

    public ChatAdapter(List<ChatBean> chatList, Context context){
        this.chatList = chatList;
        this.inflater = LayoutInflater.from(context);
        pref = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
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
            convertView = inflater.inflate(R.layout.right_chat_item, null);
            CircleImageView avatar = convertView.findViewById(R.id.iv_head);

            String avatarBase64Str = pref.getString("avatar", "");

            if (avatarBase64Str.equals("")) {
                // 设置默认头像
                avatar.setImageResource(R.drawable.baseline_avatar_default1);
            } else {
                // 转化为bitmap
                Bitmap bitmap = PhotoUtil.base64Str2Bitmap(avatarBase64Str);
                avatar.setImageBitmap(bitmap);
            }
        }
        else{
            convertView = inflater.inflate(R.layout.left_chat_item, null);
        }

        TextView textView = convertView.findViewById(R.id.tv_chat_content);
        textView.setText(message.getMessage());
        return convertView;
    }
}
