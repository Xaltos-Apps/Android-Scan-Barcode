package com.tiromansev.scanbarcode.example;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.tiromansev.scanbarcode.mlkit.MLKitCaptureActivity;
import com.tiromansev.scanbarcode.vision.VisionCaptureActivity;
import com.tiromansev.scanbarcode.zxing.ZxingCaptureActivity;
import com.tiromansev.scanbarcode.zxing.ZxingVerticalCaptureActivity;

public class MainActivity extends AppCompatActivity {

    private static final int RC_BARCODE_CAPTURE = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_scan_vision).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VisionCaptureActivity.class);
            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        });

        findViewById(R.id.btn_scan_zxing).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ZxingCaptureActivity.class);
            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        });

        findViewById(R.id.btn_scan_zxing_vertical).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ZxingVerticalCaptureActivity.class);
            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        });

        findViewById(R.id.btn_scan_mlkit).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MLKitCaptureActivity.class);
            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        });
    }
}
