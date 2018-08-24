package org.icddrb.dhis.android.sdk.utils.api;

public enum ProgramType {
    WITH_REGISTRATION("with_registration"),
    WITHOUT_REGISTRATION("without_registration");
    
    private final String value;

    private ProgramType(String value) {
        this.value = value;
    }

    public static ProgramType fromValue(String value) {
        for (ProgramType programType : values()) {
            if (programType.value.equalsIgnoreCase(value)) {
                return programType;
            }
        }
        return null;
    }

    public String getValue() {
        return this.value;
    }
}
