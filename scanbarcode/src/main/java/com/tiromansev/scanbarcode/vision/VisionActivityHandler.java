package com.tiromansev.scanbarcode.vision;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class VisionActivityHandler extends Handler {

    private final VisionCaptureActivity activity;

    public VisionActivityHandler(VisionCaptureActivity activity) {
        this.activity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Log.d("scan_delay", "start scanning");
        activity.startCapture();
    }
}
