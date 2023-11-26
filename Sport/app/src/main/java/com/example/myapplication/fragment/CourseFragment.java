package com.example.myapplication.fragment;

import static android.content.Context.SENSOR_SERVICE;

import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.adapter.videoAdapter;
import com.hjq.toast.ToastUtils;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class CourseFragment extends Fragment  {

    private ListView mListView;

    private SearchView mSearchView;
    private videoAdapter mAdapter;
    private SensorEventListener mSensorEventListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mListView = (ListView) view.findViewById(R.id.list_view);
        mAdapter = new videoAdapter(getContext());
        mListView.setAdapter(mAdapter);
        //mSensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();
        mSearchView = (SearchView) view.findViewById(R.id.searchView);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("查找");
        //ToastUtils.init(getActivity().getApplication());
        int adapterItemLen = mAdapter.mVideoIndexs.length;

        for(int i = 0;i<adapterItemLen;i++){
            mAdapter.getItem(i);
        }

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
