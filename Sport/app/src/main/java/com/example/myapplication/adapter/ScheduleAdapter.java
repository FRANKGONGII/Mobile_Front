package com.example.myapplication.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.bean.Schedule;

import java.util.List;
import java.util.Locale;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private List<Schedule> scheduleList;

    // 构造函数，接收日程列表
    public ScheduleAdapter(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
    }

    // 创建ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan, parent, false);
        return new ViewHolder(view);
    }

    // 绑定数据到ViewHolder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);

        // 设置日程项的标题、时间等信息
        holder.textViewScheduleTitle.setText(schedule.getTitle());
        holder.textViewScheduleTime.setText(schedule.getTime());

        // 在这里设置其他日程信息的视图元素

        // 添加点击事件等逻辑
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SH_TEST",position+" ");

            }
        });
    }

    // 获取列表项的数量
    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    // ViewHolder 类，用于保存日程项的视图引用
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewScheduleTitle;
        TextView textViewScheduleTime;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewScheduleTitle = itemView.findViewById(R.id.textViewScheduleTitle);
            textViewScheduleTime = itemView.findViewById(R.id.textViewScheduleTime);
            // 在这里添加其他视图元素的引用
        }
    }
}
