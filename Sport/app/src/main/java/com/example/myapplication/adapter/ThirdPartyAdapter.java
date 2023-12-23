package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.bean.ThirdPartyBean;

import java.util.List;

public class ThirdPartyAdapter extends BaseAdapter {
    private List<ThirdPartyBean> content;
    private LayoutInflater inflater;

    public ThirdPartyAdapter(List<ThirdPartyBean> content, Context context){
        this.content = content;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return content.size();
    }

    @Override
    public Object getItem(int i) {
        return content.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ThirdPartyBean bean = content.get(i);
        view = inflater.inflate(R.layout.equipment_third_party_item, null);
        ImageView imageView = view.findViewById(R.id.third_party_item_CardImageView);
        TextView textView = view.findViewById(R.id.third_party_item_CardTextViewTop);
        imageView.setImageResource(bean.getImageURI());
        textView.setText(bean.getName());
        return view;
    }
}
