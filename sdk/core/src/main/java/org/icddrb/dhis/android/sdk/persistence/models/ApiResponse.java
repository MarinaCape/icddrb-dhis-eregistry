package org.icddrb.dhis.android.sdk.persistence.models;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.persistence.models.ImportSummary.Status;

public class ApiResponse {
    public static final String RESPONSETYPE_IMPORTSUMMARIES = "ImportSummaries";
    public static final String RESPONSETYPE_IMPORTSUMMARY = "ImportSummary";
    @JsonProperty("ignored")
    int ignored;
    @JsonProperty("importCount")
    private ImportCount importCount;
    @JsonIgnore
    List<ImportSummary> importSummaries;
    @JsonProperty("importSummaries")
    private List<ImportSummary> importSummaries2;
    @JsonProperty("imported")
    int imported;
    @JsonProperty("responseType")
    private String responseType;
    @JsonProperty("status")
    private Status status;

    /* renamed from: org.icddrb.dhis.android.sdk.persistence.models.ApiResponse$1 */
    class C08621 extends TypeReference<List<ImportSummary>> {
        C08621() {
        }
    }

    @JsonProperty("response")
    public void setResponse(Map<String, Object> response) {
        try {
            String responseType = (String) response.get("responseType");
            if (responseType.equals(RESPONSETYPE_IMPORTSUMMARIES)) {
                this.importSummaries = (List) DhisController.getInstance().getObjectMapper().convertValue(response.get("importSummaries"), new C08621());
            } else if (responseType.equals("ImportSummary")) {
                ImportSummary importSummary = (ImportSummary) DhisController.getInstance().getObjectMapper().convertValue((Object) response, ImportSummary.class);
                this.importSummaries = new ArrayList();
                this.importSummaries.add(importSummary);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
    }

    public List<ImportSummary> getImportSummaries() {
        if (this.importSummaries == null) {
            return this.importSummaries2;
        }
        return this.importSummaries;
    }

    @JsonIgnore
    public void setImportSummaries(List<ImportSummary> importSummaries) {
        this.importSummaries = importSummaries;
    }

    public String getResponseType() {
        return this.responseType;
    }

    public Status getStatus() {
        return this.status;
    }

    public ImportCount getImportCount() {
        return this.importCount;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setImportCount(ImportCount importCount) {
        this.importCount = importCount;
    }
}
