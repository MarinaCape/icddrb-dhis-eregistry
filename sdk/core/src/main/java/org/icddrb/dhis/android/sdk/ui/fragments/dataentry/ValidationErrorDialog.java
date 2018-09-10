package org.icddrb.dhis.android.sdk.ui.fragments.dataentry;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import org.icddrb.dhis.android.sdk.R;
import org.icddrb.dhis.android.sdk.ui.adapters.ValidationErrorAdapter;

public final class ValidationErrorDialog extends DialogFragment implements OnClickListener {
    private static final String ERRORS_LIST_EXTRA = "extra:ErrorsList";
    private static final String HEADER_EXTRA = "extra:Header";
    private static final String TAG = ValidationErrorDialog.class.getSimpleName();
    private ValidationErrorAdapter mAdapter;
    private Button mButton;
    private TextView mHeader;
    private ListView mListView;

    public static ValidationErrorDialog newInstance(ArrayList<String> errors) {
        ValidationErrorDialog dialog = new ValidationErrorDialog();
        Bundle args = new Bundle();
        args.putStringArrayList(ERRORS_LIST_EXTRA, errors);
        dialog.setArguments(args);
        return dialog;
    }

    public static ValidationErrorDialog newInstance(String header, ArrayList<String> errors) {
        ValidationErrorDialog dialog = new ValidationErrorDialog();
        Bundle args = new Bundle();
        args.putStringArrayList(ERRORS_LIST_EXTRA, errors);
        args.putString(HEADER_EXTRA, header);
        dialog.setArguments(args);
        return dialog;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(1, R.style.Theme_AppCompat_Light_Dialog);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_validation_errors, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.mListView = (ListView) view.findViewById(R.id.simple_listview);
        this.mHeader = (TextView) view.findViewById(R.id.header);
        this.mButton = (Button) view.findViewById(R.id.closebutton);
        String header = getArguments().getString(HEADER_EXTRA);
        if (header != null) {
            this.mHeader.setText(header);
        }
        this.mAdapter = new ValidationErrorAdapter(LayoutInflater.from(getActivity().getBaseContext()));
        this.mListView.setAdapter(this.mAdapter);
        this.mButton.setOnClickListener(this);
        if (getArguments() != null) {
            this.mAdapter.swapData(getArguments().getStringArrayList(ERRORS_LIST_EXTRA));
        }
    }

    public void onClick(View v) {
        dismiss();
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, TAG);
    }
}
