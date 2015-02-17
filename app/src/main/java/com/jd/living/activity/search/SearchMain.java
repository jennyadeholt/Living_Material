package com.jd.living.activity.search;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jd.living.R;
import com.jd.living.activity.settings.SearchPreferencesFragment_;

import org.androidannotations.annotations.EFragment;

@EFragment
public class SearchMain extends Fragment {

    private FragmentTabHost mTabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabs, null);

        mTabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);

        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(
                mTabHost.newTabSpec("1").setIndicator("", getResources().getDrawable(R.drawable.tab_menu_search_result)),
                NewSearch_.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("2").setIndicator("", getResources().getDrawable(R.drawable.tab_menu_new_search)),
                SearchPreferencesFragment_.class, null);

        return view;
    }

    public void resetTabs() {
        mTabHost.setCurrentTab(0);
    }
}
