package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.autocompleterow;

import android.os.Bundle;
import android.view.View;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import java.util.ArrayList;
import java.util.List;
import org.icddrb.dhis.android.sdk.C0845R;
import org.icddrb.dhis.android.sdk.persistence.models.Option;
import org.icddrb.dhis.android.sdk.persistence.models.Option.Table;
import org.icddrb.dhis.android.sdk.ui.dialogs.AutoCompleteDialogAdapter.OptionAdapterValue;
import org.icddrb.dhis.android.sdk.ui.dialogs.AutoCompleteDialogFragment;
import org.icddrb.dhis.android.sdk.ui.dialogs.AutoCompleteDialogFragment.OnOptionSelectedListener;

public class OptionDialogFragment extends AutoCompleteDialogFragment {
    private static final String EXTRA_OPTIONSET = "extra:optionsset";

    public static OptionDialogFragment newInstance(String optionSetId, OnOptionSelectedListener listener) {
        OptionDialogFragment dialogFragment = new OptionDialogFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_OPTIONSET, optionSetId);
        dialogFragment.setArguments(args);
        dialogFragment.setOnOptionSetListener(listener);
        return dialogFragment;
    }

    private List<OptionAdapterValue> getOptions() {
        List<OptionAdapterValue> values = new ArrayList();
        String optionSetId = getArguments().getString(EXTRA_OPTIONSET);
        List<Option> options = new Select("name").from(Option.class).where(Condition.column("optionSet").is(optionSetId)).orderBy(Table.SORTINDEX).queryList();
        if (!(options == null || options.isEmpty())) {
            for (Option option : options) {
                values.add(new OptionAdapterValue(option.getName(), option.getName()));
            }
        }
        return values;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDialogLabel(C0845R.string.find_option);
        getAdapter().swapData(getOptions());
    }
}
