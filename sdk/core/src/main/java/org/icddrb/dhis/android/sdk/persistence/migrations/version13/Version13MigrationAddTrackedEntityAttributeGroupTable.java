package org.icddrb.dhis.android.sdk.persistence.migrations.version13;

import android.database.sqlite.SQLiteDatabase;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import org.icddrb.dhis.android.sdk.persistence.models.TrackedEntityAttributeGroup.Adapter;

public class Version13MigrationAddTrackedEntityAttributeGroupTable extends BaseMigration {
    public void migrate(SQLiteDatabase database) {
        //database.execSQL(new Adapter().getCreationQuery());
    }
}
