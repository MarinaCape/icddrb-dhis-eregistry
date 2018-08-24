package org.icddrb.dhis.android.sdk.persistence.migrations.version19;

import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import org.icddrb.dhis.android.sdk.persistence.migrations.MigrationUtil;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityType;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityType.Table;

public class Version19MigrationRenameTrackedEntityTable extends AlterTableMigration<TrackedEntityType> {
    public Version19MigrationRenameTrackedEntityTable(Class<TrackedEntityType> cls) {
        super(TrackedEntityType.class);
    }

    public Version19MigrationRenameTrackedEntityTable() {
        super(TrackedEntityType.class);
    }

    public void onPreMigrate() {
        super.onPreMigrate();
        if (MigrationUtil.tableExists("TrackedEntity") && !MigrationUtil.tableExists(Table.TABLE_NAME)) {
            renameFrom("TrackedEntity");
        }
    }
}
