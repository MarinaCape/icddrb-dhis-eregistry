package org.icddrb.dhis.android.sdk.persistence.migrations.version13;

import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import org.icddrb.dhis.android.sdk.persistence.migrations.MigrationUtil;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRule;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRule.Table;

public class Version13MigrationProgramRule extends AlterTableMigration<ProgramRule> {
    public Version13MigrationProgramRule(Class<ProgramRule> cls) {
        super(ProgramRule.class);
    }

    public Version13MigrationProgramRule() {
        super(ProgramRule.class);
    }

    public void onPreMigrate() {
        super.onPreMigrate();
        if (!MigrationUtil.columnExists(ProgramRule.class, Table.PRIORITY)) {
            addColumn(Integer.class, Table.PRIORITY);
        }
    }
}
