package org.icddrb.dhis.android.sdk.utils.api;

public enum ProgramRuleActionType {
    DISPLAYTEXT("displaytext"),
    DISPLAYKEYVALUEPAIR("displaykeyvaluepair"),
    HIDEFIELD("hidefield"),
    HIDESECTION("hidesection"),
    ASSIGN("assign"),
    SHOWWARNING("showwarning"),
    SHOWERROR("showerror"),
    CREATEEVENT("createevent"),
    ERRORONCOMPLETE("erroroncomplete"),
    HIDEPROGRAMSTAGE("hideprogramstage"),
    WARNINGONCOMPLETE("warningoncomplete"),
    SENDMESSAGE("sendmessage"),
    SETMANDATORYFIELD("setmandatoryfield");
    
    final String value;

    private ProgramRuleActionType(String value) {
        this.value = value;
    }

    public static ProgramRuleActionType fromValue(String value) {
        for (ProgramRuleActionType type : values()) {
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
