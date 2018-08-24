package org.icddrb.dhis.android.sdk.persistence.migrations.version13;

import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import org.icddrb.dhis.android.sdk.persistence.migrations.MigrationUtil;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRuleAction;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramRuleAction.Table;

public class Version13MigrationProgramRuleAction extends AlterTableMigration<ProgramRuleAction> {
    public Version13MigrationProgramRuleAction(Class<ProgramRuleAction> cls) {
        super(ProgramRuleAction.class);
    }

    public Version13MigrationProgramRuleAction() {
        super(ProgramRuleAction.class);
    }

    public void onPreMigrate() {
        super.onPreMigrate();
        if (!MigrationUtil.columnExists(ProgramRuleAction.class, "trackedEntityAttribute")) {
            addColumn(String.class, "trackedEntityAttribute");
        }
        if (!MigrationUtil.columnExists(ProgramRuleAction.class, Table.LOCATION)) {
            addColumn(String.class, Table.LOCATION);
        }
        if (!MigrationUtil.columnExists(ProgramRuleAction.class, Table.CONTENT)) {
            addColumn(String.class, Table.CONTENT);
        }
        if (!MigrationUtil.columnExists(ProgramRuleAction.class, Table.DATA)) {
            addColumn(String.class, Table.DATA);
        }
    }
}
