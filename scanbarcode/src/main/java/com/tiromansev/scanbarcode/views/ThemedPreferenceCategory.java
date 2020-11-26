package com.tiromansev.scanbarcode.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceViewHolder;

import com.tiromansev.scanbarcode.R;

public class ThemedPreferenceCategory extends PreferenceCategory {

    public ThemedPreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ThemedPreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ThemedPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ThemedPreferenceCategory(Context context) {
        super(context);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        TypedValue typedValue = new TypedValue();
        this.getContext().getTheme().resolveAttribute(R.attr.background, typedValue, true);
        holder.itemView.setBackgroundColor(getContext().getResources().getColor(typedValue.resourceId));

        TextView title = (TextView) holder.findViewById(android.R.id.title);
        if (title != null) {
            this.getContext().getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
            title.setTextColor(this.getContext().getResources().getColor(typedValue.resourceId));
            title.setTypeface(Typeface.DEFAULT_BOLD);
            title.setAllCaps(true);
            title.setSingleLine(false);
            int textSize = (int) this.getContext().getResources().getDimension(R.dimen.setting_text_size);
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
    }
}
