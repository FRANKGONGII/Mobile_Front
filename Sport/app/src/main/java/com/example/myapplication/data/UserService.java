package com.example.myapplication.data;

import com.example.myapplication.bean.UserAccount;

public interface UserService {

    public boolean LoginByPwd(String str,String pwd);

    //TODO:验证码登录
    public boolean LoginByCheckCode(String phone, String code);

    public boolean Register(String name,String phone, String pwd);

}
