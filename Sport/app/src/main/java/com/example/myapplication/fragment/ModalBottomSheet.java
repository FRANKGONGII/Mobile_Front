package com.example.myapplication.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.CameraActivity;
import com.example.myapplication.EditUserInfoActivity;
import com.example.myapplication.PhotoActivity;
import com.example.myapplication.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ModalBottomSheet extends BottomSheetDialogFragment {

    public static final String TAG = "ModalBottomSheet";
    private Activity mActivity;
    private int layoutResId;
    private View view;

    public ModalBottomSheet(int layoutResId) {
//        mActivity = getActivity();
        this.layoutResId = layoutResId;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(layoutResId, container, false);
//        if (view == null)
//            Log.d("SHEET", "This could not be!");
//        else
//            Log.d("SHEET", "This could be");


        return view;

    }

    private Uri latest_uri;

    public Uri getUri() {
        return latest_uri;
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
//                            Log.d("CAMERA", "get photo uri");
                            if (photoUri != null) {
                                latest_uri = photoUri;
//                                Log.d("CAMERA", String.valueOf(photoUri));

                            }
                        }
                    }
                }
            }
    );

    // 实在不想把逻辑写在这里...
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView camera = view.findViewById(R.id.choose_camera);
        TextView photo = view.findViewById(R.id.choose_photo);
        TextView random = view.findViewById(R.id.choose_random);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("EDIT", "onClick: camera");
//                Toast.makeText(getActivity(), "choose from camera", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(mActivity, CameraActivity.class);
//                startActivity(intent);
//                cameraLauncher.launch(intent);
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("EDIT", "onClick: photo");
//                Toast.makeText(getActivity(), "choose from photo", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(mActivity, PhotoActivity.class);
//                startActivity(intent);
            }
        });

        random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("EDIT", "onClick: random");
//                Toast.makeText(getActivity(), "choose random", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Nullable
    @Override
    public View getView() {
        Log.d("SHEET", "jere");
        return view;
    }

    public View findViewById(int viewId) {
        return view.findViewById(viewId);
    }

}
