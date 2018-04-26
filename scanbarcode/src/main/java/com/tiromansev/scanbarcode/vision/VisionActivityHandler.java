package com.tiromansev.scanbarcode.vision;

import android.os.Handler;
import android.os.Message;

public class VisionActivityHandler extends Handler {

    private final VisionCaptureActivity activity;

    public VisionActivityHandler(VisionCaptureActivity activity) {
        this.activity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        activity.startCapture();
    }
}
