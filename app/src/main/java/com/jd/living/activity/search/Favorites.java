package com.jd.living.activity.search;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jd.living.R;
import com.jd.living.database.DatabaseHelper;
import com.jd.living.model.Listing;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;

import java.util.List;

@EFragment
public class Favorites extends SearchList implements DatabaseHelper.FavoriteDatabaseListener  {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @UiThread
    protected void update(List<Listing> result) {
        if (isAdded()) {
            info.setText(getString(R.string.number_of_favorites, result.size()));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.favorite_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_favorite:
                return true;
            case R.id.action_delete_all_favorites:
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
