package org.icddrb.dhis.android.sdk.utils;

import android.util.Log;
import com.facebook.stetho.server.http.HttpStatus;
import com.raizlabs.android.dbflow.structure.BaseModel;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.network.APIException;
import org.icddrb.dhis.android.sdk.persistence.models.Conflict;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.FailedItem;
import org.icddrb.dhis.android.sdk.persistence.models.ImportSummary;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramIndicator;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRuleAction.Table;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;
import retrofit.client.Header;
import retrofit.converter.ConversionException;

public class NetworkUtils {
    private NetworkUtils() {
    }

    public static <T> List<T> unwrapResponse(Map<String, List<T>> response, String key) {
        if (response == null || !response.containsKey(key) || response.get(key) == null) {
            return new ArrayList();
        }
        return (List) response.get(key);
    }

    public static Header findLocationHeader(List<Header> headers) {
        String LOCATION = Table.LOCATION;
        if (!(headers == null || headers.isEmpty())) {
            for (Header header : headers) {
                if (header.getName().equalsIgnoreCase(Table.LOCATION)) {
                    return header;
                }
            }
        }
        return null;
    }

    public static void handleApiException(APIException apiException) throws APIException {
        handleApiException(apiException, null);
    }

    public static void handleApiException(APIException apiException, BaseModel model) throws APIException {
        switch (apiException.getKind()) {
            case HTTP:
                switch (apiException.getResponse().getStatus()) {
                    case 400:
                    case 403:
                    case 409:
                    case HttpStatus.HTTP_INTERNAL_SERVER_ERROR /*500*/:
                        return;
                    case 401:
                        throw apiException;
                    case HttpStatus.HTTP_NOT_FOUND /*404*/:
                        if (model != null) {
                            model.delete();
                            return;
                        }
                        return;
                    default:
                        return;
                }
            case CONVERSION:
            case UNEXPECTED:
                throw apiException;
            default:
                return;
        }
    }

    public static void handleTrackedEntityInstanceSendException(APIException apiException, TrackedEntityInstance trackedEntityInstance) {
        handleSerializableItemException(apiException, "TrackedEntityInstance", trackedEntityInstance.getLocalId());
    }

    public static void handleEnrollmentSendException(APIException apiException, Enrollment enrollment) {
        handleSerializableItemException(apiException, "Enrollment", enrollment.getLocalId());
    }

    public static void handleEventSendException(APIException apiException, Event event) {
        handleSerializableItemException(apiException, "Event", event.getLocalId());
    }

    private static void handleSerializableItemException(APIException apiException, String type, long id) {
        FailedItem failedItem = TrackerController.getFailedItem(type, id);
        switch (apiException.getKind()) {
            case NETWORK:
                if (failedItem == null) {
                    failedItem = new FailedItem();
                }
                String cause = "Network error\n\n";
                if (!(apiException == null || apiException.getCause() == null)) {
                    failedItem.setErrorMessage(cause + ExceptionUtils.getStackTrace(apiException.getCause()));
                }
                failedItem.setHttpStatusCode(-1);
                failedItem.setItemId(id);
                failedItem.setItemType(type);
                failedItem.setFailCount(failedItem.getFailCount() + 1);
                failedItem.save();
                return;
            default:
                if (failedItem == null) {
                    failedItem = new FailedItem();
                }
                if (apiException.getResponse() != null) {
                    failedItem.setHttpStatusCode(apiException.getResponse().getStatus());
                    try {
                        failedItem.setErrorMessage(new StringConverter().fromBody(apiException.getResponse().getBody(), (Type) String.class));
                    } catch (ConversionException e) {
                        e.printStackTrace();
                    }
                }
                failedItem.setItemId(id);
                failedItem.setItemType(type);
                failedItem.setHttpStatusCode(apiException.getResponse().getStatus());
                failedItem.setFailCount(failedItem.getFailCount() + 1);
                failedItem.save();
                return;
        }
    }

    public static void handleImportSummaryError(ImportSummary importSummary, String type, int code, long id) {
        FailedItem failedItem = new FailedItem();
        failedItem.setImportSummary(importSummary);
        failedItem.setItemId(id);
        failedItem.setItemType(type);
        failedItem.setHttpStatusCode(code);
        failedItem.save();
        if (!(failedItem.getImportSummary() == null || failedItem.getImportSummary().getConflicts() == null)) {
            for (Conflict conflict : failedItem.getImportSummary().getConflicts()) {
                conflict.setImportSummary(failedItem.getImportSummary().getId());
                conflict.save();
            }
        }
        Log.d("NetworkUtils", "saved item: " + failedItem.getItemId() + ProgramIndicator.SEP_OBJECT + failedItem.getItemType());
    }
}
