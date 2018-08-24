package org.icddrb.dhis.client.sdk.ui.models;

import org.icddrb.dhis.client.sdk.utils.Preconditions;

public class FormSection {
    private final String id;
    private final String label;

    public FormSection(String id, String label) {
        this.id = (String) Preconditions.isNull(id, "id must not be null");
        this.label = (String) Preconditions.isNull(label, "label must not be null");
    }

    public String getId() {
        return this.id;
    }

    public String getLabel() {
        return this.label;
    }

    public String toString() {
        return "FormSection{id='" + this.id + '\'' + ", label='" + this.label + '\'' + '}';
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        FormSection that = (FormSection) other;
        if (this.id.equals(that.id)) {
            return this.label.equals(that.label);
        }
        return false;
    }

    public int hashCode() {
        return (this.id.hashCode() * 31) + this.label.hashCode();
    }
}
