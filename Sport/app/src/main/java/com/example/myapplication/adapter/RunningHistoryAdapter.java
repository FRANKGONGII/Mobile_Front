package com.example.myapplication.adapter;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.bean.Activity;

import java.util.ArrayList;
import java.util.List;

public class RunningHistoryAdapter extends RecyclerView.Adapter<RunningHistoryAdapter.ViewHolder> {
    private List<Activity> activityList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View activityView;//存储解析到的view

        TextView recordTime; // 运动日期

        TextView distance; // 跑步距离

        TextView duration; // 跑步时间，秒计

        TextView activity_type;

        public ViewHolder(View view) {
            super(view);
            activityView = view;
            recordTime = view.findViewById(R.id.record_time);
            distance = view.findViewById(R.id.distance);
            duration = view.findViewById(R.id.duration);
            activity_type = view.findViewById(R.id.activity_type);
        }
    }

    public RunningHistoryAdapter(List<Activity> videoList) {
        activityList = videoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);//解析layout
        final ViewHolder viewHolder = new ViewHolder(view);//新建一个viewHolder绑定解析到的view
        //监听每一项的点击事件
        viewHolder.activityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int position = viewHolder.getAdapterPosition();
//                Video_bean video_bean = mContactList.get(position);
//
//                Intent intent = new Intent(view.getContext(), MediaPlayerActivity.class);
//                intent.putExtra("src", video_bean.getVideo_src());
//                intent.putExtra("video_title", video_bean.getVideo_title());
//                view.getContext().startActivity(intent);
                //TODO: 跳转到详情界面
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Activity activity_bean = activityList.get(position);
        holder.recordTime.setText(activity_bean.getRecordTime());
        holder.duration.setText(activity_bean.getDuration());
        holder.distance.setText(activity_bean.getDistance());
        holder.activity_type.setText(activity_bean.getType());
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

}