package com.jd.living.activity.search;

import com.jd.living.R;
import com.jd.living.Search;
import com.jd.living.activity.details.SearchDetailsFragment_;
import com.jd.living.database.DatabaseHelper;
import com.jd.living.help.NavigationDrawerActivity;
import com.jd.living.model.Listing;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;

import java.util.List;

@EFragment
public class NewSearch extends SearchList implements DatabaseHelper.SearchDatabaseListener {

    @UiThread
    public void update(List<Listing> result) {
        if (isAdded()) {

            String name = "";
            Search search = database.getSearchDatabase().getSearch();
            if (search != null) {
                name = search.getLocation() + " : ";
            }
            info.setText(name + getString(R.string.number_of_objects, result.size(), result.size()));
        }
    }

    @Override
    protected DatabaseHelper.DatabaseState getDataBaseState() {
        return DatabaseHelper.DatabaseState.SEARCH;
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

    @ItemClick
    void listItemClicked(Listing listing) {
        database.setCurrentId(listing.getBooliId(), getDataBaseState());
        ((NavigationDrawerActivity) getActivity()).updateFragment(new SearchDetailsFragment_());
    }

    @Override
    public void onUpdate(List<Listing> result) {
        update(result);
        searchListAdapter.onUpdate(result);
    }

    @Override
    public void onSearchStarted() {

    }

    @Override
    public void onFavoriteUpdated() {
        searchListAdapter.update();
    }

    @Override
    public void onDetailsRequested(int booliId) {

    }
}


