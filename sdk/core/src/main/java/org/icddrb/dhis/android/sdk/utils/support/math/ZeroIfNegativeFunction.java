package org.icddrb.dhis.android.sdk.utils.support.math;

public class ZeroIfNegativeFunction extends UnaryDoubleFunction {
    public static final String NAME = "zing";

    public Double eval(double arg) {
        return Double.valueOf(Math.max(0.0d, arg));
    }
}
