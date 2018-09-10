package org.icddrb.dhis.android.sdk.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.icddrb.dhis.android.sdk.R;

public class SimpleAdapter<T> extends AbsAdapter<T> {
    private ExtractStringCallback<T> mCallback;

    public interface ExtractStringCallback<T> {
        String getString(T t);
    }

    private class TextViewHolder {
        final TextView textView;

        public TextViewHolder(TextView textView) {
            this.textView = textView;
        }
    }

    public SimpleAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    public void setStringExtractor(ExtractStringCallback<T> callback) {
        this.mCallback = callback;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextViewHolder holder;
        View view;
        if (convertView == null) {
            View root = getInflater().inflate(R.layout.dialog_fragment_listview_item, parent, false);
            holder = new TextViewHolder((TextView) root.findViewById(R.id.textview_item));
            root.setTag(holder);
            view = root;
        } else {
            view = convertView;
            holder = (TextViewHolder) view.getTag();
        }
        holder.textView.setText(this.mCallback.getString(getData().get(position)));
        return view;
    }

    public T getItemSafely(int pos) {
        if (getData() == null || getData().size() <= 0) {
            return null;
        }
        return getData().get(pos);
    }
}
