package org.icddrb.dhis.android.eregistry.fragments.search;

import android.content.Context;
import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.queriable.StringQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.events.OnRowClick.ITEM_STATUS;
import org.icddrb.dhis.android.sdk.persistence.loaders.Query;
import org.icddrb.dhis.android.sdk.persistence.models.FailedItem;
import org.icddrb.dhis.android.sdk.persistence.models.Option;
import org.icddrb.dhis.android.sdk.persistence.models.OptionSet;
import org.icddrb.dhis.android.sdk.persistence.models.Program;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramTrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue.Table;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.EventRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.TrackedEntityInstanceDynamicColumnRows;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.TrackedEntityInstanceItemRow;
import org.icddrb.dhis.android.sdk.utils.ScreenSizeConfigurator;
import org.icddrb.dhis.android.sdk.utils.support.expression.Expression;

public class LocalSearchResultFragmentFormQuery implements Query<LocalSearchResultFragmentForm> {
    HashMap<String, String> attributeValueMap;
    String orgUnitId;
    String programId;

    public LocalSearchResultFragmentFormQuery(String orgUnitId, String programId, HashMap<String, String> attributeValueMap) {
        this.orgUnitId = orgUnitId;
        this.programId = programId;
        this.attributeValueMap = attributeValueMap;
    }

    public LocalSearchResultFragmentForm query(Context context) {
        LocalSearchResultFragmentForm form = new LocalSearchResultFragmentForm();
        if (!(this.orgUnitId.equals("") || this.programId.equals(""))) {
            Program selectedProgram = MetaDataController.getProgram(this.programId);
            List<EventRow> eventRows = new ArrayList();
            List<ProgramTrackedEntityAttribute> attributes = selectedProgram.getProgramTrackedEntityAttributes();
            List<String> attributesToShow = new ArrayList();
            Map<String, TrackedEntityAttribute> attributesToShowMap = new HashMap();
            TrackedEntityInstanceDynamicColumnRows attributeNames = new TrackedEntityInstanceDynamicColumnRows();
            TrackedEntityInstanceDynamicColumnRows row = new TrackedEntityInstanceDynamicColumnRows();
            int numberOfColumns = 5;
            try {
                numberOfColumns = ScreenSizeConfigurator.getInstance().getFields();
            } catch (Exception w) {
                w.printStackTrace();
            }
            for (ProgramTrackedEntityAttribute attribute : attributes) {
                if (attribute.getDisplayInList() && attributesToShow.size() < numberOfColumns) {
                    attributesToShow.add(attribute.getTrackedEntityAttributeId());
                    attributesToShowMap.put(attribute.getTrackedEntityAttributeId(), attribute.getTrackedEntityAttribute());
                    if (attribute.getTrackedEntityAttribute() != null) {
                        row.addColumn(attribute.getTrackedEntityAttribute().getName());
                        attributeNames.addColumn(attribute.getTrackedEntityAttribute().getShortName());
                    }
                }
            }
            eventRows.add(row);
            HashMap<String, String> attributesWithValuesMap = new HashMap();
            Map<String, TrackedEntityAttribute> trackedEntityAttributesUsedInQueryMap = new HashMap();
            for (String key : this.attributeValueMap.keySet()) {
                String val = (String) this.attributeValueMap.get(key);
                if (val != null) {
                    if (!val.equals("")) {
                        attributesWithValuesMap.put(key, val);
                    }
                }
                trackedEntityAttributesUsedInQueryMap.put(key, MetaDataController.getTrackedEntityAttribute(key));
            }
            String query = getTrackedEntityInstancesQuery(attributesWithValuesMap, trackedEntityAttributesUsedInQueryMap);
            if (query != null) {
                List<TrackedEntityInstance> resultTrackedEntityInstances = new StringQuery(TrackedEntityInstance.class, query).queryList();
                List<TrackedEntityAttribute> trackedEntityAttributes = new Select().from(TrackedEntityAttribute.class).queryList();
                Map<String, TrackedEntityAttribute> allTrackedEntityAttributesMap = new HashMap();
                for (TrackedEntityAttribute trackedEntityAttribute : trackedEntityAttributes) {
                    allTrackedEntityAttributesMap.put(trackedEntityAttribute.getUid(), trackedEntityAttribute);
                }
                Map<Long, TrackedEntityInstance> trackedEntityInstanceLocalIdToTeiMap = new HashMap();
                for (TrackedEntityInstance trackedEntityInstance : resultTrackedEntityInstances) {
                    trackedEntityInstanceLocalIdToTeiMap.put(Long.valueOf(trackedEntityInstance.getLocalId()), trackedEntityInstance);
                }
                Set<String> failedItemsForTrackedEntityInstances = getFailedItemsForTrackedEntityInstances(trackedEntityInstanceLocalIdToTeiMap);
                Map<String, Map<String, Option>> optionsForOptionSetsDisplayedInListMap = getCachedOptionsForOptionSets(attributesToShowMap);
                Map<Long, Map<String, TrackedEntityAttributeValue>> cachedTrackedEntityAttributeValuesForTrackedEntityInstances = getCachedTrackedEntityAttributeValuesForTrackedEntityInstances(attributesToShow, resultTrackedEntityInstances);
                for (TrackedEntityInstance trackedEntityInstance2 : resultTrackedEntityInstances) {
                    if (trackedEntityInstance2 != null) {
                        eventRows.add(createTrackedEntityInstanceItem(context, trackedEntityInstance2, attributesToShow, allTrackedEntityAttributesMap, failedItemsForTrackedEntityInstances, cachedTrackedEntityAttributeValuesForTrackedEntityInstances, optionsForOptionSetsDisplayedInListMap));
                    }
                }
                form.setEventRowList(eventRows);
                form.setColumnNames(attributeNames);
                if (selectedProgram.getTrackedEntityType() != null) {
                    row.setTrackedEntity(selectedProgram.getTrackedEntityType().getName());
                    row.setTitle(selectedProgram.getTrackedEntityType().getName() + " (" + (eventRows.size() - 1) + Expression.PAR_CLOSE);
                }
            }
        }
        return form;
    }

    private EventRow createTrackedEntityInstanceItem(Context context, TrackedEntityInstance trackedEntityInstance, List<String> attributesToShow, Map<String, TrackedEntityAttribute> trackedEntityAttributeMap, Set<String> failedEventIds, Map<Long, Map<String, TrackedEntityAttributeValue>> cachedTrackedEntityAttributeValuesForTrackedEntityInstances, Map<String, Map<String, Option>> optionsForOptionSetMap) {
        TrackedEntityInstanceItemRow trackedEntityInstanceItemRow = new TrackedEntityInstanceItemRow(context);
        trackedEntityInstanceItemRow.setTrackedEntityInstance(trackedEntityInstance);
        if (trackedEntityInstance.isFromServer()) {
            trackedEntityInstanceItemRow.setStatus(ITEM_STATUS.SENT);
        } else {
            if (failedEventIds.contains(trackedEntityInstance.getTrackedEntityInstance())) {
                trackedEntityInstanceItemRow.setStatus(ITEM_STATUS.ERROR);
            } else {
                trackedEntityInstanceItemRow.setStatus(ITEM_STATUS.OFFLINE);
            }
        }
        Map<String, TrackedEntityAttributeValue> trackedEntityAttributeValueMapForTrackedEntityInstance = (Map) cachedTrackedEntityAttributeValuesForTrackedEntityInstances.get(Long.valueOf(trackedEntityInstance.getLocalId()));
        for (int i = 0; i < attributesToShow.size(); i++) {
            String value = " ";
            String attributeUid = (String) attributesToShow.get(i);
            if (attributeUid != null) {
                TrackedEntityAttributeValue teav = null;
                if (trackedEntityAttributeValueMapForTrackedEntityInstance != null) {
                    teav = (TrackedEntityAttributeValue) trackedEntityAttributeValueMapForTrackedEntityInstance.get(attributeUid);
                }
                TrackedEntityAttribute trackedEntityAttribute = (TrackedEntityAttribute) trackedEntityAttributeMap.get(attributeUid);
                if (teav == null || trackedEntityAttribute == null) {
                    trackedEntityInstanceItemRow.addColumn(value);
                } else {
                    value = teav.getValue();
                    if (trackedEntityAttribute.isOptionSetValue()) {
                        if (trackedEntityAttribute.getOptionSet() != null) {
                            Map<String, Option> optionsMap = (Map) optionsForOptionSetMap.get(trackedEntityAttribute.getOptionSet());
                            if (optionsMap == null) {
                                trackedEntityInstanceItemRow.addColumn(value);
                            } else {
                                Option optionWithMatchingValue = (Option) optionsMap.get(value);
                                if (optionWithMatchingValue != null) {
                                    value = optionWithMatchingValue.getName();
                                }
                            }
                        }
                    }
                }
            }
            trackedEntityInstanceItemRow.addColumn(value);
        }
        return trackedEntityInstanceItemRow;
    }

    private Map<Long, Map<String, TrackedEntityAttributeValue>> getCachedTrackedEntityAttributeValuesForTrackedEntityInstances(List<String> attributesToShow, List<TrackedEntityInstance> resultTrackedEntityInstances) {
        int i;
        List<Long> trackedEntityInstanceIds = new ArrayList();
        for (TrackedEntityInstance trackedEntityInstance : resultTrackedEntityInstances) {
            trackedEntityInstanceIds.add(Long.valueOf(trackedEntityInstance.getLocalId()));
        }
        String trackedEntityInstanceIdsString = "";
        for (i = 0; i < trackedEntityInstanceIds.size(); i++) {
            trackedEntityInstanceIdsString = trackedEntityInstanceIdsString + "" + trackedEntityInstanceIds.get(i);
            if (i < trackedEntityInstanceIds.size() - 1) {
                trackedEntityInstanceIdsString = trackedEntityInstanceIdsString + ',';
            }
        }
        String attributesToShowIdString = "";
        for (i = 0; i < attributesToShow.size(); i++) {
            attributesToShowIdString = attributesToShowIdString + "'" + ((String) attributesToShow.get(i)) + "'";
            if (i < attributesToShow.size() - 1) {
                attributesToShowIdString = attributesToShowIdString + ',';
            }
        }
        List<TrackedEntityAttributeValue> cachedAttributeValuesToShow = new StringQuery(TrackedEntityAttributeValue.class, "SELECT * FROM " + TrackedEntityAttributeValue.class.getSimpleName() + " WHERE " + "localTrackedEntityInstanceId" + " IN ( " + trackedEntityInstanceIdsString + ") AND " + Table.TRACKEDENTITYATTRIBUTEID + " IN (" + attributesToShowIdString + ");").queryList();
        Map<Long, Map<String, TrackedEntityAttributeValue>> cachedTrackedEntityAttributeValuesForTrackedEntityInstances = new HashMap();
        for (TrackedEntityAttributeValue trackedEntityAttributeValue : cachedAttributeValuesToShow) {
            Map<String, TrackedEntityAttributeValue> trackedEntityAttributeValueMapForTrackedEntityInstance = (Map) cachedTrackedEntityAttributeValuesForTrackedEntityInstances.get(Long.valueOf(trackedEntityAttributeValue.getLocalTrackedEntityInstanceId()));
            if (trackedEntityAttributeValueMapForTrackedEntityInstance == null) {
                trackedEntityAttributeValueMapForTrackedEntityInstance = new HashMap();
                cachedTrackedEntityAttributeValuesForTrackedEntityInstances.put(Long.valueOf(trackedEntityAttributeValue.getLocalTrackedEntityInstanceId()), trackedEntityAttributeValueMapForTrackedEntityInstance);
            }
            trackedEntityAttributeValueMapForTrackedEntityInstance.put(trackedEntityAttributeValue.getTrackedEntityAttributeId(), trackedEntityAttributeValue);
        }
        return cachedTrackedEntityAttributeValuesForTrackedEntityInstances;
    }

    private Map<String, Map<String, Option>> getCachedOptionsForOptionSets(Map<String, TrackedEntityAttribute> trackedEntityAttributeMap) {
        Map<String, Map<String, Option>> optionsForOptionSetMap = new HashMap();
        for (TrackedEntityAttribute trackedEntityAttribute : trackedEntityAttributeMap.values()) {
            if (trackedEntityAttribute.isOptionSetValue() && trackedEntityAttribute.getOptionSet() != null) {
                OptionSet optionSet = MetaDataController.getOptionSet(trackedEntityAttribute.getOptionSet());
                if (optionSet != null) {
                    List<Option> options = MetaDataController.getOptions(optionSet.getUid());
                    if (options != null) {
                        HashMap<String, Option> optionsHashMap = new HashMap();
                        optionsForOptionSetMap.put(optionSet.getUid(), optionsHashMap);
                        for (Option option : options) {
                            optionsHashMap.put(option.getCode(), option);
                        }
                    }
                }
            }
        }
        return optionsForOptionSetMap;
    }

    private String getTrackedEntityInstancesQuery(HashMap<String, String> attributesWithValuesMap, Map<String, TrackedEntityAttribute> trackedEntityAttributeMap) {
        Iterator<String> attributesIdsUsedInQueryIterator = attributesWithValuesMap.keySet().iterator();
        if (!attributesIdsUsedInQueryIterator.hasNext()) {
            return "SELECT * FROM " + TrackedEntityInstance.class.getSimpleName() + " WHERE " + "trackedEntityInstance" + " IN (SELECT " + Table.TRACKEDENTITYINSTANCEID + " FROM " + TrackedEntityAttributeValue.class.getSimpleName() + Expression.PAR_CLOSE;
        }
        String firstCompareOperator;
        String firstValue;
        String firstId = (String) attributesIdsUsedInQueryIterator.next();
        if (((TrackedEntityAttribute) trackedEntityAttributeMap.get(firstId)).getOptionSet() != null) {
            firstCompareOperator = "IS";
            firstValue = (String) attributesWithValuesMap.get(firstId);
        } else {
            firstCompareOperator = Operation.LIKE;
            firstValue = '%' + ((String) attributesWithValuesMap.get(firstId)) + '%';
        }
        String query = "SELECT * FROM " + TrackedEntityInstance.class.getSimpleName() + " WHERE " + "trackedEntityInstance" + " IN (SELECT " + Table.TRACKEDENTITYINSTANCEID + " FROM " + TrackedEntityAttributeValue.class.getSimpleName() + " WHERE " + Table.TRACKEDENTITYATTRIBUTEID + " IS '" + firstId + "' AND " + "value" + ' ' + firstCompareOperator + ' ' + "'" + firstValue + "')";
        while (attributesIdsUsedInQueryIterator.hasNext()) {
            String compareOperator;
            String attributeValue;
            String attributeId = (String) attributesIdsUsedInQueryIterator.next();
            if (((TrackedEntityAttribute) trackedEntityAttributeMap.get(attributeId)).getOptionSet() != null) {
                compareOperator = "IS";
                attributeValue = (String) attributesWithValuesMap.get(attributeId);
            } else {
                compareOperator = Operation.LIKE;
                attributeValue = '%' + ((String) attributesWithValuesMap.get(attributeId)) + '%';
            }
            query = query + "INTERSECT SELECT * FROM " + TrackedEntityInstance.class.getSimpleName() + " WHERE " + "trackedEntityInstance" + " IN (SELECT " + Table.TRACKEDENTITYINSTANCEID + " FROM " + TrackedEntityAttributeValue.class.getSimpleName() + " WHERE " + Table.TRACKEDENTITYATTRIBUTEID + " " + compareOperator + " '" + attributeId + "' AND " + "value" + ' ' + compareOperator + ' ' + "'" + attributeValue + "')";
        }
        return query + ';';
    }

    private Set<String> getFailedItemsForTrackedEntityInstances(Map<Long, TrackedEntityInstance> trackedEntityInstanceLocalIdToTeiMap) {
        Set<Long> trackedEntityLocalIdSet = trackedEntityInstanceLocalIdToTeiMap.keySet();
        Iterator<Long> idIterator = trackedEntityLocalIdSet.iterator();
        String trackedEntityInstanceLocalIdsString = "";
        for (int i = 0; i < trackedEntityLocalIdSet.size(); i++) {
            if (idIterator.hasNext()) {
                trackedEntityInstanceLocalIdsString = trackedEntityInstanceLocalIdsString + "" + idIterator.next();
                if (i < trackedEntityLocalIdSet.size() - 1) {
                    trackedEntityInstanceLocalIdsString = trackedEntityInstanceLocalIdsString + ',';
                }
            }
        }
        List<FailedItem> newFailedItems = new StringQuery(FailedItem.class, "SELECT * FROM " + FailedItem.class.getSimpleName() + " WHERE " + FailedItem.Table.ITEMTYPE + " IS '" + "TrackedEntityInstance" + "' AND " + FailedItem.Table.ITEMID + " IN (" + trackedEntityInstanceLocalIdsString + ");").queryList();
        Set<String> failedItemsForTrackedEntityInstances = new HashSet();
        for (FailedItem failedItem : newFailedItems) {
            TrackedEntityInstance trackedEntityInstance = (TrackedEntityInstance) trackedEntityInstanceLocalIdToTeiMap.get(Long.valueOf(failedItem.getItemId()));
            if (trackedEntityInstance == null) {
                failedItem.delete();
            } else if (failedItem.getHttpStatusCode() >= 0 && trackedEntityInstance.getTrackedEntityInstance() != null) {
                failedItemsForTrackedEntityInstances.add(trackedEntityInstance.getTrackedEntityInstance());
            }
        }
        return failedItemsForTrackedEntityInstances;
    }
}
