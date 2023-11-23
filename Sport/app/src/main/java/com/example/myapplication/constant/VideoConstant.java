package com.example.myapplication.constant;

import com.example.myapplication.data.VideoService;
import com.example.myapplication.data.VideoServiceFactory;

public class VideoConstant {

    private VideoService videoService= VideoServiceFactory.getInstance();
    //视频地址
    public String[][] mVideoUrls = videoService.getVideoUrls();

    //封面图片
    public String[][] mVideoThumbs = videoService.getVideoThumbs();

    //标题
    public String[][] mVideoTitles = videoService.getVideoTitles();

    public String[][] getmVideoUrls(){return mVideoUrls;}

    public String[][] getmVideoThumbs(){return mVideoThumbs;}

    public String[][] getmVideoTitles(){return mVideoTitles;}





}
