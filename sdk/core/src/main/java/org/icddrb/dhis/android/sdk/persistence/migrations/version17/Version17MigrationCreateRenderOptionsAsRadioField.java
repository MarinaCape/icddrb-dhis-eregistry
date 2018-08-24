package org.icddrb.dhis.android.sdk.persistence.migrations.version17;

import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import org.icddrb.dhis.android.sdk.persistence.migrations.MigrationUtil;
import org.icddrb.dhis.android.sdk.persistence.models.ProgramStageDataElement;

public class Version17MigrationCreateRenderOptionsAsRadioField extends AlterTableMigration<ProgramStageDataElement> {
    public Version17MigrationCreateRenderOptionsAsRadioField(Class<ProgramStageDataElement> cls) {
        super(ProgramStageDataElement.class);
    }

    public Version17MigrationCreateRenderOptionsAsRadioField() {
        super(ProgramStageDataElement.class);
    }

    public void onPreMigrate() {
        super.onPreMigrate();
        if (!MigrationUtil.columnExists(ProgramStageDataElement.class, "renderOptionsAsRadio")) {
            addColumn(Boolean.class, "renderOptionsAsRadio");
        }
    }
}
