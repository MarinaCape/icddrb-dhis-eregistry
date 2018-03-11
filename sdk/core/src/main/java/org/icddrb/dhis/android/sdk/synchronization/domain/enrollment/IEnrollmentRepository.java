package org.icddrb.dhis.android.sdk.synchronization.domain.enrollment;


import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.ImportSummary;

import java.util.List;

public interface IEnrollmentRepository {
    void save (Enrollment enrollment);

    ImportSummary sync (Enrollment enrollment);

    Enrollment getEnrollment(String enrollmentUid);

    List<Enrollment> getEnrollmentsByTrackedEntityInstanceId(long trackedEntityInstanceLocalId);
}
