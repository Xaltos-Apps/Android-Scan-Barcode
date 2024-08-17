package com.github.wrdlbrnft.betterbarcodes.reader.permissions;

import android.app.Activity;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * Created by kapeller on 25/01/16.
 */
@Keep
public interface PermissionRequest {

    void start(Activity activity);
    void continueAfterRationale(Activity activity);

    void start(Fragment fragment);
    void continueAfterRationale(Fragment fragment);

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
