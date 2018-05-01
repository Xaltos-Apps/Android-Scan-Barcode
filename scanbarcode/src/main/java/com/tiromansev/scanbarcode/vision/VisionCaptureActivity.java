package com.tiromansev.scanbarcode.vision;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture;
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic;
import com.google.android.gms.vision.barcode.Barcode;
import com.tiromansev.scanbarcode.PreferenceActivity;
import com.tiromansev.scanbarcode.PreferencesFragment;
import com.tiromansev.scanbarcode.R;
import com.tiromansev.scanbarcode.zxing.BeepManager;
import com.warkiz.widget.IndicatorSeekBar;

import java.util.List;

import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever;

public class VisionCaptureActivity extends AppCompatActivity implements BarcodeRetriever {

    private static final String TAG = "BarcodeMain";
    public BarcodeCapture barcodeCapture;
    public BeepManager beepManager;
    private VisionActivityHandler handler;
    private static final int PREFS_REQUEST = 99;
    private IndicatorSeekBar seekBar;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vision_capture);

        barcodeCapture = (BarcodeCapture) getSupportFragmentManager().findFragmentById(R.id.barcode);
        barcodeCapture.setRetrieval(this);
        barcodeCapture.setUseZoomListener(false);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        seekBar = findViewById(R.id.seekBar);
        setProperties();
        beepManager = new BeepManager(this);
        handler = new VisionActivityHandler(this);

        ImageButton btnSettings = findViewById(R.id.btnScanSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisionCaptureActivity.this, PreferenceActivity.class);
                startActivityForResult(intent, PREFS_REQUEST);
            }
        });

        final float[] oldZoom = {0};
        seekBar.setOnSeekChangeListener(new IndicatorSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(IndicatorSeekBar seekBar, int progress, float progressFloat, boolean fromUserTouch) {
                float zoom = progress / 10;
                if (oldZoom[0] > zoom) {
                    zoom = zoom / oldZoom[0];
                }
                else {
                    zoom = zoom - oldZoom[0];
                }
                barcodeCapture.doZoom(zoom);
                zoomChanged(zoom);
            }

            @Override
            public void onSectionChanged(IndicatorSeekBar seekBar, int thumbPosOnTick, String textBelowTick, boolean fromUserTouch) {

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar, int thumbPosOnTick) {
                oldZoom[0] = seekBar.getProgress() / 10;
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });
    }

    public void zoomChanged(float zoom) {

    }

    public void setZoom(int zoom) {
        if (seekBar == null) {
            seekBar = findViewById(R.id.seekBar);
        }
        seekBar.setProgress(zoom);
        barcodeCapture.doZoom(zoom);
        Log.d("set_zoom", "set zoom = " + zoom);
    }

    public void setProperties() {
        boolean autoFocus = prefs.getBoolean(PreferencesFragment.KEY_AUTO_FOCUS, true);
        boolean showRect = prefs.getBoolean(PreferencesFragment.KEY_SHOW_VISION_RECT, false);
        boolean useFlash = prefs.getString(PreferencesFragment.KEY_FRONT_LIGHT_VISION_MODE, "OFF").equals("ON");
        barcodeCapture
                .setShowFlash(useFlash)
                .setTouchAsCallback(false)
                .setSupportMultipleScan(false)
                .setShowDrawRect(showRect)
                .setShouldShowText(showRect)
                .shouldAutoFocus(autoFocus);
        barcodeCapture.refresh(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PREFS_REQUEST) {
                setProperties();
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
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
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
        barcodeCapture.pause();
        handleDecodeInternally(barcode.displayValue);
    }

    @Override
    public void onRetrievedMultiple(final Barcode closetToClick, final List<BarcodeGraphic> barcodeGraphics) {
        barcodeCapture.pause();
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
        Log.d("scan_delay", "stop scanning with delay = " + delayMS);
        handler.sendEmptyMessageDelayed(1, delayMS);
    }

    public void startCapture() {
        Log.d("scan_delay", "refresh after pause");
        barcodeCapture.resume();
        barcodeCapture.refresh(true);
    }

}
