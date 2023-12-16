package com.example.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public abstract class BaseActivity extends AppCompatActivity {

    private View content;
    private int mX;
    private int mY;

    public void animPost() {
        Intent intent = getIntent();
        content = findViewById(R.id.sport_content);
        content.post(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mX = intent.getIntExtra("x", 0);
                    mY = intent.getIntExtra("y", 0);
                    Log.d("POS", String.valueOf(mX));
                    Log.d("POS", String.valueOf(mY));
                    Animator animator = createRevealAnimator(false, mX, mY);
                    animator.start();
                }
            }
        });
    }

    // 动画
    @SuppressLint("ResourceAsColor")
    private Animator createRevealAnimator(boolean reversed, int x, int y) {
        float hypot = (float) Math.hypot(content.getHeight(), content.getWidth());
        float startRadius = reversed ? hypot : 0;
        float endRadius = reversed ? 0 : hypot;

        Log.d("POS", String.valueOf(x));
        Log.d("POS", String.valueOf(y));
        Animator animator = ViewAnimationUtils.createCircularReveal(
                content, x, y,
                startRadius,
                endRadius);
        animator.setDuration(800);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
//        if (reversed)
//            animator.addListener(animatorListener);
        content.setBackgroundColor(Color.BLUE);

        return animator;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animPost();
    }
}
