package com.example.myapplication.data;

import com.example.myapplication.bean.UserAccount;

import java.util.ArrayList;
import java.util.List;

public class LocalUser implements UserService{
    List<UserAccount> users;

    LocalUser() {
        users = new ArrayList<>();
    }

    public boolean LoginByPwd(String str,String pwd){
        for(UserAccount userAccount:users){
            if(userAccount.getPhone().equals(str)){
                if(userAccount.getPwd().equals(pwd)){
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }

    //TODO:验证码登录
    public boolean LoginByCheckCode(String phone, String code){
        return true;
    }

    public boolean Register(String name,String phone, String pwd){
        for(UserAccount userAccount:users){
            if(userAccount.getPhone().equals(phone)){
               return false;
            }
        }
        users.add(new UserAccount(name,phone,pwd));
        return true;
    }
}
