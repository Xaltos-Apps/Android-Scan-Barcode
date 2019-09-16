package com.tiromansev.scanbarcode.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.tiromansev.scanbarcode.R;

public class MultilinePreference extends Preference {

    public MultilinePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MultilinePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MultilinePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultilinePreference(Context context) {
        super(context);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        TextView title = (TextView) holder.findViewById(android.R.id.title);
        if (title != null) {
            title.setSingleLine(false);
            int textSize = (int) getContext().getResources().getDimension(R.dimen.setting_text_size);
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
    }
}
