package com.securityapp.hukamboss.securityapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Bhupa on 24/02/18.
 */

public class CustomLayout extends TextInputLayout {
    Drawable drawable1, drawable2;
    public CustomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        replaceBackground();
    }

    @Override
    public void setError(@Nullable final CharSequence error) {
        super.setError(error);

        replaceBackground();
    }

    public void setDrawable1(Drawable drawable) {
        this.drawable1 = drawable;
    }

    public void setDrawable2(Drawable drawable) {
        this.drawable2 = drawable;
    }

    private void replaceBackground() {
        EditText editText = getEditText();
        if (editText != null) {
            editText.setBackground(isErrorEnabled() ? drawable2 : drawable1);
            Drawable drawable = editText.getBackground();
            if (drawable != null) {
                drawable.clearColorFilter();
            }
        }
    }
}

