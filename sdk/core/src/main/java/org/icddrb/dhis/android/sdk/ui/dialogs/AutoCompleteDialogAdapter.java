package org.icddrb.dhis.android.sdk.ui.dialogs;

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

public class AutoCompleteDialogAdapter extends BaseAdapter implements Filterable {
    private ArrayFilter mFilter;
    private final LayoutInflater mInflater;
    private final Object mLock = new Object();
    private List<OptionAdapterValue> mObjects;
    private ArrayList<OptionAdapterValue> mOriginalValues;

    private class ArrayFilter extends Filter {
        private ArrayFilter() {
        }

        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (AutoCompleteDialogAdapter.this.mOriginalValues == null) {
                synchronized (AutoCompleteDialogAdapter.this.mLock) {
                    AutoCompleteDialogAdapter.this.mOriginalValues = new ArrayList(AutoCompleteDialogAdapter.this.mObjects);
                }
            }
            if (prefix == null || prefix.length() == 0) {
                ArrayList<OptionAdapterValue> list;
                synchronized (AutoCompleteDialogAdapter.this.mLock) {
                    list = new ArrayList(AutoCompleteDialogAdapter.this.mOriginalValues);
                }
                results.values = list;
                results.count = list.size();
            } else {
                ArrayList<OptionAdapterValue> values;
                String prefixString = prefix.toString().toLowerCase();
                synchronized (AutoCompleteDialogAdapter.this.mLock) {
                    values = new ArrayList(AutoCompleteDialogAdapter.this.mOriginalValues);
                }
                int count = values.size();
                ArrayList<OptionAdapterValue> newValues = new ArrayList();
                for (int i = 0; i < count; i++) {
                    OptionAdapterValue optionValue = (OptionAdapterValue) values.get(i);
                    String valueText = optionValue.label.toLowerCase();
                    if (valueText.startsWith(prefixString)) {
                        newValues.add(optionValue);
                    } else {
                        for (String startsWith : valueText.split(" ")) {
                            if (startsWith.startsWith(prefixString)) {
                                newValues.add(optionValue);
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
            AutoCompleteDialogAdapter.this.mObjects = (List) results.values;
            if (results.count > 0) {
                AutoCompleteDialogAdapter.this.notifyDataSetChanged();
            } else {
                AutoCompleteDialogAdapter.this.notifyDataSetInvalidated();
            }
        }
    }

    public static class OptionAdapterValue implements Comparable<OptionAdapterValue> {
        public final String id;
        public final String label;

        public OptionAdapterValue(String id, String label) {
            this.id = id;
            this.label = label;
        }

        public boolean equals(Object o) {
            if (!(o instanceof OptionAdapterValue)) {
                return false;
            }
            OptionAdapterValue p = (OptionAdapterValue) o;
            if (objectsEqual(p.id, this.label) && objectsEqual(p.id, this.label)) {
                return true;
            }
            return false;
        }

        private static boolean objectsEqual(Object a, Object b) {
            return a == b || (a != null && a.equals(b));
        }

        public int hashCode() {
            int i = 0;
            int hashCode = this.id == null ? 0 : this.id.hashCode();
            if (this.label != null) {
                i = this.label.hashCode();
            }
            return hashCode ^ i;
        }

        public int compareTo(OptionAdapterValue optionAdapterValue) {
            return this.label.compareToIgnoreCase(optionAdapterValue.label);
        }
    }

    private static class ViewHolder {
        public final TextView textView;

        public ViewHolder(View view) {
            this.textView = (TextView) view.findViewById(R.id.textview_item);
        }
    }

    public AutoCompleteDialogAdapter(LayoutInflater inflater) {
        this.mInflater = inflater;
        this.mObjects = new ArrayList();
    }

    public int getCount() {
        return this.mObjects.size();
    }

    public OptionAdapterValue getItem(int position) {
        return (OptionAdapterValue) this.mObjects.get(position);
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
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = this.mInflater.inflate(R.layout.dialog_fragment_listview_option_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.textView.setText(getItem(position).label);
        return view;
    }

    public Filter getFilter() {
        if (this.mFilter == null) {
            this.mFilter = new ArrayFilter();
        }
        return this.mFilter;
    }

    public void swapData(List<OptionAdapterValue> values) {
        if (values == null) {
            values = new ArrayList();
        }
        clear();
        addAll(values);
        notifyDataSetChanged();
    }

    private void addAll(Collection<OptionAdapterValue> collection) {
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
}
