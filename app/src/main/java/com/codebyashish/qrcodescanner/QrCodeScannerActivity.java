package com.codebyashish.qrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class QrCodeScannerActivity extends AppCompatActivity {

    private Context mContext;


    private static final int REQUEST_CODE = 101;

    String valueToast = "";

    private CodeScanner mCodeScanner;
    private String qrId;
    private TextView tvRetake;
    private String primaryCodePrefix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_scanner);

        mContext = QrCodeScannerActivity.this;

        initView();
    }


    private void initView() {
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        tvRetake = findViewById(R.id.tvRetake);
        tvRetake.setOnClickListener(view -> {
            mCodeScanner.releaseResources();
            mCodeScanner.startPreview();
        });
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
                MediaPlayer mediaPlayer = MediaPlayer.create(mContext, R.raw.qrcode_scan_sound);
                mediaPlayer.start();
                saveQrResult(result.getText());
                tvRetake.setVisibility(View.VISIBLE);
                Log.d("qrdata", result.getText());
        }));
    }

    private void saveQrResult(String text) {
        startActivity(new Intent(QrCodeScannerActivity.this, MainActivity.class).putExtra("text", text));
    }
    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}