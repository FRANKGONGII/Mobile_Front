package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.bean.Record;

import java.util.List;

public class SportDataAdapter extends RecyclerView.Adapter<SportDataAdapter.ViewHolder> {

    private List<Record> sportRecordList;

    public SportDataAdapter(List<Record> sportRecordList) {
        this.sportRecordList = sportRecordList;
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

    @NonNull
    @Override
    public SportDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sport_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SportDataAdapter.ViewHolder holder, int position) {
        Record record = sportRecordList.get(position);
        holder.activity_type.setText(record.getRecordTypeByStr());
        holder.distance.setText(record.getDistanceByStr());
        holder.duration.setText(record.getDurationByStr());
    }

    @Override
    public int getItemCount() {
        return sportRecordList.size();
    }
}
