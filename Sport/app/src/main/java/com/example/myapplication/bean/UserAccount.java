package com.example.myapplication.bean;

import java.io.Serializable;
import java.util.Date;

public class UserAccount{

    public UserAccount(String username,String phone,String pwd){
        this.username = username;
        this.phone = phone;
        this.pwd = pwd;
        this.sex = "male";
    }

    public static int id;
    //用户名
    private String username;

    private String phone;



    //密码
    private String pwd;

    private Date birth;

    private String words;

    private String sex;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
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

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
