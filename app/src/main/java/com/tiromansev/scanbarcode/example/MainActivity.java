package com.tiromansev.scanbarcode.example;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.tiromansev.scanbarcode.external.ExternalCaptureActivity;
import com.tiromansev.scanbarcode.mlkit.MLKitCaptureActivity;

public class MainActivity extends AppCompatActivity {

    private static final int RC_BARCODE_CAPTURE = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button scanButton = findViewById(R.id.btn_scan);
        scanButton.setOnClickListener(v -> {
            // launch barcode activity.
            Intent intent = new Intent(MainActivity.this, ExternalCaptureActivity.class);
            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        });
    }
}
