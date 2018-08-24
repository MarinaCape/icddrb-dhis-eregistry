package org.icddrb.dhis.client.sdk.ui.rows;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import org.icddrb.dhis.client.sdk.ui.C0935R;
import org.icddrb.dhis.client.sdk.ui.models.FormEntity;
import org.icddrb.dhis.client.sdk.ui.models.FormEntityRadioButtons;

public class RadioButtonRowView implements RowView {
    private static final String EMPTY_FIELD = "";
    private static final String FALSE = "false";
    private static final String TRUE = "true";

    private static class OnCheckedChangedListener implements OnCheckedChangeListener {
        private FormEntityRadioButtons dataEntity;

        private OnCheckedChangedListener() {
        }

        public void setDataEntity(FormEntityRadioButtons dataEntity) {
            this.dataEntity = dataEntity;
        }

        public void onCheckedChanged(RadioGroup group, int checkedId) {
            String newValue;
            if (checkedId == C0935R.id.radiobutton_row_radiobutton_first) {
                newValue = "true";
            } else if (checkedId == C0935R.id.radiobutton_row_radiobutton_second) {
                newValue = "false";
            } else {
                newValue = "";
            }
            this.dataEntity.setValue(newValue, true);
        }
    }

    private static class RadioButtonRowViewHolder extends ViewHolder {
        public final RadioButton firstRadioButton;
        public final TextView labelTextView;
        public final OnCheckedChangedListener onCheckedChangedListener = new OnCheckedChangedListener();
        public final RadioGroup radioGroup;
        public final RadioButton secondRadioButton;

        public RadioButtonRowViewHolder(View itemView) {
            super(itemView);
            this.labelTextView = (TextView) itemView.findViewById(C0935R.id.textview_row_label);
            this.radioGroup = (RadioGroup) itemView.findViewById(C0935R.id.radiogroup_radiobutton_row);
            this.firstRadioButton = (RadioButton) itemView.findViewById(C0935R.id.radiobutton_row_radiobutton_first);
            this.secondRadioButton = (RadioButton) itemView.findViewById(C0935R.id.radiobutton_row_radiobutton_second);
            this.firstRadioButton.setText(itemView.getContext().getString(C0935R.string.yes));
            this.secondRadioButton.setText(itemView.getContext().getString(C0935R.string.no));
            this.radioGroup.setOnCheckedChangeListener(this.onCheckedChangedListener);
        }

        public void update(FormEntityRadioButtons dataEntity) {
            this.onCheckedChangedListener.setDataEntity(dataEntity);
            this.labelTextView.setText(dataEntity.getLabel());
            if ("true".equals(dataEntity.getValue())) {
                this.firstRadioButton.setChecked(true);
            } else if ("false".equals(dataEntity.getValue())) {
                this.secondRadioButton.setChecked(true);
            } else {
                this.radioGroup.clearCheck();
            }
        }
    }

    public ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        return new RadioButtonRowViewHolder(inflater.inflate(C0935R.layout.recyclerview_row_radiobutton, parent, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, FormEntity formEntity) {
        ((RadioButtonRowViewHolder) viewHolder).update((FormEntityRadioButtons) formEntity);
    }
}
