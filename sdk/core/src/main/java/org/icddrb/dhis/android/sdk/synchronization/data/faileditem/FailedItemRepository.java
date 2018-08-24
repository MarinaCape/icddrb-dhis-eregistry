package org.icddrb.dhis.android.sdk.synchronization.data.faileditem;

import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import org.icddrb.dhis.android.sdk.persistence.models.FailedItem;
import org.icddrb.dhis.android.sdk.persistence.models.FailedItem.Table;
import org.icddrb.dhis.android.sdk.synchronization.domain.faileditem.IFailedItemRepository;

public class FailedItemRepository implements IFailedItemRepository {
    public void save(FailedItem failedItem) {
        failedItem.save();
    }

    public void delete(String type, long id) {
        FailedItem item = getFailedItem(type, id);
        if (item != null) {
            item.async().delete();
        }
    }

    private FailedItem getFailedItem(String type, long id) {
        return (FailedItem) new Select().from(FailedItem.class).where(Condition.column(Table.ITEMTYPE).is(type), Condition.column(Table.ITEMID).is(Long.valueOf(id))).querySingle();
    }
}
