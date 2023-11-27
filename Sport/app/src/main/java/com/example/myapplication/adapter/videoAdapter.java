package com.example.myapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.example.myapplication.R;
import com.example.myapplication.constant.VideoConstant;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class videoAdapter extends BaseAdapter implements OnClickListener{

    private InnerItemOnclickListener mListener;

    public interface InnerItemOnclickListener {
        void itemClick(View v);
    }

    public void setOnInnerItemOnClickListener(InnerItemOnclickListener listener){
        this.mListener=listener;
    }

    @Override
    public void onClick(View v) {
        mListener.itemClick(v);
    }





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

    public String getTagsByIndex(int position){
        StringBuilder s = new StringBuilder();
        for(int i:mVideoIndexs){
            s.append(i);
        }
        Log.d("CLICK_TEST","now indexs: "+s+" "+position);
        //Log.d("CLICK_TEST","get tags by index:"+position+" "+mVideoIndexs[position]+" "+s);
        return videoConstant.mVideoTags[0][position];
    }

    public String getTypesByIndex(int position){
        StringBuilder s = new StringBuilder();
        for(int i:mVideoIndexs){
            s.append(i);
        }
        //Log.d("CLICK_TEST","get types by index:"+position+" "+mVideoIndexs[position]+" "+s);
        return videoConstant.mVideoTypes[0][position];
    }



    public void search(String query){
        List<Integer> res = new ArrayList();
        for(int i = 0;i<videoConstant.mVideoTitles[0].length;i++){
            String title = videoConstant.mVideoTitles[0][i];
            String type = videoConstant.mVideoTypes[0][i];
            String tags = videoConstant.mVideoTags[0][i];
            Log.d("SEARCH_TEST",title+" "+type+" "+tags+" "+i);

            //这里强行实现了类似模糊识别的功能，双向判断子串
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
            holder.bt1 = (Button) convertView.findViewById(R.id.video_button1);
            holder.bt2 = (Button) convertView.findViewById(R.id.video_button2);
            holder.bt1.setText(videoConstant.getmVideoTypes()[0][position]);
            holder.bt2.setText(videoConstant.getmVideoTags()[0][position]);
            //TODO:增加其他两个类型按钮的处理
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mJCVideoPlayerStandard = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);

        holder.bt1.setOnClickListener(this);
        holder.bt2.setOnClickListener(this);
        holder.bt1.setTag(position);
        holder.bt2.setTag(position);


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
        Button bt1, bt2;
    }

}
