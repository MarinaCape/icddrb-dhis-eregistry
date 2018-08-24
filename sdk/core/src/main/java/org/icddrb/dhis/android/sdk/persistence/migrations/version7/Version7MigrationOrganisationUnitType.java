package org.icddrb.dhis.android.sdk.persistence.migrations.version7;

import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import org.icddrb.dhis.android.sdk.persistence.migrations.MigrationUtil;
import org.icddrb.dhis.android.sdk.persistence.models.OrganisationUnit;

public class Version7MigrationOrganisationUnitType extends AlterTableMigration<OrganisationUnit> {
    public Version7MigrationOrganisationUnitType(Class<OrganisationUnit> cls) {
        super(OrganisationUnit.class);
    }

    public Version7MigrationOrganisationUnitType() {
        super(OrganisationUnit.class);
    }

    public void onPreMigrate() {
        super.onPreMigrate();
        if (!MigrationUtil.columnExists(OrganisationUnit.class, "type")) {
            addColumn(String.class, "type");
        }
    }
}
