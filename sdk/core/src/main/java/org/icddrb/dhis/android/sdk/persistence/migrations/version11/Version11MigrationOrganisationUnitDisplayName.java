package org.icddrb.dhis.android.sdk.persistence.migrations.version11;

import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import org.icddrb.dhis.android.sdk.persistence.migrations.MigrationUtil;
import org.icddrb.dhis.android.sdk.persistence.models.OrganisationUnit;

public class Version11MigrationOrganisationUnitDisplayName extends AlterTableMigration<OrganisationUnit> {
    public Version11MigrationOrganisationUnitDisplayName(Class<OrganisationUnit> cls) {
        super(OrganisationUnit.class);
    }

    public Version11MigrationOrganisationUnitDisplayName() {
        super(OrganisationUnit.class);
    }

    public void onPreMigrate() {
        super.onPreMigrate();
        if (!MigrationUtil.columnExists(OrganisationUnit.class, "displayName")) {
            addColumn(String.class, "displayName");
        }
    }
}
