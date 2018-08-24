package org.icddrb.dhis.android.sdk.utils.support.math;

import android.text.TextUtils;
import java.text.ParseException;
import java.util.regex.Pattern;
import org.apache.commons.lang3.time.DateUtils;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRuleVariable;
import org.icddrb.dhis.android.sdk.persistence.models.UserCredentials;
import org.icddrb.dhis.android.sdk.utils.services.VariableService;
import org.icddrb.dhis.android.sdk.utils.support.ExpressionUtils;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.Years;

public class ExpressionFunctions {
    public static final String CLASS_TAG = ExpressionFunctions.class.getSimpleName();
    public static final String NAMESPACE = "d2";

    public static Double zing(Number value) {
        if (value == null) {
            return null;
        }
        return Double.valueOf(Math.max(0.0d, value.doubleValue()));
    }

    public static Double oizp(Number value) {
        double d = 0.0d;
        if (value == null) {
            return null;
        }
        if (value.doubleValue() >= 0.0d) {
            d = 1.0d;
        }
        return Double.valueOf(d);
    }

    public static Integer zpvc(Number... values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Argument is null or empty: " + values);
        }
        int count = 0;
        for (Number value : values) {
            if (value != null && value.doubleValue() >= 0.0d) {
                count++;
            }
        }
        return Integer.valueOf(count);
    }

    public static Object condition(String condititon, Object trueValue, Object falseValue) {
        return ExpressionUtils.isTrue(condititon, null) ? trueValue : falseValue;
    }

    public static Integer daysBetween(String start, String end) throws ParseException {
        if (TextUtils.isEmpty(start) || TextUtils.isEmpty(end)) {
            return Integer.valueOf(0);
        }
        return Integer.valueOf(new Long((new DateTime((Object) end).getMillis() - new DateTime((Object) start).getMillis()) / DateUtils.MILLIS_PER_DAY).intValue());
    }

    public static Integer weeksBetween(String start, String end) throws ParseException {
        if (TextUtils.isEmpty(start) || TextUtils.isEmpty(end)) {
            return Integer.valueOf(0);
        }
        return Integer.valueOf(new Long((new DateTime((Object) end).getMillis() - new DateTime((Object) start).getMillis()) / 604800000).intValue());
    }

    public static Integer monthsBetween(String start, String end) throws ParseException {
        if (TextUtils.isEmpty(start) || TextUtils.isEmpty(end)) {
            return Integer.valueOf(0);
        }
        return Integer.valueOf(Months.monthsBetween(new DateTime((Object) start).withDayOfMonth(1), new DateTime((Object) end).withDayOfMonth(1)).getMonths());
    }

    public static Integer yearsBetween(String start, String end) {
        if (TextUtils.isEmpty(start) || TextUtils.isEmpty(end)) {
            return Integer.valueOf(0);
        }
        return Integer.valueOf(Years.yearsBetween(new DateTime((Object) start).withDayOfMonth(1).withMonthOfYear(1), new DateTime((Object) end).withDayOfMonth(1).withMonthOfYear(1)).getYears());
    }

    public static Integer floor(Number value) {
        if (value != null) {
            return Integer.valueOf(new Double(Math.floor(value.doubleValue())).intValue());
        }
        throw new IllegalArgumentException();
    }

    public static Integer modulus(Number dividend, Number divisor) {
        if (dividend != null && divisor != null) {
            return Integer.valueOf(dividend.intValue() % divisor.intValue());
        }
        throw new IllegalArgumentException();
    }

    public static String concatenate(Object... values) {
        String returnString = "";
        for (Object value : values) {
            returnString = returnString + String.valueOf(value);
        }
        return returnString;
    }

    public static String addDays(String date, Number daysToAdd) {
        if (date != null && daysToAdd != null) {
            return org.icddrb.dhis.android.sdk.utils.support.DateUtils.getMediumDateString(new DateTime((Object) date).plusDays(daysToAdd.intValue()).toDate());
        }
        throw new IllegalArgumentException();
    }

    public static String addDays(String date, String daysToAdd) {
        Number n = (daysToAdd == null || daysToAdd.isEmpty()) ? null : Integer.valueOf(daysToAdd);
        return addDays(date, n);
    }

    public static Integer count(String variableName) {
        ProgramRuleVariable programRuleVariable = (ProgramRuleVariable) VariableService.getInstance().getProgramRuleVariableMap().get(variableName);
        Integer count = Integer.valueOf(0);
        if (programRuleVariable == null || !programRuleVariable.isHasValue()) {
            return count;
        }
        if (programRuleVariable.getAllValues() != null) {
            return Integer.valueOf(programRuleVariable.getAllValues().size());
        }
        return Integer.valueOf(1);
    }

    public static Integer countIfZeroPos(String variableName) {
        ProgramRuleVariable programRuleVariable = (ProgramRuleVariable) VariableService.getInstance().getProgramRuleVariableMap().get(variableName);
        Integer count = Integer.valueOf(0);
        if (programRuleVariable == null || !programRuleVariable.isHasValue()) {
            return count;
        }
        Double value;
        if (programRuleVariable.getAllValues() == null || programRuleVariable.getAllValues().size() <= 0) {
            value = getVariableValue(programRuleVariable, programRuleVariable.getVariableValue());
            if (value == null || value.doubleValue() < 0.0d) {
                return count;
            }
            return Integer.valueOf(1);
        }
        for (int i = 0; i < programRuleVariable.getAllValues().size(); i++) {
            value = getVariableValue(programRuleVariable, (String) programRuleVariable.getAllValues().get(i));
            if (value != null && value.doubleValue() >= 0.0d) {
                count = Integer.valueOf(count.intValue() + 1);
            }
        }
        return count;
    }

    public static Integer countIfValue(String variableName, String textToCompare) {
        ProgramRuleVariable programRuleVariable = (ProgramRuleVariable) VariableService.getInstance().getProgramRuleVariableMap().get(variableName);
        Integer count = Integer.valueOf(0);
        if (programRuleVariable == null || !programRuleVariable.isHasValue()) {
            return count;
        }
        if (programRuleVariable.getAllValues() != null) {
            for (int i = 0; i < programRuleVariable.getAllValues().size(); i++) {
                if (textToCompare.equals(programRuleVariable.getAllValues().get(i))) {
                    count = Integer.valueOf(count.intValue() + 1);
                }
            }
            return count;
        } else if (textToCompare.equals(programRuleVariable.getVariableValue())) {
            return Integer.valueOf(1);
        } else {
            return count;
        }
    }

    public static Double ceil(double value) {
        return Double.valueOf(Math.ceil(value));
    }

    public static Long round(double value) {
        return Long.valueOf(Math.round(value));
    }

    public static Boolean hasValue(String variableName) {
        ProgramRuleVariable programRuleVariable = (ProgramRuleVariable) VariableService.getInstance().getProgramRuleVariableMap().get(variableName);
        boolean valueFound = false;
        if (programRuleVariable != null && programRuleVariable.isHasValue()) {
            valueFound = true;
        }
        return Boolean.valueOf(valueFound);
    }

    public static String lastEventDate(String variableName) {
        ProgramRuleVariable programRuleVariable = (ProgramRuleVariable) VariableService.getInstance().getProgramRuleVariableMap().get(variableName);
        String valueFound = "";
        if (programRuleVariable == null || programRuleVariable.getVariableEventDate() == null) {
            return valueFound;
        }
        return programRuleVariable.getVariableEventDate();
    }

    public static Boolean validatePattern(String inputToValidate, String patternString) {
        return Boolean.valueOf(Pattern.compile(patternString).matcher(inputToValidate).matches());
    }

    public static Boolean validatePattern(long inputToValidate, String patternString) {
        return validatePattern(Long.toString(inputToValidate), patternString);
    }

    public static String left(String inputString, int length) {
        if (inputString == null) {
            return "";
        }
        return inputString.substring(0, Math.min(Math.max(0, length), inputString.length()));
    }

    public static String right(String inputString, int length) {
        if (inputString == null) {
            return "";
        }
        return inputString.substring(inputString.length() - Math.min(Math.max(0, length), inputString.length()));
    }

    public static Integer length(String inputString) {
        return Integer.valueOf(inputString == null ? 0 : inputString.length());
    }

    public static Integer length(Integer input) {
        return Integer.valueOf(input == null ? 0 : String.valueOf(input).length());
    }

    public static String split(String inputString, String splitString, int fieldIndex) {
        if (inputString == null || splitString == null) {
            return "";
        }
        String[] fields = inputString == null ? new String[0] : inputString.split(Pattern.quote(splitString));
        return (fieldIndex < 0 || fieldIndex >= fields.length) ? "" : fields[fieldIndex];
    }

    public static String substring(String inputString, int startIndex, int endIndex) {
        if (inputString == null) {
            return "";
        }
        return inputString.substring(Math.min(Math.max(0, startIndex), inputString.length()), Math.min(Math.max(0, endIndex), inputString.length()));
    }

    private static Double getVariableValue(ProgramRuleVariable programRuleVariable, String evaluated) {
        Double d = null;
        if (evaluated != null) {
            try {
                d = Double.valueOf(Double.parseDouble(VariableService.processSingleValue(evaluated, programRuleVariable.getVariableType())));
            } catch (NumberFormatException e) {
            }
        }
        return d;
    }

    public static Boolean hasUserRole(String roleId) {
        boolean z = false;
        Boolean hasRole = Boolean.valueOf(false);
        try {
            UserCredentials uc = MetaDataController.getUserAccount().getUserCredentials();
            if (uc != null && uc.hasRoleId(roleId)) {
                z = true;
            }
            hasRole = Boolean.valueOf(z);
        } catch (Exception e) {
            System.out.println("Norway - checking role " + roleId + " failed.");
            e.printStackTrace();
        }
        return hasRole;
    }
}
