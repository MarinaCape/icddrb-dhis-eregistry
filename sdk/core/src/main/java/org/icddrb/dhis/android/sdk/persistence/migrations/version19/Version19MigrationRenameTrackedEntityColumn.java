package org.icddrb.dhis.android.sdk.persistence.migrations.version19;

import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import org.icddrb.dhis.android.sdk.persistence.migrations.MigrationUtil;
import org.icddrb.dhis.android.sdk.persistence.models.Program;

public class Version19MigrationRenameTrackedEntityColumn extends AlterTableMigration<Program> {
    public Version19MigrationRenameTrackedEntityColumn(Class<Program> cls) {
        super(Program.class);
    }

    public Version19MigrationRenameTrackedEntityColumn() {
        super(Program.class);
    }

    public void onPreMigrate() {
        super.onPreMigrate();
        if (MigrationUtil.columnExists(Program.class, "trackedEntityType")) {
            System.out.println("Norway - I exist");
        }
        if (!MigrationUtil.columnExists(Program.class, "trackedEntityType")) {
            addColumn(String.class, "trackedEntityType");
        }
    }
}
