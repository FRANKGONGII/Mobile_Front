package com.example.myapplication.fragment;

import static java.lang.Thread.sleep;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.HistoryActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.RecordingActivity;

public class SportFragment extends Fragment {

    String sport_type = "跑步";//默认运动是跑步
    private AlertDialog alertDialog2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_sport, container, false);

        //TODO：这里应该要去获取上面面板的数据


        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setButtonGO(view);
        setButtonChoose(view);
        set_nav1(view);
    }

    //
    public void setButtonGO(@NonNull View view) {
        Button button = view.findViewById(R.id.ButtonGo);
        String word = (String) button.getText();
        final boolean[] ifStart = {false};
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("BUTTON_TEST","press go");
                        if(!ifStart[0])  ifStart[0] = true;
                        else ifStart[0] = false;
                        if(ifStart[0]){
                            final int[] cntTime = {3};
                            Handler handler = new Handler();
                            Runnable runnable = new Runnable(){
                                @Override
                                public void run(){
                                    //数字自增
                                    // 创建文本
                                    if(!ifStart[0])return;
                                    Log.d("CNT_TEST",String.valueOf(cntTime[0]));
                                    if(cntTime[0]>0)button.setText(String.valueOf(cntTime[0]));
                                    else if(cntTime[0]==0){
                                        button.setText("GO!");
                                        Intent intent = new Intent(getActivity(), RecordingActivity.class);

                                        intent.putExtra("sport_type", sport_type);
                                        startActivity(intent);
                                        return;
                                    }

                                    cntTime[0]--;
                                    if(cntTime[0]<0)return;
                                    handler.postDelayed(this, 1000);

                                }
                            };
                            handler.post(runnable);
                            if(!ifStart[0])return;

//                        button.setBackgroundResource(R.drawable.animation_go);//把Drawable设置为button的背景
//                        //拿到这个我们定义的Drawable，实际也就是AnimationDrawable
//                        AnimationDrawable animationDrawable = (AnimationDrawable) button.getBackground();
//                        animationDrawable.setOneShot(true);// 播放一次
//                        animationDrawable.start();//开启动画
//
//                        Log.d("ANI_TEST","end playing");
                            //Button jumpFragment = view.findViewById(R.id.start_sport_fragment);

                            // 需要另外启动一个线程 在新线程里面去sleep
                            // 不要在UI线程中sleep UI被sleep会暂停刷新
//                            Thread t = new Thread(new Runnable() {
//                                @Override
//                                public void run() {
////                                    try {
////                                        //sleep(3200);
////                                        //注意，这里设置成3500是为了让GO！能显示一下
////                                    } catch (InterruptedException e) {
////                                        // TODO Auto-generated catch block
////                                        e.printStackTrace();
////                                    }
//                                    Intent intent = new Intent(getActivity(), RecordingActivity.class);
//
//                                    intent.putExtra("sport_type", sport_type);
//                                    startActivity(intent);
//                                }
//                            });
                            // 开启线程



//                            Intent intent = new Intent(getActivity(), RecordingActivity.class);
//
//                            intent.putExtra("sport_type", sport_type);
//                            startActivity(intent);
                        }else{
                            button.setText(word);
                        }


                    }
                }
        );
    }

    public void setButtonChoose(@NonNull View view){
        Button button = view.findViewById(R.id.ButtonChooseType);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleAlertDialog(view);
            }
        });
    }

    //为累计跑步控件设置跳转，跳转到历史记录界面
    public void set_nav1(@NonNull View view){
        View nav = view.findViewById(R.id.sum_distance_linear);
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HistoryActivity.class);

                startActivity(intent);
            }
        });
    }

    public void showSingleAlertDialog(View view){
        final String[] items = {"跑步", "骑行", "游泳", "快走"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle("这是单选框");

        final int[] choose = {-1};
        alertBuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(), items[i], Toast.LENGTH_SHORT).show();

                choose[0] =i;
                //Log.d("CHOOSE_TEST","CHOOSE:"+String.valueOf(choose[0]));
            }
        });

        alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("CHOOSE_TEST",String.valueOf(choose[0]));
                if(choose[0]!=-1)sport_type = items[choose[0]];
                Button button = view.findViewById(R.id.ButtonChooseType);
                button.setText(sport_type);
                Button button2 = view.findViewById(R.id.ButtonGo);
                button2.setText(sport_type+"\n"+"GO");
                alertDialog2.dismiss();
            }
        });

        alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog2.dismiss();
            }
        });
        alertDialog2 = alertBuilder.create();
        alertDialog2.show();
    }

}