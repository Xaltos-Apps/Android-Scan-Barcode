package com.github.wrdlbrnft.betterbarcodes.exceptions;

import androidx.annotation.Keep;

/**
 * Created by kapeller on 05/02/16.
 */
@Keep
public class BarcodeWriterException extends RuntimeException {

    public BarcodeWriterException(String detailMessage) {
        super(detailMessage);
    }

    public BarcodeWriterException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
