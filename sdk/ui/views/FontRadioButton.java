package org.icddrb.dhis.client.sdk.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.widget.RadioGroup;
import org.icddrb.dhis.client.sdk.ui.C0935R;
import org.icddrb.dhis.client.sdk.ui.utils.TypefaceManager;
import org.icddrb.dhis.client.sdk.utils.Preconditions;

public class FontRadioButton extends AppCompatRadioButton {
    public FontRadioButton(Context context) {
        super(context);
    }

    public FontRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FontRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        if (!isInEditMode()) {
            TypedArray attrs = context.obtainStyledAttributes(attributeSet, C0935R.styleable.ViewFont);
            setFont(attrs.getString(C0935R.styleable.ViewFont_font));
            attrs.recycle();
        }
    }

    public void setFont(@StringRes int resId) {
        setFont(getResources().getString(resId));
    }

    public void setFont(String fontName) {
        Preconditions.isNull(fontName, "fontName must not be null");
        if (getContext() != null && getContext().getAssets() != null) {
            Typeface typeface = TypefaceManager.getTypeface(getContext().getAssets(), fontName);
            if (typeface != null) {
                setPaintFlags(getPaintFlags() | 128);
                setTypeface(typeface);
            }
        }
    }

    public void toggle() {
        if (!isChecked()) {
            setChecked(true);
        } else if (getParent() instanceof RadioGroup) {
            ((RadioGroup) getParent()).clearCheck();
        }
    }
}
