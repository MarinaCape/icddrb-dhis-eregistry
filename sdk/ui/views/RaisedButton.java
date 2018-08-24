package org.icddrb.dhis.client.sdk.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class RaisedButton extends CardView {
    private final FontTextView buttonTextView;
    private final LinearLayout linearLayout;

    public RaisedButton(Context context) {
        super(context);
        this.buttonTextView = new FontTextView(context);
        this.linearLayout = new LinearLayout(context);
        init();
    }

    public RaisedButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.buttonTextView = new FontTextView(context, attrs);
        this.linearLayout = new LinearLayout(context, attrs);
        init();
    }

    public RaisedButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.buttonTextView = new FontTextView(context, attrs, defStyleAttr);
        this.linearLayout = new LinearLayout(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setRadius((float) calculatePixels(2));
        setCardElevation((float) calculatePixels(2));
        setPreventCornerOverlap(true);
        setUseCompatPadding(true);
        setClickable(true);
        addView(this.linearLayout, new LayoutParams(-1, -1, 17.0f));
        this.linearLayout.addView(this.buttonTextView, new FrameLayout.LayoutParams(-2, -2));
        int linearLayoutPadding = calculatePixels(8);
        this.linearLayout.setPadding(linearLayoutPadding, linearLayoutPadding, linearLayoutPadding, linearLayoutPadding);
        if (VERSION.SDK_INT < 16) {
            this.linearLayout.setBackgroundDrawable(getSelectableItemBackground());
        } else {
            this.linearLayout.setBackground(getSelectableItemBackground());
        }
    }

    private Drawable getSelectableItemBackground() {
        TypedArray typedArray = getContext().obtainStyledAttributes(new int[]{16843534});
        Drawable drawableFromTheme = typedArray.getDrawable(0);
        typedArray.recycle();
        return drawableFromTheme;
    }

    private int calculatePixels(int dps) {
        return (int) TypedValue.applyDimension(1, (float) dps, getResources().getDisplayMetrics());
    }

    public TextView getTextView() {
        return this.buttonTextView;
    }
}
