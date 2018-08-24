package org.icddrb.dhis.client.sdk.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import org.icddrb.dhis.client.sdk.ui.C0935R;
import org.icddrb.dhis.client.sdk.ui.adapters.OnPickerItemClickListener;
import org.icddrb.dhis.client.sdk.ui.adapters.PickerItemAdapter;
import org.icddrb.dhis.client.sdk.ui.models.Picker;
import org.icddrb.dhis.client.sdk.ui.views.AbsTextWatcher;
import org.icddrb.dhis.client.sdk.ui.views.DividerDecoration;

public class FilterableDialogFragment extends AppCompatDialogFragment {
    public static final String ARGS_PICKER = "args:picker";
    public static final String TAG = FilterableDialogFragment.class.getSimpleName();
    private OnPickerItemClickDelegate onPickerItemClickDelegate = new OnPickerItemClickDelegate();

    /* renamed from: org.icddrb.dhis.client.sdk.ui.fragments.FilterableDialogFragment$1 */
    class C09511 implements OnClickListener {
        C09511() {
        }

        public void onClick(View v) {
            FilterableDialogFragment.this.dismiss();
        }
    }

    private class OnPickerItemClickDelegate implements OnPickerItemClickListener {
        private OnPickerItemClickListener onPickerItemClickListener;

        private OnPickerItemClickDelegate() {
        }

        public void onPickerItemClickListener(Picker selectedPicker) {
            if (this.onPickerItemClickListener != null) {
                this.onPickerItemClickListener.onPickerItemClickListener(selectedPicker);
            }
            FilterableDialogFragment.this.dismiss();
        }

        public void setOnPickerItemClickListener(OnPickerItemClickListener onItemClickListener) {
            this.onPickerItemClickListener = onItemClickListener;
        }
    }

    public static FilterableDialogFragment newInstance(Picker picker) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(ARGS_PICKER, picker);
        FilterableDialogFragment fragment = new FilterableDialogFragment();
        fragment.setArguments(arguments);
        fragment.setStyle(1, C0935R.style.AppTheme_Dialog);
        return fragment;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(C0935R.layout.fragment_filterable, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Picker picker = null;
        if (getArguments() != null) {
            picker = (Picker) getArguments().getSerializable(ARGS_PICKER);
        }
        if (picker != null) {
            TextView textViewTitle = (TextView) view.findViewById(C0935R.id.textview_titlebar_title);
            if (picker.getHint() != null) {
                textViewTitle.setText(picker.getHint());
            }
            ((ImageView) view.findViewById(C0935R.id.imageview_cancel)).setOnClickListener(new C09511());
            RecyclerView recyclerView = (RecyclerView) view.findViewById(C0935R.id.recyclerview_picker_items);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(1);
            final PickerItemAdapter itemAdapter = new PickerItemAdapter(getActivity(), picker);
            itemAdapter.setOnPickerItemClickListener(this.onPickerItemClickDelegate);
            recyclerView.setAdapter(itemAdapter);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new DividerDecoration(ContextCompat.getDrawable(getActivity(), C0935R.drawable.divider)));
            ((EditText) view.findViewById(C0935R.id.edittext_filter_picker_items)).addTextChangedListener(new AbsTextWatcher() {
                public void afterTextChanged(Editable editable) {
                    itemAdapter.filter(editable.toString());
                }
            });
        }
    }

    public void setOnPickerItemClickListener(OnPickerItemClickListener clickListener) {
        this.onPickerItemClickDelegate.setOnPickerItemClickListener(clickListener);
    }
}
