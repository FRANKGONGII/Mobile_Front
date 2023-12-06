package com.example.myapplication;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayout;
    private Animator mAnimReveal;
    private Animator mAnimRevealR;

    public static final String CLICK_X = "CLICK_X";
    public static final String CLICK_Y = "CLICK_Y";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("WO", "before ");
        circularReveal(getIntent());
        Log.d("WO", "after");
    }

    private void circularReveal(Intent intent) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ||
                (intent == null && (intent == null || !intent.hasExtra(CLICK_X) || intent.getBooleanExtra(CLICK_X, false)))) {
            return;
        }

        Log.d("WO", "jere");

        Rect rect = intent.getSourceBounds();
        View v = getWindow().getDecorView();
        v.setVisibility(View.INVISIBLE);


        onGlobalLayout = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mAnimReveal != null) {
                    mAnimReveal.removeAllListeners();
                    mAnimReveal.cancel();
                }

                mAnimReveal = ViewAnimationUtils.createCircularReveal(v,
                        rect != null ? rect.centerX() : intent.getIntExtra(CLICK_X, 0),
                        rect != null ? rect.centerY() : intent.getIntExtra(CLICK_Y, 0),
                        0f,
                        v.getHeight());

                mAnimReveal.setDuration(400);
                mAnimReveal.setInterpolator(new LinearInterpolator());
                mAnimReveal.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        Log.d("WO", "start");
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        Log.d("WO", "end");
                        if (onGlobalLayout != null) {
                            v.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayout);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                Log.d("WO", "here");
                mAnimReveal.start();
            }
        };

        v.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayout);
    }

//    private void circularRevealReverse(Intent intent) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ||
//                (intent == null && (intent == null || !intent.hasExtra(CLICK_X) || intent.getBooleanExtra(CLICK_X, false)))) {
//            super.onBackPressed();
//            return;
//        }
//
//        Rect rect = intent.getSourceBounds();
//        View v = getWindow().getDecorView();
//
//        if (mAnimRevealR != null) {
//            mAnimRevealR.removeAllListeners();
//            mAnimRevealR.cancel();
//        }
//
//        mAnimRevealR = ViewAnimationUtils.createCircularReveal(v,
//                rect != null ? rect.centerX() : intent.getIntExtra(CLICK_X, 0),
//                rect != null ? rect.centerY() : intent.getIntExtra(CLICK_Y, 0),
//                v.getHeight(),
//                0f);
//
//        mAnimRevealR.setDuration(400);
//        mAnimRevealR.setInterpolator(new LinearInterpolator());
//        mAnimRevealR.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//                v.setVisibility(View.GONE);
//                super.onBackPressed();
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {
//
//            }
//        });
//
//        mAnimRevealR.start();
//    }
//
//    @Override
//    public void onBackPressed() {
//        circularRevealReverse(getIntent());
//    }

    // Other code omitted as it is not relevant to the conversion
}
