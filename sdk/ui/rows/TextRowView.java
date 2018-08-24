package org.icddrb.dhis.client.sdk.ui.rows;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.icddrb.dhis.client.sdk.ui.C0935R;
import org.icddrb.dhis.client.sdk.ui.models.FormEntity;
import org.icddrb.dhis.client.sdk.ui.models.FormEntityText;

public class TextRowView implements RowView {

    private static class TextViewHolder extends ViewHolder {
        final TextView textViewLabel;
        final TextView textViewValue;

        public TextViewHolder(View itemView) {
            super(itemView);
            this.textViewLabel = (TextView) itemView.findViewById(C0935R.id.textview_row_label);
            this.textViewValue = (TextView) itemView.findViewById(C0935R.id.textview_row_textview);
        }

        public void update(FormEntityText entityText) {
            this.textViewLabel.setText(entityText.getLabel());
            this.textViewValue.setText(entityText.getValue());
        }
    }

    public ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        return new TextViewHolder(inflater.inflate(C0935R.layout.recyclerview_row_textview, parent, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, FormEntity formEntity) {
        ((TextViewHolder) viewHolder).update((FormEntityText) formEntity);
    }
}
