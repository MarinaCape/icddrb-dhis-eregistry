package org.icddrb.dhis.android.sdk.persistence.migrations.version8;

import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import org.icddrb.dhis.android.sdk.persistence.migrations.MigrationUtil;
import org.icddrb.dhis.android.sdk.persistence.models.Program;
import org.icddrb.dhis.android.sdk.persistence.models.Program.Table;

public class Version8MigrationProgramDisplayFrontPageList extends AlterTableMigration<Program> {
    public Version8MigrationProgramDisplayFrontPageList(Class<Program> cls) {
        super(Program.class);
    }

    public Version8MigrationProgramDisplayFrontPageList() {
        super(Program.class);
    }

    public void onPreMigrate() {
        super.onPreMigrate();
        if (!MigrationUtil.columnExists(Program.class, Table.DISPLAYFRONTPAGELIST)) {
            addColumn(Boolean.class, Table.DISPLAYFRONTPAGELIST);
        }
    }
}
