package com.jd.living.help;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.jd.living.R;
import com.jd.living.activity.history.HistoryList_;
import com.jd.living.activity.search.Favorites_;
import com.jd.living.activity.search.SearchMain;
import com.jd.living.activity.settings.SearchPreferencesFragment_;
import com.jd.living.activity.settings.SettingsPreferenceFragment_;
import com.jd.living.screen.drawer.DrawerMenuAdapter;
import com.jd.living.screen.drawer.NavigationFragment;


public abstract class NavigationDrawerActivity extends ActionBarActivity implements NavigationFragment.DrawerMenuListener {

    protected NavigationFragment navigationFragment;

    protected void setupNavigationDrawer() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            navigationFragment = (NavigationFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.navigation_fragment);

            navigationFragment.setUp(
                    R.id.navigation_fragment,
                    (DrawerLayout) findViewById(R.id.drawer_layout),
                    toolbar);
        }
    }

    @Override
    public void onMenuItemClicked(int pos, DrawerMenuAdapter.DrawerMenuItem item) {
        Fragment fragment = null;
        switch (pos) {
            case 1:
                fragment = new SearchMain();
                break;
            case 2:
                fragment = new SearchPreferencesFragment_();
                break;
            case 3:
                fragment = new HistoryList_();
                break;
            case 4:
                fragment = new Favorites_();
                break;
            case 5:
                fragment = new SettingsPreferenceFragment_();
            default:
                break;
        }

        updateFragment(fragment, item);
    }

    protected void updateFragment(Fragment fragment, DrawerMenuAdapter.DrawerMenuItem item) {
        updateFragment(fragment);
        setTitle(item.getLabel());
    }

    public void updateFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commitAllowingStateLoss();
        }
    }
}
