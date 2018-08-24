package org.icddrb.dhis.android.sdk.persistence.migrations.version14;

import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import org.icddrb.dhis.android.sdk.persistence.migrations.MigrationUtil;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramTrackedEntityAttribute;

public class Version17MigrationCreatePteaRenderOptionsAsRadioField extends AlterTableMigration<ProgramTrackedEntityAttribute> {
    public Version17MigrationCreatePteaRenderOptionsAsRadioField(Class<ProgramTrackedEntityAttribute> cls) {
        super(ProgramTrackedEntityAttribute.class);
    }

    public Version17MigrationCreatePteaRenderOptionsAsRadioField() {
        super(ProgramTrackedEntityAttribute.class);
    }

    public void onPreMigrate() {
        super.onPreMigrate();
        if (!MigrationUtil.columnExists(ProgramTrackedEntityAttribute.class, "renderOptionsAsRadio")) {
            addColumn(Boolean.class, "renderOptionsAsRadio");
        }
    }
}
