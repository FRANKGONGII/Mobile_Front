package com.example.myapplication.ui.sport;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.databinding.FragmentSportBinding;

public class SportFragment extends Fragment {

    private FragmentSportBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SportViewModel sportViewModel =
                new ViewModelProvider(this).get(SportViewModel.class);

        binding = FragmentSportBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSport;
        sportViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}