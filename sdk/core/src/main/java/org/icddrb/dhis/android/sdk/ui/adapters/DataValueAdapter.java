package org.icddrb.dhis.android.sdk.ui.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.media.TransportMediator;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.icddrb.dhis.android.sdk.C0845R;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.persistence.models.DataValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.persistence.models.UserAccount;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.DataEntryRowTypes;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.QuestionCoordinatesRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.Row;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.autocompleterow.TextRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.OnDetailedInfoButtonClick;

public final class DataValueAdapter extends AbsAdapter<Row> {
    private static final String CLASS_TAG = DataValueAdapter.class.getSimpleName();
    private Map<String, Integer> dataElementsToRowIndexMap;
    private Map<String, Boolean> disabledDataElementRows = new HashMap();
    private Map<String, String> errorDataElementRows = new HashMap();
    private Map<String, Boolean> hiddenDataElementRows = new HashMap();
    private Context mContext;
    private final FragmentManager mFragmentManager;
    private ListView mListView;
    private List<String> mandatoryDataElementRows;
    private List<String> mandatoryProgramRuleDataElementRows = new ArrayList();
    private Map<String, String> warningDataElementRows = new HashMap();

    public class CustomOnEditorActionListener implements OnEditorActionListener {
        private boolean isFocusRight;

        public CustomOnEditorActionListener() {
            this.isFocusRight = false;
        }

        public CustomOnEditorActionListener(boolean isFocusRight) {
            this.isFocusRight = isFocusRight;
        }

        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            final TextView view = v;
            if (actionId != 5 || view == null || DataValueAdapter.this.mListView == null) {
                return false;
            }
            DataValueAdapter.this.mListView.smoothScrollToPosition(DataValueAdapter.this.mListView.getPositionForView(v) + 1);
            DataValueAdapter.this.mListView.postDelayed(new Runnable() {
                public void run() {
                    if (view.focusSearch(TransportMediator.KEYCODE_MEDIA_RECORD) instanceof TextView) {
                        TextView textView = null;
                        if (CustomOnEditorActionListener.this.isFocusRight && (view.focusSearch(66) instanceof TextView)) {
                            textView = (TextView) view.focusSearch(66);
                        }
                        if (textView == null) {
                            textView = (TextView) view.focusSearch(TransportMediator.KEYCODE_MEDIA_RECORD);
                        }
                        if (textView != null) {
                            if (DataValueAdapter.this.mListView.getPositionForView(textView) + 1 < DataValueAdapter.this.mData.size()) {
                                textView.setImeOptions(5);
                            }
                            textView.requestFocus();
                            return;
                        }
                        return;
                    }
                    ((InputMethodManager) DataValueAdapter.this.mContext.getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }, 200);
            return true;
        }
    }

    public DataValueAdapter(FragmentManager fragmentManager, LayoutInflater inflater, ListView listView, Context context) {
        super(inflater);
        this.mFragmentManager = fragmentManager;
        this.mListView = listView;
        this.mContext = context;
    }

    private boolean hasOrgAccess(String orgId) {
        UserAccount userAccount = MetaDataController.getUserAccount();
        if (userAccount == null || !userAccount.hasOrganisationAccess(orgId)) {
            return false;
        }
        return true;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (getData() == null) {
            return null;
        }
        Row dataEntryRow = (Row) getData().get(position);
        String id = dataEntryRow.getItemId();
        String orgId = dataEntryRow.getOrgId();
        boolean hasAccess = orgId == null || hasOrgAccess(orgId);
        boolean editable = !this.disabledDataElementRows.containsKey(id);
        System.out.println("Norway - has access " + hasAccess + " is editable: " + editable + " org: " + orgId + "  row: " + dataEntryRow.getDescription());
        boolean z = hasAccess && editable;
        dataEntryRow.setEditable(z);
        if (this.mandatoryProgramRuleDataElementRows.contains(id)) {
            dataEntryRow.setMandatory(true);
        } else if (this.mandatoryDataElementRows.contains(id)) {
            dataEntryRow.setMandatory(true);
        } else {
            dataEntryRow.setMandatory(false);
        }
        dataEntryRow.setWarning((String) this.warningDataElementRows.get(id));
        dataEntryRow.setError((String) this.errorDataElementRows.get(id));
        if (dataEntryRow instanceof QuestionCoordinatesRow) {
            ((TextRow) dataEntryRow).setOnEditorActionListener(new CustomOnEditorActionListener(true));
        } else if (dataEntryRow instanceof TextRow) {
            ((TextRow) dataEntryRow).setOnEditorActionListener(new CustomOnEditorActionListener());
        }
        View view = dataEntryRow.getView(this.mFragmentManager, getInflater(), convertView, parent);
        view.setVisibility(0);
        view.setLayoutParams(new LayoutParams(-1, -2));
        view.postInvalidate();
        view.setId(position);
        View detailedInformationButton = view.findViewById(C0845R.id.detailed_info_button_layout);
        if (dataEntryRow.getDescription() == null || dataEntryRow.getDescription().isEmpty()) {
            if (detailedInformationButton != null) {
                detailedInformationButton.setVisibility(4);
            }
        } else if (detailedInformationButton != null) {
            detailedInformationButton.setOnClickListener(new OnDetailedInfoButtonClick(dataEntryRow));
            detailedInformationButton.setVisibility(0);
        }
        if (!this.hiddenDataElementRows.containsKey(id)) {
            return view;
        }
        view.setLayoutParams(new LayoutParams(-1, 1));
        view.postInvalidate();
        view.setVisibility(8);
        return view;
    }

    public int getViewTypeCount() {
        return DataEntryRowTypes.values().length;
    }

    public int getItemViewType(int position) {
        if (getData() != null) {
            return ((Row) getData().get(position)).getViewType();
        }
        return 0;
    }

    public View getView(String dataElement, View convertView, ViewGroup parent) {
        return getView(((Integer) this.dataElementsToRowIndexMap.get(dataElement)).intValue(), convertView, parent);
    }

    public void swapData(List<Row> data) {
        boolean notifyAdapter = this.mData != data;
        this.mData = data;
        if (this.dataElementsToRowIndexMap == null) {
            this.dataElementsToRowIndexMap = new HashMap();
            this.mandatoryDataElementRows = new ArrayList();
        } else {
            this.dataElementsToRowIndexMap.clear();
            this.mandatoryDataElementRows.clear();
        }
        if (this.mData != null) {
            for (int i = 0; i < this.mData.size(); i++) {
                Row dataEntryRow = (Row) this.mData.get(i);
                BaseValue baseValue = dataEntryRow.getValue();
                if (baseValue instanceof DataValue) {
                    this.dataElementsToRowIndexMap.put(((DataValue) baseValue).getDataElement(), Integer.valueOf(i));
                    if (dataEntryRow.isMandatory()) {
                        this.mandatoryDataElementRows.add(((DataValue) baseValue).getDataElement());
                    }
                } else if ((baseValue instanceof TrackedEntityAttributeValue) && dataEntryRow.isMandatory()) {
                    this.mandatoryDataElementRows.add(((TrackedEntityAttributeValue) baseValue).getTrackedEntityAttributeId());
                }
            }
        }
        if (notifyAdapter) {
            notifyDataSetChanged();
        }
    }

    public void hideIndex(String dataElement) {
        if (this.hiddenDataElementRows == null) {
            this.hiddenDataElementRows = new HashMap();
        }
        if (dataElement != null) {
            this.hiddenDataElementRows.put(dataElement, Boolean.valueOf(true));
        }
    }

    public void disableIndex(String dataElement) {
        if (this.disabledDataElementRows == null) {
            this.disabledDataElementRows = new HashMap();
        }
        if (dataElement != null) {
            this.disabledDataElementRows.put(dataElement, Boolean.valueOf(true));
        }
    }

    public void resetHiding() {
        if (this.mData != null && this.hiddenDataElementRows != null) {
            this.hiddenDataElementRows.clear();
        }
    }

    public void resetDisabled() {
        if (this.mData != null && this.disabledDataElementRows != null) {
            this.disabledDataElementRows.clear();
        }
    }

    public void addMandatoryOnIndex(String dataElement) {
        if (this.mandatoryProgramRuleDataElementRows == null) {
            this.mandatoryProgramRuleDataElementRows = new ArrayList();
        }
        this.mandatoryProgramRuleDataElementRows.add(dataElement);
    }

    public void resetMandatory() {
        if (this.mData != null && this.mandatoryProgramRuleDataElementRows != null) {
            this.mandatoryProgramRuleDataElementRows.clear();
        }
    }

    public List<String> getMandatoryList() {
        return this.mandatoryProgramRuleDataElementRows;
    }

    public void showWarningOnIndex(String dataElement, String warning) {
        if (this.warningDataElementRows == null) {
            this.warningDataElementRows = new HashMap();
        }
        this.warningDataElementRows.put(dataElement, warning);
    }

    public void resetWarnings() {
        if (this.mData != null && this.warningDataElementRows != null) {
            this.warningDataElementRows.clear();
        }
    }

    public void showErrorOnIndex(String dataElement, String warning) {
        if (this.errorDataElementRows == null) {
            this.errorDataElementRows = new HashMap();
        }
        this.errorDataElementRows.put(dataElement, warning);
    }

    public void resetErrors() {
        if (this.mData != null && this.errorDataElementRows != null) {
            this.errorDataElementRows.clear();
        }
    }

    public void hideAll() {
        if (this.dataElementsToRowIndexMap != null) {
            for (String dataElement : this.dataElementsToRowIndexMap.keySet()) {
                hideIndex(dataElement);
            }
        }
    }

    public int getIndex(String dataElement) {
        if (this.dataElementsToRowIndexMap == null || !this.dataElementsToRowIndexMap.containsKey(dataElement)) {
            return -1;
        }
        return ((Integer) this.dataElementsToRowIndexMap.get(dataElement)).intValue();
    }
}
