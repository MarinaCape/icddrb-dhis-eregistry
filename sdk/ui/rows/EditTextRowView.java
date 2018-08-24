package org.icddrb.dhis.client.sdk.ui.rows;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.InputDeviceCompat;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import org.icddrb.dhis.client.sdk.ui.C0935R;
import org.icddrb.dhis.client.sdk.ui.models.FormEntity;
import org.icddrb.dhis.client.sdk.ui.models.FormEntityEditText;
import org.icddrb.dhis.client.sdk.ui.views.AbsTextWatcher;

public final class EditTextRowView implements RowView {

    private static class EditTextRowViewHolder extends ViewHolder {
        private static final int LONG_TEXT_LINE_COUNT = 3;
        public final EditText editText;
        private final String enterInteger;
        private final String enterLongText;
        private final String enterNegativeInteger;
        private final String enterNumber;
        private final String enterPositiveInteger;
        private final String enterPositiveOrZeroInteger;
        private final String enterText;
        public final OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener(this.textInputLayout, this.editText);
        public final OnValueChangedListener onValueChangedListener = new OnValueChangedListener();
        public final TextInputLayout textInputLayout;
        public final TextView textViewLabel;

        public EditTextRowViewHolder(View itemView) {
            super(itemView);
            Context context = itemView.getContext();
            this.enterText = context.getString(C0935R.string.enter_text);
            this.enterLongText = context.getString(C0935R.string.enter_long_text);
            this.enterNumber = context.getString(C0935R.string.enter_number);
            this.enterInteger = context.getString(C0935R.string.enter_integer);
            this.enterPositiveInteger = context.getString(C0935R.string.enter_positive_integer);
            this.enterPositiveOrZeroInteger = context.getString(C0935R.string.enter_positive_integer_or_zero);
            this.enterNegativeInteger = context.getString(C0935R.string.enter_negative_integer);
            this.textViewLabel = (TextView) itemView.findViewById(C0935R.id.textview_row_label);
            this.textInputLayout = (TextInputLayout) itemView.findViewById(C0935R.id.edittext_row_textinputlayout);
            this.editText = (EditText) itemView.findViewById(C0935R.id.edittext_row_edittext);
            this.editText.setOnFocusChangeListener(this.onFocusChangeListener);
            this.editText.addTextChangedListener(this.onValueChangedListener);
        }

        public void update(FormEntityEditText entity) {
            this.onValueChangedListener.setDataEntity(entity);
            this.textViewLabel.setText(entity.getLabel());
            this.editText.setText(entity.getValue());
            this.editText.setEnabled(!entity.isLocked());
            configureView(entity);
        }

        private boolean configureView(FormEntityEditText dataEntityText) {
            String hint;
            switch (dataEntityText.getInputType()) {
                case TEXT:
                    if (TextUtils.isEmpty(dataEntityText.getHint())) {
                        hint = this.enterText;
                    } else {
                        hint = dataEntityText.getHint();
                    }
                    return configure(hint, 1, true);
                case URL:
                    if (TextUtils.isEmpty(dataEntityText.getHint())) {
                        hint = this.enterText;
                    } else {
                        hint = dataEntityText.getHint();
                    }
                    return configure(hint, 16, true);
                case LONG_TEXT:
                    if (TextUtils.isEmpty(dataEntityText.getHint())) {
                        hint = this.enterLongText;
                    } else {
                        hint = dataEntityText.getHint();
                    }
                    return configure(hint, 1, false);
                case NUMBER:
                    if (TextUtils.isEmpty(dataEntityText.getHint())) {
                        hint = this.enterNumber;
                    } else {
                        hint = dataEntityText.getHint();
                    }
                    return configure(hint, 12290, true);
                case INTEGER:
                    if (TextUtils.isEmpty(dataEntityText.getHint())) {
                        hint = this.enterInteger;
                    } else {
                        hint = dataEntityText.getHint();
                    }
                    return configure(hint, InputDeviceCompat.SOURCE_TOUCHSCREEN, true);
                case INTEGER_NEGATIVE:
                    if (TextUtils.isEmpty(dataEntityText.getHint())) {
                        hint = this.enterNegativeInteger;
                    } else {
                        hint = dataEntityText.getHint();
                    }
                    return configure(hint, InputDeviceCompat.SOURCE_TOUCHSCREEN, true);
                case INTEGER_ZERO_OR_POSITIVE:
                    if (TextUtils.isEmpty(dataEntityText.getHint())) {
                        hint = this.enterPositiveOrZeroInteger;
                    } else {
                        hint = dataEntityText.getHint();
                    }
                    return configure(hint, 2, true);
                case INTEGER_POSITIVE:
                    if (TextUtils.isEmpty(dataEntityText.getHint())) {
                        hint = this.enterPositiveInteger;
                    } else {
                        hint = dataEntityText.getHint();
                    }
                    return configure(hint, 2, true);
                default:
                    return false;
            }
        }

        private boolean configure(String hint, int inputType, boolean line) {
            String textInputLayoutHint = TextUtils.isEmpty(this.editText.getText()) ? hint : null;
            this.onFocusChangeListener.setHint(hint);
            this.textInputLayout.setHint(textInputLayoutHint);
            this.editText.setInputType(inputType);
            this.editText.setSingleLine(line);
            if (!line) {
                this.editText.setLines(3);
            }
            return true;
        }
    }

    private static class OnFocusChangeListener implements android.view.View.OnFocusChangeListener {
        private final EditText editText;
        private CharSequence hint;
        private final TextInputLayout textInputLayout;

        public OnFocusChangeListener(TextInputLayout inputLayout, EditText editText) {
            this.textInputLayout = inputLayout;
            this.editText = editText;
        }

        public void setHint(CharSequence hint) {
            this.hint = hint;
        }

        public void onFocusChange(View view, boolean hasFocus) {
            if (hasFocus) {
                this.textInputLayout.setHint(this.hint);
            } else if (!TextUtils.isEmpty(this.editText.getText().toString())) {
                this.textInputLayout.setHint(null);
            }
        }
    }

    private static class OnValueChangedListener extends AbsTextWatcher {
        private FormEntityEditText dataEntity;

        private OnValueChangedListener() {
        }

        public void setDataEntity(FormEntityEditText dataEntity) {
            this.dataEntity = dataEntity;
        }

        public void afterTextChanged(Editable editable) {
            if (this.dataEntity != null) {
                this.dataEntity.setValue(editable.toString(), true);
            }
        }
    }

    public ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        return new EditTextRowViewHolder(inflater.inflate(C0935R.layout.recyclerview_row_edittext, parent, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, FormEntity formEntity) {
        ((EditTextRowViewHolder) viewHolder).update((FormEntityEditText) formEntity);
    }
}
