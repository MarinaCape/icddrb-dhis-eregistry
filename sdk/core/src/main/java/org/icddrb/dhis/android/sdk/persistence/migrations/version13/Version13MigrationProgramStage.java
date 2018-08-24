package org.icddrb.dhis.android.sdk.persistence.migrations.version13;

import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import org.icddrb.dhis.android.sdk.persistence.migrations.MigrationUtil;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramStage;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramStage.Table;

public class Version13MigrationProgramStage extends AlterTableMigration<ProgramStage> {
    public Version13MigrationProgramStage(Class<ProgramStage> cls) {
        super(ProgramStage.class);
    }

    public Version13MigrationProgramStage() {
        super(ProgramStage.class);
    }

    public void onPreMigrate() {
        super.onPreMigrate();
        if (!MigrationUtil.columnExists(ProgramStage.class, Table.HIDEDUEDATE)) {
            addColumn(Boolean.class, Table.HIDEDUEDATE);
        }
        if (!MigrationUtil.columnExists(ProgramStage.class, Table.PERIODTYPE)) {
            addColumn(String.class, Table.PERIODTYPE);
        }
    }
}
