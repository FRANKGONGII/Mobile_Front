package com.example.myapplication.ui.customView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

public class LightImageView extends androidx.appcompat.widget.AppCompatImageView {

    private Paint mPaint;
    private Path mPath;
    private LinearGradient mLinearGradient;
    private ValueAnimator mValueAnimator;

    public LightImageView(Context context) {
        super(context);
    }

    public LightImageView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public LightImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPath = new Path();
    }

    private void initPointAndAnimator(int w, int h) {
        Point point1 = new Point(0, 0);
        Point point2 = new Point(w, 0);
        Point point3 = new Point(w, h);
        Point point4 = new Point(0, h);

        mPath.moveTo(point1.x, point1.y);
        mPath.lineTo(point2.x, point2.y);
        mPath.lineTo(point3.x, point3.y);
        mPath.lineTo(point4.x, point4.y);
        mPath.close();

        float k = 1f * h / w;
        float offset = 1f * w / 2;

        mValueAnimator = ValueAnimator.ofFloat(0f - offset * 2, w + offset * 2);
        mValueAnimator.setRepeatCount(-1);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setDuration(1500);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mLinearGradient = new LinearGradient(value, k*value, value+offset, k*(value+offset),
                        new int[]{Color.parseColor("00FFFFFF"), Color.parseColor("1AFFFFFF"), Color.parseColor("00FFFFFF")},
                        null, Shader.TileMode.CLAMP);
                invalidate();
            }
        });
        mValueAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        initPointAndAnimator(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mValueAnimator.cancel();
    }
}
