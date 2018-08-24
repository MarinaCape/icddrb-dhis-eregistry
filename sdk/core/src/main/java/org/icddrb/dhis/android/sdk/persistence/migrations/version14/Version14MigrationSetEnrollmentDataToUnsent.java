package org.icddrb.dhis.android.sdk.persistence.migrations.version14;

import com.raizlabs.android.dbflow.sql.migration.UpdateTableMigration;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;

public class Version14MigrationSetEnrollmentDataToUnsent extends UpdateTableMigration<Enrollment> {
    public Version14MigrationSetEnrollmentDataToUnsent() {
        super(Enrollment.class);
    }

    public void onPreMigrate() {
    }
}
