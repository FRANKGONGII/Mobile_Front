package com.example.myapplication.fragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.SecondActivity;

public class SportFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /*Button button = getActivity().findViewById(R.id.ButtonGo);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("BUTTON_TEST","press go");
                    }
                }
        );*/
        View mView = inflater.inflate(R.layout.fragment_sport, container, false);

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button button = view.findViewById(R.id.ButtonGo);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("BUTTON_TEST","press go");
                        button.setText("");
                        button.setBackgroundResource(R.drawable.animation_go);//把Drawable设置为button的背景
                        //拿到这个我们定义的Drawable，实际也就是AnimationDrawable
                        AnimationDrawable animationDrawable = (AnimationDrawable) button.getBackground();
                        animationDrawable.setOneShot(true);// 播放一次
                        animationDrawable.start();//开启动画
                        Log.d("ANI_TEST","end playing");
                        //Button jumpFragment = view.findViewById(R.id.start_sport_fragment);

                        // 需要另外启动一个线程 在新线程里面去sleep
                        // 不要在UI线程中sleep UI被sleep会暂停刷新
                        Thread t = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent(getActivity(),SecondActivity.class);

                                intent.putExtra("mydata", "来自页面一");
                                startActivity(intent);
                            }
                        });

                        // 开启线程
                        t.start();



                    }
                }
        );
    }
}