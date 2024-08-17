package com.github.wrdlbrnft.betterbarcodes.reader;

import android.content.Context;
import android.os.Build;

import androidx.annotation.Keep;

import com.github.wrdlbrnft.betterbarcodes.reader.icecreamsandwich.IceCreamSandwichBarcodeReader;
import com.github.wrdlbrnft.betterbarcodes.reader.lollipop.LollipopBarcodeReader;
import com.github.wrdlbrnft.betterbarcodes.views.AspectRatioTextureView;

/**
 * Created by kapeller on 13/04/16.
 */
@Keep
public class BarcodeReaders {

    public static BarcodeReader get(Context context, AspectRatioTextureView view) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                ? new LollipopBarcodeReader(context, view)
                : new IceCreamSandwichBarcodeReader(context, view);
    }
}
