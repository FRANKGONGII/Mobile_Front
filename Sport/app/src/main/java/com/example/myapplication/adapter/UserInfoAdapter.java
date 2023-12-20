package com.example.myapplication.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.EditUserInfoActivity;
import com.example.myapplication.R;
import com.example.myapplication.user.UserInfoItem;
import com.example.myapplication.utils.PhotoUtil;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Activity activity;
    private Context mContext;
    private LayoutInflater inflater;
    private List<UserInfoItem> itemList;


    public UserInfoAdapter(List<UserInfoItem> itemList, Activity activity, Context context) {
        this.itemList = itemList;
        this.activity = activity;
        mContext = context;
    }

    public UserInfoItem getUserInfoItem() {
        return null;
    }

    public void updateUserInfoItem() {

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        View view = null;

        switch (UserInfoItem.UserInfoType.values()[viewType]) {
            case AVATAR:
                view = inflater.inflate(R.layout.item_userinfo_image, parent, false);
                AvatarViewHolder avatarViewHolder = new AvatarViewHolder(view);
                ((EditUserInfoActivity) activity).setAvatarViewHolder(avatarViewHolder);
                ((EditUserInfoActivity) activity).onAvatarClick();
                return avatarViewHolder;
            case NICKNAME:
                view = inflater.inflate(R.layout.item_userinfo_text, parent, false);
                NicknameViewHolder nicknameViewHolder = new NicknameViewHolder(view);
//                ((EditUserInfoActivity) activity).setNicknameViewHolder(nicknameViewHolder);
//                ((EditUserInfoActivity) activity).onNicknameEdit();
                return nicknameViewHolder;
            case GENDER:
                view = inflater.inflate(R.layout.item_userinfo_text, parent, false);
                return new GenderViewHolder(view);
            case BIRTH:
                view = inflater.inflate(R.layout.item_userinfo_text, parent, false);
                return new BirthViewHolder(view);
            case SIGNATURE:
                view = inflater.inflate(R.layout.item_userinfo_text, parent, false);
                return new SignatureViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UserInfoItem item = itemList.get(position);

        if (item != null) {
            switch (item.getType()) {
                case AVATAR:
                    ((AvatarViewHolder) holder).bindData(item);
                    break;
                case NICKNAME:
                    ((NicknameViewHolder) holder).bindData(item);
                    break;
                case GENDER:
                    ((GenderViewHolder) holder).bindData(item);
                    break;
                case BIRTH:
                    ((BirthViewHolder) holder).bindData(item);
                    break;
                case SIGNATURE:
                    ((SignatureViewHolder) holder).bindData(item);
                    break;
            }
        }
    }



    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getType().ordinal();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class AvatarViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private CircleImageView avatar;
        private UserInfoItem item;

        public AvatarViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.userInfoType);
            avatar = itemView.findViewById(R.id.userInfoImg);

            // 设置点击事件
//            itemView.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    // 弹出底部弹窗
//                    bottomSheet = new ModalBottomSheet(R.layout.modal_bottom_sheet_avatar);
//                    bottomSheet.show(((AppCompatActivity) mContext).getSupportFragmentManager(), bottomSheet.getTag());
//                }
//            });

        }


        public void bindData(UserInfoItem item) {
            // 设置数据
            title.setText(item.getType().getStr());
            // 读取base64Str转为bitmap设置头像
            if (!item.getContent().equals("")) {
                avatar.setImageBitmap(PhotoUtil.base64Str2Bitmap(item.getContent()));
            }

            this.item = item;
        }

        public void updateData(String filePath) {
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            avatar.setImageBitmap(bitmap);

            String base64Str = PhotoUtil.bitmap2Base64Str(bitmap);
            item.setContent(base64Str);
            ((EditUserInfoActivity) activity).updateLocalUserInfo(UserInfoItem.UserInfoType.AVATAR, base64Str);
        }

    }

    public class NicknameViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView nickname;
        private UserInfoItem item;

        public NicknameViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.userInfoType);
            nickname = itemView.findViewById(R.id.userInfoText);

            // 设置点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
                    // 弹出编辑框

                    // 创建builder
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(mContext);

                    // 设置builder属性
                    builder.setTitle("修改昵称");

                    // 设置布局
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    layoutParams.setMarginStart((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, mContext.getResources().getDisplayMetrics()));
                    layoutParams.setMarginEnd((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, mContext.getResources().getDisplayMetrics()));


                    // 创建输入框容器
                    TextInputLayout inputLayout = new TextInputLayout(mContext);;

                    // label
                    inputLayout.setHint("请输入新昵称");
                    inputLayout.setHintTextColor(ColorStateList.valueOf(R.color.blue_5));
                    // activation indicator
                    inputLayout.setBoxStrokeColor(R.color.blue_5);
                    inputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_NONE);
                    // helperText
                    inputLayout.setHelperText("    每月只能修改一次昵称哦");
                    // EndIcon
                    inputLayout.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
                    // 布局
                    inputLayout.setLayoutParams(layoutParams);


                    // 创建输入框
                    TextInputEditText editText = new TextInputEditText(mContext);

                    // 输入文本大小
                    editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                    editText.setText(item.getContent());
                    // 布局
                    editText.setLayoutParams(layoutParams);

                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });


                    builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateData(editText.getText().toString());
                        }
                    });


//                    AlertDialog dialog = builder.create();
//                    dialog.show();
//                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
//                        @Override
//                        public void onShow(DialogInterface dialogInterface) {
//                            // 设置按钮颜色
//                            Button negBtn = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
//                            Button posBtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
//                            negBtn.setTextColor(R.color.blue_5);
//                            posBtn.setTextColor(R.color.blue_5);
//                        }
//                    });




                    inputLayout.addView(editText);
                    builder.setView(inputLayout);
                    builder.show();
                }
            });
        }

        public void bindData(UserInfoItem item) {
            // 设置数据
            title.setText(item.getType().getStr());
            nickname.setText(item.getContent());
            this.item = item;
        }

        public void updateData(String content) {
            nickname.setText(content);
            // 改变UserInfoItem
            item.setContent(content);
            // 更新本地存储数据
            ((EditUserInfoActivity) activity).updateLocalUserInfo(UserInfoItem.UserInfoType.NICKNAME, content);
        }
    }

    public class GenderViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView gender;
        private UserInfoItem item;

        public GenderViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.userInfoType);
            gender = itemView.findViewById(R.id.userInfoText);
            // 设置点击事件
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 创建builder
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(mContext);

                    // 设置builder属性
                    builder.setTitle("修改性别");

                    View view = inflater.inflate(R.layout.dialog_choose_gender, null);

                    ImageButton maleButton = view.findViewById(R.id.male);
                    ImageButton unknownButton = view.findViewById(R.id.unknown);
                    ImageButton femaleButton = view.findViewById(R.id.female);

                    if (gender.getText().equals("男")) {
                        maleButton.setBackgroundResource(R.color.blue_sky);
                        maleButton.setImageResource(R.drawable.baseline_male_white_24);
                    } else if (gender.getText().equals("保密")) {
                        unknownButton.setBackgroundResource(R.color.gray0);
                        unknownButton.setImageResource(R.drawable.baseline_question_mark_white_24);
                    } else if (gender.getText().equals("女")) {
                        femaleButton.setBackgroundResource(R.color.pink_sakura);
                        femaleButton.setImageResource(R.drawable.baseline_female_white_24);
                    }

                    final String[] chosenGender = {(String) gender.getText()};
                    maleButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 设置男性按钮选中状态的背景色和图片颜色
                            maleButton.setBackgroundResource(R.color.blue_sky);
                            maleButton.setImageResource(R.drawable.baseline_male_white_24);

                            // 设置其他按钮非选中状态的背景色和图片颜色
                            unknownButton.setBackgroundResource(R.color.white);
                            unknownButton.setImageResource(R.drawable.baseline_question_mark_grey_24);

                            femaleButton.setBackgroundResource(R.color.white);
                            femaleButton.setImageResource(R.drawable.baseline_female_pink_24);

                            chosenGender[0] = "男";
                        }
                    });

                    unknownButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            maleButton.setBackgroundResource(R.color.white);
                            maleButton.setImageResource(R.drawable.baseline_male_blue_24);

                            // 设置其他按钮非选中状态的背景色和图片颜色
                            unknownButton.setBackgroundResource(R.color.gray0);
                            unknownButton.setImageResource(R.drawable.baseline_question_mark_white_24);

                            femaleButton.setBackgroundResource(R.color.white);
                            femaleButton.setImageResource(R.drawable.baseline_female_pink_24);

                            chosenGender[0] = "保密";
                        }
                    });

                    femaleButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            maleButton.setBackgroundResource(R.color.white);
                            maleButton.setImageResource(R.drawable.baseline_male_blue_24);

                            // 设置其他按钮非选中状态的背景色和图片颜色
                            unknownButton.setBackgroundResource(R.color.white);
                            unknownButton.setImageResource(R.drawable.baseline_question_mark_grey_24);

                            femaleButton.setBackgroundResource(R.color.pink_sakura);
                            femaleButton.setImageResource(R.drawable.baseline_female_white_24);

                            chosenGender[0] = "女";
                        }
                    });


                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateData(chosenGender[0]);
                        }
                    });


                    builder.setView(view);
                    builder.show();
                }
            });
        }

        public void bindData(UserInfoItem item) {
            // 设置数据
            title.setText(item.getType().getStr());
            gender.setText(item.getContent());
            this.item = item;
        }

        public void updateData(String content) {
            gender.setText(content);
            item.setContent(content);
            ((EditUserInfoActivity) activity).updateLocalUserInfo(UserInfoItem.UserInfoType.GENDER, content);
        }
    }

    public class BirthViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView birth;
        private UserInfoItem item;
        public BirthViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.userInfoType);
            birth = itemView.findViewById(R.id.userInfoText);

            // 设置点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 创建DatePicker Builder
                    MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();

                    // 设置标题(Opt)
                    builder.setTitleText("选择生日");

                    // 设置主题
                    builder.setTheme(R.style.ThemeOverlay_App_DatePicker);

                    // 设置默认选中日期
                    String dateString = item.getContent();

                    if (!dateString.equals("")) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, Integer.parseInt(dateString.substring(0, 4)));
                        calendar.set(Calendar.MONTH, Integer.parseInt(dateString.substring(5, 7))-1);
                        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateString.substring(8, 10)));
                        long timeStamp = calendar.getTimeInMillis();
                        builder.setSelection(timeStamp);
                    }



                    // 设置输入模式
//                    builder.setInputMode(MaterialDatePicker.INPUT_MODE_TEXT);


                    // 创建DatePicker
                    MaterialDatePicker<Long> datePicker = builder.build();

                    datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                        @Override
                        public void onPositiveButtonClick(Long selection) {
                            // 处理选中的日期
                            Date date = new Date(selection);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String dateString = sdf.format(date);
//                            Toast.makeText(activity, dateString, Toast.LENGTH_SHORT).show();
                            updateData(dateString);
                        }
                    });

                    //显示DatePicker
                    datePicker.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "DATE_PICKER");
                }
            });
        }

        public void bindData(UserInfoItem item) {
            // 设置数据
            title.setText(item.getType().getStr());
            birth.setText(item.getContent());
            this.item = item;
        }

        public void updateData(String content) {
            birth.setText(content);
            //改变UserInfoItem
            item.setContent(content);
            ((EditUserInfoActivity) activity).updateLocalUserInfo(UserInfoItem.UserInfoType.BIRTH, content);
        }
    }

    public class SignatureViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView signature;
        private UserInfoItem item;

        public SignatureViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.userInfoType);
            signature = itemView.findViewById(R.id.userInfoText);

            // 设置点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 弹出编辑框

                    // 创建builder
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(mContext);

                    // 设置builder属性
                    builder.setTitle("修改个性签名");

                    // 设置布局
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    layoutParams.setMarginStart((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, mContext.getResources().getDisplayMetrics()));
                    layoutParams.setMarginEnd((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, mContext.getResources().getDisplayMetrics()));


                    // 创建输入框容器
                    TextInputLayout inputLayout = new TextInputLayout(mContext);;

                    // label
                    inputLayout.setHint("请输入新签名");
//                    inputLayout.setHintTextColor(ColorStateList.valueOf(R.color.blue_5));
                    // activation indicator
//                    inputLayout.setBoxStrokeColor(R.color.blue_5);
                    inputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_NONE);
                    // helperText
//                    inputLayout.setHelperText("修改签名需要花费200$");
                    // counter
                    inputLayout.setCounterEnabled(true);
                    inputLayout.setCounterMaxLength(70);
                    // EndIcon
                    inputLayout.setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT);
                    // 布局
                    inputLayout.setLayoutParams(layoutParams);


                    // 创建输入框
                    TextInputEditText editText = new TextInputEditText(mContext);

                    // 输入文本大小
                    editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//                    editText.addTextChangedListener(new TextWatcher() {
//                        @Override
//                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//                        @Override
//                        public void onTextChanged(CharSequence s, int start, int before, int count) {
//                            if (s.length() > 0) {
//                                inputLayout.setHintEnabled(true);
//                            } else {
//                                inputLayout.setHintEnabled(true);
//                            }
//                        }
//                        @Override
//                        public void afterTextChanged(Editable s) {}
//                    });

                    // 默认显示个签
                    editText.setText(item.getContent());

                    // 布局
                    editText.setLayoutParams(layoutParams);

                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });


                    builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 这里要加字数超限拒绝保存
                            updateData(editText.getText().toString());
                        }
                    });


                    AlertDialog dialog = builder.create();
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//                    dialog.show();

                    dialog.setView(inputLayout);
                    // 设置按钮颜色
//                    Button negBtn = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
//                    Button posBtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
//
//                    negBtn.setTextColor(R.color.blue_5);
//                    posBtn.setTextColor(R.color.blue_5);

                    //




                    inputLayout.addView(editText);
                    builder.setView(inputLayout);

                    dialog.show();
//                    builder.show();
                }
            });
        }

        public void bindData(UserInfoItem item) {
            // 设置数据
            title.setText(item.getType().getStr());
            signature.setText(item.getContent());
            this.item = item;
        }

        public void updateData(String content) {
            signature.setText(content);
            item.setContent(content);
            ((EditUserInfoActivity) activity).updateLocalUserInfo(UserInfoItem.UserInfoType.SIGNATURE, content);
        }
    }
}
