package org.icddrb.dhis.android.sdk.utils.support.math;

public class OneIfZeroOrPositiveFunction extends UnaryDoubleFunction {
    public static final String NAME = "oizp";

    public Double eval(double arg) {
        double d = 0.0d;
        if (arg >= 0.0d) {
            d = 1.0d;
        }
        return Double.valueOf(d);
    }
}
