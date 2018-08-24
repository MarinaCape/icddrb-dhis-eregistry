package org.icddrb.dhis.android.sdk.ui.dialogs;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class CustomDialogFragment extends DialogFragment {
    private static final String KEY_FIRSTOPTION = "arg:KeyFirstOption";
    private static final String KEY_ICONID = "arg:KeyIconId";
    private static final String KEY_MESSAGE = "arg:KeyMessage";
    private static final String KEY_ONCLICKLISTENER = "arg:KeyOnClickListener";
    private static final String KEY_TITLE = "arg:keyTitle";
    String firstOption;
    OnClickListener firstOptionListener;
    int iconId = -1;
    String message;
    String secondOption;
    OnClickListener secondOptionListener;
    String thirdOption;
    OnClickListener thirdOptionListener;
    String title;

    /* renamed from: org.icddrb.dhis.android.sdk.ui.dialogs.CustomDialogFragment$1 */
    class C08911 implements OnClickListener {
        C08911() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    public CustomDialogFragment(String title, String message, String firstOption, OnClickListener firstOptionListener) {
        this.title = title;
        this.message = message;
        this.firstOption = firstOption;
        this.firstOptionListener = firstOptionListener;
        this.secondOption = null;
    }

    public CustomDialogFragment(String title, String message, String firstOption, int iconId, OnClickListener firstOptionListener) {
        this.title = title;
        this.message = message;
        this.firstOption = firstOption;
        this.iconId = iconId;
        this.firstOptionListener = firstOptionListener;
        this.secondOption = null;
    }

    public CustomDialogFragment(String title, String message, String firstOption) {
        this.title = title;
        this.message = message;
        this.firstOption = firstOption;
        this.secondOption = null;
        this.firstOptionListener = null;
    }

    public CustomDialogFragment(String title, String message, String firstOption, String secondOption, OnClickListener firstOptionListener) {
        this.title = title;
        this.message = message;
        this.firstOption = firstOption;
        this.secondOption = secondOption;
        this.firstOptionListener = firstOptionListener;
    }

    public CustomDialogFragment(String title, String message, String firstOption, String secondOption, int iconId, OnClickListener firstOptionListener) {
        this.title = title;
        this.message = message;
        this.firstOption = firstOption;
        this.secondOption = secondOption;
        this.iconId = iconId;
        this.firstOptionListener = firstOptionListener;
    }

    public CustomDialogFragment(String title, String message, String firstOption, String secondOption, OnClickListener firstOptionListener, OnClickListener secondOptionListener) {
        this.title = title;
        this.message = message;
        this.firstOption = firstOption;
        this.secondOption = secondOption;
        this.firstOptionListener = firstOptionListener;
        this.secondOptionListener = secondOptionListener;
    }

    public CustomDialogFragment(String title, String message, String firstOption, String secondOption, int iconId, OnClickListener firstOptionListener, OnClickListener secondOptionListener) {
        this.title = title;
        this.message = message;
        this.firstOption = firstOption;
        this.secondOption = secondOption;
        this.iconId = iconId;
        this.firstOptionListener = firstOptionListener;
        this.secondOptionListener = secondOptionListener;
    }

    public CustomDialogFragment(String title, String message, String firstOption, String secondOption, String thirdOption, OnClickListener firstOptionListener, OnClickListener secondOptionListener, OnClickListener thirdOptionListener) {
        this.title = title;
        this.message = message;
        this.firstOption = firstOption;
        this.secondOption = secondOption;
        this.thirdOption = thirdOption;
        this.firstOptionListener = firstOptionListener;
        this.secondOptionListener = secondOptionListener;
        this.thirdOptionListener = thirdOptionListener;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Builder alertDialogBuilder = new Builder(getActivity());
        if (this.iconId > 0) {
            alertDialogBuilder.setIcon(this.iconId);
        }
        alertDialogBuilder.setTitle(this.title);
        alertDialogBuilder.setMessage(this.message);
        alertDialogBuilder.setPositiveButton(this.firstOption, this.firstOptionListener);
        if (this.secondOptionListener == null) {
            this.secondOptionListener = new C08911();
        }
        if (this.secondOption != null) {
            alertDialogBuilder.setNegativeButton(this.secondOption, this.secondOptionListener);
        }
        if (this.thirdOption != null) {
            alertDialogBuilder.setNeutralButton(this.thirdOption, this.thirdOptionListener);
        }
        return alertDialogBuilder.create();
    }
}
