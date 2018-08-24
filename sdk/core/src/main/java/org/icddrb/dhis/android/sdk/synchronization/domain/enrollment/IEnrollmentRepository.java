package org.icddrb.dhis.android.sdk.synchronization.domain.enrollment;

import java.util.List;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.ImportSummary;

public interface IEnrollmentRepository {
    Enrollment getEnrollment(String str);

    List<Enrollment> getEnrollmentsByTrackedEntityInstanceId(long j);

    void save(Enrollment enrollment);

    ImportSummary sync(Enrollment enrollment);
}
