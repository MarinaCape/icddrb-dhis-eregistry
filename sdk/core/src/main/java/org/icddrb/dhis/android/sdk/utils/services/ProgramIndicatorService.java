package org.icddrb.dhis.android.sdk.utils.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import org.apache.commons.jexl2.JexlException;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.persistence.models.Constant;
import org.icddrb.dhis.android.sdk.persistence.models.DataElement;
import org.icddrb.dhis.android.sdk.persistence.models.DataValue;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramIndicator;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramStage;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramStageDataElement;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.icddrb.dhis.android.sdk.utils.api.ValueType;
import org.icddrb.dhis.android.sdk.utils.support.DateUtils;
import org.icddrb.dhis.android.sdk.utils.support.ExpressionUtils;
import org.icddrb.dhis.android.sdk.utils.support.MathUtils;
import org.icddrb.dhis.android.sdk.utils.support.TextUtils;
import org.icddrb.dhis.android.sdk.utils.support.expression.Expression;

public class ProgramIndicatorService {
    public static final String CLASS_TAG = ProgramIndicatorService.class.getSimpleName();
    private static final String NULL_REPLACEMENT = "null";

    public static String getProgramIndicatorValue(Enrollment programInstance, ProgramIndicator programIndicator) {
        if (programIndicator == null) {
            return null;
        }
        return TextUtils.fromDouble(getValue(programInstance, null, programIndicator));
    }

    public static String getProgramIndicatorValue(Event event, ProgramIndicator programIndicator) {
        if (programIndicator == null) {
            return null;
        }
        return TextUtils.fromDouble(getValue(null, event, programIndicator));
    }

    public static Map<String, String> getProgramIndicatorValues(Enrollment programInstance) {
        Map<String, String> result = new HashMap();
        for (ProgramIndicator programIndicator : programInstance.getProgram().getProgramIndicators()) {
            if (getProgramIndicatorValue(programInstance, programIndicator) != null) {
                result.put(programIndicator.getDisplayName(), getProgramIndicatorValue(programInstance, programIndicator));
            }
        }
        return result;
    }

    public static String getExpressionDescription(String expression) {
        StringBuffer description = new StringBuffer();
        Matcher matcher = ProgramIndicator.EXPRESSION_PATTERN.matcher(expression);
        while (matcher.find()) {
            String key = matcher.group(1);
            String uid = matcher.group(2);
            if ("#".equals(key)) {
                String de = matcher.group(3);
                ProgramStage programStage = MetaDataController.getProgramStage(uid);
                DataElement dataElement = MetaDataController.getDataElement(de);
                if (!(programStage == null || dataElement == null)) {
                    String programStageName = programStage.getDisplayName();
                    matcher.appendReplacement(description, programStageName + "\\." + dataElement.getDisplayName());
                }
            } else if ("A".equals(key)) {
                TrackedEntityAttribute attribute = MetaDataController.getTrackedEntityAttribute(uid);
                if (attribute != null) {
                    matcher.appendReplacement(description, attribute.getDisplayName());
                }
            } else if ("C".equals(key)) {
                Constant constant = MetaDataController.getConstant(uid);
                if (constant != null) {
                    matcher.appendReplacement(description, constant.getDisplayName());
                }
            } else if ("V".equals(key)) {
                if ("current_date".equals(uid)) {
                    matcher.appendReplacement(description, "Current date");
                } else if ("enrollment_date".equals(uid)) {
                    matcher.appendReplacement(description, "Enrollment date");
                } else if ("incident_date".equals(uid)) {
                    matcher.appendReplacement(description, "Incident date");
                } else if (ProgramIndicator.EVENT_DATE.equals(uid)) {
                    matcher.appendReplacement(description, "Event date");
                } else if ("value_count".equals(uid)) {
                    matcher.appendReplacement(description, "Value count");
                }
            }
        }
        matcher.appendTail(description);
        return description.toString();
    }

    public static String expressionIsValid(String expression) {
        StringBuffer description = new StringBuffer();
        Matcher matcher = ProgramIndicator.EXPRESSION_PATTERN.matcher(expression);
        while (matcher.find()) {
            String key = matcher.group(1);
            String uid = matcher.group(2);
            if ("#".equals(key)) {
                String de = matcher.group(3);
                ProgramStage programStage = MetaDataController.getProgramStage(uid);
                DataElement dataElement = MetaDataController.getDataElement(de);
                if (programStage == null || dataElement == null) {
                    return ProgramIndicator.EXPRESSION_NOT_WELL_FORMED;
                }
                matcher.appendReplacement(description, String.valueOf(1));
            } else if ("A".equals(key)) {
                if (MetaDataController.getTrackedEntityAttribute(uid) == null) {
                    return ProgramIndicator.EXPRESSION_NOT_WELL_FORMED;
                }
                matcher.appendReplacement(description, String.valueOf(1));
            } else if ("C".equals(key)) {
                Constant constant = MetaDataController.getConstant(uid);
                if (constant == null) {
                    return ProgramIndicator.EXPRESSION_NOT_WELL_FORMED;
                }
                matcher.appendReplacement(description, String.valueOf(constant.getValue()));
            } else if ("V".equals(key)) {
                matcher.appendReplacement(description, String.valueOf(0));
            }
        }
        matcher.appendTail(description);
        if (MathUtils.expressionHasErrors(description.toString())) {
            return ProgramIndicator.EXPRESSION_NOT_WELL_FORMED;
        }
        return ProgramIndicator.VALID;
    }

    public static Set<ProgramStageDataElement> getProgramStageDataElementsInExpression(ProgramIndicator indicator) {
        Set<ProgramStageDataElement> elements = new HashSet();
        Matcher matcher = ProgramIndicator.DATAELEMENT_PATTERN.matcher(indicator.getExpression());
        while (matcher.find()) {
            String ps = matcher.group(1);
            String de = matcher.group(2);
            ProgramStage programStage = MetaDataController.getProgramStage(ps);
            DataElement dataElement = MetaDataController.getDataElement(de);
            if (!(programStage == null || dataElement == null)) {
                elements.add(programStage.getProgramStageDataElement(dataElement.getUid()));
            }
        }
        return elements;
    }

    public static List<String> getDataElementsInExpression(ProgramIndicator indicator) {
        List<String> elements = new ArrayList();
        Matcher matcher = ProgramIndicator.DATAELEMENT_PATTERN.matcher(indicator.getExpression());
        while (matcher.find()) {
            String ps = matcher.group(1);
            elements.add(matcher.group(2));
        }
        return elements;
    }

    public static Set<TrackedEntityAttribute> getAttributesInExpression(ProgramIndicator indicator) {
        Set<TrackedEntityAttribute> attributes = new HashSet();
        Matcher matcher = ProgramIndicator.ATTRIBUTE_PATTERN.matcher(indicator.getExpression());
        while (matcher.find()) {
            TrackedEntityAttribute attribute = MetaDataController.getTrackedEntityAttribute(matcher.group(1));
            if (attribute != null) {
                attributes.add(attribute);
            }
        }
        return attributes;
    }

    private static Double getValue(Enrollment enrollmentProgramInstance, Event event, ProgramIndicator indicator) {
        StringBuffer buffer = new StringBuffer();
        Matcher matcher = ProgramIndicator.EXPRESSION_PATTERN.matcher(indicator.getExpression());
        int valueCount = 0;
        int zeroPosValueCount = 0;
        Event eventProgramStageInstance = null;
        Map<String, DataValue> dataElementToDataValues = new HashMap();
        while (matcher.find()) {
            String key = matcher.group(1);
            String uid = matcher.group(2);
            String value;
            if ("#".equals(key)) {
                String de = matcher.group(3);
                String programStageUid = uid;
                if (!(programStageUid == null || de == null)) {
                    DataValue dataValue;
                    if (enrollmentProgramInstance == null) {
                        if (eventProgramStageInstance == null) {
                            eventProgramStageInstance = event;
                            if (eventProgramStageInstance.getDataValues() != null) {
                                for (DataValue dataValue2 : eventProgramStageInstance.getDataValues()) {
                                    dataElementToDataValues.put(dataValue2.getDataElement(), dataValue2);
                                }
                            }
                        }
                    } else if (eventProgramStageInstance == null || !eventProgramStageInstance.getUid().equals(programStageUid)) {
                        eventProgramStageInstance = TrackerController.getEvent(enrollmentProgramInstance.getLocalId(), programStageUid);
                        if (eventProgramStageInstance != null) {
                            dataElementToDataValues.clear();
                            if (eventProgramStageInstance.getDataValues() != null) {
                                for (DataValue dataValue22 : eventProgramStageInstance.getDataValues()) {
                                    dataElementToDataValues.put(dataValue22.getDataElement(), dataValue22);
                                }
                            }
                        }
                    }
                    if (eventProgramStageInstance.getDataValues() != null) {
                        DataValue dataValue22 = (DataValue) dataElementToDataValues.get(de);
                        if (dataValue22 == null || dataValue22.getValue() == null || dataValue22.getValue().isEmpty()) {
                            value = "0";
                        } else {
                            if (MetaDataController.getDataElement(dataValue22.getDataElement()).getValueType() == ValueType.BOOLEAN) {
                                if (dataValue22.getValue().equals(BaseValue.TRUE)) {
                                    value = "1";
                                } else {
                                    value = "0";
                                }
                            } else if (dataValue22.getValue().endsWith(Expression.SEPARATOR)) {
                                value = dataValue22.getValue() + "0";
                            } else if (dataValue22.getValue().contains(Expression.SEPARATOR)) {
                                value = dataValue22.getValue();
                            } else {
                                value = dataValue22.getValue() + ".0";
                            }
                            valueCount++;
                            if (isZeroOrPositive(value)) {
                                zeroPosValueCount++;
                            }
                        }
                        matcher.appendReplacement(buffer, TextUtils.quote(value));
                    }
                }
            } else if ("A".equals(key)) {
                if (!(enrollmentProgramInstance == null || uid == null)) {
                    TrackedEntityAttributeValue attributeValue = TrackerController.getTrackedEntityAttributeValue(uid, enrollmentProgramInstance.getLocalTrackedEntityInstanceId());
                    if (attributeValue == null || attributeValue.getValue() == null || attributeValue.getValue().isEmpty()) {
                        value = NULL_REPLACEMENT;
                    } else {
                        value = attributeValue.getValue();
                        valueCount++;
                        if (isZeroOrPositive(value)) {
                            zeroPosValueCount++;
                        }
                    }
                    matcher.appendReplacement(buffer, TextUtils.quote(value));
                }
            } else if ("C".equals(key)) {
                Constant constant = MetaDataController.getConstant(uid);
                if (constant != null) {
                    matcher.appendReplacement(buffer, String.valueOf(constant.getValue()));
                }
            } else if ("V".equals(key) && enrollmentProgramInstance != null) {
                Date currentDate = new Date();
                Date date = null;
                if ("enrollment_date".equals(uid)) {
                    date = DateUtils.parseDate(enrollmentProgramInstance.getEnrollmentDate());
                } else if ("incident_date".equals(uid)) {
                    date = DateUtils.parseDate(enrollmentProgramInstance.getIncidentDate());
                } else if ("current_date".equals(uid)) {
                    date = currentDate;
                } else if (ProgramIndicator.EVENT_DATE.equals(uid) && enrollmentProgramInstance.getEvents().size() > 0) {
                    date = DateUtils.parseDate(((Event) enrollmentProgramInstance.getEvents().get(0)).getEventDate());
                }
                if (date != null) {
                    matcher.appendReplacement(buffer, TextUtils.quote(DateUtils.getMediumDateString(date)));
                }
            }
        }
        if (valueCount <= 0) {
            return null;
        }
        String expression = TextUtils.appendTail(matcher, buffer);
        buffer = new StringBuffer();
        matcher = ProgramIndicator.VALUECOUNT_PATTERN.matcher(expression);
        while (matcher.find()) {
            String var = matcher.group(1);
            if ("value_count".equals(var)) {
                matcher.appendReplacement(buffer, String.valueOf(valueCount));
            } else if ("zero_pos_value_count".equals(var)) {
                matcher.appendReplacement(buffer, String.valueOf(zeroPosValueCount));
            }
        }
        try {
            return ExpressionUtils.evaluateToDouble(TextUtils.appendTail(matcher, buffer), null);
        } catch (JexlException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalStateException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private static boolean isZeroOrPositive(String value) {
        return MathUtils.isNumeric(value) && Double.valueOf(value).doubleValue() >= 0.0d;
    }
}
