package org.icddrb.dhis.android.sdk.ui.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.icddrb.dhis.android.sdk.R;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.AbsTextWatcher;
import org.icddrb.dhis.android.sdk.ui.dialogs.AutoCompleteDialogAdapter.OptionAdapterValue;
import org.icddrb.dhis.android.sdk.ui.views.FontTextView;

public class AutoCompleteDialogFragment extends DialogFragment implements OnItemClickListener {
    private static final String TAG = AutoCompleteDialogFragment.class.getSimpleName();
    private AutoCompleteDialogAdapter mAdapter;
    private int mDialogId;
    private TextView mDialogLabel;
    private EditText mFilter;
    private FontTextView mFontTextView;
    private OnOptionSelectedListener mListener;
    public ProgressBar mProgressBar;

    public interface OnOptionSelectedListener {
        void onOptionSelected(int i, int i2, String str, String str2);
    }

    /* renamed from: org.icddrb.dhis.android.sdk.ui.dialogs.AutoCompleteDialogFragment$1 */
    class C08891 extends AbsTextWatcher {
        C08891() {
        }

        public void afterTextChanged(Editable s) {
            AutoCompleteDialogFragment.this.mAdapter.getFilter().filter(s.toString());
        }
    }

    /* renamed from: org.icddrb.dhis.android.sdk.ui.dialogs.AutoCompleteDialogFragment$2 */
    class C08902 implements OnClickListener {
        C08902() {
        }

        public void onClick(View v) {
            AutoCompleteDialogFragment.this.dismiss();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(1, R.style.Theme_AppCompat_Light_Dialog);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(2);
        return inflater.inflate(R.layout.dialog_fragment_auto_complete, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        ListView mListView = (ListView) view.findViewById(R.id.simple_listview);
        ImageView closeDialogButton = (ImageView) view.findViewById(R.id.close_dialog_button);
        this.mFilter = (EditText) view.findViewById(R.id.filter_options);
        this.mDialogLabel = (TextView) view.findViewById(R.id.dialog_label);
        this.mProgressBar = (ProgressBar) view.findViewById(R.id.auto_complete_dialog_progress_bar);
        this.mFontTextView = (FontTextView) view.findViewById(R.id.no_items_textview);
        ((InputMethodManager) getActivity().getSystemService("input_method")).hideSoftInputFromWindow(this.mFilter.getWindowToken(), 0);
        this.mProgressBar.setVisibility(8);
        this.mAdapter = new AutoCompleteDialogAdapter(LayoutInflater.from(getActivity()));
        mListView.setAdapter(this.mAdapter);
        mListView.setOnItemClickListener(this);
        this.mFilter.addTextChangedListener(new C08891());
        closeDialogButton.setOnClickListener(new C08902());
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (this.mListener != null) {
            OptionAdapterValue value = this.mAdapter.getItem(position);
            if (value != null) {
                this.mListener.onOptionSelected(this.mDialogId, position, value.id, value.label);
            }
        }
        dismiss();
    }

    public void dismiss() {
        ((InputMethodManager) getActivity().getSystemService("input_method")).hideSoftInputFromWindow(this.mFilter.getWindowToken(), 0);
        super.dismiss();
    }

    public void setDialogLabel(int resourceId) {
        if (this.mDialogLabel != null) {
            this.mDialogLabel.setText(resourceId);
        }
    }

    public void setDialogLabel(CharSequence sequence) {
        if (this.mDialogLabel != null) {
            this.mDialogLabel.setText(sequence);
        }
    }

    public void setDialogId(int dialogId) {
        this.mDialogId = dialogId;
    }

    public int getDialogId() {
        return this.mDialogId;
    }

    public CharSequence getDialogLabel() {
        if (this.mDialogLabel != null) {
            return this.mDialogLabel.getText();
        }
        return null;
    }

    public AutoCompleteDialogAdapter getAdapter() {
        return this.mAdapter;
    }

    public void setOnOptionSetListener(OnOptionSelectedListener listener) {
        this.mListener = listener;
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, TAG);
    }

    public void setTextToNoItemsTextView(String text) {
        this.mFontTextView.setText(text);
    }

    public void setNoItemsTextViewVisibility(int visible) {
        switch (visible) {
            case 0:
                this.mFontTextView.setVisibility(visible);
                return;
            case 4:
                this.mFontTextView.setVisibility(visible);
                return;
            case 8:
                this.mFontTextView.setVisibility(visible);
                return;
            default:
                return;
        }
    }
}
