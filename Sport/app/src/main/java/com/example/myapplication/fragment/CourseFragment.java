package com.example.myapplication.fragment;

import static android.content.Context.SENSOR_SERVICE;

import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.adapter.videoAdapter;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class CourseFragment extends Fragment {
    private ListView mListView;
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
    }
}
