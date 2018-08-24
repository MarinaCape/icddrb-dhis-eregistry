package org.icddrb.dhis.android.sdk.utils.services;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.jexl2.JexlException;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRule;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRuleAction;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRuleVariable;
import org.icddrb.dhis.android.sdk.utils.api.ProgramRuleActionType;
import org.icddrb.dhis.android.sdk.utils.support.ExpressionUtils;
import org.icddrb.dhis.android.sdk.utils.support.TextUtils;
import org.icddrb.dhis.android.sdk.utils.support.expression.Expression;

public class ProgramRuleService {
    private static final String CLASS_TAG = ProgramRuleService.class.getSimpleName();
    private static final Pattern CONDITION_PATTERN = Pattern.compile("([#AV])\\{(.+?)\\}");
    private static final Pattern CONDITION_PATTERN_SINGLE_QUOTES = Pattern.compile("'([^' ]+)'");
    private static ProgramRuleService programRuleService = new ProgramRuleService();

    public static ProgramRuleService getInstance() {
        return programRuleService;
    }

    public static ProgramRuleService getProgramRuleService() {
        return programRuleService;
    }

    public static void setProgramRuleService(ProgramRuleService programRuleService) {
        programRuleService = programRuleService;
    }

    public static boolean evaluate(String condition) {
        boolean isTrue = false;
        try {
            isTrue = ExpressionUtils.isTrue(getReplacedCondition(condition), null);
        } catch (JexlException jxlException) {
            System.out.println("Norway - condition: " + condition);
            jxlException.printStackTrace();
        }
        return isTrue;
    }

    public static String getReplacedCondition(String condition) {
        if (condition == null) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        Matcher matcher = CONDITION_PATTERN.matcher(condition);
        while (matcher.find()) {
            String variablePrefix = matcher.group(1);
            String value = VariableService.getReplacementForProgramRuleVariable(matcher.group(2));
            if (isNumericAndStartsWithDecimalSeparator(value)) {
                value = String.format("0%s", new Object[]{value});
            } else if (isNumericAndEndsWithDecimalSeparator(value)) {
                value = String.format("%s0", new Object[]{value});
            }
            if (!(ExpressionUtils.isNumeric(value) || ExpressionUtils.isBoolean(value))) {
                value = '\'' + value + '\'';
            }
            matcher.appendReplacement(buffer, value);
        }
        return TextUtils.appendTail(matcher, buffer);
    }

    private static boolean isNumericAndStartsWithDecimalSeparator(String value) {
        return value.startsWith(Expression.SEPARATOR) && ExpressionUtils.isNumeric(value.substring(1, value.length()));
    }

    private static boolean isNumericAndEndsWithDecimalSeparator(String value) {
        return value.endsWith(Expression.SEPARATOR) && ExpressionUtils.isNumeric(value.substring(0, value.length() - 1));
    }

    public static String getCalculatedConditionValue(String condition) {
        System.out.println("Norway - Condition: " + condition);
        return String.valueOf(ExpressionUtils.evaluate(getReplacedCondition(condition), null));
    }

    public static List<String> getDataElementsInRule(ProgramRule programRule) {
        String condition = programRule.getCondition();
        List<String> dataElementsInRule = getDataElementsInSingleQuotes(condition);
        Matcher matcher = CONDITION_PATTERN.matcher(condition);
        while (matcher.find()) {
            ProgramRuleVariable programRuleVariable = MetaDataController.getProgramRuleVariableByName(matcher.group(2));
            if (!(programRuleVariable == null || programRuleVariable.getDataElement() == null)) {
                dataElementsInRule.add(programRuleVariable.getDataElement());
            }
        }
        for (ProgramRuleAction programRuleAction : programRule.getProgramRuleActions()) {
            if (programRuleAction.getProgramRuleActionType().equals(ProgramRuleActionType.ASSIGN) && programRuleAction.getContent() != null) {
                programRuleVariable = (ProgramRuleVariable) VariableService.getInstance().getProgramRuleVariableMap().get(programRuleAction.getContent().substring(2, programRuleAction.getContent().length() - 1));
                if (!(programRuleVariable == null || programRuleVariable.getDataElement() == null)) {
                    dataElementsInRule.add(programRuleVariable.getDataElement());
                }
            }
            if (programRuleAction.getDataElement() != null) {
                dataElementsInRule.add(programRuleAction.getDataElement());
            }
        }
        return dataElementsInRule;
    }

    private static List<String> getDataElementsInSingleQuotes(String condition) {
        List<String> dataElementsInRule = new ArrayList();
        Matcher matcher = CONDITION_PATTERN_SINGLE_QUOTES.matcher(condition);
        while (matcher.find()) {
            ProgramRuleVariable programRuleVariable = MetaDataController.getProgramRuleVariableByName(matcher.group(1));
            if (!(programRuleVariable == null || programRuleVariable.getDataElement() == null)) {
                dataElementsInRule.add(programRuleVariable.getDataElement());
            }
        }
        return dataElementsInRule;
    }

    public static List<String> getTrackedEntityAttributesInRule(ProgramRule programRule) {
        String condition = programRule.getCondition();
        List<String> trackedEntityAttributesInRule = getTrackedEntityAttributesInSingleQuotes(condition);
        Matcher matcher = CONDITION_PATTERN.matcher(condition);
        while (matcher.find()) {
            ProgramRuleVariable programRuleVariable = MetaDataController.getProgramRuleVariableByName(matcher.group(2));
            if (!(programRuleVariable == null || programRuleVariable.getTrackedEntityAttribute() == null)) {
                trackedEntityAttributesInRule.add(programRuleVariable.getTrackedEntityAttribute());
            }
        }
        for (ProgramRuleAction programRuleAction : programRule.getProgramRuleActions()) {
            if (programRuleAction.getProgramRuleActionType().equals(ProgramRuleActionType.ASSIGN) && programRuleAction.getContent() != null) {
                programRuleVariable = (ProgramRuleVariable) VariableService.getInstance().getProgramRuleVariableMap().get(programRuleAction.getContent().substring(2, programRuleAction.getContent().length() - 1));
                if (!(programRuleVariable == null || programRuleVariable.getTrackedEntityAttribute() == null)) {
                    trackedEntityAttributesInRule.add(programRuleVariable.getTrackedEntityAttribute());
                }
            }
        }
        return trackedEntityAttributesInRule;
    }

    private static List<String> getTrackedEntityAttributesInSingleQuotes(String condition) {
        List<String> trackedEntityAttributesInRule = new ArrayList();
        Matcher matcher = CONDITION_PATTERN_SINGLE_QUOTES.matcher(condition);
        while (matcher.find()) {
            ProgramRuleVariable programRuleVariable = MetaDataController.getProgramRuleVariableByName(matcher.group(1));
            if (!(programRuleVariable == null || programRuleVariable.getTrackedEntityAttribute() == null)) {
                trackedEntityAttributesInRule.add(programRuleVariable.getTrackedEntityAttribute());
            }
        }
        return trackedEntityAttributesInRule;
    }
}
