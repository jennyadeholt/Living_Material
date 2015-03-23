package com.jd.living.screen.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jd.living.R;
import com.jd.living.screen.details.favorite.FavoriteActivity;
import com.jd.living.screen.details.favorite.FavoriteActivity_;
import com.jd.living.database.DatabaseHelper;
import com.jd.living.model.Listing;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
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
            case R.id.action_delete_all_favorites:
                favoriteDatabase.deleteAllFavorites();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @ItemClick
    void listItemClicked(Listing listing) {
        database.setCurrentId(listing.getBooliId(), getDataBaseState());

        Intent intent = new Intent(getActivity(), FavoriteActivity_.class);
        getActivity().startActivityForResult(intent, 0);
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
}
