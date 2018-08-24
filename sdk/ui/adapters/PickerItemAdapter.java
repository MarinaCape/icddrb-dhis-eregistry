package org.icddrb.dhis.client.sdk.ui.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import org.icddrb.dhis.client.sdk.ui.C0935R;
import org.icddrb.dhis.client.sdk.ui.models.Picker;
import org.icddrb.dhis.client.sdk.utils.Preconditions;

public class PickerItemAdapter extends Adapter {
    private final Context context;
    private Picker currentPicker;
    private final List<Picker> filteredPickers;
    private final LayoutInflater inflater;
    private OnPickerItemClickListener onPickerItemClickListener;
    private final List<Picker> originalPickers;

    private class PickerItemViewHolder extends ViewHolder {
        final OnClickListener onTextViewLabelClickListener = new OnClickListener();
        final TextView textViewLabel;

        private class OnClickListener implements android.view.View.OnClickListener {
            private Picker picker;

            private OnClickListener() {
            }

            public void setPicker(Picker picker) {
                this.picker = picker;
            }

            public void onClick(View view) {
                if (PickerItemAdapter.this.onPickerItemClickListener != null) {
                    PickerItemAdapter.this.onPickerItemClickListener.onPickerItemClickListener(this.picker);
                }
            }
        }

        public PickerItemViewHolder(View itemView) {
            super(itemView);
            this.textViewLabel = (TextView) itemView;
            r1 = new int[2][];
            r1[0] = new int[]{16842913};
            r1[1] = new int[0];
            this.textViewLabel.setTextColor(new ColorStateList(r1, new int[]{ContextCompat.getColor(r8.context, C0935R.color.color_primary_default), this.textViewLabel.getCurrentTextColor()}));
            this.textViewLabel.setOnClickListener(this.onTextViewLabelClickListener);
        }

        public void updateViewHolder(Picker picker, boolean isSelected) {
            this.textViewLabel.setSelected(isSelected);
            this.textViewLabel.setText(picker.getName());
            this.onTextViewLabelClickListener.setPicker(picker);
        }
    }

    public PickerItemAdapter(Context context, Picker picker) {
        this.context = (Context) Preconditions.isNull(context, "context must not be null!");
        this.inflater = LayoutInflater.from(context);
        this.currentPicker = (Picker) Preconditions.isNull(picker, "Picker must not be null");
        this.originalPickers = this.currentPicker.getChildren();
        this.filteredPickers = new ArrayList(this.currentPicker.getChildren());
    }

    public PickerItemAdapter(Context context) {
        this.context = (Context) Preconditions.isNull(context, "context must not be null!");
        this.inflater = LayoutInflater.from(context);
        this.originalPickers = new ArrayList();
        this.filteredPickers = new ArrayList();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PickerItemViewHolder(this.inflater.inflate(C0935R.layout.recyclerview_row_picker_item, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        PickerItemViewHolder pickerViewHolder = (PickerItemViewHolder) holder;
        Picker picker = (Picker) this.filteredPickers.get(position);
        if (this.currentPicker == null || this.currentPicker.getSelectedChild() == null || !picker.equals(this.currentPicker.getSelectedChild())) {
            pickerViewHolder.updateViewHolder(picker, false);
        } else {
            pickerViewHolder.updateViewHolder(picker, true);
        }
    }

    public int getItemCount() {
        return this.filteredPickers.size();
    }

    public void setOnPickerItemClickListener(OnPickerItemClickListener onPickerItemClickListener) {
        this.onPickerItemClickListener = onPickerItemClickListener;
    }

    public void swapData(Picker picker) {
        this.currentPicker = picker;
        this.originalPickers.clear();
        this.filteredPickers.clear();
        if (picker != null) {
            this.originalPickers.addAll(picker.getChildren());
            this.filteredPickers.addAll(picker.getChildren());
        }
        notifyDataSetChanged();
    }

    public Picker getData() {
        return this.currentPicker;
    }

    public void filter(@NonNull String query) {
        this.filteredPickers.clear();
        query = query.toLowerCase();
        for (Picker picker : this.originalPickers) {
            if (picker.getName() != null && picker.getName().toLowerCase().contains(query)) {
                this.filteredPickers.add(picker);
            }
        }
        notifyDataSetChanged();
    }
}
