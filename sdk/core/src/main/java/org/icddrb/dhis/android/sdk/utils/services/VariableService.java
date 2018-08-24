package org.icddrb.dhis.android.sdk.utils.services;

import android.text.TextUtils;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.queriable.StringQuery;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.persistence.models.Constant;
import org.icddrb.dhis.android.sdk.persistence.models.DataElement;
import org.icddrb.dhis.android.sdk.persistence.models.DataValue;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.Option;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRuleVariable;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.utils.api.ContextVariableType;
import org.icddrb.dhis.android.sdk.utils.api.ProgramRuleVariableSourceType;
import org.icddrb.dhis.android.sdk.utils.api.ValueType;
import org.icddrb.dhis.android.sdk.utils.comparators.EventDateComparator;
import org.joda.time.DateTime;

public class VariableService {
    private static final String CLASS_TAG = VariableService.class.getSimpleName();
    private static VariableService variableService = new VariableService();
    private Enrollment currentEnrollment;
    private Event currentEvent;
    private Map<String, DataElement> dataElementMap;
    private Map<Event, Map<String, DataValue>> eventDataValueMaps;
    private List<Event> eventsForEnrollment;
    private Map<String, List<Event>> eventsForProgramStages;
    private Map<String, ProgramRuleVariable> programRuleVariableMap;
    private Map<String, TrackedEntityAttribute> trackedEntityAttributeMap;
    private Map<String, TrackedEntityAttributeValue> trackedEntityAttributeValueMap;

    public static VariableService getInstance() {
        return variableService;
    }

    public static void reset() {
        getInstance().programRuleVariableMap = null;
        getInstance().eventsForEnrollment = null;
        getInstance().currentEnrollment = null;
        getInstance().currentEvent = null;
        getInstance().dataElementMap = null;
        getInstance().trackedEntityAttributeMap = null;
        getInstance().eventsForEnrollment = null;
        getInstance().eventDataValueMaps = null;
        getInstance().trackedEntityAttributeValueMap = null;
    }

    public static void initialize(Enrollment enrollment, Event currentEvent) {
        getInstance().setCurrentEnrollment(enrollment);
        getInstance().setCurrentEvent(currentEvent);
        initEventsForEnrollment();
        updateListOfEventsWithCurrentEvent(currentEvent);
        List<Event> eventsForEnrollment = sortEventsForEnrollment();
        initDataElementsMap();
        initTrackedEntityMap();
        initEventsForProgramStages(eventsForEnrollment);
        initEventDataValueMaps(eventsForEnrollment);
        initTrackedEntityAttributeValuesMap(enrollment);
        initProgramRuleVariableMap();
    }

    private static void initEventsForEnrollment() {
        List<Event> events;
        if (getInstance().getCurrentEnrollment() != null) {
            events = getInstance().getCurrentEnrollment().getEvents();
        } else {
            events = new ArrayList();
        }
        getInstance().setEventsForEnrollment(events);
    }

    private static void updateListOfEventsWithCurrentEvent(Event currentEvent) {
        if (currentEvent != null) {
            List<Event> eventsForEnrollment = getInstance().getEventsForEnrollment();
            Event staleEvent = null;
            for (Event event : eventsForEnrollment) {
                if (event.getLocalId() == currentEvent.getLocalId()) {
                    staleEvent = event;
                    break;
                }
            }
            if (staleEvent != null) {
                int index = eventsForEnrollment.indexOf(staleEvent);
                eventsForEnrollment.remove(staleEvent);
                eventsForEnrollment.add(index, currentEvent);
                return;
            }
            eventsForEnrollment.add(currentEvent);
        }
    }

    private static List<Event> sortEventsForEnrollment() {
        List<Event> eventsForEnrollment = new ArrayList();
        if (getInstance().getEventsForEnrollment() == null) {
            return eventsForEnrollment;
        }
        Collections.sort(getInstance().getEventsForEnrollment(), new EventDateComparator());
        return getInstance().getEventsForEnrollment();
    }

    private static void initDataElementsMap() {
        List<DataElement> dataElements = MetaDataController.getDataElements();
        getInstance().setDataElementMap(new HashMap());
        for (DataElement dataElement : dataElements) {
            getInstance().getDataElementMap().put(dataElement.getUid(), dataElement);
        }
    }

    private static void initTrackedEntityMap() {
        List<TrackedEntityAttribute> trackedEntityAttributes = MetaDataController.getTrackedEntityAttributes();
        getInstance().setTrackedEntityAttributeMap(new HashMap());
        for (TrackedEntityAttribute trackedEntityAttribute : trackedEntityAttributes) {
            getInstance().getTrackedEntityAttributeMap().put(trackedEntityAttribute.getUid(), trackedEntityAttribute);
        }
    }

    private static void initEventsForProgramStages(List<Event> eventsForEnrollment) {
        getInstance().setEventsForProgramStages(new HashMap());
        for (Event event : eventsForEnrollment) {
            List<Event> eventsForProgramStage = (List) getInstance().getEventsForProgramStages().get(event.getProgramStageId());
            if (eventsForProgramStage == null) {
                eventsForProgramStage = new ArrayList();
                getInstance().getEventsForProgramStages().put(event.getProgramStageId(), eventsForProgramStage);
            }
            eventsForProgramStage.add(event);
        }
    }

    private static void initEventDataValueMaps(List<Event> eventsForEnrollment) {
        getInstance().setEventDataValueMaps(new HashMap());
        for (Event event : eventsForEnrollment) {
            Map<String, DataValue> dataValueMap = new HashMap();
            for (DataValue dataValue : event.getDataValues()) {
                DataValue copiedDataValue = new DataValue(dataValue);
                dataValueMap.put(copiedDataValue.getDataElement(), copiedDataValue);
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (String dataElement : dataValueMap.keySet()) {
                stringBuilder.append("'");
                stringBuilder.append(dataElement);
                stringBuilder.append("'");
                stringBuilder.append(',');
            }
            if (stringBuilder.length() > 0) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
            Class cls = DataElement.class;
            for (DataElement dataElementWithOptionSet : new StringQuery(cls, "SELECT * FROM " + DataElement.class.getSimpleName() + " WHERE " + "id" + " IN (" + stringBuilder.toString() + ") AND " + "optionSet" + " != '';").queryList()) {
                DataValue dataValueToReplaceCodeWithValue = (DataValue) dataValueMap.get(dataElementWithOptionSet.getUid());
                String optionSetCode = dataValueToReplaceCodeWithValue.getValue();
                Option option = (Option) new Select().from(Option.class).where(Condition.column("optionSet").eq(dataElementWithOptionSet.getOptionSet())).and(Condition.column("code").eq(optionSetCode)).querySingle();
                if (option != null) {
                    dataValueToReplaceCodeWithValue.setValue(option.getName());
                }
            }
            getInstance().getEventDataValueMaps().put(event, dataValueMap);
        }
    }

    private static void initTrackedEntityAttributeValuesMap(Enrollment enrollment) {
        Map<String, TrackedEntityAttributeValue> trackedEntityAttributeValueMap = new HashMap();
        getInstance().setTrackedEntityAttributeValueMap(trackedEntityAttributeValueMap);
        for (TrackedEntityAttributeValue trackedEntityAttributeValue : enrollment.getAttributes()) {
            TrackedEntityAttributeValue copiedTrackedEntityAttributeValue = new TrackedEntityAttributeValue(trackedEntityAttributeValue);
            trackedEntityAttributeValueMap.put(copiedTrackedEntityAttributeValue.getTrackedEntityAttributeId(), copiedTrackedEntityAttributeValue);
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String trackedEntityAttribute : trackedEntityAttributeValueMap.keySet()) {
            stringBuilder.append("'");
            stringBuilder.append(trackedEntityAttribute);
            stringBuilder.append("'");
            stringBuilder.append(',');
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        Class cls = TrackedEntityAttribute.class;
        for (TrackedEntityAttribute trackedEntityAttributeWithOptionSet : new StringQuery(cls, "SELECT * FROM " + TrackedEntityAttribute.class.getSimpleName() + " WHERE " + "id" + " IN (" + stringBuilder.toString() + ") AND " + "optionSet" + " != '';").queryList()) {
            TrackedEntityAttributeValue trackedEntityAttributeValueToReplaceCodeWithValue = (TrackedEntityAttributeValue) trackedEntityAttributeValueMap.get(trackedEntityAttributeWithOptionSet.getUid());
            String optionSetCode = trackedEntityAttributeValueToReplaceCodeWithValue.getValue();
            Option option = (Option) new Select().from(Option.class).where(Condition.column("optionSet").eq(trackedEntityAttributeWithOptionSet.getOptionSet())).and(Condition.column("code").eq(optionSetCode)).querySingle();
            if (option != null) {
                trackedEntityAttributeValueToReplaceCodeWithValue.setValue(option.getName());
            }
        }
    }

    private static Map<String, Option> getOptionsForOptionSet(String optionSetId) {
        List<Option> options = MetaDataController.getOptions(optionSetId);
        Map<String, Option> optionMap = new HashMap();
        if (options != null) {
            for (Option option : options) {
                optionMap.put(option.getCode(), option);
            }
        }
        return optionMap;
    }

    private static void initProgramRuleVariableMap() {
        List<ProgramRuleVariable> programRuleVariables = MetaDataController.getProgramRuleVariables();
        populateDataElementAndAttributeVariables(programRuleVariables);
        programRuleVariables.addAll(createContextVariables());
        programRuleVariables.addAll(createConstantVariables());
        Map<String, ProgramRuleVariable> programRuleVariableMap = new HashMap();
        for (ProgramRuleVariable programRuleVariable : programRuleVariables) {
            programRuleVariableMap.put(programRuleVariable.getName(), programRuleVariable);
        }
        getInstance();
        setProgramRuleVariableMap(programRuleVariableMap);
    }

    public Map<String, ProgramRuleVariable> getProgramRuleVariableMap() {
        return this.programRuleVariableMap;
    }

    public static void setProgramRuleVariableMap(Map<String, ProgramRuleVariable> programRuleVariableMap) {
        getInstance().programRuleVariableMap = programRuleVariableMap;
    }

    public List<Event> getEventsForEnrollment() {
        return this.eventsForEnrollment;
    }

    public void setEventsForEnrollment(List<Event> eventsForEnrollment) {
        this.eventsForEnrollment = eventsForEnrollment;
    }

    public Enrollment getCurrentEnrollment() {
        return this.currentEnrollment;
    }

    public void setCurrentEnrollment(Enrollment currentEnrollment) {
        this.currentEnrollment = currentEnrollment;
    }

    public Event getCurrentEvent() {
        return this.currentEvent;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }

    public Map<String, DataElement> getDataElementMap() {
        return this.dataElementMap;
    }

    public void setDataElementMap(Map<String, DataElement> dataElementMap) {
        this.dataElementMap = dataElementMap;
    }

    public Map<String, TrackedEntityAttribute> getTrackedEntityAttributeMap() {
        return this.trackedEntityAttributeMap;
    }

    public void setTrackedEntityAttributeMap(Map<String, TrackedEntityAttribute> trackedEntityAttributeMap) {
        this.trackedEntityAttributeMap = trackedEntityAttributeMap;
    }

    public Map<String, List<Event>> getEventsForProgramStages() {
        return this.eventsForProgramStages;
    }

    public void setEventsForProgramStages(Map<String, List<Event>> eventsForProgramStages) {
        this.eventsForProgramStages = eventsForProgramStages;
    }

    public Map<Event, Map<String, DataValue>> getEventDataValueMaps() {
        return this.eventDataValueMaps;
    }

    public void setEventDataValueMaps(Map<Event, Map<String, DataValue>> eventDataValueMaps) {
        this.eventDataValueMaps = eventDataValueMaps;
    }

    public Map<String, TrackedEntityAttributeValue> getTrackedEntityAttributeValueMap() {
        return this.trackedEntityAttributeValueMap;
    }

    public void setTrackedEntityAttributeValueMap(Map<String, TrackedEntityAttributeValue> trackedEntityAttributeValueMap) {
        this.trackedEntityAttributeValueMap = trackedEntityAttributeValueMap;
    }

    public static String processSingleValue(String processedValue, ValueType valueType) {
        switch (valueType) {
            case LONG_TEXT:
            case TEXT:
            case DATE:
                if (processedValue != null) {
                    return "'" + processedValue + "'";
                }
                return "''";
            case BOOLEAN:
            case TRUE_ONLY:
                if (processedValue == null || processedValue.isEmpty() || !processedValue.equals(BaseValue.TRUE)) {
                    return BaseValue.FALSE;
                }
                return processedValue;
            case INTEGER:
            case NUMBER:
            case INTEGER_POSITIVE:
            case INTEGER_NEGATIVE:
            case INTEGER_ZERO_OR_POSITIVE:
            case PERCENTAGE:
                if (processedValue != null) {
                    return String.valueOf(Double.valueOf(processedValue));
                }
                return "0";
            default:
                return processedValue;
        }
    }

    private static void populateDataElementAndAttributeVariables(List<ProgramRuleVariable> programRuleVariables) {
        for (ProgramRuleVariable programRuleVariable : programRuleVariables) {
            populateDataElementOrAttributeVariable(programRuleVariable);
        }
    }

    private static void populateDataElementOrAttributeVariable(ProgramRuleVariable programRuleVariable) {
        String value = null;
        String defaultValue = "";
        List<String> allValues = new ArrayList();
        if (programRuleVariable.getSourceType() != null) {
            DataValue dataValue;
            switch (programRuleVariable.getSourceType()) {
                case CONSTANT:
                case CALCULATED_VALUE:
                    break;
                case DATAELEMENT_NEWEST_EVENT_PROGRAM:
                    List<Event> eventsForEnrollment = new ArrayList();
                    if (getInstance().getEventsForEnrollment() != null) {
                        eventsForEnrollment = getInstance().getEventsForEnrollment();
                    }
                    for (Event event : eventsForEnrollment) {
                        dataValue = (DataValue) ((Map) getInstance().getEventDataValueMaps().get(event)).get(programRuleVariable.getDataElement());
                        if (dataValue != null) {
                            if (value == null) {
                                value = dataValue.getValue();
                            }
                            allValues.add(dataValue.getValue());
                        }
                    }
                    break;
                case DATAELEMENT_NEWEST_EVENT_PROGRAM_STAGE:
                    dataValue = null;
                    String programStage = programRuleVariable.getProgramStage();
                    if (programStage != null) {
                        List<Event> eventsForProgramStage = (List) getInstance().getEventsForProgramStages().get(programStage);
                        if (eventsForProgramStage != null) {
                            for (Event event2 : eventsForProgramStage) {
                                dataValue = (DataValue) ((Map) getInstance().getEventDataValueMaps().get(event2)).get(programRuleVariable.getDataElement());
                                if (dataValue != null) {
                                    if (value == null) {
                                        value = dataValue.getValue();
                                    }
                                    allValues.add(dataValue.getValue());
                                }
                            }
                            if (dataValue == null) {
                                defaultValue = getDefaultValue(((DataElement) getInstance().getDataElementMap().get(programRuleVariable.getDataElement())).getValueType());
                                break;
                            } else {
                                value = dataValue.getValue();
                                break;
                            }
                        }
                    }
                    break;
                case DATAELEMENT_PREVIOUS_EVENT:
                    if (getInstance().getCurrentEvent() != null) {
                        Comparator<Event> comparator = new EventDateComparator();
                        List<Event> sortedEvents = getInstance().getEventsForEnrollment();
                        Collections.sort(sortedEvents, comparator);
                        Collections.reverse(sortedEvents);
                        for (Event event22 : sortedEvents) {
                            if (!event22.getUid().equals(getInstance().getCurrentEvent().getUid())) {
                                event22.getUid();
                                getInstance().getCurrentEvent().getUid();
                                if (comparator.compare(getInstance().getCurrentEvent(), event22) > 0) {
                                    dataValue = (DataValue) ((Map) getInstance().getEventDataValueMaps().get(event22)).get(programRuleVariable.getDataElement());
                                    if (dataValue != null) {
                                        if (value == null) {
                                            value = dataValue.getValue();
                                        }
                                        allValues.add(dataValue.getValue());
                                    }
                                }
                            }
                        }
                        break;
                    }
                    break;
                case TEI_ATTRIBUTE:
                    if (!(getInstance().getCurrentEnrollment() == null || programRuleVariable.getTrackedEntityAttribute() == null)) {
                        TrackedEntityAttributeValue trackedEntityAttributeValue = (TrackedEntityAttributeValue) getInstance().getTrackedEntityAttributeValueMap().get(programRuleVariable.getTrackedEntityAttribute());
                        if (trackedEntityAttributeValue != null) {
                            value = trackedEntityAttributeValue.getValue();
                            allValues.add(value);
                            break;
                        }
                    }
                    break;
                default:
                    if (getInstance().getCurrentEvent() != null) {
                        dataValue = (DataValue) ((Map) getInstance().getEventDataValueMaps().get(getInstance().getCurrentEvent())).get(programRuleVariable.getDataElement());
                        if (dataValue != null) {
                            value = dataValue.getValue();
                            allValues.add(value);
                            break;
                        }
                    }
                    break;
            }
            boolean hasVal = false;
            for (String val : allValues) {
                if (!TextUtils.isEmpty(val)) {
                    hasVal = true;
                }
            }
            if (hasVal) {
                programRuleVariable.setHasValue(true);
            } else {
                programRuleVariable.setHasValue(false);
                value = defaultValue;
            }
            programRuleVariable.setVariableValue(value);
            programRuleVariable.setAllValues(allValues);
        }
    }

    private static List<ProgramRuleVariable> createConstantVariables() {
        List<Constant> constants = MetaDataController.getConstants();
        List<ProgramRuleVariable> programRuleVariables = new ArrayList();
        for (Constant constant : constants) {
            programRuleVariables.add(createConstantVariable(constant));
        }
        return programRuleVariables;
    }

    private static ProgramRuleVariable createConstantVariable(Constant constant) {
        if (constant == null) {
            return null;
        }
        ProgramRuleVariable programRuleVariable = new ProgramRuleVariable();
        programRuleVariable.setVariableValue(Double.toString(constant.getValue()));
        programRuleVariable.setHasValue(true);
        programRuleVariable.setName(constant.getName());
        programRuleVariable.setSourceType(ProgramRuleVariableSourceType.CONSTANT);
        return programRuleVariable;
    }

    private static List<ProgramRuleVariable> createContextVariables() {
        List<ProgramRuleVariable> programRuleVariables = new ArrayList();
        List<Event> eventsForEnrollment = new ArrayList();
        if (getInstance().getEventsForEnrollment() != null) {
            eventsForEnrollment = getInstance().getEventsForEnrollment();
        }
        for (ContextVariableType type : ContextVariableType.values()) {
            programRuleVariables.add(createContextVariable(type.toString(), getInstance().getCurrentEvent(), getInstance().getCurrentEnrollment(), eventsForEnrollment));
        }
        return programRuleVariables;
    }

    private static ProgramRuleVariable createContextVariable(String variableName, Event executingEvent, Enrollment executingEnrollment, List<Event> events) {
        ContextVariableType contextVariableType = ContextVariableType.fromValue(variableName);
        ProgramRuleVariable programRuleVariable = new ProgramRuleVariable();
        programRuleVariable.setName(variableName);
        programRuleVariable.setSourceType(ProgramRuleVariableSourceType.CALCULATED_VALUE);
        switch (contextVariableType) {
            case CURRENT_DATE:
                programRuleVariable.setVariableValue(DateTime.now().toString());
                programRuleVariable.setHasValue(true);
                programRuleVariable.setVariableType(ValueType.DATE);
                break;
            case EVENT_DATE:
                if (executingEvent == null) {
                    programRuleVariable.setHasValue(false);
                    programRuleVariable.setVariableValue("");
                    programRuleVariable.setVariableType(ValueType.DATE);
                    break;
                }
                programRuleVariable.setVariableValue(executingEvent.getEventDate());
                programRuleVariable.setHasValue(true);
                programRuleVariable.setVariableType(ValueType.DATE);
                break;
            case DUE_DATE:
                if (executingEvent == null) {
                    programRuleVariable.setHasValue(false);
                    programRuleVariable.setVariableValue("");
                    programRuleVariable.setVariableType(ValueType.DATE);
                    break;
                }
                programRuleVariable.setVariableValue(executingEvent.getDueDate());
                programRuleVariable.setHasValue(true);
                programRuleVariable.setVariableType(ValueType.DATE);
                break;
            case EVENT_COUNT:
                programRuleVariable.setVariableValue(Integer.toString(events.size()));
                programRuleVariable.setHasValue(true);
                programRuleVariable.setVariableType(ValueType.INTEGER);
                break;
            case ENROLLMENT_DATE:
                if (executingEnrollment == null) {
                    programRuleVariable.setHasValue(false);
                    programRuleVariable.setVariableValue("");
                    programRuleVariable.setVariableType(ValueType.DATE);
                    break;
                }
                programRuleVariable.setVariableValue(executingEnrollment.getEnrollmentDate());
                programRuleVariable.setHasValue(true);
                programRuleVariable.setVariableType(ValueType.DATE);
                break;
            case ENROLLMENT_ID:
                if (executingEnrollment == null) {
                    programRuleVariable.setHasValue(false);
                    programRuleVariable.setVariableValue("");
                    programRuleVariable.setVariableType(ValueType.TEXT);
                    break;
                }
                programRuleVariable.setVariableValue(executingEnrollment.getEnrollment());
                programRuleVariable.setHasValue(true);
                programRuleVariable.setVariableType(ValueType.TEXT);
                break;
            case EVENT_ID:
                if (executingEvent == null) {
                    programRuleVariable.setHasValue(false);
                    programRuleVariable.setVariableValue("");
                    programRuleVariable.setVariableType(ValueType.TEXT);
                    break;
                }
                programRuleVariable.setVariableValue(executingEvent.getEvent());
                programRuleVariable.setHasValue(true);
                programRuleVariable.setVariableType(ValueType.TEXT);
                break;
            case INCIDENT_DATE:
                if (executingEnrollment == null) {
                    programRuleVariable.setHasValue(false);
                    programRuleVariable.setVariableValue("");
                    programRuleVariable.setVariableType(ValueType.DATE);
                    break;
                }
                programRuleVariable.setVariableValue(executingEnrollment.getIncidentDate());
                programRuleVariable.setHasValue(true);
                programRuleVariable.setVariableType(ValueType.DATE);
                break;
            case ENROLLMENT_COUNT:
                if (executingEnrollment == null) {
                    programRuleVariable.setVariableValue("0");
                    programRuleVariable.setHasValue(false);
                    programRuleVariable.setVariableType(ValueType.INTEGER);
                    break;
                }
                programRuleVariable.setVariableValue("1");
                programRuleVariable.setHasValue(true);
                programRuleVariable.setVariableType(ValueType.INTEGER);
                break;
            case TEI_COUNT:
                if (executingEnrollment == null) {
                    programRuleVariable.setVariableValue("0");
                    programRuleVariable.setHasValue(false);
                    programRuleVariable.setVariableType(ValueType.INTEGER);
                    break;
                }
                programRuleVariable.setVariableValue("1");
                programRuleVariable.setHasValue(true);
                programRuleVariable.setVariableType(ValueType.INTEGER);
                break;
        }
        return programRuleVariable;
    }

    public static String getReplacementForProgramRuleVariable(String variableName) {
        return retrieveVariableValue((ProgramRuleVariable) getInstance().getProgramRuleVariableMap().get(variableName));
    }

    public static String retrieveVariableValue(ProgramRuleVariable programRuleVariable) {
        String defaultValue = "";
        if (programRuleVariable == null) {
            return defaultValue;
        }
        String value = programRuleVariable.getVariableValue();
        if (TextUtils.isEmpty(value)) {
            value = defaultValue;
        }
        return value;
    }

    public static String getDefaultValue(ValueType valueType) {
        switch (valueType) {
            case LONG_TEXT:
            case TEXT:
            case DATE:
            case DATETIME:
                return "";
            case BOOLEAN:
            case TRUE_ONLY:
                return BaseValue.FALSE;
            case INTEGER:
            case NUMBER:
            case INTEGER_POSITIVE:
            case INTEGER_NEGATIVE:
            case INTEGER_ZERO_OR_POSITIVE:
            case PERCENTAGE:
            case UNIT_INTERVAL:
                return "0";
            default:
                return "";
        }
    }
}
