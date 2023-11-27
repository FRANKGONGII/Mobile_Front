package com.example.myapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class VideoChooseAdapter extends BaseAdapter implements View.OnClickListener {

    private String[] types = {"test","test2","test3","test4","test5",
            "running","se","pri","type9","type10",
            "type11","type12","type13","type14","type15"};

    private ChooseItemOnclickListener mListener;

    Context mContext;

    public interface ChooseItemOnclickListener {
        void chooseItemClick(View v);
    }

    public void setOnInnerItemOnClickListener(ChooseItemOnclickListener listener){
        this.mListener=listener;
    }

    public VideoChooseAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {
        mListener.chooseItemClick(v);
    }

    @Override
    public int getCount() {
        return types.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getTypesByIndex(int position){
        return types[position];
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //position = mIndexs[position];
        Log.d("TEST_LIST",position+" "+types[position]);
        ViewHolder holder;
        if(convertView==null){
            Log.d("TEST_LIST","convertView==null");
            holder = new ViewHolder();
            LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
            convertView = mLayoutInflater.inflate(R.layout.item_video_choose, null);


            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.bt1 = (Button) convertView.findViewById(R.id.video_choose);
        holder.bt1.setText(types[position]);
        holder.bt1.setOnClickListener(this);
        holder.bt1.setTag(position);

        return convertView;
    }

    class ViewHolder {
        Button bt1;
    }

}
