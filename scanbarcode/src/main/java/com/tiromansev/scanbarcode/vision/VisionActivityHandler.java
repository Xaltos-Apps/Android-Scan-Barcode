package com.tiromansev.scanbarcode.vision;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;

public class VisionActivityHandler extends Handler {

    private WeakReference<VisionCaptureActivity> activity;

    public VisionActivityHandler(VisionCaptureActivity activity) {
        this.activity = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Log.d("scan_delay", "start scanning");
        VisionCaptureActivity visionCaptureActivity = activity.get();
        if (visionCaptureActivity != null) {
            visionCaptureActivity.startCapture();
        }
    }
}
