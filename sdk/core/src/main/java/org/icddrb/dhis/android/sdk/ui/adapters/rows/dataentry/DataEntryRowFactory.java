package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

import android.content.Context;


import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Delete;

import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.controllers.wrappers.OptionSetWrapper;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.persistence.models.Option;
import org.icddrb.dhis.android.sdk.persistence.models.Option$Table;
import org.icddrb.dhis.android.sdk.persistence.models.OptionSet;
import org.icddrb.dhis.android.sdk.persistence.models.UnionFWA;
import org.icddrb.dhis.android.sdk.persistence.models.UnionFWADropDownItem;
import org.icddrb.dhis.android.sdk.persistence.models.meta.DbOperation;
import org.icddrb.dhis.android.sdk.persistence.preferences.AppPreferences;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.autocompleterow.AutoCompleteRow;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RowValueChangedEvent;
import org.icddrb.dhis.android.sdk.utils.DbUtils;
import org.icddrb.dhis.android.sdk.utils.api.ValueType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by katana on 21/10/16.
 */

public class DataEntryRowFactory {
    public static Row createDataEntryView(boolean mandatory, boolean allowFutureDate,
                                          String optionSetId, String rowName, BaseValue baseValue,
                                          ValueType valueType, boolean editable,
                                          boolean shouldNeverBeEdited, boolean dataEntryMethod, Context c) {
        Row row;
        String trackedEntityAttributeName = rowName;
        if (optionSetId != null) {
            OptionSet optionSet = MetaDataController.getOptionSet(optionSetId);
            if (optionSet == null) {
                row = new ShortTextEditTextRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.TEXT);
            } else {
                List<Option> options = MetaDataController.getOptions(optionSetId);

                if (isDataEntryRadioButtons(dataEntryMethod, options)) {
                    row = new RadioButtonsOptionSetRow(trackedEntityAttributeName, mandatory, null,
                            baseValue, options);
                }
                else
                    row = new AutoCompleteRow(trackedEntityAttributeName, mandatory, null, baseValue, optionSet);
            }
        } else if (valueType.equals(ValueType.TEXT)) {
            row = new ShortTextEditTextRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.TEXT);
        } else if (valueType.equals(ValueType.LONG_TEXT)) {
            row = new LongEditTextRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.LONG_TEXT);
        } else if (valueType.equals(ValueType.NUMBER)) {
            row = new NumberEditTextRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.NUMBER);
        } else if (valueType.equals(ValueType.INTEGER)) {
            row = new IntegerEditTextRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.INTEGER);
        } else if (valueType.equals(ValueType.INTEGER_ZERO_OR_POSITIVE)) {
            row = new IntegerZeroOrPositiveEditTextRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.INTEGER_ZERO_OR_POSITIVE);
        } else if (valueType.equals(ValueType.PERCENTAGE)) {
            row = new PercentageEditTextRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.PERCENTAGE);
        } else if (valueType.equals(ValueType.INTEGER_POSITIVE)) {
            row = new IntegerPositiveEditTextRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.INTEGER_POSITIVE);
        } else if (valueType.equals(ValueType.INTEGER_NEGATIVE)) {
            row = new IntegerNegativeEditTextRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.INTEGER_NEGATIVE);
        } else if (valueType.equals(ValueType.BOOLEAN)) {
            row = new RadioButtonsRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.BOOLEAN);
        } else if (valueType.equals(ValueType.TRUE_ONLY)) {
            row = new CheckBoxRow(trackedEntityAttributeName, mandatory, null, baseValue);
        } else if (valueType.equals(ValueType.DATE) || valueType.equals(ValueType.AGE)) {
            row = new DatePickerRow(trackedEntityAttributeName, mandatory, null, baseValue, allowFutureDate);
        } else if (valueType.equals(ValueType.TIME)) {
            row = new TimePickerRow(trackedEntityAttributeName, mandatory, null, baseValue);
        } else if (valueType.equals(ValueType.DATETIME)) {
            row = new DateTimePickerRow(trackedEntityAttributeName, mandatory, null, baseValue, allowFutureDate);
        } else if(valueType.equals(ValueType.COORDINATE)) {
            row = new QuestionCoordinatesRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.QUESTION_COORDINATES);
        } else  if(valueType.equals(ValueType.PHONE_NUMBER)) {
            row = new PhoneEditTextRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.PHONE_NUMBER);
        }  else  if(valueType.equals(ValueType.EMAIL)) {
            row = new EmailAddressEditTextRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.EMAIL);
        }  else  if(valueType.equals(ValueType.URL)) {
            row = new URLEditTextRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.URL);
        }  else  if (valueType.equals(ValueType.USERNAME)) {
            // Norway
            AppPreferences mPref = new AppPreferences(c);
            UnionFWA dropdown = mPref.getDropdownInfo();
            OptionSet os = getAllUsersOptionSet(c, dropdown.getUsers(mPref.getChosenOrg()));
            mPref.putUserOptionId(os.getUid());

            //System.out.println("Norway - username ("+trackedEntityAttributeName+"): " + baseValue.getValue());

            row = new AutoCompleteRow(trackedEntityAttributeName, mandatory, null, baseValue, os);

        }  else  if (valueType.equals(ValueType.ORGANISATION_UNIT)) {
            // Norway
            OptionSet os = getOrganizationOptionSet(c);
            row = new AutoCompleteRow(trackedEntityAttributeName, mandatory, null, baseValue, os);

        } else {
            row = new InvalidEditTextRow(trackedEntityAttributeName, mandatory, null, baseValue,
                    DataEntryRowTypes.INVALID_DATA_ENTRY);
        }
        row.setEditable(editable);
        row.setShouldNeverBeEdited(shouldNeverBeEdited);
        return row;
    }

    // Norway Start
    private static OptionSet getOrganizationOptionSet(Context c) {
        OptionSet os = new OptionSet();
        String osString = null;
        AppPreferences mPref = new AppPreferences(c);
        UnionFWA drop = mPref.getDropdownInfo();

        int i = 0;
        List<Option> opList = new ArrayList<>();
        List<UnionFWADropDownItem> optionSets = drop.getOrgUnits();
        if (optionSets!=null && optionSets.size() > 0) {
            for (UnionFWADropDownItem ou : optionSets) {
                Option o = new Option();
                o.setSortIndex(i);
                o.setOptionSet(ou.getId());
                o.setName(ou.getLabel());
                o.setCode(ou.getId());
                o.setUid(o.getOptionSet() + ou.getLabel().toLowerCase().replace(" ", "-").replace(",", ""));
                opList.add(o);
                osString = o.getOptionSet();
                i++;
                //System.out.println("Norway - Organization to dropdown " + ou.getLabel() + ": " + ou.getId());
            }
        } else {
            System.out.println("Norway - no new organisations found");
        }

        os.setVersion(2);
        os.setOptions(opList);
        os.setValueType(ValueType.ORGANISATION_UNIT);
        os.setUid(osString);

        List<OptionSet> osList = new ArrayList<>();
        osList.add(os);
        List<DbOperation> operations = OptionSetWrapper.getOperations(osList);
        DbUtils.applyBatch(operations);

        return os;
    }

    private static OptionSet getAllUsersOptionSet(Context c, List<UnionFWADropDownItem> optionSets) {
       return getAllUsersOptionSet(c, optionSets, null);
    }

    public static OptionSet getAllUsersOptionSet(Context c, List<UnionFWADropDownItem> optionSets, String guid) {
        OptionSet os = new OptionSet();
        int i = 0;
        if (optionSets != null && optionSets.size() > 0) {
            List<Option> options = new ArrayList<>();
            String osString = "";
            for (UnionFWADropDownItem user : optionSets) {
                Option o = new Option();
                o.setSortIndex(i);
                o.setOptionSet(user.getId());
                o.setName(user.getLabel());
                o.setCode(user.getAlternateId());
                o.setUid(o.getOptionSet() + user.getLabel().toLowerCase().replace(" ", "-").replace(",", ""));
                options.add(o);
                osString += o.getOptionSet();
               // System.out.println("Norway - Adding User to dropdown " + user.getLabel() + ": " + user.getId() + " username: "+user.getAlternateId());
                i++;
            }

            os.setVersion(2);
            os.setUid((guid==null) ? osString : guid);
            os.setOptions(options);
            os.setValueType(ValueType.USERNAME);

            List<OptionSet> osList = new ArrayList<>(); osList.add(os);
            List<DbOperation> operations = OptionSetWrapper.getOperations(osList);
            DbUtils.applyBatch(operations);

        } else {
            System.out.println("Norway - no new users found");
        }

        //AppPreferences mPref = new AppPreferences(c);
        return os;
    }

    private static void printOptionStatus(String state, String org, String uid) {
       /* List<Option> options = new Select()
                .from(Option.class)
                .where((Condition.column(Option$Table.OPTIONSET).is(uid)))
                .queryList();

        System.out.println("Norway - row change ("+state+"): " + org
                + " uid: " + uid
                + " size: "+ options.size());
        */
    }

    public static void updateFWADropdown(Context c, final RowValueChangedEvent event) {
        // Norway: update FWA name based on chosen union
        AppPreferences mPref = new AppPreferences(c);
        printOptionStatus("before", mPref.getChosenOrg(), mPref.getUserOptionId());

        // Delete that list
        new Delete()
                .from(Option.class)
                .where((Condition.column(Option$Table.OPTIONSET).is(mPref.getUserOptionId()))).query();
        printOptionStatus("deleted", mPref.getChosenOrg(), mPref.getUserOptionId());

        // create and save new list with existing guid
        UnionFWA dropdown = mPref.getDropdownInfo();
        getAllUsersOptionSet(c, dropdown.getUsers(mPref.getChosenOrg()),mPref.getUserOptionId());
        printOptionStatus("now", mPref.getChosenOrg(), mPref.getUserOptionId());
    }
    // Norway End

    private static boolean isDataEntryRadioButtons(boolean dataEntryMethod, List<Option> options) {
        return dataEntryMethod && options.size() < 8;
    }

}
