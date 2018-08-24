package org.icddrb.dhis.android.sdk.persistence.migrations;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.raizlabs.android.dbflow.annotation.NotNull;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment;
import org.icddrb.dhis.android.sdk.persistence.models.Enrollment.Table;
import org.icddrb.dhis.android.sdk.persistence.models.Event;

public class MigrationUtil {
    private static SQLiteDatabase database;

    public static boolean tableExists(String tableName) {
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = 'table' AND name = '" + tableName + "'", null);
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            cursor.close();
            if (count > 0) {
                return true;
            }
            return false;
        }
        cursor.close();
        return false;
    }

    public static boolean columnExists(@NotNull Class tableClass, @NotNull String columnName) {
        String[] columnNames = database.query(FlowManager.getTableName(tableClass), null, null, null, null, null, null).getColumnNames();
        for (String equals : columnNames) {
            if (equals.equals(columnName)) {
                return true;
            }
        }
        return false;
    }

    public static void setDatabase(SQLiteDatabase database) {
        database = database;
    }

    public static void fixInvalidIncidentDates() {
        for (Enrollment enrollment : new Select().from(Enrollment.class).where(Condition.column(Table.INCIDENTDATE).eq("")).queryList()) {
            enrollment.setIncidentDate(null);
            enrollment.update();
            Log.d("DB migration", "Enrollment " + enrollment.getUid() + ": Incident date set to null");
        }
        Log.d("DB migration", "Migration done");
    }

    public static void fixInvalidCompletedDates() {
        Log.d("DB migration", "Setting completedDate on completed Events");
        for (Event completedEvent : new Select().from(Event.class).where(Condition.column("status").eq("COMPLETED")).and(Condition.column(Event.Table.COMPLETEDDATE).isNull()).queryList()) {
            completedEvent.setCompletedDate(completedEvent.getLastUpdated());
            completedEvent.save();
            Log.d("DB migration", "Event " + completedEvent.getUid() + ": Completed date set to " + completedEvent.getLastUpdated());
        }
        Log.d("DB migration", "Migration done");
    }

    public static void migrateExistingData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("migrationFlags", 0);
        if (preferences.getBoolean("incidentDatesAreInvalid", true)) {
            fixInvalidIncidentDates();
            preferences.edit().putBoolean("incidentDatesAreInvalid", false).apply();
        }
        if (preferences.getBoolean("completedDatesAreInvalid", true)) {
            fixInvalidCompletedDates();
            preferences.edit().putBoolean("completedDatesAreInvalid", false).apply();
        }
    }
}
