package com.example.myapplication.fragment;

import static android.content.Context.SENSOR_SERVICE;

import android.graphics.Color;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.adapter.VideoChooseAdapter;
import com.example.myapplication.adapter.videoAdapter;

import com.hjq.toast.ToastUtils;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class CourseFragment extends Fragment  implements videoAdapter.InnerItemOnclickListener,
        VideoChooseAdapter.ChooseItemOnclickListener {
    @Override
    public void itemClick(View v) {
        int position;
        position = (Integer) v.getTag();

        int id = v.getId();
        if(id==R.id.video_button1){
            Log.d("CLICK_TEST","click tag1");
            String query = mAdapter.getTypesByIndex(position);
            mAdapter.search(query);
            mListView.setAdapter(mAdapter);
        }else if(id==R.id.video_button2){
            Log.d("CLICK_TEST","click tag2");
            String query = mAdapter.getTagsByIndex(position);
            mAdapter.search(query);
            mListView.setAdapter(mAdapter);
        }else if(id==R.id.video_button3){
            if(mAdapter.favourite[position]){
                mAdapter.favourite[position] = false;
                Button bt3 = v.findViewById(id);
                bt3.setBackgroundResource(R.drawable.baseline_favorite_border_24);
            }else{
                mAdapter.favourite[position] = true;
                Button bt3 = v.findViewById(id);
                bt3.setBackgroundResource(R.drawable.baseline_favorite_24);
            }

        }else{
            Log.d("ClICK_TEST","undefined button id");
        }
        Log.d("CLICK_TEST","click: "+position+" id: "+v.getId());
    }

    View lastChoose = null;



    @Override
    public void chooseItemClick(View v) {
        int position;
        position = (Integer) v.getTag();

        int id = v.getId();
        Log.d("CLICK_TEST",position+" "+id);
        if(id==R.id.video_choose){


            //Log.d("CLICK_TEST","click side list"+" "+mPosition);
            String query = mChooseAdapter.getTypesByIndex(position);
            mAdapter.search(query);
            mListView.setAdapter(mAdapter);


            if(lastChoose!=null){
                Button btLast = lastChoose.findViewById(R.id.video_choose);
                btLast.setBackgroundColor(Color.parseColor("#FFFFFF"));
                //btLast.setBackgroundResource(R.drawable.frame);
            }
            Button bt1 = v.findViewById(R.id.video_choose);
            bt1.setBackgroundColor(Color.parseColor("#CCCCCC"));
            //bt1.setBackgroundResource(R.drawable.frame_gray);

            lastChoose = v;

            mChooseAdapter.choose = position;



        }

    }








    private ListView mListView;
    private ListView mListChooseView;

    private SearchView mSearchView;
    private videoAdapter mAdapter;
    private VideoChooseAdapter mChooseAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mListView = (ListView) view.findViewById(R.id.list_view);
        mListChooseView = (ListView) view.findViewById(R.id.list_choose);



        mAdapter = new videoAdapter(getContext());
        mAdapter.setOnInnerItemOnClickListener(this);
        mListView.setAdapter(mAdapter);
        //mListView.setOnItemClickListener(this);

        mChooseAdapter = new VideoChooseAdapter(getContext());
        mChooseAdapter.setOnInnerItemOnClickListener(this);
        mListChooseView.setAdapter(mChooseAdapter);


        //mSensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();
        mSearchView = (SearchView) view.findViewById(R.id.searchView);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("查找");
        //ToastUtils.init(getActivity().getApplication());
//        int adapterItemLen = mAdapter.mVideoIndexs.length;
//
//        for(int i = 0;i<adapterItemLen;i++){
//            mAdapter.getItem(i);
//        }

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("QUERY_TEST",query);
                mAdapter.search(query);
                StringBuilder sb = new StringBuilder();
                for(int i:mAdapter.getmVideoIndexs()){
                    sb.append(i+" ");
                }
                Log.d("QUERY_TEST",sb.toString());
                mListView.setAdapter(mAdapter);
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    mListView.setFilterText(newText);
                }else{
                    mListView.clearTextFilter();
                }
                return false;
            }
        });





    }



}
