package org.icddrb.dhis.android.sdk.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.icddrb.dhis.android.sdk.R;

public final class ValidationErrorAdapter extends AbsAdapter<String> {

    private static class ViewHolder {
        public final TextView textView;

        private ViewHolder(TextView textView) {
            this.textView = textView;
        }
    }

    public ValidationErrorAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = getInflater().inflate(R.layout.dialog_fragment_listview_item_validation_error, parent, false);
            holder = new ViewHolder((TextView) view.findViewById(R.id.text_label));
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.textView.setText((CharSequence) getData().get(position));
        return view;
    }

    public boolean areAllItemsEnabled() {
        return false;
    }

    public boolean isEnabled(int position) {
        return false;
    }
}
