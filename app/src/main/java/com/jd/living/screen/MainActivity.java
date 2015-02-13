package com.jd.living.screen;

import android.os.Bundle;
import android.preference.PreferenceScreen;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.jd.living.R;
import com.jd.living.activity.history.HistoryList_;
import com.jd.living.activity.search.Favorites_;
import com.jd.living.activity.search.SearchMain;
import com.jd.living.activity.settings.PreferenceFragment.OnPreferenceAttachedListener;
import com.jd.living.activity.settings.SearchPreferencesFragment_;
import com.jd.living.activity.settings.SettingsPreferenceFragment_;
import com.jd.living.database.DatabaseHelper;
import com.jd.living.help.NavigationDrawerActivity;
import com.jd.living.model.Listing;
import com.jd.living.screen.drawer.DrawerMenuAdapter;
import com.jd.living.screen.drawer.NavigationFragment;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import java.util.List;

@EActivity
public class MainActivity extends NavigationDrawerActivity implements OnPreferenceAttachedListener,
        DatabaseHelper.SearchDatabaseListener {


    @Bean
    DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupNavigationDrawer();
        database.addDatabaseListener(this);
    }

    @Override
    public void onPreferenceAttached(PreferenceScreen root, int xmlId) {

    }

    @Override
    public void onUpdate(List<Listing> result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                navigationFragment.onUpdate(1);
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
