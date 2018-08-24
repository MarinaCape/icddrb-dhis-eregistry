package org.icddrb.dhis.client.sdk.ui.fragments;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.icddrb.dhis.android.sdk.utils.support.expression.Expression;
import org.icddrb.dhis.client.sdk.ui.C0935R;
import org.icddrb.dhis.client.sdk.ui.activities.BaseActivity;
import org.icddrb.dhis.client.sdk.ui.activities.OnBackPressedCallback;
import org.icddrb.dhis.client.sdk.ui.activities.OnBackPressedFromFragmentCallback;

public class InformationFragment extends BaseFragment implements OnBackPressedCallback {
    public static final String LIBS_LIST = "libraires_list";
    public static final String URL = "url";
    public static final String USERNAME = "username";
    private OnBackPressedFromFragmentCallback onBackPressedFromFragmentCallback;
    private String url;
    private String username;

    public static InformationFragment newInstance(String username, String url) {
        Bundle args = new Bundle();
        args.putString(USERNAME, username);
        args.putString("url", url);
        InformationFragment fragment = new InformationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        super.onCreateView(inflater, container, state);
        return inflater.inflate(C0935R.layout.fragment_information, container, false);
    }

    public static Spanned getCommitMessage(Context context) {
        String stringCommit;
        if (getCommitHash(context).contains(context.getString(C0935R.string.unavailable))) {
            stringCommit = String.format(context.getString(C0935R.string.last_commit), new Object[]{stringCommit}) + " " + context.getText(C0935R.string.lastcommit_unavailable);
        } else {
            stringCommit = String.format(context.getString(C0935R.string.last_commit), new Object[]{stringCommit});
        }
        return Html.fromHtml(stringCommit);
    }

    public static String getCommitHash(Context context) {
        int layoutId = context.getResources().getIdentifier("lastcommit", "raw", "org.icddrb.dhis.android.eregistry");
        if (layoutId == 0) {
            return context.getString(C0935R.string.unavailable);
        }
        return convertFromInputStreamToString(context.getResources().openRawResource(layoutId)).toString();
    }

    private static StringBuilder convertFromInputStreamToString(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while (true) {
                String line = r.readLine();
                if (line == null) {
                    break;
                }
                stringBuilder.append(line + StringUtils.LF);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.username = args.getString(USERNAME);
            this.url = args.getString("url");
        }
        setAppNameAndVersion(getContext().getApplicationInfo().packageName);
        setCommitHash(getContext());
        setDescription(getContext());
    }

    private void setCommitHash(Context context) {
        ((TextView) getActivity().findViewById(C0935R.id.commit_hash)).setText(getCommitMessage(context));
    }

    private void setDescription(Context context) {
        ((TextView) getActivity().findViewById(C0935R.id.description)).setText(getDescriptionMessage(context));
    }

    private SpannableString getDescriptionMessage(Context context) {
        SpannableString linkedMessage = new SpannableString(Html.fromHtml(convertFromInputStreamToString(context.getResources().openRawResource(C0935R.raw.description)).toString()));
        Linkify.addLinks(linkedMessage, 3);
        return linkedMessage;
    }

    private void setAppNameAndVersion(String packageName) {
        TextView appNameTextView = (TextView) getActivity().findViewById(C0935R.id.app_name);
        TextView sessionText = (TextView) getActivity().findViewById(C0935R.id.app_session);
        TextView appVersionTextView = (TextView) getActivity().findViewById(C0935R.id.app_version);
        ImageView appIconImageView = (ImageView) getActivity().findViewById(C0935R.id.app_icon);
        ApplicationInfo applicationInfo = null;
        PackageManager packageManager = getContext().getPackageManager();
        String appName = "";
        String appVersion = "";
        String appBuild = "";
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (applicationInfo != null) {
            appName = packageManager.getApplicationLabel(applicationInfo).toString();
            try {
                appVersion = "" + packageManager.getPackageInfo(packageName, 0).versionName;
                appBuild = "" + packageManager.getPackageInfo(packageName, 0).versionCode;
                appIconImageView.setImageDrawable(packageManager.getApplicationIcon(packageName));
            } catch (NameNotFoundException e2) {
                e2.printStackTrace();
            }
        }
        if (appName.length() > 0 && appVersion.length() > 0) {
            appNameTextView.setText(appName);
            if (appBuild.isEmpty()) {
                appVersionTextView.setText(appVersion);
            } else {
                appVersionTextView.setText(appVersion + " (" + appBuild + Expression.PAR_CLOSE);
            }
        }
        if (this.url != null && this.username != null) {
            sessionText.setText(String.format(Locale.getDefault(), "%s %s\n", new Object[]{getString(C0935R.string.logged_in_as), this.username}));
            sessionText.append(getString(C0935R.string.logged_in_at) + " ");
            addUrl(sessionText, this.url);
            sessionText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    protected void addUrl(TextView textView, String url) {
        textView.append(Html.fromHtml(String.format(Locale.getDefault(), "<a href=\"%s\">%s</a>", new Object[]{url, url})));
    }

    public void onAttach(Context context) {
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).setOnBackPressedCallback(this);
        }
        if (context instanceof OnBackPressedFromFragmentCallback) {
            this.onBackPressedFromFragmentCallback = (OnBackPressedFromFragmentCallback) context;
        }
        super.onAttach(context);
    }

    public void onDetach() {
        if (getActivity() != null && (getActivity() instanceof BaseActivity)) {
            ((BaseActivity) getActivity()).setOnBackPressedCallback(null);
        }
        this.onBackPressedFromFragmentCallback = null;
        super.onDetach();
    }

    public boolean onBackPressed() {
        if (this.onBackPressedFromFragmentCallback == null) {
            return true;
        }
        this.onBackPressedFromFragmentCallback.onBackPressedFromFragment();
        return false;
    }
}
