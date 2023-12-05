package com.example.myapplication.bean;

import java.io.Serializable;

/**
 * 描述: 账号密码
 * 作者: james
 * 日期: 2019/2/25 19:59
 * 类名: UserAccount
 */
public class UserAccount{

    private int id;

    //用户名
    private String username;

    //密码
    private String pwd;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
