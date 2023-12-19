package com.example.myapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.fragment.CourseFragment;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class VideoChooseAdapter extends BaseAdapter implements View.OnClickListener {

    private String[] types = {"跑步","健走","骑行","健身","减脂",
            "塑形","增肌", "瑜伽",
            "初级","中级","高级"};

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

    public  int choose = -1;

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
        holder.bt1 = (TextView) convertView.findViewById(R.id.video_choose);
        holder.bt1.setText(types[position]);
        holder.bt1.setOnClickListener(this);
        holder.bt1.setTag(position);

        if(position==choose){
            holder.bt1.setBackgroundColor(Color.parseColor("#CCCCCC"));
        }else{
            holder.bt1.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }



        return convertView;
    }

    class ViewHolder {
        TextView bt1;
    }

}
