package com.jd.living.database;

import com.jd.living.LivingApplication;
import com.jd.living.database.ormlite.FavoriteRepository;
import com.jd.living.model.Listing;
import com.jd.living.model.Result;
import com.jd.living.model.ormlite.Favorite;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

@EBean(scope = EBean.Scope.Singleton)
public class FavoriteDatabase extends BooliDatabase {

    @Bean
    SearchDatabase listingsDatabase;

    public interface FavoriteListener {
        void onUpdateFavorites();
    }

    private FavoriteListener favoriteListener;
    private FavoriteRepository favoriteRepository;

    @Override
    protected void init() {
        for (Favorite favorite : getRepository().getFavorites()) {
            server.getListing(favorite.getId());
        }
    }

    public void setFavoriteListener(FavoriteListener listener) {
        favoriteListener = listener;
        listener.onUpdateFavorites();
    }

    @Override
    public void setCurrentId(int booliId) {
        currentBooliId = booliId;
    }

    public void updateFavorite(Listing listing) {
        if (isFavorite(listing)) {
            removeFavorite(listing);
        } else {
            addFavorite(listing);
            server.getListing(listing.getBooliId());
        }
    }

    public void deleteAllFavorites() {
        List<Listing> favorites = getResult();
        for (Listing favorite : favorites) {
            getRepository().deleteFavorite(favorite.getBooliId());
        }

        setResult(new ArrayList<Listing>());
        notifyListeners();

    }

    private void addFavorite(Listing listing) {
        getRepository().addFavorite(new Favorite(listing.getBooliId()));
    }

    private void removeFavorite(Listing listing) {
        listing = getListing(listing.getBooliId());

        getResult().remove(listing);
        getRepository().deleteFavorite(listing.getBooliId());
        notifyListeners();
    }

    public boolean isFavorite(Listing listing) {
        return getRepository().isFavorite(listing.getBooliId());
    }

    @Override
    public void onListingsResult(BooliDatabase.ActionCode action, Result result) {
        switch (action) {
            case FAVORITE:
                getResult().addAll(result.getResult());
                notifyListeners();
                break;
            default:
                break;
        }
    }

    private void notifyListeners() {
        favoriteListener.onUpdateFavorites();
    }

    private FavoriteRepository getRepository() {
        if (favoriteRepository == null) {
            favoriteRepository = ((LivingApplication) context.getApplicationContext()).getFavoriteRepository();
        }
        return favoriteRepository;
    }
}
