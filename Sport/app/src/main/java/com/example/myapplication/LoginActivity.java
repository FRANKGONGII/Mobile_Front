package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    Button btLogin;
    Button btReg;
    ImageButton wcLogin;
    ImageButton qqLogin;

    ImageButton btPsd;

    EditText etUsername;

    EditText etPsd;
    private InputMethodManager imm;

    private boolean hide_psd = true;


    public View.OnClickListener onClickListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init_listener();
        init_view();
    }

    public void init_listener(){
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.container) {
                    hideSoftKeyBoard();
                } else if (id == R.id.btLogin) {
                    hideSoftKeyBoard();
                    verify();
//                    if (isPsd) {
//                        psdLoginFragment.checkAccount(this::login);
//                    } else {
//                        fastLoginFragment.checkAccount(this::login);
//                    }
                } else if (id == R.id.btReg) {
                    hideSoftKeyBoard();
                    startActivity(new Intent(LoginActivity.this, RegistActivity.class));
                }
                else if(id == R.id.qqLogin || id == R.id.wcLogin){
                    Toast.makeText(view.getContext(), "功能开发中....", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.btPsd){
                    if(hide_psd){
                        Bitmap hide = BitmapFactory.decodeResource(view.getContext().getResources(),R.mipmap.icon_psd_s);
                        btPsd.setImageBitmap(hide);
                        etPsd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        hide_psd = false;
                    }
                    else{
                        Bitmap not_hide = BitmapFactory.decodeResource(view.getContext().getResources(),R.mipmap.icon_psd_h);
                        btPsd.setImageBitmap(not_hide);
                        etPsd.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        hide_psd = true;
                    }
                    // pass
                }
            }
        };
    }

    public void verify(){
        btLogin.setEnabled(false);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
//        showLoadingView();
//        new Handler().postDelayed(() -> {
//            dismissLoadingView();
//            btLogin.setEnabled(true);
//            if (isPsd) {
//                if (dataManager.checkAccount(account, psd))
//                    loginSuccess(account, psd);
//                else
//                    ToastUtils.showShort("账号或密码错误!");
//            } else {
//                if (dataManager.checkAccount(account))
//                    loginSuccess(account, "");
//                else
//                    ToastUtils.showShort("账号不存在!");
//            }
//        }, Conn.Delayed);
    }

    public void hideSoftKeyBoard() {
        Log.d("Login_test","here");
        View localView = getCurrentFocus();
        if (this.imm == null) {
            this.imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        }
        if ((localView != null) && (this.imm != null)) {
            this.imm.hideSoftInputFromWindow(localView.getWindowToken(), 2);
        }
    }

    public void init_view(){
        btLogin = findViewById(R.id.btLogin);
        btReg = findViewById(R.id.btReg);
        wcLogin = findViewById(R.id.wcLogin);
        qqLogin = findViewById(R.id.qqLogin);
        btPsd = findViewById(R.id.btPsd);
        etPsd = findViewById(R.id.et_psd);
        etUsername = findViewById(R.id.et_username);

        findViewById(R.id.container).setOnClickListener(onClickListener);
        btLogin.setOnClickListener(onClickListener);
        btReg.setOnClickListener(onClickListener);
        wcLogin.setOnClickListener(onClickListener);
        qqLogin.setOnClickListener(onClickListener);
        btPsd.setOnClickListener(onClickListener);
    }
}