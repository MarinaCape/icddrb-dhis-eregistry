package org.icddrb.dhis.android.sdk.persistence.migrations.version10;

import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import org.icddrb.dhis.android.sdk.persistence.migrations.MigrationUtil;
import org.icddrb.dhis.android.sdk.persistence.models.Event;
import org.icddrb.dhis.android.sdk.persistence.models.Event.Table;

public class Version10MigrationCompletedDate extends AlterTableMigration<Event> {
    public Version10MigrationCompletedDate(Class<Event> cls) {
        super(Event.class);
    }

    public Version10MigrationCompletedDate() {
        super(Event.class);
    }

    public void onPreMigrate() {
        super.onPreMigrate();
        if (!MigrationUtil.columnExists(Event.class, Table.COMPLETEDDATE)) {
            addColumn(String.class, Table.COMPLETEDDATE);
        }
    }
}
