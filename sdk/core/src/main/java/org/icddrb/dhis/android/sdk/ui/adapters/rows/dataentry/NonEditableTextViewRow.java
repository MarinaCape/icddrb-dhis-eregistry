package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.icddrb.dhis.android.sdk.R;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.autocompleterow.TextRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.OnDetailedInfoButtonClick;

public abstract class NonEditableTextViewRow extends TextRow {
    protected String mValue;

    public static class ViewHolder {
        final View detailedInfoButton;
        final TextView textLabel;
        final TextView textValue;

        public ViewHolder(TextView textLabel, TextView textValue, View detailedInfoButton) {
            this.textLabel = textLabel;
            this.textValue = textValue;
            this.detailedInfoButton = detailedInfoButton;
        }
    }

    public abstract String getName();

    public NonEditableTextViewRow(String value) {
        this.mValue = value;
        checkNeedsForDescriptionButton();
    }

    public View getView(FragmentManager fragmentManager, LayoutInflater inflater, View convertView, ViewGroup container) {
        ViewHolder holder;
        View view;
        if (convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
            View root = inflater.inflate(R.layout.listview_row_indicator, container, false);
            holder = new ViewHolder((TextView) root.findViewById(R.id.text_label), (TextView) root.findViewById(R.id.indicator_row), root.findViewById(R.id.detailed_info_button_layout));
            root.setTag(holder);
            view = root;
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.textLabel.setText(getName());
        if (isEditable()) {
            holder.textValue.setEnabled(true);
        } else {
            holder.textValue.setEnabled(false);
        }
        holder.detailedInfoButton.setOnClickListener(new OnDetailedInfoButtonClick(this));
        holder.textValue.setText(this.mValue);
        if (isDetailedInfoButtonHidden()) {
            holder.detailedInfoButton.setVisibility(4);
        } else {
            holder.detailedInfoButton.setVisibility(0);
        }
        holder.textValue.setOnEditorActionListener(this.mOnEditorActionListener);
        return view;
    }

    public void updateValue(String value) {
        this.mValue = value;
    }

    public String getStringValue() {
        return this.mValue;
    }
}
