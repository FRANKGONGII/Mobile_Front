package com.example.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolylineOptions;
import com.example.myapplication.bean.Record;
import com.example.myapplication.data.DataService;
import com.example.myapplication.data.DataServiceFactory;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    private DataService dataService = null;
    private AMap aMap;
    TextView tvDistance;
    TextView tvDuration;
    TextView tvSpeed;
    TextView tvDistribution;
    TextView tvCalorie;
    TextView chat_text;


//    private LineChart chart;
//
//
//
//
//
//    private void setData(List<Record> records) {
//
//        ArrayList<Entry> values = new ArrayList<>();
//        int count = records.size();
//
//        for (int i = 0; i < count; i++) {
//
//            float val = (float) records.get(i).getDistance();
//            //这里的star可以替换为你自己项目的图标
//            values.add(new Entry(i, val));
//        }
//
//        LineDataSet set1;
//
//        if (chart.getData() != null &&
//                chart.getData().getDataSetCount() > 0) {
//            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
//            set1.setValues(values);
//            set1.notifyDataSetChanged();
//            chart.getData().notifyDataChanged();
//            chart.notifyDataSetChanged();
//        } else {
//            // create a dataset and give it a type
//            set1 = new LineDataSet(values, "DataSet 1");
//
//            set1.setDrawIcons(false);
//
//            // draw dashed line
//            set1.enableDashedLine(10f, 5f, 0f);
//
//            // black lines and points
//            set1.setColor(Color.BLACK);
//            set1.setCircleColor(Color.BLACK);
//
//            // line thickness and point size
//            set1.setLineWidth(1f);
//            set1.setCircleRadius(3f);
//
//            // draw points as solid circles
//            set1.setDrawCircleHole(false);
//
//            // customize legend entry
//            set1.setFormLineWidth(1f);
//            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
//            set1.setFormSize(15.f);
//
//            // text size of values
//            set1.setValueTextSize(9f);
//
//            // draw selection line as dashed
//            set1.enableDashedHighlightLine(10f, 5f, 0f);
//
//            // set the filled area
//            set1.setDrawFilled(true);
//            set1.setFillFormatter(new IFillFormatter() {
//                @Override
//                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
//                    return chart.getAxisLeft().getAxisMinimum();
//                }
//            });
//
//            // set color of filled area
//            if (Utils.getSDKInt() >= 18) {
//                // drawables only supported on api level 18 and above
//
//                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.baseline_male_blue_24);
//                set1.setFillDrawable(drawable);
//            } else {
//                set1.setFillColor(Color.BLACK);
//            }
//
//            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//            dataSets.add(set1); // add the data sets
//
//            // create a data object with the data sets
//            LineData data = new LineData(dataSets);
//
//            // set data
//            chart.setData(data);
//        }
//    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sportresult);
        Log.d("URL_TEST","jump");
        ToastUtils.init(this.getApplication());
        dataService = DataServiceFactory.getInstance();


        Intent intent = getIntent();

        long id = intent.getLongExtra("passId",-1);
        Log.d("URL_TEST","need id:"+id);
        String act = intent.getStringExtra("formActivity");
        //Log.d("JUMP_TEST","need id:"+id+" from: "+act);

        if(id==-1) {
            ToastUtils.show("获取运动数据错误");
        }


        //setData(dataService.getAllRecords());


        ImageView back = findViewById(R.id.re_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(act.equals("history")){
                    finish();
                }else if(act.equals("record")){
                    Intent intent1 = new Intent(ResultActivity.this,MainActivity.class);
                    startActivity(intent1);
                }
            }
        });



        Record record = dataService.getRecord(id);
        if(record == null){
            // 正常不会到这里，除非你真的是故意找茬
            Log.d("URL_TEST","need id:"+id);
            Toast.makeText(this, "Record not exists (errorNo:404)", Toast.LENGTH_SHORT).show();
            record = Record.getDefaultOne();
        }

        //Log.d("ID_TEST","after find"+record);
        List<LatLng> list = record.getLatLngList();
        //Log.d("ID_TEST", list.get(0).latitude+" "+list.get(0).longitude);
        Log.d("URL_TEST",String.valueOf(record.getId()));

        MapView mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mapView.getMap();

        tvDistance = findViewById(R.id.tvDistance);
        tvDuration = findViewById(R.id.tvDuration);
        tvSpeed = findViewById(R.id.tvSpeed);
        tvDistribution = findViewById(R.id.tvDistribution);
        tvCalorie = findViewById(R.id.tvCalorie);
        chat_text = findViewById(R.id.text_chat);

        tvDistance.setText(record.getDistanceByStr());
        tvDuration.setText(record.getDurationByStr());
        tvSpeed.setText(record.getSpeed1());
        tvDistribution.setText(record.getSpeed2());
        tvCalorie.setText(record.getCalorie());

        String type_name = record.getRecordTypeByStr();

        chat_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChatActivity.class);
                intent.putExtra("TaskType", "EvalRecord");
                String data = "我进行了一次" + type_name + "运动，总距离为" +
                        tvDistance.getText() + "公里， 用时" + tvDuration.getText() +
                        "。我的性别为男，年龄为20岁，身高、体重等身体数据假定为同年龄人群中的平均值。请你根据" +
                        "上述数据对我本次运动的表现给出意见和建议。省略计算过程，忽略其他条件的影响，如果需要其他数据，" +
                        "可以挑最重要的在最后提出，不超过2条。结尾加上'如果您还有什么其他问题，欢迎随时向我提出'";

                intent.putExtra("RecordData", data);

                view.getContext().startActivity(intent);
            }
        });



        if(list!=null){
            CameraPosition cameraPosition = new CameraPosition(list.get(0), 64, 0, 0);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            aMap.moveCamera(cameraUpdate);

            for(int i = 3;i<list.size();i++){
                LatLng l1 = list.get(i-1);
                LatLng l2 = list.get(i);
                double speed = AMapUtils.calculateLineDistance(l1,l2);
                speed = speed*3600/1000;

                PolylineOptions options = new PolylineOptions();
                options.add(l1);
                options.add(l2);

                if(speed>6) {
                    aMap.addPolyline(options.width(10).color(Color.argb(255, 204, 0, 51)));
                } else if(speed<=6&&speed>=4){
                    aMap.addPolyline(options.width(10).color(Color.argb(255, 255, 255, 0)));
                }else{
                    aMap.addPolyline(options.width(10).color(Color.argb(255, 36, 164, 255)));
                }
            }

            Marker marker1 = aMap.addMarker(new MarkerOptions().
                    position(list.get(2)).title("北京").snippet("DefaultMarker"));
            Marker marker2 = aMap.addMarker(new MarkerOptions().
                    position(list.get(list.size()-1)).title("北京").snippet("DefaultMarker"));

            showMedal();
        }


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
        SpannableString spannableString = new SpannableString("5km征服者\n已获得!");
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(30, true), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FFD700")), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

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
}
