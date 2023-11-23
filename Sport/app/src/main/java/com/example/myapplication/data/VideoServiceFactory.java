package com.example.myapplication.data;

public class VideoServiceFactory {
    private static VideoService videoService = null;

    public static VideoService getInstance() {
        if(videoService==null){
            videoService = new LocalVideo();
        }
        return videoService;
    }
}
