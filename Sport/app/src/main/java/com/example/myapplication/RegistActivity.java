package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class RegistActivity extends AppCompatActivity {

    EditText etAccount;
    EditText etCode;
    Chronometer chronometer;
    EditText etPsd;
    EditText etCheckPsd;
    Button btRegist;


    private String code = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        init_view();
    }

    public void init_view(){
        etAccount = findViewById(R.id.et_account);
        etCode = findViewById(R.id.et_code);
        chronometer = findViewById(R.id.chronometer);
        etPsd = findViewById(R.id.et_psd);
        etCheckPsd = findViewById(R.id.et_checkPsd);
        btRegist = findViewById(R.id.bt_regist);
    }
}
