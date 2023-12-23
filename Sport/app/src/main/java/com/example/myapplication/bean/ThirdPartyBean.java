package com.example.myapplication.bean;

import android.widget.ImageView;

public class ThirdPartyBean {
    private String Name;
    private int ImageURI;

    public ThirdPartyBean(String name, int uri){
        ImageURI = uri;
        Name = name;
    }

    public int getImageURI() {
        return ImageURI;
    }

    public String getName() {
        return Name;
    }
}
