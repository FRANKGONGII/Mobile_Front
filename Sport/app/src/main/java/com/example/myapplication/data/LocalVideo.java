package com.example.myapplication.data;

public class LocalVideo implements VideoService {
    public static String[][] mVideoUrls = {{
            "https://stream7.iqilu.com/10339/upload_transcode/202002/18/20200218114723HDu3hhxqIT.mp4",
            "https://stream7.iqilu.com/10339/upload_transcode/202002/18/20200218093206z8V1JuPlpe.mp4",
            "https://stream7.iqilu.com/10339/article/202002/18/2fca1c77730e54c7b500573c2437003f.mp4",
            "https://stream7.iqilu.com/10339/upload_transcode/202002/18/20200218025702PSiVKDB5ap.mp4",
            "https://stream7.iqilu.com/10339/upload_transcode/202002/18/202002181038474liyNnnSzz.mp4"
    }
    };

    //封面图片
    public static String[][] mVideoThumbs = {{
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1526158872907&di=410c4c0fdc85f768e6b4fb8a8b6cf208&imgtype=0&src=http%3A%2F%2F09.imgmini.eastday.com%2Fmobile%2F20180506%2F20180506234300_a1330efaec84d2361efd72e3976ee181_2.jpeg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1526158872907&di=410c4c0fdc85f768e6b4fb8a8b6cf208&imgtype=0&src=http%3A%2F%2F09.imgmini.eastday.com%2Fmobile%2F20180506%2F20180506234300_a1330efaec84d2361efd72e3976ee181_2.jpeg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1526158872907&di=410c4c0fdc85f768e6b4fb8a8b6cf208&imgtype=0&src=http%3A%2F%2F09.imgmini.eastday.com%2Fmobile%2F20180506%2F20180506234300_a1330efaec84d2361efd72e3976ee181_2.jpeg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1526158872907&di=410c4c0fdc85f768e6b4fb8a8b6cf208&imgtype=0&src=http%3A%2F%2F09.imgmini.eastday.com%2Fmobile%2F20180506%2F20180506234300_a1330efaec84d2361efd72e3976ee181_2.jpeg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1526158872907&di=410c4c0fdc85f768e6b4fb8a8b6cf208&imgtype=0&src=http%3A%2F%2F09.imgmini.eastday.com%2Fmobile%2F20180506%2F20180506234300_a1330efaec84d2361efd72e3976ee181_2.jpeg"
    },
    };

    //标题
    public static String[][] mVideoTitles = {{
            "test5",
            "test2",
            "test2",
            "test4",
            "test6"
    },
    };

    public static String[][] mVideoTypes = {{
            "running",
            "running",
            "swimming",
            "hiking",
            "cycling"
    },
    };

    public static String[][] mVideoTags = {{
            "senior",
            "senior",
            "primary",
            "primary",
            "senior"
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
