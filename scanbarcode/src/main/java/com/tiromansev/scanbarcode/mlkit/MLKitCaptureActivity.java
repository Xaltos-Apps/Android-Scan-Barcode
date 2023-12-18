package com.tiromansev.scanbarcode.mlkit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;
import com.tiromansev.scanbarcode.PreferenceActivity;
import com.tiromansev.scanbarcode.PreferencesFragment;
import com.tiromansev.scanbarcode.zxing.BeepManager;

import java.util.ArrayList;
import java.util.List;

public class MLKitCaptureActivity extends AppCompatActivity {

    private GmsBarcodeScanner scanner;
    public BeepManager beepManager;
    private MLKitActivityHandler handler;
    private boolean started = true;
    private final GmsBarcodeScannerOptions.Builder options = new GmsBarcodeScannerOptions.Builder();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new MLKitActivityHandler(this);
        initSettings();
        restartScan();
        beepManager = new BeepManager(this);
    }

    private void initSettings() {
        List<Integer> decodeFormats = new ArrayList<>();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean(PreferencesFragment.KEY_MLKIT_DECODE_CODE_128, true)) {
            decodeFormats.add(Barcode.FORMAT_CODE_128);
        }
        if (prefs.getBoolean(PreferencesFragment.KEY_MLKIT_DECODE_CODABAR, true)) {
            decodeFormats.add(Barcode.FORMAT_CODABAR);
        }
        if (prefs.getBoolean(PreferencesFragment.KEY_MLKIT_DECODE_CODE_39, true)) {
            decodeFormats.add(Barcode.FORMAT_CODE_39);
        }
        if (prefs.getBoolean(PreferencesFragment.KEY_MLKIT_DECODE_CODE_93, true)) {
            decodeFormats.add(Barcode.FORMAT_CODE_93);
        }
        if (prefs.getBoolean(PreferencesFragment.KEY_MLKIT_DECODE_DATA_MATRIX, true)) {
            decodeFormats.add(Barcode.FORMAT_DATA_MATRIX);
        }
        if (prefs.getBoolean(PreferencesFragment.KEY_MLKIT_DECODE_EAN_13, true)) {
            decodeFormats.add(Barcode.FORMAT_EAN_13);
        }
        if (prefs.getBoolean(PreferencesFragment.KEY_MLKIT_DECODE_EAN_8, true)) {
            decodeFormats.add(Barcode.FORMAT_EAN_8);
        }
        if (prefs.getBoolean(PreferencesFragment.KEY_MLKIT_DECODE_ITF, true)) {
            decodeFormats.add(Barcode.FORMAT_ITF);
        }
        if (prefs.getBoolean(PreferencesFragment.KEY_MLKIT_DECODE_UPC_A, true)) {
            decodeFormats.add(Barcode.FORMAT_UPC_A);
        }
        if (prefs.getBoolean(PreferencesFragment.KEY_MLKIT_DECODE_UPC_E, true)) {
            decodeFormats.add(Barcode.FORMAT_UPC_E);
        }
        if (prefs.getBoolean(PreferencesFragment.KEY_MLKIT_DECODE_AZTEC, true)) {
            decodeFormats.add(Barcode.FORMAT_AZTEC);
        }
        if (prefs.getBoolean(PreferencesFragment.KEY_MLKIT_DECODE_QR_CODES, true)) {
            decodeFormats.add(Barcode.FORMAT_QR_CODE);
        }
        if (prefs.getBoolean(PreferencesFragment.KEY_MLKIT_DECODE_PDF_417, true)) {
            decodeFormats.add(Barcode.FORMAT_PDF417);
        }

        if (decodeFormats.size() > 0) {
            int formatsCount = decodeFormats.size();
            int[] formats = new int[formatsCount > 1 ? formatsCount - 1 : 0];
            int code1 = decodeFormats.get(0);

            for (int i = 0; i < formatsCount; i++) {
                if (i == 0) {
                    code1 = decodeFormats.get(0);
                    continue;
                }
                formats[i - 1] = decodeFormats.get(i);
            }

            if (formats.length > 0) {
                options.setBarcodeFormats(code1, formats);
            } else {
                options.setBarcodeFormats(code1);
            }
        }
    }

    public void restartScan() {

    }

    private void handleBarcode(String rawResult) {
        if (started && rawResult != null) {
            started = false;
            handleDecodeInternally(rawResult);
        }
    }

    public Intent getPrefsIntent() {
        return new Intent(MLKitCaptureActivity.this, PreferenceActivity.class);
    }

    public void handleDecodeInternally(String rawResult) {
        Log.d("vision_scan", "scan result = " + rawResult);
    }

    public void handleError(Exception e) {
        finish();
    }

    public void playBeepSoundAndVibrate() {
        beepManager.playBeepSoundAndVibrate();
    }

    public void playFailedSoundAndVibrate() {
        beepManager.playFailedSoundAndVibrate();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        Log.d("scan_delay", "stop scanning with delay = " + delayMS);
        handler.sendEmptyMessageDelayed(1, delayMS);
    }

    @Override
    protected void onStart() {
        super.onStart();
        beepManager.updatePrefs();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beepManager.close();
        scanner = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void startCapture() {
        Log.d("scan_delay", "refresh after pause");
        started = true;
        scanner = GmsBarcodeScanning.getClient(this, options.build());
        scanner.startScan()
                .addOnSuccessListener(
                        barcode -> {
                            handleBarcode(barcode.getRawValue());
                        })
                .addOnCanceledListener(
                        this::finish)
                .addOnFailureListener(
                        this::handleError);
    }
}
