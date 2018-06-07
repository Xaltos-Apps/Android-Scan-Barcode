package com.tiromansev.scanbarcode.zxing;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;

public class ScanActivityHandler extends Handler {

    private WeakReference<ZxingVerticalCaptureActivity> activity;

    public ScanActivityHandler(ZxingVerticalCaptureActivity activity) {
        this.activity = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Log.d("scan_delay", "start scanning");
        ZxingVerticalCaptureActivity visionCaptureActivity = activity.get();
        if (visionCaptureActivity != null) {
            visionCaptureActivity.startCapture();
        }
    }
}
