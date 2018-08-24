package org.icddrb.dhis.android.sdk.utils.support;

import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.JexlException;
import org.apache.commons.jexl2.MapContext;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.utils.support.math.ExpressionFunctions;

public class ExpressionUtils {
    private static final String CLASS_TAG = ExpressionUtils.class.getSimpleName();
    private static final Pattern DATE2_FIX_PATTERN = Pattern.compile(".*?d2:daysBetween\\('\\d{2}-\\d{2}-\\d{4}'.*?");
    private static final Pattern DATE_FIX_PATTERN = Pattern.compile(".*?d2:yearsBetween\\('\\d{2}-\\d{2}-\\d{4}'.*?");
    private static final Map<String, String> EL_SQL_MAP = new HashMap();
    private static final String IGNORED_KEYWORDS_REGEX = "(^|[^:])(SUM|sum|AVERAGE|average|COUNT|count|STDDEV|stddev|VARIANCE|variance|MIN|min|MAX|max|NONE|none)";
    private static final JexlEngine JEXL = new JexlEngine();
    private static final JexlEngine JEXL_STRICT = new JexlEngine();
    private static final Pattern LENGTH_FIX_PATTERN = Pattern.compile("^d2:length\\(\\d+\\).*?");
    private static final Pattern LT_FIX_PATTERN = Pattern.compile(".*?=<.*?");
    private static final Pattern NULL_FIX_PATTERN = Pattern.compile(".*?'null'.*?");
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("^(-?0|-?[1-9]\\d*)(\\.\\d+)?$");

    static {
        Map<String, Object> functions = new HashMap();
        functions.put(ExpressionFunctions.NAMESPACE, new ExpressionFunctions());
        JEXL.setFunctions(functions);
        JEXL.setCache(512);
        JEXL.setSilent(false);
        JEXL.setLenient(true);
        JEXL_STRICT.setFunctions(functions);
        JEXL_STRICT.setCache(512);
        JEXL_STRICT.setSilent(false);
        JEXL_STRICT.setStrict(true);
        EL_SQL_MAP.put("&&", "and");
        EL_SQL_MAP.put("\\|\\|", "or");
        EL_SQL_MAP.put("==", Operation.EQUALS);
    }

    public static Object evaluate(String expression, Map<String, Object> vars) {
        try {
            if (expression.contains("add")) {
                System.out.println("Norway - EU evaluate expression: " + expression);
            }
            return evaluate(expression, vars, false);
        } catch (Exception e) {
            System.out.println("Norway - expression error: " + e.getMessage() + " expr: " + expression);
            return null;
        }
    }

    private static Object evaluate(String expression, Map<String, Object> vars, boolean strict) {
        return (strict ? JEXL_STRICT : JEXL).createExpression(exprFix(expression.replaceAll(IGNORED_KEYWORDS_REGEX, ""))).evaluate(vars != null ? new MapContext(vars) : new MapContext());
    }

    private static String exprFix(String expression) {
        if (LENGTH_FIX_PATTERN.matcher(expression).matches()) {
            expression = expression.replaceAll("\\(([0-9]+)\\)", "('$1')");
        }
        if (LT_FIX_PATTERN.matcher(expression).matches()) {
            expression = expression.replaceAll("=<", Operation.LESS_THAN_OR_EQUALS);
        }
        if (DATE_FIX_PATTERN.matcher(expression).matches()) {
            expression = expression.replaceAll("d2:yearsBetween\\('(\\d{2})-(\\d{2})-(\\d{4})'", "d2:yearsBetween('$3-$2-$1'");
        }
        if (DATE2_FIX_PATTERN.matcher(expression).matches()) {
            System.out.println("Norway - Date format 1: " + expression);
            expression = expression.replaceAll("d2:daysBetween\\('(\\d{2})-(\\d{2})-(\\d{4})'", "d2:daysBetween('$3-$2-$1'");
            System.out.println("Norway - Date format 2: " + expression);
        }
        if (NULL_FIX_PATTERN.matcher(expression).matches()) {
            return expression.replaceAll("'null'", "''");
        }
        return expression;
    }

    public static Double evaluateToDouble(String expression, Map<String, Object> vars) {
        Object result = evaluate(expression, vars);
        if (result == null) {
            throw new IllegalStateException("Result must be not null");
        } else if (isNumeric(String.valueOf(result))) {
            return Double.valueOf(String.valueOf(result));
        } else {
            throw new IllegalStateException("Result must be numeric: " + result + ", " + result.getClass());
        }
    }

    public static boolean isTrue(String expression, Map<String, Object> vars) {
        Object result = evaluate(expression, vars);
        return (result == null || !(result instanceof Boolean)) ? false : ((Boolean) result).booleanValue();
    }

    public static boolean isBoolean(String expression, Map<String, Object> vars) {
        try {
            return evaluate(expression, vars) instanceof Boolean;
        } catch (JexlException e) {
            return false;
        }
    }

    public static boolean isBoolean(String value) {
        if (value == null) {
            return false;
        }
        if (value.equals(BaseValue.TRUE) || value.equals(BaseValue.FALSE)) {
            return true;
        }
        return false;
    }

    public static boolean isValid(String expression, Map<String, Object> vars) {
        try {
            if (evaluate(expression, vars, true) != null) {
                return true;
            }
            return false;
        } catch (JexlException ex) {
            if (ex.getMessage().contains("divide error")) {
                return true;
            }
            return false;
        }
    }

    public static boolean isNumeric(String value) {
        return NUMERIC_PATTERN.matcher(value).matches();
    }

    public static String asSql(String expression) {
        if (expression == null) {
            return null;
        }
        for (String key : EL_SQL_MAP.keySet()) {
            expression = expression.replaceAll(key, (String) EL_SQL_MAP.get(key));
        }
        return expression;
    }
}
