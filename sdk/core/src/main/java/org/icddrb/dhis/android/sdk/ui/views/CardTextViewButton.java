package org.icddrb.dhis.android.sdk.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View.OnClickListener;
import android.widget.FrameLayout.LayoutParams;
import org.icddrb.dhis.android.sdk.R;

public class CardTextViewButton extends CardView {
    private CharSequence mHint;
    private FontTextView mTextView;

    public CardTextViewButton(Context context) {
        super(context);
        init(context);
    }

    public CardTextViewButton(Context context, AttributeSet attributes) {
        super(context, attributes);
        init(context);
        if (!isInEditMode()) {
            TypedArray attrs = context.obtainStyledAttributes(attributes, R.styleable.ButtonHint);
            this.mHint = attrs.getString(R.styleable.ButtonHint_hint);
            setText(this.mHint);
            attrs.recycle();
        }
    }

    private void init(Context context) {
        int pxs = getResources().getDimensionPixelSize(R.dimen.card_text_view_margin);
        LayoutParams textViewParams = new LayoutParams(-1, -2);
        textViewParams.setMargins(pxs, pxs, pxs, pxs);
        this.mTextView = new FontTextView(context);
        this.mTextView.setClickable(true);
        this.mTextView.setId(getId());
        this.mTextView.setBackgroundResource(R.drawable.spinner_background_holo_light);
        this.mTextView.setFont(getContext().getString(R.string.regular_font_name));
        this.mTextView.setLayoutParams(textViewParams);
        this.mTextView.setTextSize(0, getResources().getDimension(R.dimen.medium_text_size));
        addView(this.mTextView);
    }

    public void setText(CharSequence sequence) {
        if (this.mTextView != null && sequence != null) {
            this.mTextView.setText(sequence);
        }
    }

    public void setOnClickListener(OnClickListener listener) {
        this.mTextView.setOnClickListener(listener);
    }

    public void setEnabled(boolean isEnabled) {
        super.setEnabled(isEnabled);
        this.mTextView.setEnabled(isEnabled);
        setText(this.mHint);
    }
}
