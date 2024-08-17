package com.github.wrdlbrnft.betterbarcodes.reader.permissions;

import androidx.annotation.Keep;

/**
 * Created by kapeller on 28/01/16.
 */
@Keep
public interface PermissionHandler {
    void onNewPermissionRequest(PermissionRequest request);

    boolean onShowRationale();

    void onPermissionGranted();

    void onPermissionDenied();

    @Keep
    abstract class Adapter implements PermissionHandler {

        @Override
        public boolean onShowRationale() {
            return false;
        }

        @Override
        public void onPermissionGranted() {

        }

        @Override
        public void onPermissionDenied() {

        }
    }
}
