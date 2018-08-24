package org.icddrb.dhis.client.sdk.ui.rows;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import org.icddrb.dhis.client.sdk.ui.C0935R;
import org.icddrb.dhis.client.sdk.ui.models.FormEntity;
import org.icddrb.dhis.client.sdk.ui.models.FormEntityCheckBox;

public class CheckBoxRowView implements RowView {
    private static final String EMPTY_FIELD = "";
    private static final String TRUE = "true";

    private static class CheckBoxRowViewHolder extends ViewHolder {
        public final CheckBox checkBox;
        public final OnCheckBoxListener onCheckBoxListener = new OnCheckBoxListener();
        public final TextView textViewLabel;

        public CheckBoxRowViewHolder(View itemView) {
            super(itemView);
            this.checkBox = (CheckBox) itemView.findViewById(C0935R.id.checkbox_row_checkbox);
            this.textViewLabel = (TextView) itemView.findViewById(C0935R.id.textview_row_label);
            this.checkBox.setOnCheckedChangeListener(this.onCheckBoxListener);
            itemView.setOnClickListener(new OnRowClickListener(this.checkBox));
        }

        public void update(FormEntityCheckBox dataEntity) {
            this.textViewLabel.setText(dataEntity.getLabel());
            this.onCheckBoxListener.setDataEntity(dataEntity);
            if ("".equals(dataEntity.getValue())) {
                this.checkBox.setChecked(false);
            } else if ("true".equals(dataEntity.getValue())) {
                this.checkBox.setChecked(true);
            }
        }
    }

    private static class OnCheckBoxListener implements OnCheckedChangeListener {
        private FormEntityCheckBox dataEntity;

        private OnCheckBoxListener() {
        }

        public void setDataEntity(FormEntityCheckBox dataEntity) {
            this.dataEntity = dataEntity;
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            this.dataEntity.setValue(isChecked ? "true" : "", true);
        }
    }

    private static class OnRowClickListener implements OnClickListener {
        private final CheckBox checkBox;

        public OnRowClickListener(CheckBox checkBox) {
            this.checkBox = checkBox;
        }

        public void onClick(View v) {
            this.checkBox.setChecked(!this.checkBox.isChecked());
        }
    }

    public ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        return new CheckBoxRowViewHolder(inflater.inflate(C0935R.layout.recyclerview_row_checkbox, parent, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, FormEntity formEntity) {
        ((CheckBoxRowViewHolder) viewHolder).update((FormEntityCheckBox) formEntity);
    }
}
