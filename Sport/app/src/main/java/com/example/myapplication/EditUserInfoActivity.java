package com.example.myapplication;



import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.UserInfoAdapter;
import com.example.myapplication.fragment.ModalBottomSheet;
import com.example.myapplication.user.UserInfoItem;
import com.example.myapplication.utils.PhotoUtil;
import com.google.android.material.divider.MaterialDividerItemDecoration;

import java.io.File;
import java.util.ArrayList;

public class EditUserInfoActivity extends AppCompatActivity {

    private LayoutInflater inflater;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private MaterialDividerItemDecoration divider;
    private UserInfoAdapter userInfoAdapter;
    private Window window;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edituserinfo);
        inflater = LayoutInflater.from(this);

        recyclerView = findViewById(R.id.userInfoRecyclerView);
        toolbar = findViewById(R.id.toolbar);
        divider = new MaterialDividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        window = getWindow();
        pref = getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = pref.edit();

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


         String avatar = pref.getString("avatar", "");
         String nickname = pref.getString("nickname", "人机交互");
         String gender = pref.getString("gender", "");
         String birth = pref.getString("birth", "");
         String signature = pref.getString("signature", "这个人很有个性，但没有签名");

         UserInfoItem avatarItem = new UserInfoItem(UserInfoItem.UserInfoType.AVATAR, avatar);
         UserInfoItem nicknameItem = new UserInfoItem(UserInfoItem.UserInfoType.NICKNAME, nickname);
         UserInfoItem genderItem = new UserInfoItem(UserInfoItem.UserInfoType.GENDER, gender);
         UserInfoItem birthItem = new UserInfoItem(UserInfoItem.UserInfoType.BIRTH, birth);
         UserInfoItem signatureItem = new UserInfoItem(UserInfoItem.UserInfoType.SIGNATURE, signature);

         itemArrayList.add(avatarItem);
         itemArrayList.add(nicknameItem);
         itemArrayList.add(genderItem);
         itemArrayList.add(birthItem);
         itemArrayList.add(signatureItem);

         userInfoAdapter = new UserInfoAdapter(itemArrayList, this, this);

         recyclerView.setAdapter(userInfoAdapter);
    }



    public ModalBottomSheet bottomSheet;

    public UserInfoAdapter.AvatarViewHolder avatarViewHolder;
    public void setAvatarViewHolder(UserInfoAdapter.AvatarViewHolder avatarViewHolder) {
        this.avatarViewHolder = avatarViewHolder;
    }

    public void onAvatarClick() {
        avatarViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet = new ModalBottomSheet(R.layout.modal_bottom_sheet_avatar);
                bottomSheet.show(EditUserInfoActivity.this.getSupportFragmentManager(), bottomSheet.getTag());
            }
        });
    }

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri photoUri = data.getData();
                            if (photoUri != null) {
                                File file = PhotoUtil.uriToFile(photoUri, EditUserInfoActivity.this);
                                avatarViewHolder.updateData(file.getPath());
                            }
                        }
                    }

                    bottomSheet.dismiss();
                }
            }
    );


    private final ActivityResultLauncher<Intent> photoLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        Log.d("AVA", "OK");
                        Intent data = result.getData();
                        if (data != null) {
                            Uri photoUri = data.getData();
                            if (photoUri != null) {
                                File file = PhotoUtil.uriToFile(photoUri, EditUserInfoActivity.this);
                                avatarViewHolder.updateData(file.getPath());
//                                Log.d("URI", "True: " + latest_uri);
                            }
                        }
                    }
                    bottomSheet.dismiss();
                }
            }
    );



    public void onAvatarEdit(View view) {
//        TextView camera = view.findViewById(R.id.choose_camera);
//        TextView photo = view.findViewById(R.id.choose_photo);
//        TextView random = view.findViewById(R.id.choose_random);

        RadioButton camera = view.findViewById(R.id.choose_camera);
        RadioButton photo = view.findViewById(R.id.choose_photo);
        RadioButton random = view.findViewById(R.id.choose_random);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("EDIT", "onClick: camera");
//                Toast.makeText(EditUserInfoActivity.this, "choose from camera", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditUserInfoActivity.this, CameraActivity.class);
//                startActivity(intent);
                cameraLauncher.launch(intent);
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("EDIT", "onClick: photo");
//                Toast.makeText(EditUserInfoActivity.this, "choose from photo", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditUserInfoActivity.this, PhotoActivity.class);
//                startActivity(intent);
                photoLauncher.launch(intent);
            }
        });

        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("EDIT", "onClick: random");
//                Toast.makeText(EditUserInfoActivity.this, "choose random", Toast.LENGTH_SHORT).show();
                avatarViewHolder.updateData("");
                bottomSheet.dismiss();
            }
        });
    }


    public void updateLocalUserInfo(UserInfoItem.UserInfoType type, String content) {
        switch (type) {
            case AVATAR: {
                editor.putString("avatar", content);
                editor.apply();
                break;
            }
            case NICKNAME: {
                editor.putString("nickname", content);
                editor.apply();
                break;
            }
            case GENDER: {
                editor.putString("gender", content);
                editor.apply();
                break;
            }
            case BIRTH: {
                editor.putString("birth", content);
                editor.apply();
                break;
            }
            case SIGNATURE: {
                editor.putString("signature", content);
                editor.apply();
                break;
            }
        }
    }
}
