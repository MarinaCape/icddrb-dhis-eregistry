package org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry;

import android.content.Context;
import java.util.List;
import org.icddrb.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.icddrb.dhis.android.sdk.persistence.models.BaseValue;
import org.icddrb.dhis.android.sdk.persistence.models.Option;
import org.icddrb.dhis.android.sdk.persistence.models.OptionSet;
import org.icddrb.dhis.android.sdk.persistence.preferences.AppPreferences;
import org.icddrb.dhis.android.sdk.ui.adapters.rows.dataentry.autocompleterow.AutoCompleteRow;
import org.icddrb.dhis.android.sdk.ui.fragments.dataentry.RowValueChangedEvent;
import org.icddrb.dhis.android.sdk.utils.api.ValueType;

public class DataEntryRowFactory {
    public static Row createDataEntryView(boolean mandatory, boolean allowFutureDate, String optionSetId, String rowName, BaseValue baseValue, ValueType valueType, boolean editable, boolean shouldNeverBeEdited, boolean dataEntryMethod, Context c) {
        Row row;
        String trackedEntityAttributeName = rowName;
        if (optionSetId != null) {
            OptionSet optionSet = MetaDataController.getOptionSet(optionSetId);
            if (optionSet == null) {
                row = new ShortTextEditTextRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.TEXT);
            } else {
                List<Option> options = MetaDataController.getOptions(optionSetId);
                if (isDataEntryRadioButtons(dataEntryMethod, options)) {
                    row = new RadioButtonsOptionSetRow(trackedEntityAttributeName, mandatory, null, baseValue, options);
                } else {
                    Row autoCompleteRow = new AutoCompleteRow(trackedEntityAttributeName, mandatory, null, baseValue, optionSet);
                }
            }
        } else {
            Row shortTextEditTextRow;
            if (valueType.equals(ValueType.TEXT)) {
                shortTextEditTextRow = new ShortTextEditTextRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.TEXT);
            } else {
                if (valueType.equals(ValueType.LONG_TEXT)) {
                    shortTextEditTextRow = new LongEditTextRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.LONG_TEXT);
                } else {
                    if (valueType.equals(ValueType.NUMBER)) {
                        shortTextEditTextRow = new NumberEditTextRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.NUMBER);
                    } else {
                        if (valueType.equals(ValueType.INTEGER)) {
                            shortTextEditTextRow = new IntegerEditTextRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.INTEGER);
                        } else {
                            if (valueType.equals(ValueType.INTEGER_ZERO_OR_POSITIVE)) {
                                shortTextEditTextRow = new IntegerZeroOrPositiveEditTextRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.INTEGER_ZERO_OR_POSITIVE);
                            } else {
                                if (valueType.equals(ValueType.PERCENTAGE)) {
                                    shortTextEditTextRow = new PercentageEditTextRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.PERCENTAGE);
                                } else {
                                    if (valueType.equals(ValueType.INTEGER_POSITIVE)) {
                                        shortTextEditTextRow = new IntegerPositiveEditTextRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.INTEGER_POSITIVE);
                                    } else {
                                        if (valueType.equals(ValueType.INTEGER_NEGATIVE)) {
                                            shortTextEditTextRow = new IntegerNegativeEditTextRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.INTEGER_NEGATIVE);
                                        } else {
                                            if (valueType.equals(ValueType.BOOLEAN)) {
                                                shortTextEditTextRow = new RadioButtonsRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.BOOLEAN);
                                            } else {
                                                if (valueType.equals(ValueType.TRUE_ONLY)) {
                                                    row = new CheckBoxRow(trackedEntityAttributeName, mandatory, null, baseValue);
                                                } else {
                                                    if (valueType.equals(ValueType.DATE)) {
                                                        shortTextEditTextRow = new DatePickerRow(trackedEntityAttributeName, mandatory, null, baseValue, allowFutureDate);
                                                    } else {
                                                        if (valueType.equals(ValueType.AGE)) {
                                                            shortTextEditTextRow = new DatePickerRow(trackedEntityAttributeName, mandatory, null, baseValue, allowFutureDate, "age");
                                                        } else {
                                                            if (valueType.equals(ValueType.TIME)) {
                                                                row = new TimePickerRow(trackedEntityAttributeName, mandatory, null, baseValue);
                                                            } else {
                                                                if (valueType.equals(ValueType.DATETIME)) {
                                                                    shortTextEditTextRow = new DateTimePickerRow(trackedEntityAttributeName, mandatory, null, baseValue, allowFutureDate);
                                                                } else {
                                                                    if (valueType.equals(ValueType.COORDINATE)) {
                                                                        shortTextEditTextRow = new QuestionCoordinatesRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.QUESTION_COORDINATES);
                                                                    } else {
                                                                        if (valueType.equals(ValueType.PHONE_NUMBER)) {
                                                                            shortTextEditTextRow = new PhoneEditTextRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.PHONE_NUMBER);
                                                                        } else {
                                                                            if (valueType.equals(ValueType.EMAIL)) {
                                                                                shortTextEditTextRow = new EmailAddressEditTextRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.EMAIL);
                                                                            } else {
                                                                                if (valueType.equals(ValueType.URL)) {
                                                                                    shortTextEditTextRow = new URLEditTextRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.URL);
                                                                                } else {
                                                                                    if (valueType.equals(ValueType.USERNAME)) {
                                                                                        AppPreferences appPreferences = new AppPreferences(c);
                                                                                        OptionSet os = appPreferences.getDropdownInfo().getAllUsersOptionSet(appPreferences.getChosenOrg(), null);
                                                                                        appPreferences.putUserOptionId(os.getUid());
                                                                                        shortTextEditTextRow = new AutoCompleteRow(trackedEntityAttributeName, mandatory, null, baseValue, os);
                                                                                    } else {
                                                                                        if (valueType.equals(ValueType.ORGANISATION_UNIT)) {
                                                                                            shortTextEditTextRow = new AutoCompleteRow(trackedEntityAttributeName, mandatory, null, baseValue, new AppPreferences(c).getDropdownInfo().getOrganizationOptionSet());
                                                                                        } else {
                                                                                            Row invalidEditTextRow = new InvalidEditTextRow(trackedEntityAttributeName, mandatory, null, baseValue, DataEntryRowTypes.INVALID_DATA_ENTRY);
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        row.setEditable(editable);
        row.setShouldNeverBeEdited(shouldNeverBeEdited);
        return row;
    }

    public static void updateFWADropdown(Context c, RowValueChangedEvent event) {
        AppPreferences mPref = new AppPreferences(c);
        mPref.getDropdownInfo().updateFWADropdown(mPref.getChosenOrg(), mPref.getUserOptionId());
    }

    private static boolean isDataEntryRadioButtons(boolean dataEntryMethod, List<Option> options) {
        return dataEntryMethod && options.size() < 8;
    }
}
