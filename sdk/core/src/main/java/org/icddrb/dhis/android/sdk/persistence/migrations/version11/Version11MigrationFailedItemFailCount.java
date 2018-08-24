package org.icddrb.dhis.android.sdk.persistence.migrations.version11;

import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import org.icddrb.dhis.android.sdk.persistence.migrations.MigrationUtil;
import org.icddrb.dhis.android.sdk.persistence.models.FailedItem;
import org.icddrb.dhis.android.sdk.persistence.models.FailedItem.Table;

public class Version11MigrationFailedItemFailCount extends AlterTableMigration<FailedItem> {
    public Version11MigrationFailedItemFailCount(Class<FailedItem> cls) {
        super(FailedItem.class);
    }

    public Version11MigrationFailedItemFailCount() {
        super(FailedItem.class);
    }

    public void onPreMigrate() {
        super.onPreMigrate();
        if (!MigrationUtil.columnExists(FailedItem.class, Table.FAILCOUNT)) {
            addColumn(Integer.class, Table.FAILCOUNT);
        }
    }
}
