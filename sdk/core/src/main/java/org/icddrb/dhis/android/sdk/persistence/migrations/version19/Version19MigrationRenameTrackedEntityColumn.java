package org.icddrb.dhis.android.sdk.persistence.migrations.version19;

import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;

import org.icddrb.dhis.android.sdk.persistence.Dhis2Database;
import org.icddrb.dhis.android.sdk.persistence.migrations.MigrationUtil;
import org.icddrb.dhis.android.sdk.persistence.models.Program;


@Migration(version = 19, databaseName = Dhis2Database.NAME)
public class Version19MigrationRenameTrackedEntityColumn extends
        AlterTableMigration<Program> {

    public Version19MigrationRenameTrackedEntityColumn(Class<Program> table) {
        super(Program.class);
    }

    public Version19MigrationRenameTrackedEntityColumn() {
        super(Program.class);
    }

    @Override
    public void onPreMigrate() {
        super.onPreMigrate();
        if (MigrationUtil.columnExists(Program.class,"trackedEntityType")) {
            System.out.println("Norway - I exist");
        }
        if (!MigrationUtil.columnExists(Program.class, "trackedEntityType")) {
            addColumn(String.class, "trackedEntityType");
        }
    }
}
