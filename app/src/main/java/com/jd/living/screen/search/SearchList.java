package com.jd.living.screen.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
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

import java.lang.reflect.Field;
import java.util.List;

@EFragment
public abstract class SearchList extends ListFragment {

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

    protected abstract void update(List<Listing> result);

    protected abstract DatabaseHelper.DatabaseState getDataBaseState();

    @AfterViews
    public void init() {
        setListAdapter(searchListAdapter);
        registerForContextMenu(getListView());

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Listing listing = database.getListingBasedOnLocation(position, getDataBaseState());
                if (listing != null) {
                    favoriteDatabase.updateFavorite(listing);
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list, container, false);
}

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

