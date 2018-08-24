package org.icddrb.dhis.android.sdk.persistence.migrations.version14;

import com.raizlabs.android.dbflow.sql.migration.UpdateTableMigration;
import org.icddrb.dhis.android.sdk.persistence.models.Event;

public class Version14MigrationSetEventDataToUnsent extends UpdateTableMigration<Event> {
    public Version14MigrationSetEventDataToUnsent(Class<Event> cls) {
        super(Event.class);
    }

    public Version14MigrationSetEventDataToUnsent() {
        super(Event.class);
    }

    public void onPreMigrate() {
    }
}
