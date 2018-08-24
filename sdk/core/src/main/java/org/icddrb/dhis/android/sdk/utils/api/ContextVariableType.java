package org.icddrb.dhis.android.sdk.utils.api;

import org.icddrb.dhis.android.sdk.persistence.models.ProgramIndicator;

public enum ContextVariableType {
    CURRENT_DATE("current_date"),
    EVENT_DATE(ProgramIndicator.EVENT_DATE),
    DUE_DATE("due_date"),
    EVENT_COUNT("event_count"),
    ENROLLMENT_DATE("enrollment_date"),
    ENROLLMENT_ID("enrollment_id"),
    EVENT_ID("event_id"),
    INCIDENT_DATE("incident_date"),
    ENROLLMENT_COUNT("enrollment_count"),
    TEI_COUNT("tei_count");
    
    final String value;

    private ContextVariableType(String value) {
        this.value = value;
    }

    public static ContextVariableType fromValue(String value) {
        for (ContextVariableType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }

    public String toString() {
        return this.value;
    }
}
