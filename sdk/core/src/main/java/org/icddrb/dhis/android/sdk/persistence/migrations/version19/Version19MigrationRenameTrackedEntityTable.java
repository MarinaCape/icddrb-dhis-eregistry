package org.icddrb.dhis.android.sdk.persistence.migrations.version19;

import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;

import org.icddrb.dhis.android.sdk.persistence.Dhis2Database;
import org.icddrb.dhis.android.sdk.persistence.migrations.MigrationUtil;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityType;


@Migration(version = 19, databaseName = Dhis2Database.NAME)
public class Version19MigrationRenameTrackedEntityTable extends
        AlterTableMigration<TrackedEntityType> {

    public Version19MigrationRenameTrackedEntityTable(Class<TrackedEntityType> table) {
        super(TrackedEntityType.class);
    }

    public Version19MigrationRenameTrackedEntityTable() {
        super(TrackedEntityType.class);
    }

    @Override
    public void onPreMigrate() {
        super.onPreMigrate();
        if (MigrationUtil.tableExists("TrackedEntity") && !MigrationUtil.tableExists("TrackedEntityType")) {
            renameFrom("TrackedEntity");
        }
    }
}
