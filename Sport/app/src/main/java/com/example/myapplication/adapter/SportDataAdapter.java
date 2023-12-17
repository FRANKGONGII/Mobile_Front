
package com.example.myapplication.adapter;

import static com.example.myapplication.bean.Record.RecordType.RUNNING;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.TestActivity;
import com.example.myapplication.bean.Record;
import com.example.myapplication.user.UserInfoItem;

import java.util.List;

public class SportDataAdapter extends RecyclerView.Adapter<SportDataAdapter.ViewHolder> {

    private List<Record> sportRecordList;

    public SportDataAdapter(List<Record> sportRecordList) {
        this.sportRecordList = sportRecordList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
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

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public SportDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sport_card, parent, false);

        view.findViewById(R.id.sportCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), TestActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("activity_type", Record.RecordType.values()[viewType]);
                intent.putExtras(bundle);
                parent.getContext().startActivity(intent);
            }
        });

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
    public int getItemViewType(int position) {
        return sportRecordList.get(position).getRecordType().ordinal();
    }

    @Override
    public int getItemCount() {
        return sportRecordList.size();
    }
}