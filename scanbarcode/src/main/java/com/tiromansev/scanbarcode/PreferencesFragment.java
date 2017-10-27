package com.tiromansev.scanbarcode;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;

import butterknife.ButterKnife;

public final class PreferencesFragment
        extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private CheckBoxPreference[] checkBoxPrefs;

    public static final String KEY_DECODE_1D_PRODUCT = "preferences_decode_1D_product";
    public static final String KEY_DECODE_1D_INDUSTRIAL = "preferences_decode_1D_industrial";
    public static final String KEY_DECODE_QR = "preferences_decode_QR";
    public static final String KEY_DECODE_DATA_MATRIX = "preferences_decode_Data_Matrix";
    public static final String KEY_DECODE_AZTEC = "preferences_decode_Aztec";
    public static final String KEY_DECODE_PDF417 = "preferences_decode_PDF417";
    private static final String KEY_CUSTOM_PRODUCT_SEARCH = "preferences_custom_product_search";

    public static final String KEY_PLAY_BEEP = "preferences_play_beep";
    public static final String KEY_VIBRATE = "preferences_vibrate";
    public static final String KEY_FRONT_LIGHT_MODE = "preferences_front_light_mode";
    public static final String KEY_AUTO_FOCUS = "preferences_auto_focus";
    public static final String KEY_INVERT_SCAN = "preferences_invert_scan";
    public static final String KEY_PACKET_SCAN_TYPE_INT = "preferences_packet_scan_type_int";
    public static final String KEY_DISABLE_AUTO_ORIENTATION = "preferences_orientation";

    public static final String KEY_DISABLE_CONTINUOUS_FOCUS = "preferences_disable_continuous_focus";
    public static final String KEY_DISABLE_EXPOSURE = "preferences_disable_exposure";
    public static final String KEY_DISABLE_METERING = "preferences_disable_metering";
    public static final String KEY_DISABLE_BARCODE_SCENE_MODE = "preferences_disable_barcode_scene_mode";

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.barcode_preferences);
        ButterKnife.bind(this, getActivity());

        PreferenceScreen preferences = getPreferenceScreen();
        checkBoxPrefs = findDecodePrefs(preferences,
                KEY_DECODE_1D_PRODUCT,
                KEY_DECODE_1D_INDUSTRIAL,
                KEY_DECODE_QR,
                KEY_DECODE_DATA_MATRIX,
                KEY_DECODE_AZTEC,
                KEY_DECODE_PDF417);
        disableLastCheckedPref();

        //заголовок
        getActivity().setTitle(R.string.caption_setting_scan);

        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
            initSummary(getPreferenceScreen().getPreference(i));
        }

        EditTextPreference customProductSearch = (EditTextPreference)
                preferences.findPreference(KEY_CUSTOM_PRODUCT_SEARCH);
        if (customProductSearch != null) {
            customProductSearch.setOnPreferenceChangeListener(new CustomSearchURLValidator());
        }
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

    }

    private static CheckBoxPreference[] findDecodePrefs(PreferenceScreen preferences, String... keys) {
        CheckBoxPreference[] prefs = new CheckBoxPreference[keys.length];
        for (int i = 0; i < keys.length; i++) {
            prefs[i] = (CheckBoxPreference) preferences.findPreference(keys[i]);
        }
        return prefs;
    }

    @Override
    public void onResume() {
        super.onResume();

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        disableLastCheckedPref();
        updatePrefSummary(findPreference(key));
    }

    private void initSummary(Preference p) {
        if (p instanceof PreferenceCategory) {
            PreferenceCategory pCat = (PreferenceCategory) p;
            for (int i = 0; i < pCat.getPreferenceCount(); i++) {
                initSummary(pCat.getPreference(i));
            }
        } else {
            updatePrefSummary(p);
        }
    }

    private void updatePrefSummary(Preference p) {
        if (p == null) {
            return;
        }
        if (p instanceof ListPreference) {
            ListPreference listPref = (ListPreference) p;
            p.setSummary(listPref.getEntry());
        }
    }

    private void disableLastCheckedPref() {
        Collection<CheckBoxPreference> checked = new ArrayList<>(checkBoxPrefs.length);
        for (CheckBoxPreference pref : checkBoxPrefs) {
            if (pref != null) {
                if (pref.isChecked()) {
                    checked.add(pref);
                }
            }
        }
        boolean disable = checked.size() <= 1;
        for (CheckBoxPreference pref : checkBoxPrefs) {
            if (pref != null) {
                pref.setEnabled(!(disable && checked.contains(pref)));
            }
        }
    }

    private class CustomSearchURLValidator implements Preference.OnPreferenceChangeListener {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (!isValid(newValue)) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(PreferencesFragment.this.getActivity(), R.style.AppCompatAlertDialogStyle);
                builder.setTitle(R.string.zxing_msg_error);
                builder.setMessage(R.string.zxing_msg_invalid_value);
                builder.setCancelable(true);
                builder.show();
                return false;
            }
            return true;
        }

        private boolean isValid(Object newValue) {
            // Allow empty/null value
            if (newValue == null) {
                return true;
            }
            String valueString = newValue.toString();
            if (valueString.isEmpty()) {
                return true;
            }
            // Before validating, remove custom placeholders, which will not
            // be considered valid parts of the URL in some locations:
            // Blank %t and %s:
            valueString = valueString.replaceAll("%[st]", "");
            // Blank %f but not if followed by digit or a-f as it may be a hex sequence
            valueString = valueString.replaceAll("%f(?![0-9a-f])", "");
            // Require a scheme otherwise:
            try {
                URI uri = new URI(valueString);
                return uri.getScheme() != null;
            } catch (URISyntaxException use) {
                return false;
            }
        }
    }

}
