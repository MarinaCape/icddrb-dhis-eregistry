package org.icddrb.dhis.android.eregistry.fragments.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import com.squareup.okhttp.HttpUrl;
import com.squareup.otto.Subscribe;
import java.io.IOException;
import org.icddrb.dhis.android.eregistry.C0773R;
import org.icddrb.dhis.android.eregistry.export.ExportData;
import org.icddrb.dhis.android.sdk.controllers.DhisController;
import org.icddrb.dhis.android.sdk.controllers.DhisService;
import org.icddrb.dhis.android.sdk.events.LoadingMessageEvent;
import org.icddrb.dhis.android.sdk.events.UiEvent;
import org.icddrb.dhis.android.sdk.network.Session;
import org.icddrb.dhis.android.sdk.persistence.preferences.AppPreferences;
import org.icddrb.dhis.android.sdk.ui.activities.LoginActivity;
import org.icddrb.dhis.android.sdk.ui.views.FontButton;
import org.icddrb.dhis.android.sdk.ui.views.FontCheckBox;
import org.icddrb.dhis.android.sdk.utils.UiUtils;

public class SettingsFragment extends org.icddrb.dhis.android.sdk.ui.fragments.settings.SettingsFragment {
    private FontButton exportDataButton;
    private Button hardLogoutButton;

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.settings.SettingsFragment$1 */
    class C08231 implements OnClickListener {
        C08231() {
        }

        public void onClick(View v) {
            SettingsFragment.this.onExportDataClick();
        }
    }

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.settings.SettingsFragment$2 */
    class C08242 implements OnClickListener {
        C08242() {
        }

        public void onClick(View v) {
            SettingsFragment.this.onHardLogoutClick();
        }
    }

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.settings.SettingsFragment$3 */
    class C08253 implements OnCheckedChangeListener {
        C08253() {
        }

        public void onCheckedChanged(CompoundButton compoundButton, boolean value) {
            Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(SettingsFragment.this.getActivity().getApplicationContext()).edit();
            prefEditor.putBoolean(SettingsFragment.this.getActivity().getApplicationContext().getResources().getString(C0773R.string.developer_option_key), value);
            prefEditor.commit();
            SettingsFragment.this.toggleOptions(value);
        }
    }

    /* renamed from: org.icddrb.dhis.android.eregistry.fragments.settings.SettingsFragment$4 */
    class C08274 implements DialogInterface.OnClickListener {

        /* renamed from: org.icddrb.dhis.android.eregistry.fragments.settings.SettingsFragment$4$1 */
        class C08261 implements DialogInterface.OnClickListener {
            C08261() {
            }

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }

        C08274() {
        }

        public void onClick(DialogInterface dialog, int which) {
            if (DhisController.hasUnSynchronizedDatavalues) {
                UiUtils.showErrorDialog(SettingsFragment.this.getActivity(), SettingsFragment.this.getString(C0773R.string.error_message), SettingsFragment.this.getString(C0773R.string.unsynchronized_data_values), new C08261());
                return;
            }
            Session session = DhisController.getInstance().getSession();
            if (session != null) {
                HttpUrl httpUrl = session.getServerUrl();
                if (httpUrl != null) {
                    new AppPreferences(SettingsFragment.this.getActivity().getApplicationContext()).putServerUrl(httpUrl.toString());
                }
            }
            DhisService.logOutUser(SettingsFragment.this.getActivity(), true);
            if (VERSION.SDK_INT >= 15) {
                SettingsFragment.this.startActivity(new Intent(SettingsFragment.this.getActivity().getApplicationContext(), LoginActivity.class));
                SettingsFragment.this.getActivity().finishAffinity();
                return;
            }
            SettingsFragment.this.startActivity(new Intent(SettingsFragment.this.getActivity().getApplicationContext(), LoginActivity.class));
            SettingsFragment.this.getActivity().finish();
        }
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.exportDataButton = (FontButton) view.findViewById(C0773R.id.settings_export_data);
        this.exportDataButton.setOnClickListener(new C08231());
        this.hardLogoutButton = (Button) view.findViewById(C0773R.id.settings_logout_button);
        this.hardLogoutButton.setOnClickListener(new C08242());
        FontCheckBox fontCheckBox = (FontCheckBox) view.findViewById(C0773R.id.checkbox_developers_options);
        fontCheckBox.setChecked(PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getBoolean(getActivity().getApplicationContext().getResources().getString(C0773R.string.developer_option_key), false));
        toggleOptions(fontCheckBox.isChecked());
        fontCheckBox.setOnCheckedChangeListener(new C08253());
    }

    private void toggleOptions(boolean value) {
        if (value) {
            this.exportDataButton.setVisibility(0);
            this.hardLogoutButton.setVisibility(0);
            return;
        }
        this.exportDataButton.setVisibility(4);
        this.hardLogoutButton.setVisibility(4);
    }

    public void onExportDataClick() {
        Intent emailIntent = null;
        try {
            emailIntent = new ExportData().dumpAndSendToAIntent(getActivity());
        } catch (IOException e) {
            Toast.makeText(getContext(), C0773R.string.error_exporting_data, 1).show();
        }
        if (emailIntent != null) {
            startActivity(emailIntent);
        }
    }

    public void onHardLogoutClick() {
        UiUtils.showConfirmDialog(getActivity(), getString(C0773R.string.logout_title), getString(C0773R.string.logout_message), getString(C0773R.string.logout), getString(C0773R.string.cancel), new C08274());
    }

    @Subscribe
    public void onSynchronizationFinishedEvent(UiEvent event) {
        super.onSynchronizationFinishedEvent(event);
    }

    @Subscribe
    public void onLoadingMessageEvent(LoadingMessageEvent event) {
        super.onLoadingMessageEvent(event);
    }
}
