package org.icddrb.dhis.android.sdk.persistence.migrations.version8;

import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import org.icddrb.dhis.android.sdk.persistence.migrations.MigrationUtil;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramStage;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramStage.Table;

public class Version8MigrationProgramStageExecutionLabel extends AlterTableMigration<ProgramStage> {
    public Version8MigrationProgramStageExecutionLabel(Class<ProgramStage> cls) {
        super(ProgramStage.class);
    }

    public Version8MigrationProgramStageExecutionLabel() {
        super(ProgramStage.class);
    }

    public void onPreMigrate() {
        super.onPreMigrate();
        if (!MigrationUtil.columnExists(ProgramStage.class, Table.EXECUTIONDATELABEL)) {
            addColumn(String.class, Table.EXECUTIONDATELABEL);
        }
    }
}
