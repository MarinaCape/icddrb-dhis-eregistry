package org.icddrb.dhis.android.sdk.synchronization.data.enrollment;

import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import java.util.List;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;

public class EnrollmentLocalDataSource {
    public void save(Enrollment enrollment) {
        enrollment.save();
    }

    public List<Enrollment> getEnrollmentsByTrackedEntityInstanceId(long localTEIId) {
        return new Select().from(Enrollment.class).where(Condition.column("localTrackedEntityInstanceId").is(Long.valueOf(localTEIId))).and(Condition.column("fromServer").is(Boolean.valueOf(false))).queryList();
    }

    public Enrollment getEnrollment(String enrollmentUid) {
        return (Enrollment) new Select().from(Enrollment.class).where(Condition.column("enrollment").is(enrollmentUid)).querySingle();
    }
}
