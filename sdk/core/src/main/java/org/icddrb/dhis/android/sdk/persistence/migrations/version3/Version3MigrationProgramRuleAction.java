package org.icddrb.dhis.android.sdk.persistence.migrations.version3;

import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import org.icddrb.dhis.android.sdk.persistence.migrations.MigrationUtil;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRuleAction;

public class Version3MigrationProgramRuleAction extends AlterTableMigration<ProgramRuleAction> {
    public Version3MigrationProgramRuleAction() {
        super(ProgramRuleAction.class);
    }

    public void onPreMigrate() {
        super.onPreMigrate();
        if (!MigrationUtil.columnExists(ProgramRuleAction.class, "programStageSection")) {
            addColumn(String.class, "programStageSection");
        }
    }
}
