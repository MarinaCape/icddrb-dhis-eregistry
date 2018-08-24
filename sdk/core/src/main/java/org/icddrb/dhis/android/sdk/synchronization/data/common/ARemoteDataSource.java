package org.icddrb.dhis.android.sdk.synchronization.data.common;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.lang.reflect.Type;
import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.network.DhisApi;
import org.icddrb.dhis.android.sdk.persistence.models.ApiResponse;
import org.icddrb.dhis.android.sdk.persistence.models.ImportSummary;
import org.icddrb.dhis.android.sdk.utils.StringConverter;
import org.joda.time.DateTime;
import retrofit.client.Response;
import retrofit.converter.ConversionException;

public abstract class ARemoteDataSource {
    public DhisApi dhisApi;

    public DateTime getServerTime() {
        return this.dhisApi.getSystemInfo().getServerDate();
    }

    public ImportSummary getImportSummary(Response response) {
        if (response.getStatus() != 200) {
            return null;
        }
        try {
            JsonNode node = DhisController.getInstance().getObjectMapper().readTree(new StringConverter().fromBody(response.getBody(), (Type) String.class));
            if (node == null) {
                return null;
            }
            if (node.has("response")) {
                return getPutImportSummary(response);
            }
            return getPostImportSummary(response);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ConversionException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private ImportSummary getPostImportSummary(Response response) {
        ImportSummary importSummary = null;
        try {
            return (ImportSummary) DhisController.getInstance().getObjectMapper().readValue(new StringConverter().fromBody(response.getBody(), (Type) String.class), ImportSummary.class);
        } catch (IOException e) {
            e.printStackTrace();
            return importSummary;
        } catch (ConversionException e2) {
            e2.printStackTrace();
            return importSummary;
        }
    }

    private ImportSummary getPutImportSummary(Response response) {
        try {
            ApiResponse apiResponse = (ApiResponse) DhisController.getInstance().getObjectMapper().readValue(new StringConverter().fromBody(response.getBody(), (Type) String.class), ApiResponse.class);
            if (!(apiResponse == null || apiResponse.getImportSummaries() == null || apiResponse.getImportSummaries().isEmpty())) {
                return (ImportSummary) apiResponse.getImportSummaries().get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ConversionException e2) {
            e2.printStackTrace();
        }
        return null;
    }
}
