package org.icddrb.dhis.android.sdk.synchronization.domain.common;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.controllers.tracker.TrackerController;
import org.icddrb.dhis.android.sdk.network.APIException;
import org.icddrb.dhis.android.sdk.persistence.models.ApiResponse;
import org.icddrb.dhis.android.sdk.persistence.models.Conflict;
import org.icddrb.dhis.android.sdk.persistence.models.FailedItem;
import org.icddrb.dhis.android.sdk.persistence.models.ImportSummary;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramIndicator;
import org.icddrb.dhis.android.sdk.synchronization.domain.faileditem.IFailedItemRepository;
import org.icddrb.dhis.android.sdk.utils.StringConverter;
import retrofit.client.Response;
import retrofit.converter.ConversionException;

public class Synchronizer {
    IFailedItemRepository mFailedItemRepository;

    public Synchronizer(IFailedItemRepository failedItemRepository) {
        this.mFailedItemRepository = failedItemRepository;
    }

    public void clearFailedItem(String type, long id) {
        this.mFailedItemRepository.delete(type, id);
    }

    public void handleImportSummaryError(ImportSummary importSummary, String type, int code, long id) {
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
        System.out.println("saved item: " + failedItem.getItemId() + ProgramIndicator.SEP_OBJECT + failedItem.getItemType());
    }

    public void handleSerializableItemException(APIException apiException, String type, long id) {
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
                this.mFailedItemRepository.save(failedItem);
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
                if (apiException.getResponse() != null) {
                    failedItem.setHttpStatusCode(apiException.getResponse().getStatus());
                }
                failedItem.setFailCount(failedItem.getFailCount() + 1);
                this.mFailedItemRepository.save(failedItem);
                return;
        }
    }

    public List<ImportSummary> getImportSummary(Response response) {
        List<ImportSummary> list = null;
        if (response.getStatus() == 409) {
            try {
                JsonNode node = DhisController.getInstance().getObjectMapper().readTree(new StringConverter().fromBody(response.getBody(), (Type) String.class));
                if (node != null && node.has("response")) {
                    list = getFailedBatchImportSummary(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ConversionException e2) {
                e2.printStackTrace();
            }
        }
        return list;
    }

    private List<ImportSummary> getFailedBatchImportSummary(Response response) {
        try {
            ApiResponse apiResponse = (ApiResponse) DhisController.getInstance().getObjectMapper().readValue(new StringConverter().fromBody(response.getBody(), (Type) String.class), ApiResponse.class);
            if (!(apiResponse == null || apiResponse.getImportSummaries() == null || apiResponse.getImportSummaries().isEmpty())) {
                return apiResponse.getImportSummaries();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ConversionException e2) {
            e2.printStackTrace();
        }
        return null;
    }
}
