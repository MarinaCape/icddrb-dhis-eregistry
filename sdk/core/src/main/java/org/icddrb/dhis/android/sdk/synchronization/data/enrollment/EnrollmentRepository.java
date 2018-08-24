package org.icddrb.dhis.android.sdk.synchronization.data.enrollment;

import java.util.List;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.ImportSummary;
import org.icddrb.dhis.android.sdk.synchronization.domain.enrollment.IEnrollmentRepository;

public class EnrollmentRepository implements IEnrollmentRepository {
    EnrollmentLocalDataSource mEnrollmentLocalDataSource;
    EnrollmentRemoteDataSource mEnrollmentRemoteDataSource;

    public EnrollmentRepository(EnrollmentLocalDataSource enrollmentLocalDataSource, EnrollmentRemoteDataSource enrollmentRemoteDataSource) {
        this.mEnrollmentLocalDataSource = enrollmentLocalDataSource;
        this.mEnrollmentRemoteDataSource = enrollmentRemoteDataSource;
    }

    public void save(Enrollment enrollment) {
        this.mEnrollmentLocalDataSource.save(enrollment);
    }

    public ImportSummary sync(Enrollment enrollment) {
        ImportSummary importSummary = this.mEnrollmentRemoteDataSource.save(enrollment);
        if (ImportSummary.SUCCESS.equals(importSummary.getStatus()) || ImportSummary.OK.equals(importSummary.getStatus())) {
            updateEnrollmentTimestamp(enrollment);
        }
        return importSummary;
    }

    private void updateEnrollmentTimestamp(Enrollment enrollment) {
        Enrollment remoteEvent = this.mEnrollmentRemoteDataSource.getEnrollment(enrollment.getEnrollment());
        enrollment.setCreated(remoteEvent.getCreated());
        enrollment.setLastUpdated(remoteEvent.getLastUpdated());
        this.mEnrollmentLocalDataSource.save(enrollment);
    }

    public Enrollment getEnrollment(String enrollmentUid) {
        return this.mEnrollmentLocalDataSource.getEnrollment(enrollmentUid);
    }

    public List<Enrollment> getEnrollmentsByTrackedEntityInstanceId(long trackedEntityInstancelocalId) {
        return this.mEnrollmentLocalDataSource.getEnrollmentsByTrackedEntityInstanceId(trackedEntityInstancelocalId);
    }
}
