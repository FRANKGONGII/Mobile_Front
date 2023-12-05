package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.services.auto.ListData;
import com.example.myapplication.R;
import com.example.myapplication.bean.Record;

import java.util.List;

public class SportCardAdapter extends BaseAdapter {

    private Context mContext;
    private List<Record> recordList;
    private LayoutInflater inflater;

    public SportCardAdapter(Context context, List<Record> recordList) {
        mContext = context;
        this.recordList = recordList;
        this.inflater = LayoutInflater.from(context);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View activityView;//存储解析到的view
        TextView activity_type; //运动类型
        TextView distance; // 运动距离
        TextView duration; // 运动时间，秒计

        public ViewHolder(View view) {
            super(view);
            activityView = view;
            activity_type = view.findViewById(R.id.sportCardType);
            distance = view.findViewById(R.id.sportCardDistance);
            duration = view.findViewById(R.id.sportCardDuration);
        }
    }

    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int position) {
        return recordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_sport_card, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Record record = recordList.get(position);
        holder.activity_type.setText(record.getType());
        holder.distance.setText(record.getDistance());
        holder.duration.setText(record.getDuration());


        return convertView;
    }

}
