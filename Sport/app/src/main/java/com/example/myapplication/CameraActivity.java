package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.BuildConfig;

import java.io.File;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity {

//    private LayoutPhotoActivityBinding binding;
    private Uri photoUri;

    private final ActivityResultLauncher<Uri> takePicture = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean success) {
                    if (success) {
                        if (photoUri != null) {
//                            binding.ivPhoto.setImageURI(photoUri);
                            Log.d("CAMERA", "SUCCESS");
                            Intent resultIntent = new Intent();
                            resultIntent.setData(photoUri);
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = DataBindingUtil.setContentView(this, R.layout.layout_photo_activity);

        photoUri = getPhotoFileUri();
        takePicture.launch(photoUri);
//        binding.btnTakePhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    private Uri getPhotoFileUri() {
        File storageFile;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageFile = getExternalCacheDir();
        } else {
            storageFile = getCacheDir();
        }

        File photoFile;
        try {
            photoFile = File.createTempFile("tmp_image_file", ".png", storageFile);
            photoFile.createNewFile();
            photoFile.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileProvider", photoFile);
    }


}
