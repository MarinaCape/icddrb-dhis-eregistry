package org.icddrb.dhis.android.sdk.persistence.migrations.version8;

import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import org.icddrb.dhis.android.sdk.persistence.migrations.MigrationUtil;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttribute.Table;

public class Version8MigrationTrackedEntityAttributeGenerated extends AlterTableMigration<TrackedEntityAttribute> {
    public Version8MigrationTrackedEntityAttributeGenerated(Class<TrackedEntityAttribute> cls) {
        super(TrackedEntityAttribute.class);
    }

    public Version8MigrationTrackedEntityAttributeGenerated() {
        super(TrackedEntityAttribute.class);
    }

    public void onPreMigrate() {
        super.onPreMigrate();
        if (!MigrationUtil.columnExists(TrackedEntityAttribute.class, Table.GENERATED)) {
            addColumn(Boolean.class, Table.GENERATED);
        }
        if (!MigrationUtil.columnExists(TrackedEntityAttribute.class, Table.PATTERN)) {
            addColumn(String.class, Table.PATTERN);
        }
    }
}
