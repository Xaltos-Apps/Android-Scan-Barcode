package com.tiromansev.scanbarcode;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TouchableRadioButton extends androidx.appcompat.widget.AppCompatRadioButton {

    private boolean isTouched = false;
    private OnChangeListener changeListener;

    public void setChangeListener(OnChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public TouchableRadioButton(Context context) {
        super(context);
        init();
    }

    public TouchableRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TouchableRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isTouched) {
                isTouched = false;
                if (changeListener != null) {
                    changeListener.onChanged(isChecked);
                }
            }
        });
    }

    public interface OnChangeListener {
        void onChanged(boolean isChecked);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouched = true;
                break;
            case MotionEvent.ACTION_UP:
                performClick();
                break;
        }
        return true;
    }

}
