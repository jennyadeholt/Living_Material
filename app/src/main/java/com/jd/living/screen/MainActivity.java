package com.jd.living.screen;

import android.os.Bundle;
import android.preference.PreferenceScreen;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;

import com.jd.living.R;
import com.jd.living.activity.history.HistoryList_;
import com.jd.living.activity.search.Favorites_;
import com.jd.living.activity.search.NewSearch_;
import com.jd.living.activity.settings.SearchPreferencesFragment_;
import com.jd.living.activity.settings.SettingsPreferenceFragment_;
import com.jd.living.database.DatabaseHelper;
import com.jd.living.model.Listing;
import com.jd.living.screen.drawer.DrawerMenuAdapter;
import com.jd.living.screen.drawer.NavigationFragment;

import com.jd.living.activity.settings.PreferenceFragment.OnPreferenceAttachedListener;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import java.util.List;

@EActivity
public class MainActivity extends ActionBarActivity implements NavigationFragment.DrawerMenuListener,
        OnPreferenceAttachedListener, DatabaseHelper.SearchDatabaseListener {

    @Bean
    DatabaseHelper database;

    NavigationFragment navigationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        database.addDatabaseListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Menu item info pressed!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMenuItemClicked(int pos, DrawerMenuAdapter.DrawerMenuItem item) {
        Fragment fragment = null;
        switch (pos) {
            case 0:
                fragment = new NewSearch_();
                break;
            case 1:
                fragment = new SearchPreferencesFragment_();
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

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commitAllowingStateLoss();
        }
        setTitle(item.getLabel());
    }

    @Override
    public void onPreferenceAttached(PreferenceScreen root, int xmlId) {

    }

    @Override
    public void onUpdate(List<Listing> result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                navigationFragment.onUpdate(0);
            }
        });
    }

    @Override
    public void onSearchStarted() {

    }

    @Override
    public void onDetailsRequested(int booliId) {

    }

    @Override
    public void onFavoriteUpdated() {

    }
}
