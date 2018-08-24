package org.icddrb.dhis.android.sdk.ui.dialogs;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filter.FilterResults;
import android.widget.Filterable;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.icddrb.dhis.android.sdk.C0845R;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramTrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.ui.views.FontTextView;

public class QueryTrackedEntityInstancesResultDialogAdapter extends BaseAdapter implements Filterable {
    private Context mContext;
    private ArrayFilter mFilter;
    private final LayoutInflater mInflater;
    private final Object mLock = new Object();
    private List<TrackedEntityInstance> mObjects;
    private ArrayList<TrackedEntityInstance> mOriginalValues;
    private Map<String, ProgramTrackedEntityAttribute> programTrackedEntityAttributeMap;
    private List<TrackedEntityInstance> selectedTrackedEntityInstances;

    private class ArrayFilter extends Filter {
        private ArrayFilter() {
        }

        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (QueryTrackedEntityInstancesResultDialogAdapter.this.mOriginalValues == null) {
                synchronized (QueryTrackedEntityInstancesResultDialogAdapter.this.mLock) {
                    QueryTrackedEntityInstancesResultDialogAdapter.this.mOriginalValues = new ArrayList(QueryTrackedEntityInstancesResultDialogAdapter.this.mObjects);
                }
            }
            if (prefix == null || prefix.length() == 0) {
                ArrayList<TrackedEntityInstance> list;
                synchronized (QueryTrackedEntityInstancesResultDialogAdapter.this.mLock) {
                    list = new ArrayList(QueryTrackedEntityInstancesResultDialogAdapter.this.mOriginalValues);
                }
                results.values = list;
                results.count = list.size();
            } else {
                ArrayList<TrackedEntityInstance> values;
                String prefixString = prefix.toString().toLowerCase();
                synchronized (QueryTrackedEntityInstancesResultDialogAdapter.this.mLock) {
                    values = new ArrayList(QueryTrackedEntityInstancesResultDialogAdapter.this.mOriginalValues);
                }
                int count = values.size();
                ArrayList<TrackedEntityInstance> newValues = new ArrayList();
                for (int i = 0; i < count; i++) {
                    TrackedEntityInstance trackedEntityInstanceValue = (TrackedEntityInstance) values.get(i);
                    for (TrackedEntityAttributeValue attrValue : trackedEntityInstanceValue.getAttributes()) {
                        String value = attrValue.getValue();
                        if (value == null) {
                            break;
                        }
                        String valueText = value.toLowerCase();
                        if (valueText.startsWith(prefixString)) {
                            newValues.add(trackedEntityInstanceValue);
                        } else {
                            for (String startsWith : valueText.split(" ")) {
                                if (startsWith.startsWith(prefixString)) {
                                    newValues.add(trackedEntityInstanceValue);
                                    break;
                                }
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
            QueryTrackedEntityInstancesResultDialogAdapter.this.mObjects = (List) results.values;
            if (results.count > 0) {
                QueryTrackedEntityInstancesResultDialogAdapter.this.notifyDataSetChanged();
            } else {
                QueryTrackedEntityInstancesResultDialogAdapter.this.notifyDataSetInvalidated();
            }
        }
    }

    private static class CustomTypefaceSpan extends MetricAffectingSpan {
        private final Typeface typeface;

        public CustomTypefaceSpan(Typeface typeface) {
            this.typeface = typeface;
        }

        public void updateDrawState(TextPaint drawState) {
            apply(drawState);
        }

        public void updateMeasureState(TextPaint paint) {
            apply(paint);
        }

        private void apply(Paint paint) {
            Typeface oldTypeface = paint.getTypeface();
            int fakeStyle = (oldTypeface != null ? oldTypeface.getStyle() : 0) & (this.typeface.getStyle() ^ -1);
            if ((fakeStyle & 1) != 0) {
                paint.setFakeBoldText(true);
            }
            if ((fakeStyle & 2) != 0) {
                paint.setTextSkewX(-0.25f);
            }
            paint.setTypeface(this.typeface);
        }
    }

    public static class OptionAdapterValue {
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
    }

    private static class ViewHolder {
        LinearLayout attributeContainer;
        Context mContext;
        LayoutInflater mInflater;

        private ViewHolder(View view, LayoutInflater inflater, Context context) {
            this.mInflater = inflater;
            this.attributeContainer = (LinearLayout) view.findViewById(C0845R.id.textviewcontainer);
            this.mContext = context;
        }

        public void setData(List<TrackedEntityAttributeValue> values, Map<String, ProgramTrackedEntityAttribute> programTrackedEntityAttributeMap) {
            this.attributeContainer.removeAllViews();
            if (values != null) {
                Collections.sort(values, new TrackedEntityAttributeValueByIndexInProgramSorter(programTrackedEntityAttributeMap));
                for (int i = 0; i < values.size(); i++) {
                    TrackedEntityAttributeValue trackedEntityAttributeValue = (TrackedEntityAttributeValue) values.get(i);
                    if (programTrackedEntityAttributeMap.containsKey(trackedEntityAttributeValue.getTrackedEntityAttributeId()) && trackedEntityAttributeValue != null) {
                        ProgramTrackedEntityAttribute programTrackedEntityAttribute = (ProgramTrackedEntityAttribute) programTrackedEntityAttributeMap.get(trackedEntityAttributeValue.getTrackedEntityAttributeId());
                        StringBuilder builder = new StringBuilder();
                        builder.append(programTrackedEntityAttribute.getTrackedEntityAttribute().getDisplayName());
                        builder.append(this.attributeContainer.getContext().getString(C0845R.string.delimiter));
                        LinearLayout attributeLayout = (LinearLayout) this.mInflater.inflate(C0845R.layout.two_horizontal_textviews, this.attributeContainer, false);
                        FontTextView labelTextView = (FontTextView) attributeLayout.findViewById(C0845R.id.left_textview);
                        labelTextView.setText(builder.toString());
                        Typeface font = Typeface.createFromAsset(this.mContext.getAssets(), "fonts/" + this.mContext.getString(C0845R.string.light_font_name));
                        Typeface font2 = Typeface.createFromAsset(this.mContext.getAssets(), "fonts/" + this.mContext.getString(C0845R.string.medium_font_name));
                        SpannableStringBuilder SS = new SpannableStringBuilder(builder.toString() + trackedEntityAttributeValue.getValue());
                        SS.setSpan(new CustomTypefaceSpan(font), 0, builder.toString().length(), 34);
                        SS.setSpan(new CustomTypefaceSpan(font2), builder.toString().length(), builder.toString().length() + trackedEntityAttributeValue.getValue().length(), 34);
                        labelTextView.setText(SS);
                        this.attributeContainer.addView(attributeLayout);
                    }
                }
            }
        }
    }

    public QueryTrackedEntityInstancesResultDialogAdapter(LayoutInflater inflater, List<TrackedEntityInstance> selectedTrackedEntityInstances, Map<String, ProgramTrackedEntityAttribute> programTrackedEntityAttributeMap, Context context) {
        this.mInflater = inflater;
        this.mObjects = new ArrayList();
        this.selectedTrackedEntityInstances = selectedTrackedEntityInstances;
        this.programTrackedEntityAttributeMap = programTrackedEntityAttributeMap;
        this.mContext = context;
    }

    public List<TrackedEntityInstance> getData() {
        return this.mObjects;
    }

    public int getCount() {
        if (this.mObjects == null) {
            return 0;
        }
        return this.mObjects.size();
    }

    public TrackedEntityInstance getItem(int position) {
        return (TrackedEntityInstance) this.mObjects.get(position);
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
        TrackedEntityInstance trackedEntityInstance = (TrackedEntityInstance) this.mObjects.get(position);
        List<TrackedEntityAttributeValue> trackedEntityAttributesByProgram = getTrackedEntityAttributesByProgram(trackedEntityInstance.getAttributes(), this.programTrackedEntityAttributeMap);
        if (convertView == null) {
            view = this.mInflater.inflate(C0845R.layout.dialog_fragment_listview_item_teiqueryresult, parent, false);
            holder = new ViewHolder(view, this.mInflater, this.mContext);
            view.setTag(holder);
        } else {
            view = convertView;
            ((CheckBox) view.findViewById(C0845R.id.checkBoxTeiQuery)).setChecked(this.selectedTrackedEntityInstances.contains(trackedEntityInstance));
            holder = (ViewHolder) view.getTag();
        }
        if (trackedEntityInstance != null) {
            holder.setData(trackedEntityAttributesByProgram, this.programTrackedEntityAttributeMap);
        }
        view.setId(position);
        return view;
    }

    private List<TrackedEntityAttributeValue> getTrackedEntityAttributesByProgram(List<TrackedEntityAttributeValue> attributes, Map<String, ProgramTrackedEntityAttribute> programTrackedEntityAttributeMap) {
        List<TrackedEntityAttributeValue> attributesByProgram = new ArrayList();
        for (int i = 0; i < attributes.size(); i++) {
            TrackedEntityAttributeValue attributeValue = (TrackedEntityAttributeValue) attributes.get(i);
            if (programTrackedEntityAttributeMap.containsKey(attributeValue.getTrackedEntityAttributeId())) {
                attributesByProgram.add(attributeValue);
            }
        }
        return attributesByProgram;
    }

    public Filter getFilter() {
        if (this.mFilter == null) {
            this.mFilter = new ArrayFilter();
        }
        return this.mFilter;
    }

    public void swapData(List<TrackedEntityInstance> values) {
        if (values == null) {
            values = new ArrayList();
        }
        clear();
        addAll(values);
        notifyDataSetChanged();
    }

    private void addAll(Collection<TrackedEntityInstance> collection) {
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
