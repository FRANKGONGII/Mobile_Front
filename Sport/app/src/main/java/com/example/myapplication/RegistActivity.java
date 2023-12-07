package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.utils.Utils;

public class RegistActivity extends AppCompatActivity {

    EditText etAccount;
    EditText etCode;
    Chronometer chronometer;
    EditText etPsd;
    EditText etCheckPsd;
    Button btRegist;

    View rlBadk;

    private InputMethodManager imm;

    public View.OnClickListener onClickListener;
    private String code = "123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        init_listener();
        init_view();
    }

    public void init_view(){
        etAccount = findViewById(R.id.et_account);
        etCode = findViewById(R.id.et_code);
        chronometer = findViewById(R.id.chronometer);
        etPsd = findViewById(R.id.et_psd);
        etCheckPsd = findViewById(R.id.et_checkPsd);
        btRegist = findViewById(R.id.bt_regist);
        rlBadk  = findViewById(R.id.rlBadk);

        findViewById(R.id.container).setOnClickListener(onClickListener);
        rlBadk.setOnClickListener(onClickListener);
        chronometer.setOnClickListener(onClickListener);
        btRegist.setOnClickListener(onClickListener);
    }

    public void init_listener(){
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                if(id == R.id.container){
                    Log.d("login_test","123");
                    hideSoftKeyBoard();
                }
                else if(id == R.id.rlBadk){
                    finish();
                }
                else if(id == R.id.chronometer){
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
                }
                else if(id == R.id.bt_regist){
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
                }
                else{

                }
            }
        };
    }

    private void regist(){
        // TODO
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

    public void showInfo(String text){
        Toast.makeText(RegistActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    public void yanZhengMa() {
//        showLoadingView();
        new Handler().postDelayed(() -> {
//            dismissLoadingView();
            int numcode = (int) ((Math.random() * 9 + 1) * 100000);
            code = numcode + "";
            yzmStart();
            showInfo("验证获取成功！");
            etCode.setText(code);
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
