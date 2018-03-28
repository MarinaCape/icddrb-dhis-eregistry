package org.icddrb.dhis.android.eregistry.fragments.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.squareup.okhttp.HttpUrl;
import com.squareup.otto.Subscribe;

import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.controllers.DhisService;
import org.icddrb.dhis.android.sdk.events.LoadingMessageEvent;
import org.icddrb.dhis.android.sdk.events.UiEvent;
import org.icddrb.dhis.android.sdk.network.Session;
import org.icddrb.dhis.android.sdk.persistence.preferences.AppPreferences;
import org.icddrb.dhis.android.sdk.ui.activities.LoginActivity;
import org.icddrb.dhis.android.sdk.ui.views.FontButton;
import org.icddrb.dhis.android.sdk.ui.views.FontCheckBox;
import org.icddrb.dhis.android.eregistry.R;
import org.icddrb.dhis.android.eregistry.export.ExportData;
import org.icddrb.dhis.android.sdk.utils.UiUtils;

import java.io.IOException;

public class SettingsFragment extends
        org.icddrb.dhis.android.sdk.ui.fragments.settings.SettingsFragment{

    private FontButton exportDataButton;
    private Button hardLogoutButton;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        exportDataButton = (FontButton) view.findViewById(R.id.settings_export_data);
        exportDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onExportDataClick();
            }
        });

        hardLogoutButton = (Button) view.findViewById(R.id.settings_logout_button);
        hardLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHardLogoutClick();
            }
        });


        FontCheckBox fontCheckBox = (FontCheckBox) view.findViewById(
                R.id.checkbox_developers_options);

        Context context = getActivity().getApplicationContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                context);
        fontCheckBox.setChecked(sharedPreferences.getBoolean(
                getActivity().getApplicationContext().getResources().getString(
                        R.string.developer_option_key), false));
        toggleOptions(fontCheckBox.isChecked());
        fontCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean value) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(
                        getActivity().getApplicationContext());
                SharedPreferences.Editor prefEditor =
                        sharedPref.edit(); // Get preference in editor mode
                prefEditor.putBoolean(
                        getActivity().getApplicationContext().getResources().getString(
                                org.icddrb.dhis.android.sdk.R.string.developer_option_key), value);
                prefEditor.commit();
                toggleOptions(value);
            }
        });
    }

    private void toggleOptions(boolean value) {
        if (value) {
            exportDataButton.setVisibility(View.VISIBLE);
            hardLogoutButton.setVisibility(View.VISIBLE);
        } else {
            exportDataButton.setVisibility(View.INVISIBLE);
            hardLogoutButton.setVisibility(View.INVISIBLE);
        }
    }

    public void onExportDataClick() {
        ExportData exportData = new ExportData();
        Intent emailIntent = null;
        try {
            emailIntent = exportData.dumpAndSendToAIntent(getActivity());
        } catch (IOException e) {
            Toast.makeText(getContext(), org.icddrb.dhis.android.sdk.R.string.error_exporting_data, Toast.LENGTH_LONG).show();
        }
        if (emailIntent != null) {
            startActivity(emailIntent);
        }
    }

    public void onHardLogoutClick() {
        UiUtils.showConfirmDialog(getActivity(), getString(org.icddrb.dhis.android.sdk.R.string.logout_title),
                getString(org.icddrb.dhis.android.sdk.R.string.logout_message),
                getString(org.icddrb.dhis.android.sdk.R.string.logout), getString(org.icddrb.dhis.android.sdk.R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (DhisController.hasUnSynchronizedDatavalues) {
                            //show error dialog
                            UiUtils.showErrorDialog(getActivity(),
                                    getString(org.icddrb.dhis.android.sdk.R.string.error_message),
                                    getString(org.icddrb.dhis.android.sdk.R.string.unsynchronized_data_values),
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                        } else {
                            Session session = DhisController.getInstance().getSession();
                            if (session != null) {
                                HttpUrl httpUrl = session.getServerUrl();
                                if (httpUrl != null) {
                                    String serverUrlString = httpUrl.toString();
                                    AppPreferences appPreferences = new AppPreferences(
                                            getActivity().getApplicationContext());
                                    appPreferences.putServerUrl(serverUrlString);
                                }
                            }
                            DhisService.logOutUser(getActivity(), true);

                            int apiVersion = Build.VERSION.SDK_INT;
                            if(apiVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                                Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finishAffinity();
                            }
                            else {
                                Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }

                        }
                    }
                });
    }


    @Subscribe
    public void onSynchronizationFinishedEvent(final UiEvent event)
    {
        super.onSynchronizationFinishedEvent(event);
    }

    @Subscribe
    public void onLoadingMessageEvent(final LoadingMessageEvent event) {
        super.onLoadingMessageEvent(event);
    }
}