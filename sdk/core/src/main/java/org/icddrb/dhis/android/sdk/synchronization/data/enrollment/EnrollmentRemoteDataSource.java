package org.icddrb.dhis.android.sdk.synchronization.data.enrollment;

import java.util.HashMap;
import java.util.Map;
import org.icddrb.dhis.android.sdk.network.APIException;
import org.icddrb.dhis.android.sdk.network.DhisApi;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.ImportSummary;
import org.icddrb.dhis.android.sdk.synchronization.data.common.ARemoteDataSource;

public class EnrollmentRemoteDataSource extends ARemoteDataSource {
    public EnrollmentRemoteDataSource(DhisApi dhisApi) {
        this.dhisApi = dhisApi;
    }

    public Enrollment getEnrollment(String enrollment) {
        Map<String, String> QUERY_PARAMS = new HashMap();
        QUERY_PARAMS.put("fields", "created,lastUpdated");
        return this.dhisApi.getEnrollment(enrollment, QUERY_PARAMS);
    }

    public ImportSummary save(Enrollment enrollment) {
        if (enrollment.getCreated() == null) {
            return postEnrollment(enrollment, this.dhisApi);
        }
        return putEnrollment(enrollment, this.dhisApi);
    }

    private ImportSummary postEnrollment(Enrollment enrollment, DhisApi dhisApi) throws APIException {
        return getImportSummary(dhisApi.postEnrollment(enrollment));
    }

    private ImportSummary putEnrollment(Enrollment enrollment, DhisApi dhisApi) throws APIException {
        return getImportSummary(dhisApi.putEnrollment(enrollment.getEnrollment(), enrollment));
    }
}
