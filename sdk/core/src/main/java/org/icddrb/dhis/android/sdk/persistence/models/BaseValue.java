package org.icddrb.dhis.android.sdk.persistence.models;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.structure.BaseModel;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties({"modelAdapter"})
public abstract class BaseValue extends BaseModel implements Serializable {
    public static final String EMPTY_VALUE = "";
    public static final String FALSE = "false";
    public static final String TRUE = "true";
    @JsonProperty("value")
    String value;

    protected BaseValue(BaseValue baseValue) {
        this.value = baseValue.value;
    }

    protected BaseValue() {
    }

    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static <T extends BaseValue> Map<String, T> toMap(Collection<T> objects) {
        Map<String, T> map = new HashMap();
        if (objects != null && objects.size() > 0) {
            for (T object : objects) {
                if (object.getValue() != null) {
                    map.put(object.getValue(), object);
                }
            }
        }
        return map;
    }
}
