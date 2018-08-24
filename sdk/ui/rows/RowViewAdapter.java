package org.icddrb.dhis.client.sdk.ui.rows;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.icddrb.dhis.client.sdk.ui.models.FormEntity;
import org.icddrb.dhis.client.sdk.ui.models.FormEntity.Type;
import org.icddrb.dhis.client.sdk.ui.models.FormEntityAction;
import org.icddrb.dhis.client.sdk.ui.models.FormEntityAction.FormEntityActionType;
import org.icddrb.dhis.client.sdk.ui.models.FormEntityCharSequence;
import org.icddrb.dhis.client.sdk.ui.models.FormEntityEditText;
import org.icddrb.dhis.client.sdk.ui.models.FormEntityFilter;
import org.icddrb.dhis.client.sdk.ui.models.Picker;
import org.icddrb.dhis.client.sdk.utils.Preconditions;

public class RowViewAdapter extends Adapter<ViewHolder> {
    private final FragmentManager fragmentManager;
    private final List<FormEntity> modifiedDataEntities = new ArrayList();
    private final List<FormEntity> originalDataEntities = new ArrayList();
    private final List<RowView> rowViews = new ArrayList();

    public RowViewAdapter(FragmentManager fragmentManager) {
        this.fragmentManager = (FragmentManager) Preconditions.isNull(fragmentManager, "fragmentManager must not be null");
        assignRowViewsToItemViewTypes();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ((RowView) this.rowViews.get(viewType)).onCreateViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        ((RowView) this.rowViews.get(holder.getItemViewType())).onBindViewHolder(holder, getItem(position));
    }

    public int getItemCount() {
        return this.modifiedDataEntities.size();
    }

    public int getItemViewType(int position) {
        FormEntity formEntity = getItem(position);
        return formEntity != null ? formEntity.getType().ordinal() : -1;
    }

    private void assignRowViewsToItemViewTypes() {
        for (int ordinal = 0; ordinal < Type.values().length; ordinal++) {
            switch (Type.values()[ordinal]) {
                case TEXT:
                    this.rowViews.add(ordinal, new TextRowView());
                    break;
                case EDITTEXT:
                    this.rowViews.add(ordinal, new EditTextRowView());
                    break;
                case CHECKBOX:
                    this.rowViews.add(ordinal, new CheckBoxRowView());
                    break;
                case COORDINATES:
                    this.rowViews.add(ordinal, new CoordinateRowView());
                    break;
                case RADIO_BUTTONS:
                    this.rowViews.add(ordinal, new RadioButtonRowView());
                    break;
                case DATE:
                    this.rowViews.add(ordinal, new DatePickerRowView(this.fragmentManager));
                    break;
                case FILTER:
                    this.rowViews.add(ordinal, new FilterableRowView(this.fragmentManager));
                    break;
                default:
                    break;
            }
        }
    }

    private void assignRowViewsToDataViewTypes() {
        for (int ordinal = 0; ordinal < Type.values().length; ordinal++) {
            switch (Type.values()[ordinal]) {
                case TEXT:
                    this.rowViews.add(ordinal, new TextRowView());
                    break;
                case EDITTEXT:
                    this.rowViews.add(ordinal, new EditTextRowView());
                    break;
                case CHECKBOX:
                    this.rowViews.add(ordinal, new CheckBoxRowView());
                    break;
                case COORDINATES:
                    this.rowViews.add(ordinal, new CoordinateRowView());
                    break;
                case RADIO_BUTTONS:
                    this.rowViews.add(ordinal, new RadioButtonRowView());
                    break;
                case DATE:
                    this.rowViews.add(ordinal, new DatePickerRowView(this.fragmentManager));
                    break;
                case FILTER:
                    this.rowViews.add(ordinal, new FilterableRowView(this.fragmentManager));
                    break;
                default:
                    break;
            }
        }
    }

    private FormEntity getItem(int position) {
        return this.modifiedDataEntities.size() > position ? (FormEntity) this.modifiedDataEntities.get(position) : null;
    }

    public void update(List<FormEntityAction> actions) {
        applyFormEntityActions(actions, true);
    }

    public void swap(List<FormEntity> formEntities) {
        swapData(formEntities, null);
    }

    public void swap(List<FormEntity> formEntities, List<FormEntityAction> actions) {
        swapData(formEntities, actions);
    }

    private void swapData(List<FormEntity> dataEntities, List<FormEntityAction> actions) {
        this.originalDataEntities.clear();
        this.modifiedDataEntities.clear();
        if (dataEntities != null) {
            this.originalDataEntities.addAll(dataEntities);
        }
        applyFormEntityActions(actions, false);
        notifyDataSetChanged();
    }

    private void applyFormEntityActions(List<FormEntityAction> formEntityActions, boolean granularUiUpdatesEnabled) {
        Map<String, FormEntityAction> actionMap = mapActions(formEntityActions);
        applyHideFormEntityActions(actionMap, granularUiUpdatesEnabled);
        applyAssignFormEntityActions(actionMap, granularUiUpdatesEnabled);
    }

    private void applyHideFormEntityActions(Map<String, FormEntityAction> actionMap, boolean granularUiUpdatesEnabled) {
        List<FormEntity> activeFormEntities = distinctActiveFormEntities(actionMap);
        List<FormEntity> previousDataEntities = new ArrayList(this.modifiedDataEntities);
        this.modifiedDataEntities.clear();
        this.modifiedDataEntities.addAll(activeFormEntities);
        if (!previousDataEntities.isEmpty()) {
            int currentFormEntityPosition = 0;
            while (currentFormEntityPosition < previousDataEntities.size()) {
                FormEntity formEntity = (FormEntity) previousDataEntities.get(currentFormEntityPosition);
                if (activeFormEntities.indexOf(formEntity) < 0) {
                    if (granularUiUpdatesEnabled) {
                        notifyItemRemoved(currentFormEntityPosition);
                    }
                    previousDataEntities.remove(currentFormEntityPosition);
                    if (formEntity instanceof FormEntityCharSequence) {
                        ((FormEntityCharSequence) formEntity).setValue("", false);
                    } else if (formEntity instanceof FormEntityFilter) {
                        Picker picker = ((FormEntityFilter) formEntity).getPicker();
                        if (picker != null) {
                            picker.setSelectedChild(null);
                            ((FormEntityFilter) formEntity).setPicker(picker);
                        }
                    }
                } else {
                    currentFormEntityPosition++;
                }
            }
        }
        for (int index = 0; index < activeFormEntities.size(); index++) {
            formEntity = (FormEntity) activeFormEntities.get(index);
            if (granularUiUpdatesEnabled && previousDataEntities.indexOf(formEntity) < 0) {
                notifyItemInserted(index);
            }
        }
    }

    private void applyAssignFormEntityActions(Map<String, FormEntityAction> actionMap, boolean granularUiUpdatesEnabled) {
        for (FormEntity formEntity : this.originalDataEntities) {
            if (formEntity instanceof FormEntityEditText) {
                FormEntityAction entityAction = (FormEntityAction) actionMap.get(formEntity.getId());
                FormEntityEditText formEntityEditText = (FormEntityEditText) formEntity;
                int indexOfVisibleEntity;
                if (entityAction != null && FormEntityActionType.ASSIGN.equals(entityAction.getActionType())) {
                    formEntityEditText.setValue(entityAction.getValue(), false);
                    if (!formEntityEditText.isLocked()) {
                        formEntityEditText.setLocked(true);
                    }
                    indexOfVisibleEntity = this.modifiedDataEntities.indexOf(formEntity);
                    if (granularUiUpdatesEnabled && indexOfVisibleEntity >= 0) {
                        notifyItemChanged(indexOfVisibleEntity);
                    }
                } else if (formEntityEditText.isLocked()) {
                    formEntityEditText.setLocked(false);
                    indexOfVisibleEntity = this.modifiedDataEntities.indexOf(formEntity);
                    if (granularUiUpdatesEnabled && indexOfVisibleEntity >= 0) {
                        notifyItemChanged(indexOfVisibleEntity);
                    }
                }
            }
        }
    }

    private List<FormEntity> distinctActiveFormEntities(Map<String, FormEntityAction> actionMap) {
        List<FormEntity> activeDataEntities = new ArrayList();
        for (FormEntity originalDataEntity : this.originalDataEntities) {
            FormEntityAction formEntityAction = (FormEntityAction) actionMap.get(originalDataEntity.getId());
            if (formEntityAction == null || !FormEntityActionType.HIDE.equals(formEntityAction.getActionType())) {
                activeDataEntities.add(originalDataEntity);
            }
        }
        return activeDataEntities;
    }

    private static Map<String, FormEntityAction> mapActions(List<FormEntityAction> actions) {
        Map<String, FormEntityAction> formEntityActionMap = new HashMap();
        if (!(actions == null || actions.isEmpty())) {
            for (FormEntityAction action : actions) {
                formEntityActionMap.put(action.getId(), action);
            }
        }
        return formEntityActionMap;
    }
}
