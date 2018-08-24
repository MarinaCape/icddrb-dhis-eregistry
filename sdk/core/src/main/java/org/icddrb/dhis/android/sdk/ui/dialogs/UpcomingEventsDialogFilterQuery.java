package org.icddrb.dhis.android.sdk.ui.dialogs;

import android.content.Context;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.icddrb.dhis.android.sdk.persistence.loaders.Query;
import org.icddrb.dhis.android.sdk.ui.dialogs.AutoCompleteDialogAdapter.OptionAdapterValue;
import org.icddrb.dhis.android.sdk.ui.dialogs.UpcomingEventsDialogFilter.Type;

public class UpcomingEventsDialogFilterQuery implements Query<List<OptionAdapterValue>> {
    public List<OptionAdapterValue> query(Context context) {
        List<OptionAdapterValue> optionAdapterValues = new ArrayList();
        List<Type> types = Arrays.asList(Type.values());
        for (int i = 0; i < types.size(); i++) {
            optionAdapterValues.add(new OptionAdapterValue(Integer.toString(i), ((Type) types.get(i)).name()));
        }
        return optionAdapterValues;
    }
}
