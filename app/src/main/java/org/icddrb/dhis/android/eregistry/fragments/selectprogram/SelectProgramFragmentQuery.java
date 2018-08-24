package org.icddrb.dhis.android.eregistry.fragments.selectprogram;

import android.content.Context;
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
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.FailedItem;
import org.icddrb.dhis.android.sdk.persistence.models.Option;
import org.icddrb.dhis.android.sdk.persistence.models.OptionSet;
import org.icddrb.dhis.android.sdk.persistence.models.Program;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramStage;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramTrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue.Table;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.EventRow;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.TrackedEntityInstanceDynamicColumnRows;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.TrackedEntityInstanceItemRow;
import org.icddrb.dhis.android.sdk.ui.fragments.selectprogram.SelectProgramFragmentForm;
import org.icddrb.dhis.android.sdk.utils.ScreenSizeConfigurator;
import org.icddrb.dhis.android.sdk.utils.support.expression.Expression;

public class SelectProgramFragmentQuery implements Query<SelectProgramFragmentForm> {
    private final String mOrgUnitId;
    private final String mProgramId;

    public SelectProgramFragmentQuery(String orgUnitId, String programId) {
        this.mOrgUnitId = orgUnitId;
        this.mProgramId = programId;
    }

    public SelectProgramFragmentForm query(Context context) {
        SelectProgramFragmentForm fragmentForm = new SelectProgramFragmentForm();
        List<EventRow> teiRows = new ArrayList();
        Program selectedProgram = MetaDataController.getProgram(this.mProgramId);
        if (!(selectedProgram == null || isListEmpty(selectedProgram.getProgramStages()))) {
            fragmentForm.setProgram(selectedProgram);
            ProgramStage programStage = (ProgramStage) selectedProgram.getProgramStages().get(0);
            if (!(programStage == null || isListEmpty(programStage.getProgramStageDataElements()))) {
                List<ProgramTrackedEntityAttribute> attributes = selectedProgram.getProgramTrackedEntityAttributes();
                if (!isListEmpty(attributes)) {
                    List<String> attributesToShow = new ArrayList();
                    Map<String, TrackedEntityAttribute> attributesToShowMap = new HashMap();
                    TrackedEntityInstanceDynamicColumnRows columnNames = new TrackedEntityInstanceDynamicColumnRows();
                    TrackedEntityInstanceDynamicColumnRows attributeNames = new TrackedEntityInstanceDynamicColumnRows();
                    for (ProgramTrackedEntityAttribute attribute : attributes) {
                        if (attribute.getDisplayInList() && attributesToShow.size() < ScreenSizeConfigurator.getInstance().getFields()) {
                            attributesToShow.add(attribute.getTrackedEntityAttributeId());
                            if (attribute.getTrackedEntityAttribute() != null) {
                                String name = attribute.getTrackedEntityAttribute().getName();
                                if (attributesToShow.size() <= ScreenSizeConfigurator.getInstance().getFields()) {
                                    columnNames.addColumn(name);
                                    attributeNames.addColumn(attribute.getTrackedEntityAttribute().getShortName());
                                }
                                attributesToShowMap.put(attribute.getTrackedEntityAttributeId(), attribute.getTrackedEntityAttribute());
                            }
                        }
                    }
                    teiRows.add(columnNames);
                    if (selectedProgram.isDisplayFrontPageList()) {
                        String query = getTrackedEntityInstancesWithEnrollmentQuery(this.mOrgUnitId, this.mProgramId);
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
                            Map<String, Map<String, Option>> optionsForOptionSetMap = getCachedOptionsForOptionSets(attributesToShowMap);
                            Map<Long, Map<String, TrackedEntityAttributeValue>> cachedTrackedEntityAttributeValuesForTrackedEntityInstances = getCachedTrackedEntityAttributeValuesForTrackedEntityInstances(attributesToShow, resultTrackedEntityInstances);
                            for (TrackedEntityInstance trackedEntityInstance2 : resultTrackedEntityInstances) {
                                if (trackedEntityInstance2 != null) {
                                    teiRows.add(createTrackedEntityInstanceItem(context, trackedEntityInstance2, attributesToShow, attributesToShowMap, failedItemsForTrackedEntityInstances, cachedTrackedEntityAttributeValuesForTrackedEntityInstances, optionsForOptionSetMap));
                                }
                            }
                            fragmentForm.setEventRowList(teiRows);
                            fragmentForm.setColumnNames(columnNames);
                            fragmentForm.setColumnNames(attributeNames);
                            if (selectedProgram.getTrackedEntityType() != null) {
                                columnNames.setTrackedEntity(selectedProgram.getTrackedEntityType().getName());
                                columnNames.setTitle(selectedProgram.getTrackedEntityType().getName() + " (" + (teiRows.size() - 1) + Expression.PAR_CLOSE);
                            }
                        }
                    }
                }
            }
        }
        return fragmentForm;
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
        int i = 0;
        while (i < attributesToShow.size() && i <= attributesToShow.size()) {
            String attributeUid = (String) attributesToShow.get(i);
            if (attributeUid != null) {
                TrackedEntityAttributeValue teav = null;
                if (trackedEntityAttributeValueMapForTrackedEntityInstance != null) {
                    teav = (TrackedEntityAttributeValue) trackedEntityAttributeValueMapForTrackedEntityInstance.get(attributeUid);
                }
                TrackedEntityAttribute trackedEntityAttribute = (TrackedEntityAttribute) trackedEntityAttributeMap.get(attributeUid);
                if (teav == null || trackedEntityAttribute == null) {
                    trackedEntityInstanceItemRow.addColumn("");
                } else {
                    String value = teav.getValue();
                    if (trackedEntityAttribute.isOptionSetValue()) {
                        if (trackedEntityAttribute.getOptionSet() == null) {
                            trackedEntityInstanceItemRow.addColumn("");
                        } else {
                            Map<String, Option> optionsMap = (Map) optionsForOptionSetMap.get(trackedEntityAttribute.getOptionSet());
                            if (optionsMap == null) {
                                trackedEntityInstanceItemRow.addColumn("");
                            } else {
                                Option optionWithMatchingValue = (Option) optionsMap.get(value);
                                if (optionWithMatchingValue != null) {
                                    value = optionWithMatchingValue.getName();
                                }
                            }
                        }
                    }
                    trackedEntityInstanceItemRow.addColumn(value);
                }
            }
            i++;
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

    private String getTrackedEntityInstancesWithEnrollmentQuery(String organisationUnitId, String programId) {
        return "SELECT * FROM " + TrackedEntityInstance.class.getSimpleName() + " t1 JOIN ( SELECT DISTINCT " + "localTrackedEntityInstanceId" + ", " + "lastUpdated" + " FROM " + Enrollment.class.getSimpleName() + " WHERE " + "program" + " IS '" + programId + "' AND " + "orgUnit" + " IS '" + organisationUnitId + "') t2 ON t1." + "localId" + "=t2." + "localTrackedEntityInstanceId" + " GROUP BY t1." + "localId" + "  ORDER BY t2." + "lastUpdated" + " ASC";
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

    private static <T> boolean isListEmpty(List<T> items) {
        return items == null || items.isEmpty();
    }
}
