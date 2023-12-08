package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplication.data.UserService;
import com.example.myapplication.data.UserServiceFactory;
import com.example.myapplication.utils.Utils;

public class RegistActivity extends AppCompatActivity {

    EditText etAccount;
    EditText etCode;

    EditText etUsername;
    Chronometer chronometer;
    EditText etPsd;
    EditText etCheckPsd;
    Button btRegist;

    View rlBadk;

    private InputMethodManager imm;

    public View.OnClickListener onClickListener;
    private String code = "123";

    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        userService = UserServiceFactory.getInstance();
        init_listener();
        init_view();
        other_init();
    }

    public void other_init(){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel_regist";
            String description = "channel_regist";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("channel_regist", name, importance);
            channel.setDescription(description);
            // 确保渠道被注册
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void init_view() {
        etAccount = findViewById(R.id.et_account);
        etCode = findViewById(R.id.et_code);
        chronometer = findViewById(R.id.chronometer);
        etPsd = findViewById(R.id.et_psd);
        etCheckPsd = findViewById(R.id.et_checkPsd);
        btRegist = findViewById(R.id.bt_regist);
        rlBadk = findViewById(R.id.rlBadk);
        etUsername = findViewById(R.id.et_username);

        findViewById(R.id.container).setOnClickListener(onClickListener);
        rlBadk.setOnClickListener(onClickListener);
        chronometer.setOnClickListener(onClickListener);
        btRegist.setOnClickListener(onClickListener);
    }

    public void init_listener() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.container) {
                    Log.d("login_test", "123");
                    hideSoftKeyBoard();
                } else if (id == R.id.rlBadk) {
                    finish();
                } else if (id == R.id.chronometer) {
                    String phone = etAccount.getText().toString();
                    if (TextUtils.isEmpty(phone)) {
                        showInfo("请输入11位手机号码");
                        return;
                    }
                    if (!Utils.isMobile(phone)) {
                        showInfo("请输入正确的手机号码");
                        return;
                    }

                    // 先隐藏输入法
                    hideSoftKeyBoard();

                    yanZhengMa();
                } else if (id == R.id.bt_regist) {
                    hideSoftKeyBoard();
                    if (TextUtils.isEmpty(etAccount.getText())) {
                        showInfo("请输入11位手机号码!");
                    } else if (!Utils.isMobile(etAccount.getText().toString())) {
                        showInfo("请输入正确的手机号码!");
                    } else if (TextUtils.isEmpty(etCode.getText().toString())) {
                        showInfo("验证码不可以为空!");
                    } else if (!TextUtils.equals(etCode.getText(), code)) {
                        showInfo("请输入正确的验证码!");
                    } else if (TextUtils.isEmpty(etPsd.getText().toString())) {
                        showInfo("密码不可以为空!");
                    } else if (etPsd.getText().length() < 6) {
                        showInfo("请输入大于六位数的密码!");
                    } else if (TextUtils.isEmpty(etCheckPsd.getText().toString())) {
                        showInfo("校验密码不可以为空!");
                    } else if (!TextUtils.equals(etPsd.getText(), etCheckPsd.getText())) {
                        showInfo("两次密码输入不一致，请检验!");
                    } else {
                        btRegist.setEnabled(false);
                        regist();
                    }
                } else {

                }
            }
        };
    }

    private void regist() {
        // TODO
        String userName = String.valueOf(etUsername.getText());
        String phone = String.valueOf(etAccount.getText());
        String pwd = String.valueOf(etPsd.getText());
        userService.Register(userName,phone,pwd);
        showInfo("注册成功");
        finish();
    }

    public void hideSoftKeyBoard() {
        View localView = getCurrentFocus();
        if (this.imm == null) {
            this.imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        }
        if ((localView != null) && (this.imm != null)) {
            this.imm.hideSoftInputFromWindow(localView.getWindowToken(), 2);
        }
    }

    public void showInfo(String text) {
        Toast.makeText(RegistActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    public void showNotification(String title,String text) {
        // 发送消息之前要先创建通知渠道，创建代码见MainApplication.java
        // 创建一个跳转到活动页面的意图
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.POST_NOTIFICATIONS)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.POST_NOTIFICATIONS},1);
        }
        Intent clickIntent = new Intent(this, MainActivity.class);
        // 创建一个用于页面跳转的延迟意图
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                R.string.app_name, clickIntent, PendingIntent.FLAG_IMMUTABLE);
        // 创建一个通知消息的建造器
        Notification.Builder builder = new Notification.Builder(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android 8.0开始必须给每个通知分配对应的渠道
            builder = new Notification.Builder(this, "channel_regist");
        }
        builder.setContentIntent(contentIntent) // 设置内容的点击意图
                .setAutoCancel(true) // 点击通知栏后是否自动清除该通知
                .setSmallIcon(R.mipmap.icon_code) // 设置应用名称左边的小图标
                .setPriority(Notification.PRIORITY_MAX)
                // 设置通知栏右边的大图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.smile_icon))
                .setContentTitle(title) // 设置通知栏里面的标题文本
                .setContentText(text); // 设置通知栏里面的内容文本
        Notification notify = builder.build(); // 根据通知建造器构建一个通知对象
        // 从系统服务中获取通知管理器
        NotificationManager notifyMgr = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        // 使用通知管理器推送通知，然后在手机的通知栏就会看到该消息
        notifyMgr.notify(R.string.app_name, notify);

    }

    public void yanZhengMa() {
//        showLoadingView();
        new Handler().postDelayed(() -> {
//            dismissLoadingView();
            int numcode = (int) ((Math.random() * 9 + 1) * 1000);
            code = numcode + "";
            yzmStart();
            showNotification("验证码","您的验证码为："+ code);
        }, 1500);
    }

    public void yzmStart() {
        chronometer.setTag(SystemClock.elapsedRealtime() / 1000 + 60);
        chronometer.setText("(60)重新获取");
        chronometer.setEnabled(false);

        Handler handler = new Handler(Looper.getMainLooper());
        Runnable updateUI = new Runnable() {
            int count_down = 60;
            @Override
            public void run() {
                // 更新 UI 的代码
                chronometer.setText(String.format("重新获取(%ds)",count_down));
                // 再次post，循环执行
                count_down --;
                if(count_down != 0){
                    handler.postDelayed(this, 1000); // 每秒更新一次
                }
                else{
                    chronometer.setText("重新获取");
                    chronometer.setEnabled(true);
                }
            }
        };

        handler.post(updateUI);

        this.getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

}
