package com.jd.living.activity.search;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jd.living.R;
import com.jd.living.database.DatabaseHelper;
import com.jd.living.database.FavoriteDatabase;
import com.jd.living.model.Listing;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;


@EFragment
public abstract class SearchList extends ListFragment{

    @ViewById
    ListView list;

    @ViewById
    TextView info;

    @Bean
    DatabaseHelper database;
    @Bean
    SearchListAdapter searchListAdapter;
    @Bean
    FavoriteDatabase favoriteDatabase;

    @AfterViews
    public void init() {
        setListAdapter(searchListAdapter);
        registerForContextMenu(getListView());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list, container, false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == android.R.id.list) {
            Listing listing = database.getListingBasedOnLocation((
                    (AdapterView.AdapterContextMenuInfo) menuInfo).position, getDataBaseState());
            getActivity().getMenuInflater().inflate(R.menu.search_list_menu, menu);
            menu.setHeaderTitle(listing.getAddress());

            if (favoriteDatabase.isFavorite(listing)) {
                menu.removeItem(R.id.action_add);
            } else {
                menu.removeItem(R.id.action_remove);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = ( (AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
        Listing listing = database.getListingBasedOnLocation(position, getDataBaseState());

        switch(item.getItemId()) {
            case R.id.action_add:
            case R.id.action_remove:
                favoriteDatabase.updateFavorite(listing);
                return true;
            case R.id.action_view:
                database.setCurrentId(listing.getBooliId(), getDataBaseState());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    protected abstract void update(List<Listing> result);

    protected abstract DatabaseHelper.DatabaseState getDataBaseState();
}

