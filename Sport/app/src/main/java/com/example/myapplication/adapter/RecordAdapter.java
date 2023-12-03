package com.example.myapplication.adapter;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.ResultActivity;
import com.example.myapplication.bean.Record;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private List<Record> recordList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View recordView;//存储解析到的view

        TextView recordTime; // 运动日期

        TextView distance; // 跑步距离

        TextView duration; // 跑步时间，秒计

        TextView record_type;

        int record_id;

        public ViewHolder(View view) {
            super(view);
            recordView = view;
            recordTime = view.findViewById(R.id.record_time);
            distance = view.findViewById(R.id.distance);
            duration = view.findViewById(R.id.duration);
            record_type = view.findViewById(R.id.record_type);
        }
    }

    public RecordAdapter(List<Record> recordList) {
        this.recordList = recordList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);//解析layout
        final ViewHolder viewHolder = new ViewHolder(view);//新建一个viewHolder绑定解析到的view
        //监听每一项的点击事件
        viewHolder.recordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = viewHolder.record_id;

                Intent intent = new Intent(view.getContext(), ResultActivity.class);
                intent.putExtra("passId",id);

                view.getContext().startActivity(intent);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Record record_bean = recordList.get(position);
        holder.recordTime.setText(record_bean.getRecordTime());
        holder.duration.setText(record_bean.getDurationByStr());
        holder.distance.setText(record_bean.getDistanceByStr());
        holder.record_type.setText(record_bean.getRecordTypeByStr());
        holder.record_id = record_bean.getId();
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

}