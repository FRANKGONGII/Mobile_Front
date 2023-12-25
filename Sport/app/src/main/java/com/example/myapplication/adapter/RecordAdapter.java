package com.example.myapplication.adapter;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ChatActivity;
import com.example.myapplication.HistoryActivity;
import com.example.myapplication.R;
import com.example.myapplication.ResultActivity;
import com.example.myapplication.bean.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private List<Record> recordList;

    public static final List<Integer> rankUriList = new ArrayList<Integer>(){{
        add(R.drawable.rank_s);
        add(R.drawable.rank_a);
        add(R.drawable.rank_b);
        add(R.drawable.rank_c);
    }};

    public static final HashMap<Record.RecordType, Integer> typeUriMap = new HashMap<Record.RecordType, Integer>(){{
        put(Record.RecordType.RUNNING, R.drawable.record_type_running);
        put(Record.RecordType.RIDING, R.drawable.record_type_riding);
        put(Record.RecordType.WALKING, R.drawable.record_type_walking);
        put(Record.RecordType.SWIMMING, R.drawable.record_type_swimming);
    }};

    public static final List<String> rankList = new ArrayList<String>(){{
        add("Excellent!");
        add("Well Done!");
        add("Not Bad!");
        add("Come On!");
    }};

    public static final List<Integer> rankColorList = new ArrayList<Integer>(){{
        add(Color.parseColor("#DB0E0E"));
        add(Color.parseColor("#FD9735"));
        add(Color.parseColor("#38618F"));
        add(Color.parseColor("#C7C5B8"));
    }};

    static class ViewHolder extends RecyclerView.ViewHolder {
        View recordView;//存储解析到的view

        TextView recordTime; // 运动日期

        TextView distance; // 跑步距离

        TextView duration; // 跑步时间，秒计

        TextView record_type;
//        Button chat_btn;
        ImageView type_img;
        ImageView rank_img;
        TextView rank_text;

        long record_id;

        public ViewHolder(View view) {
            super(view);
            recordView = view;
            recordTime = view.findViewById(R.id.record_time);
            distance = view.findViewById(R.id.distance);
            duration = view.findViewById(R.id.duration);
            record_type = view.findViewById(R.id.record_type);
//            chat_btn = view.findViewById(R.id.btn_chat);
            type_img = view.findViewById(R.id.typeImg);
            rank_img = view.findViewById(R.id.rankImg);
            rank_text = view.findViewById(R.id.rankText);
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
                long id = viewHolder.record_id;

                Intent intent = new Intent(view.getContext(), ResultActivity.class);
                intent.putExtra("passId",id);
                intent.putExtra("formActivity","history");

                view.getContext().startActivity(intent);
            }
        });

        // Button的onClick，用于进入ai界面对话分析运动数据
//        viewHolder.chat_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), ChatActivity.class);
//                intent.putExtra("TaskType", "EvalRecord");
//                String data = "我进行了一次" + viewHolder.record_type.getText() + "运动，总距离为" +
//                        viewHolder.distance.getText() + "公里， 用时" + viewHolder.duration.getText() +
//                        "。请你根据上述数据对我本次运动的表现给出意见。";
//
//                intent.putExtra("RecordData", data);
//
//                view.getContext().startActivity(intent);
//            }
//        });

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

        holder.type_img.setImageResource(typeUriMap.get(record_bean.getRecordType()));
        int rank_num = getRank(record_bean);
        holder.rank_img.setImageResource(rankUriList.get(rank_num));
        holder.rank_text.setText(rankList.get(rank_num));
        holder.rank_text.setTextColor(rankColorList.get(rank_num));
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public int getRank(Record record){
        HashMap<Record.RecordType, Double> distRateMap = new HashMap<Record.RecordType, Double>(){{
            put(Record.RecordType.RUNNING, 1.0);
            put(Record.RecordType.RIDING, 3.0);
            put(Record.RecordType.WALKING, 1.2);
            put(Record.RecordType.SWIMMING, 0.4);
        }};

        HashMap<Record.RecordType, Double> speedRateMap = distRateMap;

        LinkedHashMap<Double, Integer> distRankMap = new LinkedHashMap<Double, Integer>(){{
            put(3.0, 0);
            put(2.4, 1);
            put(1.5, 2);
        }};

        LinkedHashMap<Double, Integer> speedRankMap = new LinkedHashMap<Double, Integer>(){{
            put(4.76, 0);
            put(3.7, 1);
            put(3.03, 2);
        }};

        int distRank = searchRankTable(distRankMap, record.getDistance() * distRateMap.get(record.getRecordType()));
        double speed = record.getDistance() / record.getDuration() * 1000;
        int speedRank = searchRankTable(speedRankMap, speed * speedRateMap.get(record.getRecordType()));
        return (distRank + speedRank + 1) / 2;
    }

    public int searchRankTable(HashMap<Double, Integer> table, double value){
        for(Map.Entry<Double, Integer> entry : table.entrySet()){
            if(value >= entry.getKey()) return entry.getValue();
        }
        return 3;
    }

}