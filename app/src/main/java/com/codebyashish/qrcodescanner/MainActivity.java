package com.codebyashish.qrcodescanner;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.codebyashish.qrcodescanner.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1000;
    ActivityMainBinding binding;
    MainActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mContext = MainActivity.this;


        binding.btnScan.setOnClickListener(click -> {
            checkRuntimePermission();
        });

        binding.cardView.setVisibility(View.INVISIBLE);
        binding.fabCopy.setVisibility(View.INVISIBLE);
        binding.tvQrTexts.setText("");

        Intent intent= getIntent();
        if (intent != null){
            String text = intent.getStringExtra("text");
            if (text != null){
                binding.cardView.setVisibility(View.VISIBLE);
                binding.fabCopy.setVisibility(View.VISIBLE);
                binding.tvQrTexts.setText(text);

                binding.fabCopy.setOnClickListener(view -> {
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData data = ClipData.newPlainText("text",text);
                    cm.setPrimaryClip(data);
                    Toast.makeText(mContext, "copied", Toast.LENGTH_SHORT).show();
                });
            }
        }



    }

    private void checkRuntimePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if
            (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(mContext, CAMERA) == PackageManager.PERMISSION_GRANTED

            ) {
                startActivity(new Intent(MainActivity.this, QrCodeScannerActivity.class));

            } else {
                requestAllPermission();
            }
        }
        else {
            if
            (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            ) {
                startActivity(new Intent(MainActivity.this, QrCodeScannerActivity.class));

            } else {
                requestAllPermission();
            }
        }
    }

    private void requestAllPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(mContext,
                    new String[]{READ_MEDIA_IMAGES, CAMERA}, PERMISSION_REQUEST_CODE);
        }
        else {
            ActivityCompat.requestPermissions(mContext,
                    new String[]{READ_EXTERNAL_STORAGE, CAMERA, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(mContext, "Permission Granted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, QrCodeScannerActivity.class));

            } else {
                Toast.makeText(mContext, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
            finishAffinity();
    }
}