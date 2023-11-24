package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.amap.api.services.auto.ListData;
import com.example.myapplication.bean.Record;

import java.util.List;

public class SportCardAdapter extends BaseAdapter {

    private List<Record> recordList;
    private LayoutInflater layoutInflater;


    public SportCardAdapter(Context context, List<Record> recordList) {
        this.recordList = recordList;
        layoutInflater = LayoutInflater.from(context);
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
