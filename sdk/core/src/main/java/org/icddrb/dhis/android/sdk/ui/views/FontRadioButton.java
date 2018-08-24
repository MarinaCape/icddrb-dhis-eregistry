package org.icddrb.dhis.android.sdk.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.widget.RadioGroup;
import org.icddrb.dhis.android.sdk.C0845R;
import org.icddrb.dhis.android.sdk.utils.TypefaceManager;

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
            TypedArray attrs = context.obtainStyledAttributes(attributeSet, C0845R.styleable.ViewFont);
            setFont(attrs.getString(C0845R.styleable.ViewFont_font));
            attrs.recycle();
        }
    }

    public void setFont(String fontName) {
        if (getContext() != null && getContext().getAssets() != null && fontName != null) {
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
