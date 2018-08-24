package org.icddrb.dhis.android.sdk.utils.i18n;

import java.util.Locale;
import org.icddrb.dhis.android.sdk.persistence.models.BaseIdentifiableObject;

public class I18nLocale extends BaseIdentifiableObject {
    private String locale;

    public I18nLocale() {
        setName("English (United Kingdom)");
        this.locale = "en_GB";
    }

    public I18nLocale(Locale locale) {
        setName(locale.getDisplayName());
        this.locale = locale.toString();
    }

    public String getLocale() {
        return this.locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getUid() {
        return null;
    }

    public void setUid(String id) {
    }
}
