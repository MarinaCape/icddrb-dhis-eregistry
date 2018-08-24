package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.internal.view.SupportMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import org.icddrb.dhis.android.sdk.C0845R;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.persistence.Dhis2Application;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramStage;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.events.OnCompleteEventClick;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.ValidationErrorDialog;
import org.icddrb.dhis.android.sdk.ui.fragments.eventdataentry.EventDataEntryFragment;

public final class StatusRow extends Row {
    public static final String CLASS_TAG = StatusRow.class.getSimpleName();
    private Context context;
    private FragmentActivity fragmentActivity;
    private StatusViewHolder holder;
    private final Event mEvent;
    private ProgramStage programStage;

    private static class OnCompleteClickListener implements OnClickListener, DialogInterface.OnClickListener {
        private Activity activity;
        private final Button complete;
        private final Context context;
        private final Event event;
        private final ProgramStage programStage;
        private final TextView tv;

        public OnCompleteClickListener(Context context, Button complete, TextView tv, Event event, ProgramStage programStage) {
            this.context = context;
            this.complete = complete;
            this.event = event;
            this.programStage = programStage;
            this.tv = tv;
        }

        public void onClick(View v) {
            if (this.activity != null) {
                String label = "HaOwL7bIdrs".equals(this.programStage.getUid()) ? this.event.getStatus().equals("COMPLETED") ? this.activity.getString(C0845R.string.incomplete) : "Complete Pregnancy Record" : this.event.getStatus().equals("COMPLETED") ? this.activity.getString(C0845R.string.incomplete) : this.activity.getString(C0845R.string.complete);
                Dhis2Application.getEventBus().post(new OnCompleteEventClick(label, this.event.getStatus().equals("COMPLETED") ? this.activity.getString(C0845R.string.incomplete_confirm) : this.activity.getString(C0845R.string.complete_confirm), this.event, this.complete, this.tv));
            }
        }

        private void setActivity(Activity activity) {
            this.activity = activity;
        }

        public void onClick(DialogInterface dialog, int which) {
            if (this.event.getStatus().equals("COMPLETED")) {
                this.event.setStatus("ACTIVE");
            } else {
                this.event.setStatus("COMPLETED");
            }
            StatusViewHolder.updateViews(this.event, this.programStage, this.complete, this.tv, this.context);
        }
    }

    private static class OnValidateClickListener implements OnClickListener {
        private final Context context;
        private final Event event;
        private FragmentActivity fragmentActivity;
        private final Button validate;

        public OnValidateClickListener(Context context, Button validate, Event event) {
            this.validate = validate;
            this.event = event;
            this.context = context;
        }

        public void onClick(View v) {
            ArrayList<String> errors = EventDataEntryFragment.getValidationErrors(this.event, MetaDataController.getProgramStage(this.event.getProgramStageId()), this.context);
            ValidationErrorDialog dialog;
            if (errors.isEmpty()) {
                dialog = ValidationErrorDialog.newInstance(this.context.getString(C0845R.string.validation_success), new ArrayList());
                if (this.fragmentActivity != null) {
                    dialog.show(this.fragmentActivity.getSupportFragmentManager());
                    return;
                }
                return;
            }
            dialog = ValidationErrorDialog.newInstance(errors);
            if (this.fragmentActivity != null) {
                dialog.show(this.fragmentActivity.getSupportFragmentManager());
            }
        }

        public void setFragmentActivity(FragmentActivity fragmentActivity) {
            this.fragmentActivity = fragmentActivity;
        }
    }

    private static class StatusViewHolder {
        private final Button complete;
        private final Event event;
        private final OnCompleteClickListener onCompleteButtonClickListener;
        private final OnValidateClickListener onValidateButtonClickListener;
        private final ProgramStage programStage;
        private final TextView tv;
        private final Button validate;

        public StatusViewHolder(Context context, View view, Event event, ProgramStage programStage) {
            this.event = event;
            this.programStage = programStage;
            this.complete = (Button) view.findViewById(C0845R.id.complete);
            this.validate = (Button) view.findViewById(C0845R.id.validate);
            this.tv = (TextView) view.findViewById(C0845R.id.tv_complete);
            this.onCompleteButtonClickListener = new OnCompleteClickListener(context, this.complete, this.tv, this.event, this.programStage);
            this.onValidateButtonClickListener = new OnValidateClickListener(context, this.validate, this.event);
            this.complete.setOnClickListener(this.onCompleteButtonClickListener);
            this.validate.setOnClickListener(this.onValidateButtonClickListener);
            updateViews(event, programStage, this.complete, this.tv, context);
        }

        public static void updateViews(Event event, ProgramStage programStage, Button button, TextView tv, Context context) {
            if (!event.getStatus().equals("COMPLETED")) {
                button.setText(C0845R.string.complete);
            } else if (context != null) {
                button.setText(context.getString(C0845R.string.incomplete));
                button.setBackgroundColor(SupportMenu.CATEGORY_MASK);
                tv.setVisibility(0);
            }
        }
    }

    public StatusRow(Context context, Event event, ProgramStage programStage) {
        this.context = context;
        this.mEvent = event;
        this.programStage = programStage;
    }

    public void setFragmentActivity(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    public View getView(FragmentManager fragmentManager, LayoutInflater inflater, View convertView, ViewGroup container) {
        View view;
        if (convertView == null || !(convertView.getTag() instanceof StatusViewHolder)) {
            View root = inflater.inflate(C0845R.layout.listview_row_status, container, false);
            this.holder = new StatusViewHolder(this.context, root, this.mEvent, this.programStage);
            root.setTag(this.holder);
            view = root;
        } else {
            view = convertView;
            this.holder = (StatusViewHolder) view.getTag();
        }
        this.holder.onValidateButtonClickListener.setFragmentActivity(this.fragmentActivity);
        this.holder.onCompleteButtonClickListener.setActivity(this.fragmentActivity);
        if (isEditable()) {
            this.holder.complete.setEnabled(true);
            this.holder.validate.setEnabled(true);
        } else {
            this.holder.complete.setEnabled(false);
            this.holder.validate.setEnabled(false);
        }
        return view;
    }

    public int getViewType() {
        return DataEntryRowTypes.EVENT_COORDINATES.ordinal();
    }
}
