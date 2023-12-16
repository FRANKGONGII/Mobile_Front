package com.example.myapplication;



import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class PhotoActivity extends AppCompatActivity {


    private ActivityResultLauncher<String> selectPhoto = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if (uri != null) {
                        Log.d("PHOTO", "" + uri);
                        Intent resultIntent = new Intent();
                        resultIntent.setData(uri);
                        setResult(Activity.RESULT_OK, resultIntent);
                    } else
                        Log.d("PHOTO", "NO PHOTO");

                    finish();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("PHOTO", "onCreate: ");
        selectPhoto.launch("image/*");
    }
}
