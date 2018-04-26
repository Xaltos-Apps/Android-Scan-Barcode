package com.tiromansev.scanbarcode.vision;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture;
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic;
import com.google.android.gms.vision.barcode.Barcode;
import com.tiromansev.scanbarcode.PreferencesFragment;
import com.tiromansev.scanbarcode.R;
import com.tiromansev.scanbarcode.zxing.BeepManager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever;

public class VisionCaptureActivity extends AppCompatActivity implements BarcodeRetriever {

    private static final String TAG = "BarcodeMain";
    public BarcodeCapture barcodeCapture;
    public BeepManager beepManager;
    private VisionActivityHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vision_capture);
        hideStatusBar();

        barcodeCapture = (BarcodeCapture) getSupportFragmentManager().findFragmentById(R.id.barcode);
        barcodeCapture.setRetrieval(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean autoFocus = prefs.getBoolean(PreferencesFragment.KEY_AUTO_FOCUS, true);
        boolean showRect = prefs.getBoolean(PreferencesFragment.KEY_SHOW_VISION_RECT, false);
        boolean useFlash = prefs.getString(PreferencesFragment.KEY_FRONT_LIGHT_VISION_MODE, "OFF").equals("ON");
        barcodeCapture
                .setShowFlash(useFlash)
                .setTouchAsCallback(false)
                .setShowDrawRect(showRect)
                .shouldAutoFocus(autoFocus);
        barcodeCapture.refresh(true);
        beepManager = new BeepManager(this);
        handler = new VisionActivityHandler(this);
    }

    private void hideStatusBar() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
        }
    }

    public void playBeepSoundAndVibrate() {
        beepManager.playBeepSoundAndVibrate();
    }

    public void handleDecodeInternally(String rawResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        beepManager.updatePrefs();
    }

    @Override
    protected void onPause() {
        super.onPause();
        beepManager.close();
    }

    public void setTorch(boolean on) {
        barcodeCapture.setShowFlash(on);
        barcodeCapture.refresh(true);
    }

    @Override
    public void onRetrieved(final Barcode barcode) {
        Log.d(TAG, "Barcode read: " + barcode.displayValue);
        handleDecodeInternally(barcode.displayValue);
        barcodeCapture.stopScanning();
    }

    @Override
    public void onRetrievedMultiple(final Barcode closetToClick, final List<BarcodeGraphic> barcodeGraphics) {
        String barcodes = "";
        for (int index = 0; index < barcodeGraphics.size(); index++) {
            Barcode barcode = barcodeGraphics.get(index).getBarcode();
            barcodes += (index + 1) + "," + barcode.displayValue + "\n";
        }
        handleDecodeInternally(barcodes);
    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onRetrievedFailed(String reason) {

    }

    @Override
    public void onPermissionRequestDenied() {

    }

    public void restartPreviewAfterDelay(long delayMS) {
        barcodeCapture.pause();
        Log.d("scan_delay", "stop scanning with delay = " + delayMS);
        handler.sendEmptyMessageDelayed(1, delayMS);
    }

    public void startCapture() {
        Log.d("scan_delay", "refresh after pause");
        barcodeCapture.resume();
        barcodeCapture.refresh(false);
    }

}
