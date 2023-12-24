package com.example.myapplication;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.fragment.Medal1Fragment;
import com.example.myapplication.fragment.Medal2Fragment;
import com.example.myapplication.fragment.Medal3Fragment;
import com.example.myapplication.fragment.Medal4Fragment;
import com.example.myapplication.utils.PhotoUtil;
import com.google.android.material.tabs.TabLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class MedalActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medal); // 替换为你的布局文件名
        pref = getSharedPreferences("userInfo", MODE_PRIVATE);

        ImageButton back = findViewById(R.id.medal_back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 获取布局中的组件
        circleImageView = findViewById(R.id.circularImageView);
        TextView imageLabel = findViewById(R.id.imageLabel);

        // 设置圆形图片


        // 设置TextView的文本
        imageLabel.setText("继续收集奖牌吧！");


        // 获取布局中的组件
        TabLayout tabLayout = findViewById(R.id.medal_tabLayout);
        ViewPager viewPager = findViewById(R.id.medal_viewPager);

        // 创建适配器并将其与ViewPager关联
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        // 将TabLayout与ViewPager关联
        tabLayout.setupWithViewPager(viewPager);

        // 添加滑块动画效果
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                // 根据选中的位置切换对应的Fragment
//                adapter.getItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                // 可选，当Tab被取消选中时的操作
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//                // 可选，当已经选中的Tab再次被点击时的操作
//            }
//        });




    }

    @Override
    protected void onResume() {
        super.onResume();

        String avatarBase64Str = pref.getString("avatar", "");

        if (avatarBase64Str.equals("")) {
            // 设置默认头像
            circleImageView.setImageResource(R.drawable.baseline_avatar_default1);
        } else {
            // 转化为bitmap
            Bitmap bitmap = PhotoUtil.base64Str2Bitmap(avatarBase64Str);
            circleImageView.setImageBitmap(bitmap);
        }

    }

    // 定义FragmentPagerAdapter
    private static class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }


        @Override
        public Fragment getItem(int position) {
            Log.d("MEDAL_TEST","come: "+position);
            // 根据位置返回不同的Fragment
            switch (position) {
                case 0:
                    Log.d("MEDAL_TEST",position+"");
                    return new Medal1Fragment();
                case 1:
                case 3:
                case 2:
                    Log.d("MEDAL_TEST",position+"");
                    return new Medal2Fragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // 返回Fragment的数量
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // 返回TabLayout中每个标签的标题
            switch (position) {
                case 0:
                    return "运动征途";
                case 1:
                    return "荣誉赛程";
                case 2:
                    return "社交达人";
                case 3:
                    return "趣味记录";
                default:
                    return null;
            }
        }

    }


}
