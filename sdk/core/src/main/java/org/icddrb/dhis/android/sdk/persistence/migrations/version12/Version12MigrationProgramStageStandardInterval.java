package org.icddrb.dhis.android.sdk.persistence.migrations.version12;

import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import org.icddrb.dhis.android.sdk.persistence.migrations.MigrationUtil;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramStage;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramStage.Table;

public class Version12MigrationProgramStageStandardInterval extends AlterTableMigration<ProgramStage> {
    public Version12MigrationProgramStageStandardInterval(Class<ProgramStage> cls) {
        super(ProgramStage.class);
    }

    public Version12MigrationProgramStageStandardInterval() {
        super(ProgramStage.class);
    }

    public void onPreMigrate() {
        super.onPreMigrate();
        if (!MigrationUtil.columnExists(ProgramStage.class, Table.STANDARDINTERVAL)) {
            addColumn(Integer.class, Table.STANDARDINTERVAL);
        }
    }
}
