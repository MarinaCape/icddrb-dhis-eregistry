package org.icddrb.dhis.android.sdk.ui.dialogs;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.raizlabs.android.dbflow.structure.Model;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.icddrb.dhis.android.sdk.R;
import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.job.JobExecutor;
import org.icddrb.dhis.android.sdk.job.NetworkJob;
import org.icddrb.dhis.android.sdk.network.APIException;
import org.icddrb.dhis.android.sdk.persistence.loaders.DbLoader;
import org.icddrb.dhis.android.sdk.persistence.models.BaseSerializableModel;
import org.icddrb.dhis.android.sdk.persistence.models.Conflict;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.FailedItem;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import org.icddrb.dhis.android.sdk.persistence.preferences.ResourceType;
import org.icddrb.dhis.android.sdk.synchronization.data.enrollment.EnrollmentLocalDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.enrollment.EnrollmentRemoteDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.enrollment.EnrollmentRepository;
import org.icddrb.dhis.android.sdk.synchronization.data.event.EventLocalDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.event.EventRemoteDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.event.EventRepository;
import org.icddrb.dhis.android.sdk.synchronization.data.faileditem.FailedItemRepository;
import org.icddrb.dhis.android.sdk.synchronization.data.trackedentityinstance.TrackedEntityInstanceLocalDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.trackedentityinstance.TrackedEntityInstanceRemoteDataSource;
import org.icddrb.dhis.android.sdk.synchronization.data.trackedentityinstance.TrackedEntityInstanceRepository;
import org.icddrb.dhis.android.sdk.synchronization.domain.enrollment.IEnrollmentRepository;
import org.icddrb.dhis.android.sdk.synchronization.domain.event.SyncEventUseCase;
import org.icddrb.dhis.android.sdk.ui.views.FontTextView;
import org.icddrb.dhis.android.sdk.utils.LogUtils;

public abstract class ItemStatusDialogFragment extends DialogFragment implements OnClickListener, LoaderCallbacks<ItemStatusDialogFragmentForm> {
    public static final String EXTRA_ARGUMENTS = "extra:Arguments";
    public static final String EXTRA_ID = "extra:id";
    public static final String EXTRA_SAVED_INSTANCE_STATE = "extra:savedInstanceState";
    public static final String EXTRA_TYPE = "extra:type";
    private static final int LOADER_ID = 9564013;
    private static final String TAG = ItemStatusDialogFragment.class.getSimpleName();
    private LinearLayout detailsTrigger;
    private FontTextView mDetails;
    private int mDialogId;
    private TextView mDialogLabel;
    private ItemStatusDialogFragmentForm mForm;
    private ImageView mItemStatusImage;
    private FontTextView mStatus;
    private ImageView mTriangle;

    /* renamed from: org.icddrb.dhis.android.sdk.ui.dialogs.ItemStatusDialogFragment$1 */
    class C08921 implements OnMenuItemClickListener {
        C08921() {
        }

        public boolean onMenuItemClick(MenuItem item) {
            ItemStatusDialogFragment.this.onContextItemSelected(item);
            return true;
        }
    }

    private class MediaScanner implements MediaScannerConnectionClient {
        private MediaScannerConnection mediaScannerConnection;
        private String pathToFile;

        public MediaScanner(Context context, String pathToFile) {
            this.pathToFile = pathToFile;
            this.mediaScannerConnection = new MediaScannerConnection(context, this);
            this.mediaScannerConnection.connect();
        }

        public void onMediaScannerConnected() {
            this.mediaScannerConnection.scanFile(this.pathToFile, null);
        }

        public void onScanCompleted(String path, Uri uri) {
            this.mediaScannerConnection.disconnect();
        }
    }

    public abstract void sendToServer(BaseSerializableModel baseSerializableModel, ItemStatusDialogFragment itemStatusDialogFragment);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(1, R.style.Theme_AppCompat_Light_Dialog);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(2);
        return inflater.inflate(R.layout.dialog_fragment_trackedentityinstancestatus, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.mItemStatusImage = (ImageView) view.findViewById(R.id.itemstatus);
        this.detailsTrigger = (LinearLayout) view.findViewById(R.id.detailTrigger);
        this.mDetails = (FontTextView) view.findViewById(R.id.item_detailed_info);
        this.mTriangle = (ImageView) view.findViewById(R.id.triangle);
        this.mStatus = (FontTextView) view.findViewById(R.id.statusinfo);
        ImageView syncDialogButton = (ImageView) view.findViewById(R.id.sync_dialog_button);
        ImageView closeDialogButton = (ImageView) view.findViewById(R.id.close_dialog_button);
        this.mDialogLabel = (TextView) view.findViewById(R.id.dialog_label);
        closeDialogButton.setOnClickListener(this);
        syncDialogButton.setOnClickListener(this);
        this.mDetails.setOnClickListener(this);
        this.detailsTrigger.setOnClickListener(this);
        registerForContextMenu(this.mDetails);
        setDialogLabel(R.string.status);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle argumentsBundle = new Bundle();
        argumentsBundle.putBundle("extra:Arguments", getArguments());
        argumentsBundle.putBundle("extra:savedInstanceState", savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, argumentsBundle, this);
    }

    public Loader<ItemStatusDialogFragmentForm> onCreateLoader(int id, Bundle args) {
        if (LOADER_ID != id || !isAdded()) {
            return null;
        }
        List<Class<? extends Model>> modelsToTrack = new ArrayList();
        modelsToTrack.add(TrackedEntityInstance.class);
        modelsToTrack.add(Enrollment.class);
        modelsToTrack.add(Event.class);
        modelsToTrack.add(FailedItem.class);
        Bundle fragmentArguments = args.getBundle("extra:Arguments");
        return new DbLoader(getActivity().getBaseContext(), modelsToTrack, new ItemStatusDialogFragmentQuery(fragmentArguments.getLong(EXTRA_ID), fragmentArguments.getString(EXTRA_TYPE)));
    }

    public void onLoadFinished(Loader<ItemStatusDialogFragmentForm> loader, ItemStatusDialogFragmentForm data) {
        Log.d(TAG, "load finished");
        if (loader.getId() == LOADER_ID && isAdded()) {
            this.mForm = data;
            switch (this.mForm.getStatus()) {
                case SENT:
                    this.detailsTrigger.setVisibility(8);
                    this.mItemStatusImage.setImageResource(R.drawable.ic_from_server);
                    this.mStatus.setText(getString(R.string.status_sent_description));
                    return;
                case ERROR:
                    this.detailsTrigger.setVisibility(0);
                    FailedItem failedItem = TrackerController.getFailedItem(data.getType(), data.getItem().getLocalId());
                    if (failedItem.getHttpStatusCode() == -1) {
                        this.mItemStatusImage.setImageResource(R.drawable.ic_offline);
                    } else {
                        this.mItemStatusImage.setImageResource(R.drawable.ic_event_error);
                    }
                    this.mStatus.setText(getString(R.string.default_synchronisation_error));
                    if (failedItem != null) {
                        String details = "";
                        if (failedItem.getErrorMessage() != null) {
                            details = details + toPrettyFormat(failedItem.getErrorMessage().toString()) + '\n';
                        }
                        if (!(failedItem.getImportSummary() == null || failedItem.getImportSummary().getDescription() == null)) {
                            details = details + toPrettyFormat(failedItem.getImportSummary().getDescription()) + '\n';
                        }
                        if (!(failedItem.getImportSummary() == null || failedItem.getImportSummary().getConflicts() == null)) {
                            for (Conflict conflict : failedItem.getImportSummary().getConflicts()) {
                                if (conflict != null) {
                                    details = details + conflict.getObject() + ": " + conflict.getValue() + StringUtils.LF;
                                }
                            }
                        }
                        this.mDetails.setText(details);
                        return;
                    }
                    return;
                case OFFLINE:
                    this.detailsTrigger.setVisibility(8);
                    this.mStatus.setText(getString(R.string.status_offline_description));
                    this.mItemStatusImage.setImageResource(R.drawable.ic_offline);
                    return;
                default:
                    return;
            }
        }
    }

    public static String toPrettyFormat(String jsonString) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(jsonString).getAsJsonObject());
    }

    public void onLoaderReset(Loader<ItemStatusDialogFragmentForm> loader) {
    }

    public void setDialogLabel(int resourceId) {
        if (this.mDialogLabel != null) {
            this.mDialogLabel.setText(resourceId);
        }
    }

    public void setDialogLabel(CharSequence sequence) {
        if (this.mDialogLabel != null) {
            this.mDialogLabel.setText(sequence);
        }
    }

    public void setDialogId(int dialogId) {
        this.mDialogId = dialogId;
    }

    public int getDialogId() {
        return this.mDialogId;
    }

    public CharSequence getDialogLabel() {
        if (this.mDialogLabel != null) {
            return this.mDialogLabel.getText();
        }
        return null;
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, TAG);
    }

    public void copyToClipboard() {
        if (this.mDetails != null && this.mDetails.getText().length() > 0) {
            ((ClipboardManager) getActivity().getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("Copied Text", this.mDetails.getText()));
            Toast.makeText(getActivity(), getString(R.string.copied_text), 0).show();
        }
    }

    public void writeErrorToSDCard() {
        if (this.mDetails != null && this.mDetails.getText().length() > 0) {
            StringBuilder filePath = new StringBuilder();
            String dir = getResources().getString(R.string.directory);
            String fileName = getResources().getString(R.string.error_log_file_name);
            filePath.append(Environment.getExternalStorageDirectory().getAbsolutePath());
            filePath.append(dir);
            filePath.append(fileName);
            boolean success = LogUtils.writeErrorLogToSDCard(filePath.toString(), this.mDetails.getText().toString());
            MediaScanner mediaScanner = new MediaScanner(getActivity(), filePath.toString());
            if (success) {
                Toast.makeText(getActivity(), getResources().getString(R.string.text_written_to_sd_card), 0).show();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.sd_card_error_message), 0).show();
            }
        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.sync_dialog_button) {
            Toast.makeText(getActivity(), getString(R.string.sending_data_server), 1).show();
            sendToServer(this.mForm.getItem(), this);
            dismiss();
        } else if (v.getId() == R.id.close_dialog_button) {
            dismiss();
        } else if (v.getId() == R.id.item_detailed_info) {
            if (this.mDetails != null && this.mDetails.getText().length() > 0) {
            }
        } else if (v.getId() != R.id.detailTrigger) {
        } else {
            if (this.mDetails.getVisibility() == 0) {
                this.mDetails.setVisibility(8);
                this.mTriangle.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_triangle_collapsed));
                return;
            }
            this.mDetails.setVisibility(0);
            this.mTriangle.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_triangle_expanded));
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        String[] contextMenu;
        int i;
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.item_detailed_info) {
            menu.setHeaderTitle(R.string.error_description);
            contextMenu = getResources().getStringArray(R.array.copy_store_error_message);
        } else {
            menu.setHeaderTitle(R.string.error_description);
            contextMenu = getResources().getStringArray(R.array.copy_store_error_message);
        }
        for (i = 0; i < contextMenu.length; i++) {
            menu.add(0, i, i, contextMenu[i]);
        }
        OnMenuItemClickListener listener = new C08921();
        int n = menu.size();
        for (i = 0; i < n; i++) {
            menu.getItem(i).setOnMenuItemClickListener(listener);
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        int menuItemIndex = item.getItemId();
        String[] contextMenu = getResources().getStringArray(R.array.copy_store_error_message);
        String menuItemName = contextMenu[menuItemIndex];
        if (item.getTitle().toString().equalsIgnoreCase(contextMenu[0])) {
            copyToClipboard();
        } else if (item.getTitle().toString().equalsIgnoreCase(contextMenu[1])) {
            writeErrorToSDCard();
        }
        return super.onContextItemSelected(item);
    }

    public static void sendEvent(final Event event) {
        JobExecutor.enqueueJob(new NetworkJob<Object>(0, ResourceType.EVENT) {
            public Object execute() throws APIException {
                IEnrollmentRepository enrollmentRepository = new EnrollmentRepository(new EnrollmentLocalDataSource(), new EnrollmentRemoteDataSource(DhisController.getInstance().getDhisApi()));
                new SyncEventUseCase(new EventRepository(new EventLocalDataSource(), new EventRemoteDataSource(DhisController.getInstance().getDhisApi())), enrollmentRepository, new TrackedEntityInstanceRepository(new TrackedEntityInstanceLocalDataSource(), new TrackedEntityInstanceRemoteDataSource(DhisController.getInstance().getDhisApi())), new FailedItemRepository()).execute(event);
                return new Object();
            }
        });
    }
}
