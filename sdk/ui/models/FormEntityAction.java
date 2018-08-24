package org.icddrb.dhis.client.sdk.ui.models;

import org.icddrb.dhis.client.sdk.utils.Preconditions;

public class FormEntityAction {
    private final FormEntityActionType actionType;
    private final String id;
    private final String value;

    public enum FormEntityActionType {
        HIDE,
        ASSIGN
    }

    public FormEntityAction(String id, String value, FormEntityActionType actionType) {
        this.id = (String) Preconditions.isNull(id, "id must not be null");
        this.actionType = (FormEntityActionType) Preconditions.isNull(actionType, "actionType must not be null");
        this.value = value;
    }

    public String getId() {
        return this.id;
    }

    public String getValue() {
        return this.value;
    }

    public FormEntityActionType getActionType() {
        return this.actionType;
    }

    public String toString() {
        return "FormEntityAction{id='" + this.id + '\'' + ", value='" + this.value + '\'' + ", actionType=" + this.actionType + '}';
    }
}
