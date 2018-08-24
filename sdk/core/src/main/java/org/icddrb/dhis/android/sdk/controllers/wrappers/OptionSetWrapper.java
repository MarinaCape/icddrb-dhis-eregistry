package org.icddrb.dhis.android.sdk.controllers.wrappers;

import java.util.ArrayList;
import java.util.List;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.persistence.models.Option;
import org.icddrb.dhis.android.sdk.persistence.models.OptionSet;
import org.icddrb.dhis.android.sdk.persistence.models.meta.DbOperation;
import org.icddrb.dhis.android.sdk.utils.DbUtils;
import org.joda.time.DateTime;

public class OptionSetWrapper {
    public static List<DbOperation> getOperations(List<OptionSet> optionSets) {
        List<DbOperation> operations = new ArrayList();
        List<OptionSet> persistedOptionSets = MetaDataController.getOptionSets();
        if (!(optionSets == null || optionSets.isEmpty())) {
            for (OptionSet optionSet : optionSets) {
                if (!(optionSet == null || optionSet.getOptions() == null)) {
                    OptionSet persistedOptionSet = MetaDataController.getOptionSet(optionSet.getUid());
                    List<Option> persistedOptions = null;
                    if (persistedOptionSet != null) {
                        persistedOptions = persistedOptionSet.getOptions();
                    }
                    int sortIndex = 0;
                    for (Option option : optionSet.getOptions()) {
                        option.setUid(optionSet.getUid() + option.getCode());
                        option.setLastUpdated(new DateTime().toString());
                        option.setCreated(new DateTime().toString());
                        option.setOptionSet(optionSet.getUid());
                        option.setSortIndex(sortIndex);
                        sortIndex++;
                    }
                    operations.addAll(DbUtils.createOperations(persistedOptions, optionSet.getOptions(), false));
                }
            }
        }
        operations.addAll(DbUtils.createOperations(persistedOptionSets, optionSets, true));
        return operations;
    }
}
