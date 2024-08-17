package com.github.wrdlbrnft.betterbarcodes.writer;

import android.graphics.Bitmap;

import androidx.annotation.Keep;

/**
 * Created by kapeller on 05/02/16.
 */
@Keep
public interface BarcodeWriter {
    Bitmap write(String text, int width, int height);
}
