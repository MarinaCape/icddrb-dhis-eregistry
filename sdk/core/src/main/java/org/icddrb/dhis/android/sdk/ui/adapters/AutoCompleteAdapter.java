package org.icddrb.dhis.android.sdk.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.icddrb.dhis.android.sdk.R;

public class AutoCompleteAdapter extends BaseAdapter implements Filterable {
    private ArrayFilter mFilter;
    private LayoutInflater mInflater;
    private final Object mLock;
    private List<String> mObjects;
    private ArrayList<String> mOriginalValues;

    private class ArrayFilter extends Filter {
        private ArrayFilter() {
        }

        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (AutoCompleteAdapter.this.mOriginalValues == null) {
                synchronized (AutoCompleteAdapter.this.mLock) {
                    AutoCompleteAdapter.this.mOriginalValues = new ArrayList(AutoCompleteAdapter.this.mObjects);
                }
            }
            if (prefix == null || prefix.length() == 0) {
                ArrayList<String> list;
                synchronized (AutoCompleteAdapter.this.mLock) {
                    list = new ArrayList(AutoCompleteAdapter.this.mOriginalValues);
                }
                results.values = list;
                results.count = list.size();
            } else {
                ArrayList<String> values;
                String prefixString = prefix.toString().toLowerCase();
                synchronized (AutoCompleteAdapter.this.mLock) {
                    values = new ArrayList(AutoCompleteAdapter.this.mOriginalValues);
                }
                int count = values.size();
                ArrayList<String> newValues = new ArrayList();
                for (int i = 0; i < count; i++) {
                    String value = (String) values.get(i);
                    String valueText = value.toLowerCase();
                    if (valueText.startsWith(prefixString)) {
                        newValues.add(value);
                    } else {
                        for (String startsWith : valueText.split(" ")) {
                            if (startsWith.startsWith(prefixString)) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        protected void publishResults(CharSequence constraint, FilterResults results) {
            AutoCompleteAdapter.this.mObjects = (List) results.values;
            if (results.count > 0) {
                AutoCompleteAdapter.this.notifyDataSetChanged();
            } else {
                AutoCompleteAdapter.this.notifyDataSetInvalidated();
            }
        }
    }

    public AutoCompleteAdapter() {
        this.mLock = new Object();
        this.mObjects = new ArrayList();
    }

    public AutoCompleteAdapter(LayoutInflater inflater) {
        this.mLock = new Object();
        this.mInflater = inflater;
        this.mObjects = new ArrayList();
    }

    public int getCount() {
        return this.mObjects.size();
    }

    public String getItem(int position) {
        return (String) this.mObjects.get(position);
    }

    public int getPosition(String item) {
        return this.mObjects.indexOf(item);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent);
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent);
    }

    private View createViewFromResource(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            textView = (TextView) this.mInflater.inflate(R.layout.simple_spinner_dropdown_item_custom, parent, false);
        } else {
            textView = (TextView) convertView;
        }
        textView.setText(getItem(position));
        return textView;
    }

    public Filter getFilter() {
        if (this.mFilter == null) {
            this.mFilter = new ArrayFilter();
        }
        return this.mFilter;
    }

    public void swapData(List<String> values) {
        clear();
        addAll(values);
        notifyDataSetChanged();
    }

    private void addAll(Collection<String> collection) {
        synchronized (this.mLock) {
            if (this.mOriginalValues != null) {
                this.mOriginalValues.addAll(collection);
            } else {
                this.mObjects.addAll(collection);
            }
        }
    }

    private void clear() {
        synchronized (this.mLock) {
            if (this.mOriginalValues != null) {
                this.mOriginalValues.clear();
            } else {
                this.mObjects.clear();
            }
        }
    }

    public void setLayoutInflater(LayoutInflater inflater) {
        this.mInflater = inflater;
    }
}
