package org.icddrb.dhis.android.sdk.synchronization.domain.faileditem;

import org.icddrb.dhis.android.sdk.persistence.models.FailedItem;

public interface IFailedItemRepository {
    void delete(String str, long j);

    void save(FailedItem failedItem);
}
