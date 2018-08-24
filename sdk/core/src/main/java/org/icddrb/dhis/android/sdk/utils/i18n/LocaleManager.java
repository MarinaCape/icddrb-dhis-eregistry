package org.icddrb.dhis.android.sdk.utils.i18n;

import java.util.List;
import java.util.Locale;

public interface LocaleManager {
    public static final Locale DHIS_STANDARD_LOCALE = new Locale("en");
    public static final String ID = LocaleManager.class.getName();

    List<Locale> getAvailableLocales();

    Locale getCurrentLocale();

    Locale getFallbackLocale();

    List<Locale> getLocalesOrderedByPriority();

    void setCurrentLocale(Locale locale);
}
