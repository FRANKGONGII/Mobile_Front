package com.example.myapplication.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

import java.util.Locale;

public class Medal2Fragment extends Fragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("MEDAL_TEST","there");
        View view = inflater.inflate(R.layout.fragment_medal_empty, container, false);
        return view;
    }
}
