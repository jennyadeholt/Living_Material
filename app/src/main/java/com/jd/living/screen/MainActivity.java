package com.jd.living.screen;

import android.os.Bundle;
import android.preference.PreferenceScreen;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.jd.living.R;
import com.jd.living.activity.history.HistoryList_;
import com.jd.living.activity.search.Favorites_;
import com.jd.living.activity.search.SearchMain;
import com.jd.living.activity.search.SearchMain_;
import com.jd.living.activity.settings.PreferenceFragment.OnPreferenceAttachedListener;
import com.jd.living.activity.settings.SettingsPreferenceFragment_;
import com.jd.living.database.DatabaseHelper;
import com.jd.living.model.Listing;
import com.jd.living.screen.drawer.DrawerMenuAdapter;
import com.jd.living.screen.drawer.NavigationFragment;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import java.util.List;

@EActivity
public class MainActivity extends ActionBarActivity implements NavigationFragment.DrawerMenuListener,
        OnPreferenceAttachedListener, DatabaseHelper.SearchDatabaseListener {

    @Bean
    DatabaseHelper database;

    protected NavigationFragment navigationFragment;
    private Fragment currentFragment;

    private static int currentPosition = 1;
    private CharSequence mTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            currentPosition = savedInstanceState.getInt("current");
        }

        setContentView(R.layout.activity_main);
        setupNavigationDrawer();


        navigationFragment.onUpdate(currentPosition);
    }

    @Override
    protected void onResume() {
        super.onResume();
        database.addDatabaseListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        database.removeDatabaseListener(this);
    }

    @Override
    public void onPreferenceAttached(PreferenceScreen root, int xmlId) {

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("current", currentPosition);
    }

    @Override
    public void onBackPressed() {
        getSupportActionBar().setTitle(mTitle);
        super.onBackPressed();
    }

    @Override
    public void onUpdate(List<Listing> result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (currentPosition == 1 && currentFragment != null && currentFragment instanceof SearchMain) {
                    ((SearchMain) currentFragment).resetTabs();
                } else if (currentPosition == 2) {
                    currentPosition = 1;
                    navigationFragment.onUpdate(currentPosition);
                }
            }
        });
    }

    protected void setupNavigationDrawer() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);

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
                fragment = new SearchMain_();
                break;
            case 2:
                fragment = new HistoryList_();
                break;
            case 3:
                fragment = new Favorites_();
                break;
            case 4:
                fragment = new SettingsPreferenceFragment_();
            default:
                break;
        }

        currentPosition = pos;
        updateFragment(fragment, item);
    }

    protected void updateFragment(Fragment fragment, DrawerMenuAdapter.DrawerMenuItem item) {

        if (fragment != null) {
            currentFragment = fragment;

            FragmentManager fragmentManager = getSupportFragmentManager();
            if (!isFinishing()) {
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commitAllowingStateLoss();
            } else {
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();
            }

        }
        mTitle = item.getLabel();
        setTitle(item.getLabel());
    }

    @Override
    public void onSearchStarted() {

    }

    @Override
    public void onFavoriteUpdated() {

    }


}
