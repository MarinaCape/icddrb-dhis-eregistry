package org.icddrb.dhis.client.sdk.ui.rows;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import org.icddrb.dhis.client.sdk.ui.C0935R;
import org.icddrb.dhis.client.sdk.ui.adapters.OnPickerItemClickListener;
import org.icddrb.dhis.client.sdk.ui.fragments.FilterableDialogFragment;
import org.icddrb.dhis.client.sdk.ui.models.FormEntity;
import org.icddrb.dhis.client.sdk.ui.models.FormEntityFilter;
import org.icddrb.dhis.client.sdk.ui.models.Picker;
import org.icddrb.dhis.client.sdk.utils.Preconditions;

public class FilterableRowView implements RowView {
    private final FragmentManager fragmentManager;

    private class FilterViewHolder extends ViewHolder {
        private static final String EMPTY_STRING = "";
        private final ImageButton buttonDropDown;
        private final ImageButton clearButton;
        private final EditText filterEditText;
        private final OnClickListener onClickListener = new OnClickListener();
        private final OnItemClickListener onItemClickListener = new OnItemClickListener(this.filterEditText);
        private final TextView textViewLabel;

        private class OnClickListener implements android.view.View.OnClickListener {
            private FormEntityFilter formEntityFilter;

            private OnClickListener() {
            }

            public void setFormEntityFilter(FormEntityFilter formEntityFilter) {
                this.formEntityFilter = formEntityFilter;
            }

            public void onClick(View view) {
                if (view.getId() == C0935R.id.recyclerview_row_filter_edittext) {
                    showFilterableDialogFragment();
                } else if (view.getId() == C0935R.id.button_dropdown) {
                    showFilterableDialogFragment();
                } else if (view.getId() == C0935R.id.button_clear) {
                    FilterViewHolder.this.filterEditText.setText("");
                    if (this.formEntityFilter != null && this.formEntityFilter.getPicker() != null) {
                        this.formEntityFilter.getPicker().setSelectedChild(null);
                        this.formEntityFilter.setPicker(this.formEntityFilter.getPicker());
                    }
                }
            }

            private void showFilterableDialogFragment() {
                if (this.formEntityFilter.getPicker() != null) {
                    FilterableDialogFragment dialogFragment = FilterableDialogFragment.newInstance(this.formEntityFilter.getPicker());
                    dialogFragment.setOnPickerItemClickListener(FilterViewHolder.this.onItemClickListener);
                    dialogFragment.show(FilterableRowView.this.fragmentManager, FilterableDialogFragment.TAG);
                }
            }
        }

        private class OnItemClickListener implements OnPickerItemClickListener {
            private final EditText formEditText;
            private FormEntityFilter formEntityFilter;

            public OnItemClickListener(EditText formEditText) {
                this.formEditText = formEditText;
            }

            public void setFormEntityFilter(FormEntityFilter formEntityFilter) {
                this.formEntityFilter = formEntityFilter;
            }

            public void onPickerItemClickListener(Picker selectedPicker) {
                if (selectedPicker.getParent() != null) {
                    Picker parentPicker = selectedPicker.getParent();
                    parentPicker.setSelectedChild(selectedPicker);
                    if (this.formEntityFilter != null) {
                        this.formEntityFilter.setPicker(parentPicker);
                    }
                }
                this.formEditText.setText(selectedPicker.getName());
            }
        }

        public FilterViewHolder(View itemView) {
            super(itemView);
            this.textViewLabel = (TextView) itemView.findViewById(C0935R.id.textview_row_label);
            this.filterEditText = (EditText) itemView.findViewById(C0935R.id.recyclerview_row_filter_edittext);
            this.buttonDropDown = (ImageButton) itemView.findViewById(C0935R.id.button_dropdown);
            this.clearButton = (ImageButton) itemView.findViewById(C0935R.id.button_clear);
            this.filterEditText.setOnClickListener(this.onClickListener);
            this.clearButton.setOnClickListener(this.onClickListener);
            this.buttonDropDown.setOnClickListener(this.onClickListener);
        }

        public void update(FormEntityFilter formEntityFilter) {
            this.onClickListener.setFormEntityFilter(formEntityFilter);
            this.onItemClickListener.setFormEntityFilter(formEntityFilter);
            this.textViewLabel.setText(formEntityFilter.getLabel());
            String filterEditTextValue = "";
            Picker picker = formEntityFilter.getPicker();
            if (!(picker == null || picker.getSelectedChild() == null)) {
                filterEditTextValue = picker.getSelectedChild().getName();
            }
            this.filterEditText.setText(filterEditTextValue);
            attachListenerToExistingFragment(picker);
        }

        private void attachListenerToExistingFragment(Picker picker) {
            FilterableDialogFragment fragment = (FilterableDialogFragment) FilterableRowView.this.fragmentManager.findFragmentByTag(FilterableDialogFragment.TAG);
            if (fragment != null) {
                Bundle arguments = fragment.getArguments();
                if (arguments != null && arguments.containsKey(FilterableDialogFragment.ARGS_PICKER) && picker.equals((Picker) arguments.getSerializable(FilterableDialogFragment.ARGS_PICKER))) {
                    fragment.setOnPickerItemClickListener(this.onItemClickListener);
                }
            }
        }
    }

    public FilterableRowView(FragmentManager fragmentManager) {
        this.fragmentManager = (FragmentManager) Preconditions.isNull(fragmentManager, "fragmentManager must not be null");
    }

    public ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        return new FilterViewHolder(inflater.inflate(C0935R.layout.recyclerview_row_filter, parent, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, FormEntity formEntity) {
        ((FilterViewHolder) viewHolder).update((FormEntityFilter) formEntity);
    }
}
