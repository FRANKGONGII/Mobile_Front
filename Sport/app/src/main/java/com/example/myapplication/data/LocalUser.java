package com.example.myapplication.data;

import android.util.Log;

import com.example.myapplication.bean.UserAccount;

import java.util.ArrayList;
import java.util.List;

public class LocalUser implements UserService{
    List<UserAccount> users;

    public LocalUser() {
        users = new ArrayList<>();
    }

    public boolean LoginByPwd(String str,String pwd){
        for(UserAccount userAccount:users){
            Log.d("my_test","?"+userAccount.toString());
            if(userAccount.getPhone().equals(str)){
                Log.d("my_test","1");
                return userAccount.getPwd().equals(pwd);
            }
        }
        Log.d("my_test","2");
        return false;
    }

    //TODO:验证码登录
    public boolean LoginByCheckCode(String phone, String code){
        return true;
    }

    public boolean Register(String name,String phone, String pwd){
        Log.d("my_test","regist ok"+name + " " + phone + " " + pwd);
        for(UserAccount userAccount:users){
            if(userAccount.getPhone().equals(phone)){
               return false;
            }
        }
        users.add(new UserAccount(name,phone,pwd));
        return true;
    }
}
