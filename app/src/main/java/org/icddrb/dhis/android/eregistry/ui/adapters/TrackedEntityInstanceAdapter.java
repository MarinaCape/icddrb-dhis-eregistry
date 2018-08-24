package org.icddrb.dhis.android.eregistry.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filter.FilterResults;
import android.widget.Filterable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.icddrb.dhis.android.eregistry.ui.rows.upcomingevents.EventRowType;
import org.icddrb.dhis.android.sdk.events.OnRowClick.ITEM_STATUS;
import org.icddrb.dhis.android.sdk.ui.adapters.AbsAdapter;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.EventRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.TrackedEntityInstanceDynamicColumnRows;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.TrackedEntityInstanceItemRow;
import org.icddrb.dhis.android.sdk.utils.support.expression.Expression;
import org.joda.time.DateTime;

public class TrackedEntityInstanceAdapter extends AbsAdapter<EventRow> implements Filterable {
    public static final int FILTER_ALL_ATTRIBUTES = 7;
    public static final int FILTER_DATE = 6;
    public static final int FILTER_FIRST_COLUMN = 3;
    public static final int FILTER_SEARCH = 1;
    public static final int FILTER_SECOND_COLUMN = 4;
    public static final int FILTER_STATUS = 2;
    public static final int FILTER_THIRD_COLUMN = 5;
    public static final String TAG = TrackedEntityInstanceAdapter.class.getSimpleName();
    private List<EventRow> allRows;
    private Filter filter;
    private int filteredColumn;
    private List<EventRow> filteredRows;
    private boolean listIsReversed;

    private class RowComparator<T extends EventRow> implements Comparator<T> {
        private final int column;
        TrackedEntityInstanceItemRow left = null;
        DateTime lhsDate = null;
        DateTime rhsDate = null;
        TrackedEntityInstanceItemRow right = null;

        public RowComparator(int column) {
            this.column = column;
        }

        public int compare(T lhs, T rhs) {
            return sortAscending(lhs, rhs);
        }

        private int sortAscending(T lhs, T rhs) {
            try {
                this.left = (TrackedEntityInstanceItemRow) lhs;
                this.right = (TrackedEntityInstanceItemRow) rhs;
            } catch (Exception e) {
            }
            if (this.left == null || this.right == null) {
                return 0;
            }
            String leftFirstItem = (String) this.left.getColumns().get(0);
            String rightFirstItem = (String) this.right.getColumns().get(0);
            if (leftFirstItem == null) {
                leftFirstItem = "";
            }
            if (rightFirstItem == null) {
                rightFirstItem = "";
            }
            if (rightFirstItem.equalsIgnoreCase(leftFirstItem)) {
                return 0;
            }
            return leftFirstItem.toLowerCase().compareTo(rightFirstItem.toLowerCase());
        }

        private int sortDescending(T lhs, T rhs) {
            try {
                this.left = (TrackedEntityInstanceItemRow) lhs;
                this.right = (TrackedEntityInstanceItemRow) rhs;
            } catch (Exception e) {
            }
            int compare = 0;
            if (!(this.left == null || this.right == null)) {
                String leftFirstItem = (String) this.left.getColumns().get(0);
                String rightFirstItem = (String) this.right.getColumns().get(0);
                if (leftFirstItem == null) {
                    leftFirstItem = "";
                }
                if (rightFirstItem == null) {
                    rightFirstItem = "";
                }
                if (rightFirstItem.equalsIgnoreCase(leftFirstItem)) {
                    return 0;
                }
                compare = rightFirstItem.toLowerCase().compareTo(leftFirstItem.toLowerCase());
            }
            return compare;
        }
    }

    private class TrackedEntityInstanceRowFilter extends Filter {
        private TrackedEntityInstanceRowFilter() {
        }

        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d("Filter", constraint.toString());
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            List<EventRow> filteredItems = new ArrayList();
            int l;
            int i;
            EventRow row;
            if (constraint.toString().startsWith(Integer.toString(1))) {
                constraint = constraint.subSequence(1, constraint.length());
                if (constraint == null || constraint.toString().length() <= 0) {
                    synchronized (this) {
                        filteredItems.addAll(TrackedEntityInstanceAdapter.this.allRows);
                    }
                } else {
                    l = TrackedEntityInstanceAdapter.this.allRows.size();
                    for (i = 0; i < l; i++) {
                        row = (EventRow) TrackedEntityInstanceAdapter.this.allRows.get(i);
                        if (row != null && (row instanceof TrackedEntityInstanceItemRow)) {
                            for (String column : ((TrackedEntityInstanceItemRow) row).getColumns()) {
                                if (column.toLowerCase().contains(constraint)) {
                                    filteredItems.add(row);
                                }
                            }
                        } else if (row instanceof TrackedEntityInstanceDynamicColumnRows) {
                            filteredItems.add(row);
                        }
                    }
                }
                Collections.sort(filteredItems, new RowComparator(3));
                result.count = filteredItems.size();
                result.values = filteredItems;
            } else if (constraint.toString().startsWith(Integer.toString(2))) {
                List<EventRow> offlineRows = new ArrayList();
                List<EventRow> errorRows = new ArrayList();
                List<EventRow> sentRows = new ArrayList();
                l = TrackedEntityInstanceAdapter.this.allRows.size();
                for (i = 0; i < l; i++) {
                    row = (EventRow) TrackedEntityInstanceAdapter.this.allRows.get(i);
                    ITEM_STATUS status = null;
                    if (row instanceof TrackedEntityInstanceDynamicColumnRows) {
                        filteredItems.add(row);
                    } else if (row instanceof TrackedEntityInstanceItemRow) {
                        status = ((TrackedEntityInstanceItemRow) row).getStatus();
                    }
                    if (status != null) {
                        if (status.toString().equalsIgnoreCase(ITEM_STATUS.OFFLINE.toString())) {
                            offlineRows.add(row);
                        }
                        if (status.toString().equalsIgnoreCase(ITEM_STATUS.ERROR.toString())) {
                            errorRows.add(row);
                        }
                        if (status.toString().equalsIgnoreCase(ITEM_STATUS.SENT.toString())) {
                            sentRows.add(row);
                        }
                    }
                }
                filteredItems.addAll(offlineRows);
                filteredItems.addAll(errorRows);
                filteredItems.addAll(sentRows);
                Collections.sort(filteredItems, new RowComparator(3));
            } else if (constraint.toString().startsWith(Integer.toString(3))) {
                filteredItems = filterColumnValues(1);
                TrackedEntityInstanceAdapter.this.setFilteredColumn(1);
            } else if (constraint.toString().startsWith(Integer.toString(4))) {
                filteredItems = filterColumnValues(2);
                TrackedEntityInstanceAdapter.this.setFilteredColumn(2);
            } else if (constraint.toString().startsWith(Integer.toString(5))) {
                filteredItems = filterColumnValues(3);
                TrackedEntityInstanceAdapter.this.setFilteredColumn(3);
            } else {
                synchronized (this) {
                    result.values = TrackedEntityInstanceAdapter.this.allRows;
                    result.count = TrackedEntityInstanceAdapter.this.allRows.size();
                }
            }
            result.count = filteredItems.size();
            result.values = filteredItems;
            return result;
        }

        public List<EventRow> filterColumnValues(int columnNumber) {
            List<EventRow> filteredItems = new ArrayList();
            EventRow headerRow = null;
            int l = TrackedEntityInstanceAdapter.this.allRows.size();
            for (int i = 0; i < l; i++) {
                EventRow row = (EventRow) TrackedEntityInstanceAdapter.this.allRows.get(i);
                if (row instanceof TrackedEntityInstanceDynamicColumnRows) {
                    headerRow = row;
                } else if (row instanceof TrackedEntityInstanceItemRow) {
                    filteredItems.add(row);
                }
            }
            Collections.sort(filteredItems, new RowComparator(columnNumber));
            filteredItems.add(0, headerRow);
            return filteredItems;
        }

        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (constraint.equals("")) {
                TrackedEntityInstanceAdapter.this.swapData(TrackedEntityInstanceAdapter.this.allRows);
            } else if (results.count == 0) {
                Log.d(getClass().getSimpleName(), "results count == 0");
                TrackedEntityInstanceAdapter.this.swapData(new ArrayList());
            } else {
                TrackedEntityInstanceAdapter.this.filteredRows = (ArrayList) results.values;
                for (EventRow eventRow : TrackedEntityInstanceAdapter.this.filteredRows) {
                    if (eventRow instanceof TrackedEntityInstanceDynamicColumnRows) {
                        TrackedEntityInstanceDynamicColumnRows row = (TrackedEntityInstanceDynamicColumnRows) eventRow;
                        row.setTitle(row.getTrackedEntity() + " (" + (TrackedEntityInstanceAdapter.this.filteredRows.size() - 1) + Expression.PAR_CLOSE);
                        break;
                    }
                }
                TrackedEntityInstanceAdapter.this.swapData(TrackedEntityInstanceAdapter.this.filteredRows);
            }
        }
    }

    public TrackedEntityInstanceAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    public void setData(List<EventRow> allRows) {
        this.allRows = allRows;
    }

    public long getItemId(int position) {
        if (getData() != null) {
            return ((EventRow) getData().get(position)).getId();
        }
        return -1;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (getData() != null) {
            return ((EventRow) getData().get(position)).getView(getInflater(), convertView, parent);
        }
        return null;
    }

    public boolean areAllItemsEnabled() {
        return false;
    }

    public boolean isEnabled(int position) {
        return getData() != null && ((EventRow) getData().get(position)).isEnabled();
    }

    public int getViewTypeCount() {
        return EventRowType.values().length;
    }

    public int getItemViewType(int position) {
        if (getData() != null) {
            return ((EventRow) getData().get(position)).getViewType();
        }
        return 0;
    }

    public int getFilteredColumn() {
        return this.filteredColumn;
    }

    public void setFilteredColumn(int filteredColumn) {
        this.filteredColumn = filteredColumn;
    }

    public boolean isListIsReversed(int column) {
        return this.listIsReversed;
    }

    public void setListIsReversed(boolean listIsReversed, int column) {
        this.listIsReversed = listIsReversed;
    }

    public Filter getFilter() {
        if (this.filter == null) {
            this.filter = new TrackedEntityInstanceRowFilter();
        }
        return this.filter;
    }
}
