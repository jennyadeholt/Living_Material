package com.jd.living.database;


import android.content.pm.LabeledIntent;

import com.jd.living.model.Listing;
import com.jd.living.model.Result;
import com.jd.living.model.ormlite.SearchHistory;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@EBean(scope = EBean.Scope.Singleton)
public class DatabaseHelper implements SearchDatabase.SearchListener, FavoriteDatabase.FavoriteListener {

    public enum DatabaseState {
        FAVORITE,
        SEARCH
    }

    @Bean
    protected SearchDatabase searchDatabase;
    @Bean
    protected FavoriteDatabase favoriteDatabase;

    public interface DatabaseListener {
        void onUpdate(List<Listing> result);

        void onDetailsRequested(int booliId);
    }

    public interface FavoriteDatabaseListener extends DatabaseListener {
    }

    public interface SearchDatabaseListener extends DatabaseListener {
        void onFavoriteUpdated();

        void onSearchStarted();
    }

    private List<FavoriteDatabaseListener> favoriteDatabaseListeners = new ArrayList<>();
    private List<SearchDatabaseListener> searchDatabaseListeners = new ArrayList<>();

    private List<Listing> searchResult = new ArrayList<>();
    private List<Listing> favoriteResult = new ArrayList<>();

    @AfterInject
    public void init() {
        searchDatabase.setListingsListeners(this);
        favoriteDatabase.setFavoriteListener(this);
    }

    public void addDatabaseListener(DatabaseListener listener) {
        if (listener instanceof FavoriteDatabaseListener) {
            favoriteDatabaseListeners.add((FavoriteDatabaseListener) listener);
            listener.onUpdate(favoriteResult);
        }
        if (listener instanceof SearchDatabaseListener) {
            searchDatabaseListeners.add((SearchDatabaseListener) listener);
            if (searchResult.isEmpty()) {
                searchDatabase.launchListingsSearch();
            } else {
                listener.onUpdate(searchResult);
            }
        }
    }

    public void removeDatabaseListener(DatabaseListener listener) {
        if (listener instanceof FavoriteDatabaseListener) {
            favoriteDatabaseListeners.remove(listener);
        }
        if (listener instanceof SearchDatabaseListener) {
            searchDatabaseListeners.remove(listener);
        }
    }

    public void setCurrentListIndex(int i, DatabaseState state) {
        switch (state) {
            case SEARCH:
                searchDatabase.setCurrentListIndex(i);
                break;
            case FAVORITE:
                favoriteDatabase.setCurrentListIndex(i);
                break;
        }
    }

    public void setCurrentId(int i, DatabaseState state) {
        switch (state) {
            case SEARCH:
                searchDatabase.setCurrentId(i);
                break;
            case FAVORITE:
                favoriteDatabase.setCurrentId(i);
                break;
        }
    }

    public Listing getCurrentListing(DatabaseState state) {
        switch (state) {
            case SEARCH:
                return searchDatabase.getCurrentListing();
            case FAVORITE:
                return favoriteDatabase.getCurrentListing();
        }
        return null;
    }

    public Listing getListingBasedOnLocation(int objectIndex, DatabaseState state) {
        switch (state) {
            case SEARCH:
                return searchDatabase.getListingBasedOnLocation(objectIndex);
            case FAVORITE:
                return favoriteDatabase.getListingBasedOnLocation(objectIndex);
        }
        return new Listing();
    }

    public int getListIndex(int booliId, DatabaseState state) {
        switch (state) {
            case SEARCH:
                return searchDatabase.getListIndex(booliId);
            case FAVORITE:
                return favoriteDatabase.getListIndex(booliId);
        }
        return -1;
    }

    public Listing getListing(int booliId, DatabaseState state) {
        switch (state) {
            case SEARCH:
                return searchDatabase.getListing(booliId);
            case FAVORITE:
                return favoriteDatabase.getListing(booliId);
        }
        return null;
    }

    public List<Listing> getResult(DatabaseState state) {
        switch (state) {
            case SEARCH:
                return searchResult;
            case FAVORITE:
                return favoriteResult;
        }
        return new ArrayList<>();
    }

    public void launchSearch() {
        searchDatabase.launchListingsSearch();
    }

    public void launchSearch(SearchHistory searchHistory) {
        searchDatabase.launchListingsSearch(searchHistory);
    }

    @Override
    public void onUpdateFavorites() {
        favoriteResult = favoriteDatabase.getResult();
        onUpdate(DatabaseState.FAVORITE);
    }

    @Override
    public void onUpdateSearch() {
        searchResult = searchDatabase.getResult();
        onUpdate(DatabaseState.SEARCH);
    }

    @Override
    public void onSearchStarted() {
        for (SearchDatabaseListener listener : searchDatabaseListeners) {
            listener.onSearchStarted();
        }
    }

    @Override
    public void onDetailsRequestedForFavorite(int booliId) {
        onDetailsRequested(DatabaseState.FAVORITE, booliId);
    }

    @Override
    public void onDetailsRequestedForSearch(int booliId) {
        onDetailsRequested(DatabaseState.SEARCH, booliId);
    }

    public FavoriteDatabase getFavoriteDatabase() {
        return favoriteDatabase;
    }

    public SearchDatabase getSearchDatabase() {
        return searchDatabase;
    }

    synchronized private void onUpdate(DatabaseState state) {

        switch (state) {
            case FAVORITE:
                for (FavoriteDatabaseListener listener : favoriteDatabaseListeners) {
                    listener.onUpdate(favoriteResult);
                }

                for (SearchDatabaseListener listener : searchDatabaseListeners) {
                    listener.onFavoriteUpdated();
                }

                break;
            case SEARCH:
                List<SearchDatabaseListener> b = new ArrayList<>(searchDatabaseListeners);
                for (DatabaseListener listener : b) {
                    listener.onUpdate(searchResult);
                }

                break;
            default:
                break;
        }
    }

    private void onDetailsRequested(DatabaseState state, int booliId) {
        if (state == DatabaseState.FAVORITE)
            for (FavoriteDatabaseListener listener : favoriteDatabaseListeners) {
                listener.onDetailsRequested(booliId);
            }
    }
}

