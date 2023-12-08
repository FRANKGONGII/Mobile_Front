package com.example.myapplication.user;

import android.util.Log;

import com.example.myapplication.bean.Record;

import java.util.HashMap;
import java.util.Map;

public class UserInfoItem {

    public enum UserInfoType {
        AVATAR, NICKNAME, GENDER, BIRTH, SIGNATURE;
        private static final HashMap<UserInfoItem.UserInfoType,String> mp = new HashMap<UserInfoType, String>(){{
            put(AVATAR, "头像");
            put(NICKNAME, "昵称");
            put(GENDER, "性别");
            put(BIRTH, "出生年月");
            put(SIGNATURE, "个性签名");
        }};

        public static UserInfoType getValue(String type) {
            for(Map.Entry<UserInfoType,String> entry : mp.entrySet()){
                if(entry.getValue().equals(type)){
                    return entry.getKey();
                }
            }

            Log.d("UserInfo","Undefined type");
            return null;
        }

        public String getStr(){
            return mp.get(this);
        }
    }

    private UserInfoType type;
    private String content;

    public UserInfoItem(UserInfoType type, String content) {
        this.type = type;
        this.content = content;
    }

    public UserInfoType getType() { return type; }

    public String getContent() { return content; }

    public  void setContent(String content) { this.content = content; }

}
