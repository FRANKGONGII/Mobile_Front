package com.example.myapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.constant.VideoConstant;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class videoAdapter extends BaseAdapter{
    public int[] mVideoIndexs = {0,1,2,3,4};
    Context mContext;
    int mPager = -1;

    VideoConstant videoConstant = new VideoConstant();

    public videoAdapter(Context context) {
        this.mContext = context;
    }

    public videoAdapter(Context context, int pager) {
        this.mContext = context;
        this.mPager = pager;
    }


    //自定义接口，用于回调按钮点击事件到Activity

    @Override
    public int getCount() {
        return mPager == -1 ? mVideoIndexs.length : 2;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int[] getmVideoIndexs(){return mVideoIndexs;}

    public void search(String query){
        List<Integer> res = new ArrayList();
        for(int i = 0;i<videoConstant.mVideoTitles[0].length;i++){
            String title = videoConstant.mVideoTitles[0][i];
            String type = videoConstant.mVideoTypes[0][i];
            String tags = videoConstant.mVideoTags[0][i];
            Log.d("SEARCH_TEST",title+" "+type+" "+tags+" "+i);

            //TODO:这里强行实现了类似模糊识别的功能，双向判断子串
            if(query.contains(title)||query.contains(type)||query.contains(tags)
            ||title.contains(query)||type.contains(query)||tags.contains(query)){
                res.add(i);
            }
        }
        mVideoIndexs = new int[res.size()];
        for(int i = 0;i<res.size();i++){
            mVideoIndexs[i] = res.get(i);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Log.d("SEARCH_TEST",position+" "+mVideoIndexs[position]);
        position = mVideoIndexs[position];
        ViewHolder holder;
        if (null == convertView) {
            holder = new ViewHolder();
            LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
            convertView = mLayoutInflater.inflate(R.layout.item_videoview, null);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mJCVideoPlayerStandard = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
//        if(position==2) {
//            TextView tx = convertView.findViewById(R.id.video_type);
//            tx.setText("haha");
//        }
        TextView txType = convertView.findViewById(R.id.video_tag1);
        txType.setText(videoConstant.getmVideoTypes()[0][position]);

        TextView txTag = convertView.findViewById(R.id.video_tag2);
        txTag.setText(videoConstant.getmVideoTags()[0][position]);

//        txType.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String searchKey = (String) txType.getText();
//                Log.d("CLICK_TEST","searchKey: "+searchKey);
//                search(searchKey);
//
//            }
//        });
//
//        txTag.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String searchKey = (String) txTag.getText();
//                Log.d("CLICK_TEST","searchKey: "+searchKey);
//                search(searchKey);
//
//            }
//        });





        if (mPager == -1) {
            holder.mJCVideoPlayerStandard.setUp(
                    videoConstant.mVideoUrls[0][position], JCVideoPlayer.SCREEN_LAYOUT_LIST,
                    videoConstant.mVideoTitles[0][position]);
            Log.e("QUERY_TEST", "setUp" + position);
            Picasso.with(convertView.getContext())
                    .load(videoConstant.mVideoThumbs[0][position])
                    .into(holder.mJCVideoPlayerStandard.thumbImageView);
        } else {
            holder.mJCVideoPlayerStandard.setUp(
                    videoConstant.mVideoUrls[mPager][position], JCVideoPlayer.SCREEN_LAYOUT_LIST,
                    videoConstant.mVideoTitles[mPager][position]);
            Picasso.with(convertView.getContext())
                    .load(videoConstant.mVideoThumbs[mPager][position])
                    .into(holder.mJCVideoPlayerStandard.thumbImageView);
        }
        return convertView;
    }

    class ViewHolder {
        JCVideoPlayerStandard mJCVideoPlayerStandard;
    }

}
