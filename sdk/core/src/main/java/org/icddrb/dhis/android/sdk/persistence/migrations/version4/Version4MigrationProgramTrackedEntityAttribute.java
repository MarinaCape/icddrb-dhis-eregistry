package org.icddrb.dhis.android.sdk.persistence.migrations.version4;

import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import org.icddrb.dhis.android.sdk.persistence.migrations.MigrationUtil;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramTrackedEntityAttribute;

public class Version4MigrationProgramTrackedEntityAttribute extends AlterTableMigration<ProgramTrackedEntityAttribute> {
    public Version4MigrationProgramTrackedEntityAttribute() {
        super(ProgramTrackedEntityAttribute.class);
    }

    public void onPreMigrate() {
        super.onPreMigrate();
        if (!MigrationUtil.columnExists(ProgramTrackedEntityAttribute.class, "sortOrder")) {
            addColumn(Integer.TYPE, "sortOrder");
        }
    }
}
