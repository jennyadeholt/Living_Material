package com.jd.living.activity.details.search;

import com.jd.living.activity.details.DetailsActivity;
import com.jd.living.database.DatabaseHelper;

import org.androidannotations.annotations.EActivity;

@EActivity
public class SearchActivity extends DetailsActivity implements DatabaseHelper.SearchDatabaseListener {

    @Override
    protected DatabaseHelper.DatabaseState getDataBaseState() {
        return DatabaseHelper.DatabaseState.SEARCH;
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
    public void onFavoriteUpdated() {

    }

    @Override
    public void onSearchStarted() {

    }

}
