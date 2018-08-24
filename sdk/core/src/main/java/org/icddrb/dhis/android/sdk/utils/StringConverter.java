package org.icddrb.dhis.android.sdk.utils;

import java.io.IOException;
import java.lang.reflect.Type;
import okio.ByteString;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;
import retrofit.mime.TypedString;

public class StringConverter implements Converter {
    public String fromBody(TypedInput body, Type type) throws ConversionException {
        try {
            return ByteString.read(body.in(), (int) body.length()).utf8();
        } catch (IOException e) {
            throw new ConversionException("Problem when convert string", e);
        } catch (NullPointerException e2) {
            return "";
        }
    }

    public TypedOutput toBody(Object object) {
        return new TypedString((String) object);
    }
}
