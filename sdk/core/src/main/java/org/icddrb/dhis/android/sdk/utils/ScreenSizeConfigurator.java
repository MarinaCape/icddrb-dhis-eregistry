package org.icddrb.dhis.android.sdk.utils;

import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ScreenSizeConfigurator {
    private static final int SINGLE_PIECE = 250;
    public static ScreenSizeConfigurator screenSizeConfigurator;
    private WindowManager windowManager;

    private ScreenSizeConfigurator(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    public static ScreenSizeConfigurator init(WindowManager windowManager) {
        Preconditions.isNull(windowManager, "context must not be null");
        if (screenSizeConfigurator == null) {
            screenSizeConfigurator = new ScreenSizeConfigurator(windowManager);
        }
        return screenSizeConfigurator;
    }

    public static ScreenSizeConfigurator getInstance() {
        Preconditions.isNull(screenSizeConfigurator, "screenSizeConfigurator must not be null");
        return screenSizeConfigurator;
    }

    private int getColumnsByScreen(WindowManager windowManager) {
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels / 250;
    }

    public int getFields() {
        return getColumnsByScreen(this.windowManager);
    }
}
