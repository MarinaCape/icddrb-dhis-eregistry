package org.icddrb.dhis.android.sdk.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import org.icddrb.dhis.android.sdk.C0845R;

public class CardSpinner extends CardView {
    private CharSequence hint;
    private FontSpinner spinner;

    public CardSpinner(Context context) {
        super(context);
        init(context);
    }

    public CardSpinner(Context context, AttributeSet attributes) {
        super(context, attributes);
        init(context);
        if (!isInEditMode()) {
            TypedArray attrs = context.obtainStyledAttributes(attributes, C0845R.styleable.ButtonHint);
            this.hint = attrs.getString(C0845R.styleable.ButtonHint_hint);
            setText(this.hint);
            attrs.recycle();
        }
    }

    private void init(Context context) {
        this.spinner = new FontSpinner(context);
        this.spinner.setClickable(true);
        this.spinner.setBackgroundResource(C0845R.drawable.spinner_background_holo_light);
        this.spinner.setFont(getContext().getString(C0845R.string.regular_font_name));
        addView(this.spinner);
    }

    public void setText(CharSequence sequence) {
        if (this.spinner != null && sequence == null) {
        }
    }

    public void setOnClickListener(OnClickListener listener) {
        this.spinner.setOnClickListener(listener);
    }

    public void setEnabled(boolean isEnabled) {
        super.setEnabled(isEnabled);
        this.spinner.setEnabled(isEnabled);
        setText(this.hint);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.spinner.setOnItemSelectedListener(listener);
    }

    public void setAdapter(ArrayAdapter adapter) {
        this.spinner.setAdapter(adapter);
    }

    public int getSelectedItemPosition() {
        return this.spinner.getSelectedItemPosition();
    }

    public void setSelection(int position) {
        this.spinner.setSelection(position);
    }
}
