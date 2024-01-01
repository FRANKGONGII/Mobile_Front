package com.example.myapplication.data;

public class LocalVideo implements VideoService {
    public static String[][] mVideoUrls = {{
            "https://stream7.iqilu.com/10339/upload_transcode/202002/18/20200218114723HDu3hhxqIT.mp4",
            "https://stream7.iqilu.com/10339/upload_transcode/202002/18/20200218093206z8V1JuPlpe.mp4",
            "https://stream7.iqilu.com/10339/article/202002/18/2fca1c77730e54c7b500573c2437003f.mp4",
            "https://stream7.iqilu.com/10339/upload_transcode/202002/18/20200218025702PSiVKDB5ap.mp4",
            //"https://stream7.iqilu.com/10339/upload_transcode/202002/18/202002181038474liyNnnSzz.mp4"
            "https://box.nju.edu.cn/seafhttp/files/af65164e-176d-446d-8bb6-84ca10e87065/%E8%BF%90%E5%8A%A8%E8%AE%AD%E7%BB%831.mp4"
    }
    };

    //封面图片
    public static String[][] mVideoThumbs = {{
            "https://s1.wzznft.com/i/2023/12/19/nah4t4.jpg",
            "https://s1.wzznft.com/i/2023/12/19/6hlb4np.jpg",
            "https://s1.wzznft.com/i/2023/12/19/n5ng7h.jpg",
            "https://s1.wzznft.com/i/2023/12/19/ncxlj6.jpg",
            "https://s1.wzznft.com/i/2023/12/19/n93vwv.jpg"
    }};

    //标题
    public static String[][] mVideoTitles = {{
            "新手健走训练",
            "21天新手10K完赛",
            "14天速效减脂",
            "山地耐力骑行训练",
            "21天新手20K完赛",
    },
    };

    public static String[][] mVideoTypes = {{
            "健走",
            "跑步",
            "减脂",
            "骑行",
            "跑步"
    },
    };

    public static String[][] mVideoTags = {{
            "初级",
            "初级",
            "高级",
            "高级",
            "中级"
    },
    };





    @Override
    public String[][] getVideoUrls() {
        return mVideoUrls;
    }

    @Override
    public String[][] getVideoThumbs() {
        return mVideoThumbs;
    }

    @Override
    public String[][] getVideoTitles() {
        return mVideoTitles;
    }

    public String[][] getVideoTypes(){return mVideoTypes;}

    public String[][] getVideoTags(){return mVideoTags;}


}
