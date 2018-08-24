package org.icddrb.dhis.android.sdk.persistence.migrations.version17;

import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import org.icddrb.dhis.android.sdk.persistence.migrations.MigrationUtil;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRuleAction;

public class Version17MigrationCreateProgramStageField extends AlterTableMigration<ProgramRuleAction> {
    public Version17MigrationCreateProgramStageField(Class<ProgramRuleAction> cls) {
        super(ProgramRuleAction.class);
    }

    public Version17MigrationCreateProgramStageField() {
        super(ProgramRuleAction.class);
    }

    public void onPreMigrate() {
        super.onPreMigrate();
        if (!MigrationUtil.columnExists(ProgramRuleAction.class, "programStage")) {
            addColumn(Boolean.class, "programStage");
        }
    }
}
