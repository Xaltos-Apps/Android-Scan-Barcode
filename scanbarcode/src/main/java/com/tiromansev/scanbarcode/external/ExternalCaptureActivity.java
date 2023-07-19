package com.tiromansev.scanbarcode.external;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.tiromansev.scanbarcode.PreferenceActivity;
import com.tiromansev.scanbarcode.PreferencesFragment;
import com.tiromansev.scanbarcode.R;
import com.tiromansev.scanbarcode.zxing.BeepManager;

public class ExternalCaptureActivity extends AppCompatActivity {

    public static final String BARCODE = "BARCODE";

    public EditText edtBarcode;
    public TextView tvBarcode;
    public Button btnClose;
    public static final String ENTER_SYMBOL = "0";
    public static final String TAB_SYMBOL = "1";
    public static final String SPACE_SYMBOL = "2";
    boolean isEnterSetting = true;
    boolean isTabSetting = false;
    boolean isSpaceSetting = false;
    public BeepManager beepManager;
    private static final int PREFS_REQUEST = 99;
    private String barcode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_capture);
        edtBarcode = findViewById(R.id.edtBarcode);
        tvBarcode = findViewById(R.id.tvBarcode);
        btnClose = findViewById(R.id.btnClose);
        edtBarcode.getBackground().mutate().setColorFilter(getResources().getColor(R.color.color_external_caption), PorterDuff.Mode.SRC_ATOP);
        tvBarcode.getBackground().mutate().setColorFilter(getResources().getColor(R.color.color_external_caption), PorterDuff.Mode.SRC_ATOP);

        setProperties();
        beepManager = new BeepManager(this);
        ImageButton btnSettings = findViewById(R.id.btnScanSettings);
        btnSettings.setOnClickListener(v -> {
            Log.d("external_scan", "settings clicked");
            Intent intent = getPrefsIntent();
            startActivityForResult(intent, PREFS_REQUEST);
        });

        btnClose.setOnClickListener(v -> closeView(edtBarcode.getText().toString()));
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyAction = event.getAction();
        int keyCode = event.getKeyCode();
        int ch = event.getUnicodeChar();

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }

        if (keyAction == KeyEvent.ACTION_DOWN) {
            if (TextUtils.isEmpty(barcode)) {
                restartScan();
                return true;
            }

            barcode += (char) ch;
            tvBarcode.setText(barcode);
            Log.d("external_scan", "barcode = " + barcode);

            boolean enterHandled = keyCode == KeyEvent.KEYCODE_ENTER;
            boolean tabHandled = keyCode == KeyEvent.KEYCODE_TAB;
            boolean spaceHandled = keyCode == KeyEvent.KEYCODE_SPACE;

            if (enterHandled || tabHandled || spaceHandled) {
                if ((isEnterSetting && enterHandled) || (isTabSetting && tabHandled) || (isSpaceSetting && spaceHandled)) {
                    handleBarcode(barcode);
                }
                resetBarcode();
            }

            return true;
        }

        return super.dispatchKeyEvent(event);
    }

    private void resetBarcode() {
        barcode = "";
        tvBarcode.setText("");
    }

    public void closeView(String barcode) {

    }

    public Intent getPrefsIntent() {
        return new Intent(ExternalCaptureActivity.this, PreferenceActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PREFS_REQUEST) {
                setProperties();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setProperties() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String lastSymbol = prefs.getString(PreferencesFragment.KEY_SCAN_LAST_SYMBOL, ENTER_SYMBOL);
        isEnterSetting = lastSymbol.equals(ENTER_SYMBOL);
        isTabSetting = lastSymbol.equals(TAB_SYMBOL);
        isSpaceSetting = lastSymbol.equals(SPACE_SYMBOL);
    }

    public void handleBarcode(String rawResult) {

    }

    public void restartScan() {
        resetBarcode();
    }

    public void playBeepSoundAndVibrate() {
        beepManager.playBeepSoundAndVibrate();
    }

    public void playFailedSoundAndVibrate() {
        beepManager.playFailedSoundAndVibrate();
    }

    @Override
    protected void onPause() {
        beepManager.close();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        beepManager.updatePrefs();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
