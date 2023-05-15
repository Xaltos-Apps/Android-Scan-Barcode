package com.tiromansev.scanbarcode.mlkit;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;

public class MLKitActivityHandler extends Handler {

    private WeakReference<MLKitCaptureActivity> activity;

    public MLKitActivityHandler(MLKitCaptureActivity activity) {
        this.activity = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Log.d("scan_delay", "start scanning");
        MLKitCaptureActivity visionCaptureActivity = activity.get();
        if (visionCaptureActivity != null) {
            visionCaptureActivity.startCapture();
        }
    }
}
