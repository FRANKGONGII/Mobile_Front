package com.example.myapplication;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterViewAnimator;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class EditUserInfoActivity extends AppCompatActivity {

    private LayoutInflater inflater;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private MaterialDividerItemDecoration divider;
    private UserInfoAdapter userInfoAdapter;
    private Window window;
    private View bindView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edituserinfo);
        inflater = LayoutInflater.from(this);

        recyclerView = findViewById(R.id.userInfoRecyclerView);
        toolbar = findViewById(R.id.toolbar);
        divider = new MaterialDividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        window = getWindow();

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





        // FAB
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


    }

    @Override
    protected void onResume() {
        super.onResume();
//        View view = findViewById(R.layout.activity_edituserinfo);
//        onUserInfoEdit();
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

         userInfoAdapter = new UserInfoAdapter(itemArrayList, this, this);

         recyclerView.setAdapter(userInfoAdapter);

    }

    public ModalBottomSheet bottomSheet;
    public UserInfoAdapter.AvatarViewHolder avatarViewHolder;
    public void onUserInfoEdit() {
//         avatarViewHolder = userInfoAdapter.avatarViewHolder;
//        if (avatarViewHolder == null)
//            Log.d("AVA", "NO ");
//        else
//            Log.d("AVA", "YES ");

        avatarViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet = new ModalBottomSheet(R.layout.modal_bottom_sheet_avatar);
                bottomSheet.show(EditUserInfoActivity.this.getSupportFragmentManager(), bottomSheet.getTag());

            }
        });
    }

    public Uri latest_uri;
    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Log.d("AVA", "OK");
                        Intent data = result.getData();
                        if (data != null) {
                            Uri photoUri = data.getData();
//                            Log.d("CAMERA", "get photo uri");
                            if (photoUri != null) {
                                latest_uri = photoUri;
//                                Log.d("CAMERA", String.valueOf(photoUri));
                                avatarViewHolder.updateData(latest_uri);
                            }
                        }
                    } else {
                        Log.d("AVA", "CANCEL");
                    }

                    bottomSheet.dismiss();

                }
            }
    );

    public void onAvatarEdit(View view) {
        TextView camera = view.findViewById(R.id.choose_camera);
        TextView photo = view.findViewById(R.id.choose_photo);
        TextView random = view.findViewById(R.id.choose_random);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("EDIT", "onClick: camera");
//                Toast.makeText(EditUserInfoActivity.this, "choose from camera", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditUserInfoActivity.this, CameraActivity.class);
//                startActivity(intent);
                latest_uri = null;
                cameraLauncher.launch(intent);
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("EDIT", "onClick: photo");
                Toast.makeText(EditUserInfoActivity.this, "choose from photo", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(mActivity, PhotoActivity.class);
//                startActivity(intent);
            }
        });

        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("EDIT", "onClick: random");
                Toast.makeText(EditUserInfoActivity.this, "choose random", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setAvatarViewHolder(UserInfoAdapter.AvatarViewHolder avatarViewHolder) {
        this.avatarViewHolder = avatarViewHolder;
    }

    public void onUserInfoUpdate() {

    }
}
