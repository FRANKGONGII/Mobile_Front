package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    ViewPager vp;
    Button btLogin;
    Button btReg;
    ImageButton wcLogin;
    ImageButton qqLogin;

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
//                    hideSoftKeyBoard();
                } else if (id == R.id.btLogin) {
//                    hideSoftKeyBoard();
//                    if (isPsd) {
//                        psdLoginFragment.checkAccount(this::login);
//                    } else {
//                        fastLoginFragment.checkAccount(this::login);
//                    }
                } else if (id == R.id.btReg) {
//                    hideSoftKeyBoard();
                    startActivity(new Intent(LoginActivity.this, RegistActivity.class));
                }
                else if(id == R.id.qqLogin || id == R.id.wcLogin){
                    Toast.makeText(view.getContext(), "功能开发中....", Toast.LENGTH_SHORT).show();
                }
                else{

                }
            }
        };
    }

    public void init_view(){
        vp = findViewById(R.id.vp);
        btLogin = findViewById(R.id.btLogin);
        btReg = findViewById(R.id.btReg);
        wcLogin = findViewById(R.id.wcLogin);
        qqLogin = findViewById(R.id.qqLogin);

        btLogin.setOnClickListener(onClickListener);
        btReg.setOnClickListener(onClickListener);
        wcLogin.setOnClickListener(onClickListener);
        qqLogin.setOnClickListener(onClickListener);
    }
}