package org.icddrb.dhis.client.sdk.ui.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import java.io.Serializable;
import org.icddrb.dhis.client.sdk.ui.C0935R;
import org.icddrb.dhis.client.sdk.utils.Preconditions;

public class WrapperFragment extends BaseFragment implements OnClickListener {
    public static final String ARG_NESTED_BUNDLE = "arg:nestedBundle";
    private static final String ARG_NESTED_FRAGMENT = "arg:nestedFragment";
    private static final String ARG_TITLE = "arg:title";
    private Toolbar toolbar;

    @NonNull
    public static WrapperFragment newInstance(@NonNull Class<? extends Fragment> fragmentClass, String title) {
        Preconditions.isNull(fragmentClass, "Fragment class must bot be null");
        Preconditions.isNull(title, "title must bot be null");
        Bundle arguments = new Bundle();
        arguments.putString(ARG_TITLE, title);
        arguments.putSerializable(ARG_NESTED_FRAGMENT, fragmentClass);
        WrapperFragment wrapperFragment = new WrapperFragment();
        wrapperFragment.setArguments(arguments);
        return wrapperFragment;
    }

    @NonNull
    public static WrapperFragment newInstance(@NonNull Class<? extends Fragment> fragmentClass, String title, Bundle bundle) {
        Preconditions.isNull(fragmentClass, "Fragment class must bot be null");
        Preconditions.isNull(title, "title must bot be null");
        Bundle arguments = new Bundle();
        arguments.putString(ARG_TITLE, title);
        arguments.putSerializable(ARG_NESTED_FRAGMENT, fragmentClass);
        arguments.putBundle(ARG_NESTED_BUNDLE, bundle);
        WrapperFragment wrapperFragment = new WrapperFragment();
        wrapperFragment.setArguments(arguments);
        return wrapperFragment;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(C0935R.layout.fragment_wrapper, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.toolbar = (Toolbar) view.findViewById(C0935R.id.toolbar);
        Drawable buttonDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(getActivity(), C0935R.drawable.ic_menu));
        DrawableCompat.setTint(buttonDrawable, ContextCompat.getColor(getContext(), 17170443));
        this.toolbar.setNavigationIcon(buttonDrawable);
        this.toolbar.setNavigationOnClickListener(this);
        this.toolbar.setTitle(getTitle());
        if (getFragmentClass() != null) {
            Fragment fragment = getFragment();
            fragment.setArguments(getArguments().getBundle(ARG_NESTED_BUNDLE));
            attachFragment(fragment, getFragmentClass().getSimpleName());
        }
    }

    public void onClick(View v) {
        toggleNavigationDrawer();
    }

    @NonNull
    private String getTitle() {
        if (!isAdded() || getArguments() == null) {
            return "";
        }
        return getArguments().getString(ARG_TITLE, "");
    }

    @NonNull
    private Fragment getFragment() {
        if (isAdded() && getArguments() != null) {
            Class<? extends Fragment> fragmentClass = getFragmentClass();
            if (fragmentClass != null) {
                try {
                    return (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return new Fragment();
    }

    @NonNull
    protected Toolbar getToolbar() {
        return this.toolbar;
    }

    private Class<? extends Fragment> getFragmentClass() {
        Serializable fragmentClassSerialized = getArguments().getSerializable(ARG_NESTED_FRAGMENT);
        if (fragmentClassSerialized != null) {
            return (Class) fragmentClassSerialized;
        }
        return null;
    }

    private void attachFragment(@NonNull Fragment fragment, String tag) {
        FragmentManager fragmentManager = getChildFragmentManager();
        if (fragmentManager.findFragmentByTag(tag) == null) {
            fragmentManager.beginTransaction().replace(C0935R.id.container_fragment_frame, fragment, tag).commit();
        }
    }
}
