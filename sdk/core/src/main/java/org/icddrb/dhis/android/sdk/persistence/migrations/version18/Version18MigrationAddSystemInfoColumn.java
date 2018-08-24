package org.icddrb.dhis.android.sdk.persistence.migrations.version18;

import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import org.icddrb.dhis.android.sdk.persistence.migrations.MigrationUtil;
import org.icddrb.dhis.android.sdk.persistence.models.SystemInfo;

public class Version18MigrationAddSystemInfoColumn extends AlterTableMigration<SystemInfo> {
    public Version18MigrationAddSystemInfoColumn(Class<SystemInfo> cls) {
        super(SystemInfo.class);
    }

    public Version18MigrationAddSystemInfoColumn() {
        super(SystemInfo.class);
    }

    public void onPreMigrate() {
        super.onPreMigrate();
        if (!MigrationUtil.columnExists(SystemInfo.class, "version")) {
            addColumn(Boolean.class, "version");
        }
    }
}
