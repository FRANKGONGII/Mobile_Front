package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.ChatActivity;
import com.example.myapplication.HistoryActivity;
import com.example.myapplication.R;
import com.example.myapplication.TestActivity;
import com.example.myapplication.data.DataService;
import com.example.myapplication.data.RemoteData;

public class CommunityFragment extends Fragment {

    //DataService dataService = new RemoteData();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_community, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = new Intent(getActivity(), TestActivity.class);
        intent.putExtra("TaskType", "NormalChat");
        startActivity(intent);
        getActivity().finish(); //本句有用，不然返回会回到这里
    }
}
