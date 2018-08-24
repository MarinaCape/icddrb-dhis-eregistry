package org.icddrb.dhis.android.sdk.persistence.converters;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.raizlabs.android.dbflow.converter.TypeConverter;
import java.io.IOException;
import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.persistence.models.Access;

public final class AccessConverter extends TypeConverter<String, Access> {
    public String getDBValue(Access model) {
        String access = null;
        try {
            access = DhisController.getInstance().getObjectMapper().writeValueAsString(model);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return access;
    }

    public Access getModelValue(String data) {
        Access access = null;
        try {
            return (Access) DhisController.getInstance().getObjectMapper().readValue(data, Access.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return access;
        } catch (JsonParseException e2) {
            e2.printStackTrace();
            return access;
        } catch (IOException e3) {
            e3.printStackTrace();
            return access;
        }
    }
}
