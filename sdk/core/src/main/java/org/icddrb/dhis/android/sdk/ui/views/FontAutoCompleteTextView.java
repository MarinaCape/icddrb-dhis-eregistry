package org.icddrb.dhis.android.sdk.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import org.icddrb.dhis.android.sdk.C0845R;
import org.icddrb.dhis.android.sdk.utils.TypefaceManager;

public class FontAutoCompleteTextView extends TextView {
    public FontAutoCompleteTextView(Context context) {
        super(context);
    }

    public FontAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FontAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
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

    private void setFont(String fontName) {
        if (getContext() != null && getContext().getAssets() != null && fontName != null) {
            Typeface typeface = TypefaceManager.getTypeface(getContext().getAssets(), fontName);
            if (typeface != null) {
                setPaintFlags(getPaintFlags() | 128);
                setTypeface(typeface);
            }
        }
    }
}
