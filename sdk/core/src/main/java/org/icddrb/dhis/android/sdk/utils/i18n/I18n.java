package org.icddrb.dhis.android.sdk.utils.i18n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class I18n {
    final String ID = I18n.class.getName();
    private ResourceBundle globalResourceBundle;
    private ResourceBundle specificResourceBundle;

    public I18n(ResourceBundle globalResourceBundle, ResourceBundle specificResourceBundle) {
        this.globalResourceBundle = globalResourceBundle;
        this.specificResourceBundle = specificResourceBundle;
    }

    public String getString(String key) {
        String translation = key;
        if (this.specificResourceBundle != null) {
            try {
                translation = this.specificResourceBundle.getString(key);
            } catch (MissingResourceException e) {
            }
        }
        if (translation.equals(key) && this.globalResourceBundle != null) {
            try {
                translation = this.globalResourceBundle.getString(key);
            } catch (MissingResourceException e2) {
            }
        }
        return translation;
    }
}
