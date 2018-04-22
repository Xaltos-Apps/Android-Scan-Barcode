package com.tiromansev.scanbarcode.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tiromansev.scanbarcode.external.ExternalCaptureActivity;
import com.tiromansev.scanbarcode.vision.VisionCaptureActivity;

public class MainActivity extends ExternalCaptureActivity {

    private static final int RC_BARCODE_CAPTURE = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button scanButton = findViewById(R.id.btn_scan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // launch barcode activity.
                Intent intent = new Intent(MainActivity.this, VisionCaptureActivity.class);
                intent.putExtra(VisionCaptureActivity.AutoFocus, true);
                intent.putExtra(VisionCaptureActivity.UseFlash, false);

                startActivityForResult(intent, RC_BARCODE_CAPTURE);
            }
        });
    }
}
