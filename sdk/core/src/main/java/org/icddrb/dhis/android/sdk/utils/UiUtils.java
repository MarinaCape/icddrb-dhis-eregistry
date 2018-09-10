package org.icddrb.dhis.android.sdk.utils;

import android.app.Activity;
import android.content.DialogInterface.OnClickListener;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import org.icddrb.dhis.android.sdk.R;
import org.icddrb.dhis.android.sdk.events.LoadingMessageEvent;
import org.icddrb.dhis.android.sdk.events.LoadingMessageEvent.EventType;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.BaseSerializableModel;
import org.icddrb.dhis.android.sdk.ui.dialogs.CustomDialogFragment;
import org.icddrb.dhis.android.sdk.ui.fragments.progressdialog.ProgressDialogFragment;

public final class UiUtils {
    private static UiUtils uiUtils = new UiUtils();
    private ProgressDialogFragment progressDialogFragment;

    private ProgressDialogFragment getProgressDialogFragment() {
        return this.progressDialogFragment;
    }

    private void setProgressDialogFragment(ProgressDialogFragment progressDialogFragment) {
        this.progressDialogFragment = progressDialogFragment;
    }

    private static UiUtils getInstance() {
        return uiUtils;
    }

    private UiUtils() {
    }

    public static void showErrorDialog(final Activity activity, final String title, final String message) {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    new CustomDialogFragment(title, message, activity.getString(R.string.ok_option), null).show(activity.getFragmentManager(), title);
                }
            });
        }
    }

    public static void showErrorDialog(final Activity activity, final String title, final String message, final int iconId) {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    new CustomDialogFragment(title, message, activity.getString(R.string.ok_option), iconId, null).show(activity.getFragmentManager(), title);
                }
            });
        }
    }

    public static void showErrorDialog(final Activity activity, final String title, final String message, final OnClickListener onConfirmClickListener) {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    new CustomDialogFragment(title, message, activity.getString(R.string.ok_option), onConfirmClickListener).show(activity.getFragmentManager(), title);
                }
            });
        }
    }

    public static void showConfirmDialog(Activity activity, String title, String message, String confirmOption, String cancelOption, OnClickListener onClickListener) {
        new CustomDialogFragment(title, message, confirmOption, cancelOption, onClickListener).show(activity.getFragmentManager(), title);
    }

    public static void showConfirmDialog(Activity activity, String title, String message, String confirmOption, OnClickListener onClickListener) {
        new CustomDialogFragment(title, message, confirmOption, onClickListener).show(activity.getFragmentManager(), title);
    }

    public static void showConfirmDialog(Activity activity, String title, String message, String confirmOption, String cancelOption, int iconId, OnClickListener onClickListener) {
        new CustomDialogFragment(title, message, confirmOption, cancelOption, iconId, onClickListener).show(activity.getFragmentManager(), title);
    }

    public static void showConfirmDialog(Activity activity, String title, String message, String confirmOption, String cancelOption, OnClickListener onConfirmListener, OnClickListener onCancelListener) {
        new CustomDialogFragment(title, message, confirmOption, cancelOption, onConfirmListener, onCancelListener).show(activity.getFragmentManager(), title);
    }

    public static void showConfirmDialog(Activity activity, String title, String message, String firstOption, String secondOption, String thirdOption, OnClickListener firstOptionListener, OnClickListener secondOptionListener, OnClickListener thirdOptionListener) {
        new CustomDialogFragment(title, message, firstOption, secondOption, thirdOption, firstOptionListener, secondOptionListener, thirdOptionListener).show(activity.getFragmentManager(), title);
    }

    public static void postProgressMessage(final String message, final EventType eventType) {
        new Thread() {
            public void run() {
                Dhis2Application.bus.post(new LoadingMessageEvent(message, eventType));
            }
        }.start();
    }

    @Deprecated
    public static void showStatusDialog(FragmentManager fragmentManager, BaseSerializableModel item) {
    }

    public static void showLoadingDialog(FragmentManager fragmentManager, int message) {
        if (getInstance().getProgressDialogFragment() != null) {
            getInstance().getProgressDialogFragment().dismissAllowingStateLoss();
            getInstance().setProgressDialogFragment(null);
        }
        getInstance().setProgressDialogFragment(ProgressDialogFragment.newInstance(message));
        getInstance().getProgressDialogFragment().show(fragmentManager, ProgressDialogFragment.TAG);
    }

    public static void hideLoadingDialog(FragmentManager fragmentManager) {
        if (getInstance().getProgressDialogFragment() != null) {
            if (getInstance().getProgressDialogFragment().isAdded()) {
                getInstance().getProgressDialogFragment().dismissAllowingStateLoss();
            }
            getInstance().setProgressDialogFragment(null);
        }
    }

    public static void showSnackBar(View parentLayout, String text, int duration) {
        Snackbar.make(parentLayout, (CharSequence) text, duration).show();
    }

    public static void showSnackBarWithAction(View parentLayout, String text, int duration, int resId, View.OnClickListener onClickListener) {
        Snackbar.make(parentLayout, (CharSequence) text, duration).setAction(resId, onClickListener).show();
    }

    public static void showSnackBarWithAction(View parentLayout, String text, int duration, String actionText, View.OnClickListener onClickListener) {
        Snackbar.make(parentLayout, (CharSequence) text, duration).setAction((CharSequence) actionText, onClickListener).show();
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) activity.getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 2);
        }
    }
}
