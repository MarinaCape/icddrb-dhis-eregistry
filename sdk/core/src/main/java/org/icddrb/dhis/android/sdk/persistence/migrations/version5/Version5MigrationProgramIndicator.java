package org.icddrb.dhis.android.sdk.persistence.migrations.version5;

import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import org.icddrb.dhis.android.sdk.persistence.migrations.MigrationUtil;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramIndicator;

public class Version5MigrationProgramIndicator extends AlterTableMigration<ProgramIndicator> {
    public Version5MigrationProgramIndicator() {
        super(ProgramIndicator.class);
    }

    public void onPreMigrate() {
        super.onPreMigrate();
        if (!MigrationUtil.columnExists(ProgramIndicator.class, "missingValueReplacement")) {
            addColumn(Integer.class, "missingValueReplacement");
        }
    }
}
