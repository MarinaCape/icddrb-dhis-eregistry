package org.icddrb.dhis.android.sdk.utils.support;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;
import org.apache.commons.validator.routines.DoubleValidator;
import org.apache.commons.validator.routines.IntegerValidator;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.utils.Operator;
import org.icddrb.dhis.android.sdk.utils.support.math.OneIfZeroOrPositiveFunction;
import org.icddrb.dhis.android.sdk.utils.support.math.ZeroIfNegativeFunction;
import org.nfunk.jep.JEP;

public class MathUtils {
    private static DoubleValidator DOUBLE_VALIDATOR = new DoubleValidator();
    private static final Pattern INT_PATTERN = Pattern.compile("^(0|-?[1-9]\\d*)$");
    private static IntegerValidator INT_VALIDATOR = new IntegerValidator();
    private static final Locale LOCALE = new Locale("en");
    private static final Pattern NEGATIVE_INT_PATTERN = Pattern.compile("^-[1-9]\\d*$");
    private static final Pattern NUMERIC_LENIENT_PATTERN = Pattern.compile(NUMERIC_LENIENT_REGEXP);
    public static final String NUMERIC_LENIENT_REGEXP = "^(-?[0-9]+)(\\.[0-9]+)?(E(-)?\\d+)?$";
    private static final Pattern NUMERIC_PATTERN = Pattern.compile(NUMERIC_REGEXP);
    public static final String NUMERIC_REGEXP = "^(-?0|-?[1-9]\\d*)(\\.\\d+)?(E(-)?\\d+)?$";
    private static final Pattern POSITIVE_INT_PATTERN = Pattern.compile("^[1-9]\\d*$");
    private static final Pattern POSITIVE_OR_ZERO_INT_PATTERN = Pattern.compile("(^0$)|(^[1-9]\\d*$)");
    private static final double TOLERANCE = 0.01d;
    public static final Double ZERO = new Double(0.0d);
    private static final Pattern ZERO_PATTERN = Pattern.compile("^0(\\.0*)?$");

    public static boolean expressionIsTrue(double leftSide, Operator operator, double rightSide) {
        return expressionIsTrue(leftSide + operator.getMathematicalOperator() + rightSide);
    }

    public static boolean expressionIsTrue(String expression) {
        JEP parser = getJep();
        parser.parseExpression(expression);
        return parser.getValue() == 1.0d;
    }

    public static double calculateExpression(String expression) {
        JEP parser = getJep();
        parser.parseExpression(expression);
        return parser.getValue();
    }

    public static boolean isValidDouble(Double d) {
        return (d == null || Double.isInfinite(d.doubleValue()) || Double.isNaN(d.doubleValue())) ? false : true;
    }

    public static boolean expressionHasErrors(String expression) {
        JEP parser = getJep();
        parser.parseExpression(expression);
        return parser.hasError();
    }

    public static String getExpressionErrorInfo(String expression) {
        JEP parser = getJep();
        parser.parseExpression(expression);
        return parser.getErrorInfo();
    }

    private static JEP getJep() {
        JEP parser = new JEP();
        parser.addStandardFunctions();
        parser.addStandardConstants();
        parser.addFunction(OneIfZeroOrPositiveFunction.NAME, new OneIfZeroOrPositiveFunction());
        parser.addFunction(ZeroIfNegativeFunction.NAME, new ZeroIfNegativeFunction());
        return parser;
    }

    public static double getFloor(double value) {
        return Math.floor(value);
    }

    public static double getRounded(double value, int decimals) {
        double factor = Math.pow(10.0d, (double) decimals);
        return ((double) Math.round(value * factor)) / factor;
    }

    public static double getRounded(double value) {
        if (value >= 1.0d || value <= -1.0d) {
            return getRounded(value, 1);
        }
        return getRounded(value, 2);
    }

    public static Object getRoundedObject(Object value) {
        return (value == null || !Double.class.equals(value.getClass())) ? value : Double.valueOf(getRounded(((Double) value).doubleValue()));
    }

    public static double roundSignificant(double value) {
        if (value >= 10.0d || value <= -10.0d) {
            return getRounded(value, 1);
        }
        return roundToSignificantDigits(value, 3);
    }

    public static double roundToSignificantDigits(double value, int n) {
        if (value == 0.0d) {
            return 0.0d;
        }
        double d;
        if (value < 0.0d) {
            d = -value;
        } else {
            d = value;
        }
        double magnitude = Math.pow(10.0d, (double) (n - ((int) Math.ceil(Math.log10(d)))));
        return ((double) Math.round(value * magnitude)) / magnitude;
    }

    public static String roundToString(double value, int significantFigures) {
        return new BigDecimal(value).round(new MathContext(significantFigures)).toPlainString();
    }

    public static int getMin(int number, int min) {
        return number < min ? min : number;
    }

    public static int getMax(int number, int max) {
        return number > max ? max : number;
    }

    public static int getWithin(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    public static boolean isNumeric(String value) {
        return value != null && DOUBLE_VALIDATOR.isValid(value, LOCALE) && NUMERIC_PATTERN.matcher(value).matches();
    }

    public static boolean isNumericLenient(String value) {
        return value != null && DOUBLE_VALIDATOR.isValid(value, LOCALE) && NUMERIC_LENIENT_PATTERN.matcher(value).matches();
    }

    public static boolean isUnitInterval(String value) {
        if (!isNumeric(value)) {
            return false;
        }
        Double dbl = Double.valueOf(Double.parseDouble(value));
        if (dbl.doubleValue() < 0.0d || dbl.doubleValue() > 1.0d) {
            return false;
        }
        return true;
    }

    public static boolean isPercentage(String value) {
        if (!isInteger(value)) {
            return false;
        }
        Integer integer = Integer.valueOf(value);
        if (integer.intValue() < 0 || integer.intValue() > 100) {
            return false;
        }
        return true;
    }

    public static boolean isInteger(String value) {
        return value != null && INT_VALIDATOR.isValid(value) && INT_PATTERN.matcher(value).matches();
    }

    public static boolean isPositiveInteger(String value) {
        return value != null && INT_VALIDATOR.isValid(value) && POSITIVE_INT_PATTERN.matcher(value).matches();
    }

    public static boolean isZeroOrPositiveInteger(String value) {
        return value != null && INT_VALIDATOR.isValid(value) && POSITIVE_OR_ZERO_INT_PATTERN.matcher(value).matches();
    }

    public static boolean isNegativeInteger(String value) {
        return value != null && INT_VALIDATOR.isValid(value) && NEGATIVE_INT_PATTERN.matcher(value).matches();
    }

    public static boolean isZero(String value) {
        return value != null && ZERO_PATTERN.matcher(value).matches();
    }

    public static boolean isBool(String value) {
        return value != null && (value.equals(BaseValue.TRUE) || value.equals(BaseValue.FALSE));
    }

    public static boolean isEqual(Double d1, Double d2) {
        if (d1 == null || d2 == null || Math.abs(d1.doubleValue() - d2.doubleValue()) >= TOLERANCE) {
            return false;
        }
        return true;
    }

    public static boolean isEqual(double d1, double d2) {
        return Math.abs(d1 - d2) < TOLERANCE;
    }

    public static boolean isZero(double value) {
        return isEqual(value, 0.0d);
    }

    public static double zeroIfNull(Double value) {
        return value == null ? 0.0d : value.doubleValue();
    }

    public static int getRandom() {
        return new Random().nextInt(999);
    }

    public static Double getMin(double[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        double min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return Double.valueOf(min);
    }

    public static Double getMax(double[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        double max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return Double.valueOf(max);
    }

    public static Double getAverage(List<Double> values) {
        return Double.valueOf(getSum(values).doubleValue() / ((double) values.size()));
    }

    public static Double getSum(List<Double> values) {
        Double sum = Double.valueOf(0.0d);
        for (Double value : values) {
            if (value != null) {
                sum = Double.valueOf(sum.doubleValue() + value.doubleValue());
            }
        }
        return sum;
    }

    public static Double parseDouble(String value) {
        Double d = null;
        if (!(value == null || value.trim().isEmpty())) {
            try {
                d = Double.valueOf(Double.parseDouble(value));
            } catch (NumberFormatException e) {
            }
        }
        return d;
    }

    public static Integer parseInt(String string) {
        Integer num = null;
        if (!(string == null || string.trim().isEmpty())) {
            try {
                num = Integer.valueOf(Integer.parseInt(string));
            } catch (NumberFormatException e) {
            }
        }
        return num;
    }

    public static double getLowBound(double stdDev, double stdDevNo, double average) {
        return average - (stdDev * stdDevNo);
    }

    public static double getHighBound(double stdDev, double stdDevFactor, double average) {
        return average + (stdDev * stdDevFactor);
    }

    public static int divideToCeil(int numerator, int denominator) {
        return Double.valueOf(Math.ceil(((double) numerator) / ((double) denominator))).intValue();
    }

    public static int divideToFloor(int numerator, int denominator) {
        return Double.valueOf(Math.floor(((double) numerator) / ((double) denominator))).intValue();
    }
}
