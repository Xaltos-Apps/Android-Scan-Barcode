package com.github.wrdlbrnft.betterbarcodes.reader;

import androidx.annotation.Keep;

import com.github.wrdlbrnft.betterbarcodes.BarcodeFormat;
import com.github.wrdlbrnft.betterbarcodes.reader.permissions.PermissionHandler;

/**
 * Created by kapeller on 25/01/16.
 */
@Keep
public interface BarcodeReader {

    @Keep
    interface Callback {
        void onResult(String text);
    }

    void startPreview();
    void startScanning();
    void stopScanning();
    void stopPreview();
    void setFormat(@BarcodeFormat int... format);
    void setCallback(Callback callback);
    void setCameraPermissionHandler(PermissionHandler handler);
}
