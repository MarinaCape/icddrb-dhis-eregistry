package org.icddrb.dhis.android.sdk.persistence.migrations.version16;

import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import org.icddrb.dhis.android.sdk.persistence.migrations.MigrationUtil;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramIndicator;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramIndicator.Table;

public class Version16MigrationCreateProgramIndicatorField extends AlterTableMigration<ProgramIndicator> {
    public Version16MigrationCreateProgramIndicatorField(Class<ProgramIndicator> cls) {
        super(ProgramIndicator.class);
    }

    public Version16MigrationCreateProgramIndicatorField() {
        super(ProgramIndicator.class);
    }

    public void onPreMigrate() {
        super.onPreMigrate();
        if (!MigrationUtil.columnExists(ProgramIndicator.class, Table.DISPLAYINFORM)) {
            addColumn(Boolean.class, Table.DISPLAYINFORM);
        }
    }
}
