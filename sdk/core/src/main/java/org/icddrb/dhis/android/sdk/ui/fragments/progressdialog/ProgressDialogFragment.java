package org.icddrb.dhis.android.sdk.ui.fragments.progressdialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import org.icddrb.dhis.android.sdk.R;

public class ProgressDialogFragment extends DialogFragment {
    private static final String ARG_INDETERMINATE = "indeterminate";
    private static final String ARG_MESSAGE = "message";
    public static boolean DIALOG_CANCELABLE = false;
    public static boolean DIALOG_INDETERMINATE = true;
    public static boolean DIALOG_NOT_INDETERMINATE;
    public static final String TAG = ProgressDialogFragment.class.getSimpleName();

    public static ProgressDialogFragment newInstance() {
        return newInstance(R.string.loading);
    }

    public static ProgressDialogFragment newInstance(int message) {
        return newInstance(message, DIALOG_INDETERMINATE, DIALOG_CANCELABLE);
    }

    public static ProgressDialogFragment newInstance(int message, boolean indeterminate, boolean cancelable) {
        Bundle args = new Bundle();
        args.putInt("message", message);
        args.putBoolean(ARG_INDETERMINATE, indeterminate);
        ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
        progressDialogFragment.setArguments(args);
        progressDialogFragment.setCancelable(cancelable);
        return progressDialogFragment;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        String message = getString(arguments.getInt("message"));
        boolean indeterminate = arguments.getBoolean(ARG_INDETERMINATE, DIALOG_NOT_INDETERMINATE);
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(indeterminate);
        return progressDialog;
    }
}
