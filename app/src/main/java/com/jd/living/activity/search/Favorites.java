package com.jd.living.activity.search;

import com.jd.living.R;
import com.jd.living.database.DatabaseHelper;
import com.jd.living.model.Listing;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;

import java.util.List;

@EFragment
public class Favorites extends SearchList implements DatabaseHelper.FavoriteDatabaseListener  {

    @UiThread
    protected void update(List<Listing> result) {
        if (isAdded()) {
            info.setText(getString(R.string.number_of_favorites, result.size()));
        }
    }

    @Override
    protected DatabaseHelper.DatabaseState getDataBaseState() {
        return DatabaseHelper.DatabaseState.FAVORITE;
    }

    @Override
    public void onResume() {
        super.onResume();
        database.addDatabaseListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        database.removeDatabaseListener(this);
    }

    @Override
    public void onUpdate(List<Listing> result) {
        update(result);
        searchListAdapter.onUpdate(result);
    }

    @Override
    public void onDetailsRequested(int booliId) {

    }
}
