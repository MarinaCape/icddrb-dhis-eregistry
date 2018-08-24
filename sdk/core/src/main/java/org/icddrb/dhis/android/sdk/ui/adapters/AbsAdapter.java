package org.icddrb.dhis.android.sdk.ui.adapters;

import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import java.util.List;
import org.icddrb.dhis.android.sdk.utils.Preconditions;

public abstract class AbsAdapter<T> extends BaseAdapter {
    protected List<T> mData;
    private final LayoutInflater mInflater;

    public AbsAdapter(LayoutInflater inflater) {
        this.mInflater = (LayoutInflater) Preconditions.isNull(inflater, "LayoutInflater must not be null");
    }

    public final int getCount() {
        if (this.mData != null) {
            return this.mData.size();
        }
        return 0;
    }

    public final Object getItem(int position) {
        if (this.mData != null) {
            return this.mData.get(position);
        }
        return null;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    protected LayoutInflater getInflater() {
        return this.mInflater;
    }

    protected List<T> getData() {
        return this.mData;
    }

    public void swapData(List<T> data) {
        boolean notifyAdapter = this.mData != data;
        this.mData = data;
        if (notifyAdapter) {
            notifyDataSetChanged();
        }
    }
}
