package org.icddrb.dhis.android.sdk.persistence.migrations.version13;

import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import org.icddrb.dhis.android.sdk.persistence.migrations.MigrationUtil;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRuleVariable;

public class Version13MigrationProgramRuleVariable extends AlterTableMigration<ProgramRuleVariable> {
    public Version13MigrationProgramRuleVariable(Class<ProgramRuleVariable> cls) {
        super(ProgramRuleVariable.class);
    }

    public Version13MigrationProgramRuleVariable() {
        super(ProgramRuleVariable.class);
    }

    public void onPreMigrate() {
        super.onPreMigrate();
        if (!MigrationUtil.columnExists(ProgramRuleVariable.class, "trackedEntityAttribute")) {
            addColumn(String.class, "trackedEntityAttribute");
        }
        if (!MigrationUtil.columnExists(ProgramRuleVariable.class, "programStage")) {
            addColumn(String.class, "programStage");
        }
    }
}
