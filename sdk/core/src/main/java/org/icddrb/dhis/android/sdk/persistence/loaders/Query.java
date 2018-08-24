package org.icddrb.dhis.android.sdk.persistence.loaders;

import android.content.Context;

public interface Query<T> {
    T query(Context context);
}
