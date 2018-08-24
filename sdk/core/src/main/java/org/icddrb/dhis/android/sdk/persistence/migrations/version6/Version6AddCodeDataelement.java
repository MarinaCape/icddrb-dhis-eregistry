package org.icddrb.dhis.android.sdk.persistence.migrations.version6;

import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import org.icddrb.dhis.android.sdk.persistence.migrations.MigrationUtil;
import org.icddrb.dhis.android.sdk.persistence.models.DataElement;

public class Version6AddCodeDataelement extends AlterTableMigration<DataElement> {
    public Version6AddCodeDataelement(Class<DataElement> cls) {
        super(DataElement.class);
    }

    public Version6AddCodeDataelement() {
        super(DataElement.class);
    }

    public void onPreMigrate() {
        if (!MigrationUtil.columnExists(DataElement.class, "type")) {
            addColumn(String.class, "code");
        }
    }
}
