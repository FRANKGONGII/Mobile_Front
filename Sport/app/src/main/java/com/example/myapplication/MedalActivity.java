package com.example.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.load.model.FileLoader;
import com.example.myapplication.fragment.Medal1Fragment;
import com.example.myapplication.fragment.Medal2Fragment;
import com.example.myapplication.fragment.Medal3Fragment;
import com.example.myapplication.fragment.Medal4Fragment;
import com.example.myapplication.utils.PhotoUtil;
import com.google.android.material.card.MaterialCardView;
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

        // 按sp值修改TabLayout中标题的字体
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                // Create a custom TextView for the tab
                TextView customTextView = new TextView(this);
                customTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12); // Adjust the text size as per your requirement
                customTextView.setText(tab.getText());

                // Set the custom view for the tab
                tab.setCustomView(customTextView);
            }
        }

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

//        showMedal();

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void showMedal() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogView = getLayoutInflater().inflate(R.layout.show_medal, null);
        builder.setView(dialogView);


//        ImageView gifImageView = new ImageView(this);
        ImageView gifImageView = dialogView.findViewById(R.id.medal);


//        gifImageView.setBackgroundColor(Color.TRANSPARENT);
//        gifImageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        gifImageView.setImageResource(R.drawable.medal3);
        gifImageView.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
//        gifImageView.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
//        Outline outline = new Outline();
//        gifImageView.getOutlineProvider().getOutline(gifImageView, outline);
//
//        Drawable glowDrawable = getResources().getDrawable(R.drawable.glow);
//        glowDrawable.

        TextView textView = dialogView.findViewById(R.id.hint);
        SpannableString spannableString = new SpannableString("初见成效\n已获得!");
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(30, true), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FFD700")), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);
        textView.setVisibility(View.INVISIBLE);


        // 创建一个属性动画对象
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(gifImageView, "rotationY", 0f, 360f*15);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(gifImageView, "scaleX", 0.01f, 0.5f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(gifImageView, "scaleY", 0.01f, 0.5f);

        // 创建一个动画集合
        AnimatorSet animatorSet = new AnimatorSet();

        // 设置动画持续时间和重复次数
        rotationAnimator.setDuration(4000);
        rotationAnimator.setRepeatCount(0);
        scaleXAnimator.setDuration(4000);
        scaleXAnimator.setRepeatCount(0);
        scaleYAnimator.setDuration(4000);
        scaleYAnimator.setRepeatCount(0);


        // 将动画添加到动画集合中
        animatorSet.playTogether(rotationAnimator, scaleXAnimator, scaleYAnimator);

        // 设置插值器
//        animatorSet.setInterpolator(new DecelerateInterpolator());

        // 开始动画
        animatorSet.start();

        // 在动画结束后执行闪金光的效果
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                gifImageView.setColorFilter(null);
                textView.setVisibility(View.VISIBLE);

                ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
                animator.setDuration(2000);
                animator.setRepeatCount(ValueAnimator.INFINITE);
                animator.setRepeatMode(ValueAnimator.REVERSE);

                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                        Float value = (Float) animation.getAnimatedValue();

                        gifImageView.setOutlineProvider(new ViewOutlineProvider() {
                            @Override
                            public void getOutline(View view, Outline outline) {
                                outline.setOval(
                                        (int) (gifImageView.getWidth() / 2 - gifImageView.getWidth() / 2 * value),
                                        (int) (gifImageView.getHeight() / 2 - gifImageView.getHeight() / 2 * value),
                                        (int) (gifImageView.getWidth() / 2 + gifImageView.getWidth() / 2 * value),
                                        (int) (gifImageView.getHeight() / 2 + gifImageView.getHeight() / 2 * value)
                                );
                            }
                        });

                        gifImageView.invalidateOutline();
                    }
                });

                animator.start();



//                // 创建一个ValueAnimator来实现闪金光的效果
////                ValueAnimator glowAnimator = ValueAnimator.ofInt(0, 255);
//                ValueAnimator glowAnimator = ValueAnimator.ofArgb(Color.TRANSPARENT, Color.parseColor("#FFD700"));
//                glowAnimator.setDuration(1000);
//                glowAnimator.setRepeatCount(ValueAnimator.INFINITE);
//                glowAnimator.setRepeatMode(ValueAnimator.REVERSE);
//
//                // 设置动画更新监听器
//                glowAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//                        int alpha = (int) animation.getAnimatedValue();
//                        gifImageView.setOutlineProvider(new ViewOutlineProvider() {
//                            @Override
//                            public void getOutline(View view, Outline outline) {
//                                outline.setOval(0, 0, view.getWidth(), view.getHeight());
//                            }
//                        });
//                        gifImageView.setClipToOutline(true);
//                        gifImageView.setAlpha(alpha);
//                        gifImageView.invalidate();
////                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
////                            gifImageView.setOutlineSpotShadowColor(alpha);
////                            gifImageView.invalidate();
////                        }
//                    }
//                });
//
//                // 开始闪金光的动画
////                glowAnimator.start();
            }
        });

//        builder.setView(gifImageView);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void showMedal2() {

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
