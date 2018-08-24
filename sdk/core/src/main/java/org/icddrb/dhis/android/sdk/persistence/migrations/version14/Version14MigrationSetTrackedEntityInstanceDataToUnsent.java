package org.icddrb.dhis.android.sdk.persistence.migrations.version14;

import com.raizlabs.android.dbflow.sql.migration.UpdateTableMigration;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityInstance;

public class Version14MigrationSetTrackedEntityInstanceDataToUnsent extends UpdateTableMigration<TrackedEntityInstance> {
    public Version14MigrationSetTrackedEntityInstanceDataToUnsent() {
        super(TrackedEntityInstance.class);
    }

    public void onPreMigrate() {
    }
}
