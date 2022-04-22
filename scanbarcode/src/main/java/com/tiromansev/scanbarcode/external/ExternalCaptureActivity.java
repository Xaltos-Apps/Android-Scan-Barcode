package com.tiromansev.scanbarcode.external;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.tiromansev.scanbarcode.PreferenceActivity;
import com.tiromansev.scanbarcode.PreferencesFragment;
import com.tiromansev.scanbarcode.R;
import com.tiromansev.scanbarcode.zxing.BeepManager;

public class ExternalCaptureActivity extends AppCompatActivity {

    public static final String BARCODE = "BARCODE";

    public EditText edtBarcode;
    public Button btnClose;
    public static final String ENTER_SYMBOL = "0";
    public static final String TAB_SYMBOL = "1";
    public static final String SPACE_SYMBOL = "2";
    private String lastSymbol = ENTER_SYMBOL;
    public BeepManager beepManager;
    private static final int PREFS_REQUEST = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_capture);
        edtBarcode = findViewById(R.id.edtBarcode);
        btnClose = findViewById(R.id.btnClose);
        edtBarcode.getBackground().mutate().setColorFilter(getResources().getColor(R.color.color_external_caption), PorterDuff.Mode.SRC_ATOP);

        edtBarcode.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                finish();
                return true;
            }
            String barcode = edtBarcode.getText().toString();
//            if (TextUtils.isEmpty(barcode)) {
//                restartScan();
//                return true;
//            }
            boolean enterHandled = (lastSymbol.equals(ENTER_SYMBOL) && keyCode == KeyEvent.KEYCODE_ENTER);
            boolean tabHandled = (lastSymbol.equals(TAB_SYMBOL) && keyCode == KeyEvent.KEYCODE_TAB);
            boolean spaceHandled = (lastSymbol.equals(SPACE_SYMBOL) && keyCode == KeyEvent.KEYCODE_SPACE);
            if (enterHandled || tabHandled || spaceHandled) {
                handleBarcode(barcode);
                edtBarcode.setText(null);
                return true;
            }
            return false;
        });

        setProperties();
        beepManager = new BeepManager(this);
        ImageButton btnSettings = findViewById(R.id.btnScanSettings);
        btnSettings.setOnClickListener(v -> {
            Intent intent = getPrefsIntent();
            startActivityForResult(intent, PREFS_REQUEST);
        });

        btnClose.setOnClickListener(v -> closeView(edtBarcode.getText().toString()));
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
        lastSymbol = prefs.getString(PreferencesFragment.KEY_SCAN_LAST_SYMBOL, ENTER_SYMBOL);
    }

    public void handleBarcode(String rawResult) {

    }

    public void restartScan() {

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
        setFocus();
    }

    public void setFocus() {
        edtBarcode.requestFocus();
        edtBarcode.selectAll();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
