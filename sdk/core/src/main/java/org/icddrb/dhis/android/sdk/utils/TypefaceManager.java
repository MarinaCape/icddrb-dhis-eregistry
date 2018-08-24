package org.icddrb.dhis.android.sdk.utils;

import android.content.res.AssetManager;
import android.graphics.Typeface;

public final class TypefaceManager {
    private static final String FONTS_PATH = "fonts/";

    private TypefaceManager() {
    }

    public static Typeface getTypeface(AssetManager assetManager, String fontName) {
        if (assetManager == null) {
            throw new IllegalArgumentException("AssetManager must not be null");
        } else if (fontName != null) {
            return Typeface.createFromAsset(assetManager, FONTS_PATH + fontName);
        } else {
            throw new IllegalArgumentException("AssetManager must not be null");
        }
    }
}
