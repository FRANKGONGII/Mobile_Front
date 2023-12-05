package com.example.myapplication;



import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.UserInfoAdapter;
import com.example.myapplication.fragment.ModalBottomSheet;
import com.example.myapplication.user.UserInfoItem;
import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.divider.MaterialDividerItemDecoration;

import java.util.ArrayList;

public class EditUserInfoActivity extends AppCompatActivity {

    private LayoutInflater inflater;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private MaterialDividerItemDecoration divider;
    private UserInfoAdapter userInfoAdapter;
    private Window window;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edituserinfo);
        inflater = LayoutInflater.from(this);

        recyclerView = findViewById(R.id.userInfoRecyclerView);
        toolbar = findViewById(R.id.toolbar);
        divider = new MaterialDividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        window = getWindow();

//        if (avatarBottomSheetView == null) {
//            Log.i("Sheet", "Not Found");
//        } else {
//            Log.i("Sheet", "Found");
//        }

//        ModalBottomSheet bottomSheet = new ModalBottomSheet();
//        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());



        int toolbarColor = ((ColorDrawable)toolbar.getBackground()).getColor();
        window.setStatusBarColor(toolbarColor);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 设置分割线
        divider.setDividerInsetStart(32);
        divider.setLastItemDecorated(false);
        recyclerView.addItemDecoration(divider);

        // 设置不可滑动
        layoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(layoutManager);

        onUserInfoInit();
    }

    public void onUserInfoInit() {
         ArrayList<UserInfoItem> itemArrayList = new ArrayList<>();

         UserInfoItem avatar = new UserInfoItem(UserInfoItem.UserInfoType.AVATAR, "");
         UserInfoItem nickname = new UserInfoItem(UserInfoItem.UserInfoType.NICKNAME, "朝阳冬泳怪鸽");
         UserInfoItem gender = new UserInfoItem(UserInfoItem.UserInfoType.GENDER, "男");
         UserInfoItem birth = new UserInfoItem(UserInfoItem.UserInfoType.BIRTH, "1990-10-1");
         UserInfoItem signature = new UserInfoItem(UserInfoItem.UserInfoType.SIGNATURE, "加油---奥里给!");

         itemArrayList.add(avatar);
         itemArrayList.add(nickname);
         itemArrayList.add(gender);
         itemArrayList.add(birth);
         itemArrayList.add(signature);

         userInfoAdapter = new UserInfoAdapter(itemArrayList, this, EditUserInfoActivity.this);

         recyclerView.setAdapter(userInfoAdapter);

    }
}
