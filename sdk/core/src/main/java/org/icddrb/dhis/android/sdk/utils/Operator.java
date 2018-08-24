package org.icddrb.dhis.android.sdk.utils;

import com.raizlabs.android.dbflow.sql.builder.Condition.Operation;

public enum Operator {
    equal_to("=="),
    not_equal_to(Operation.NOT_EQUALS),
    greater_than(Operation.GREATER_THAN),
    greater_than_or_equal_to(Operation.GREATER_THAN_OR_EQUALS),
    less_than(Operation.LESS_THAN),
    less_than_or_equal_to(Operation.LESS_THAN_OR_EQUALS),
    compulsory_pair("[Compulsory pair]");
    
    private final String mathematicalOperator;

    private Operator(String mathematicalOperator) {
        this.mathematicalOperator = mathematicalOperator;
    }

    public String getMathematicalOperator() {
        return this.mathematicalOperator;
    }

    public static Operator fromValue(String value) {
        for (Operator operator : values()) {
            if (operator.mathematicalOperator.equalsIgnoreCase(value)) {
                return operator;
            }
        }
        return null;
    }

    public static Operator safeValueOf(String name) {
        return name != null ? valueOf(name) : null;
    }
}
