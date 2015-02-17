package com.jd.living.activity.details.favorite;

import com.jd.living.activity.details.DetailsActivity;
import com.jd.living.database.DatabaseHelper;
import com.jd.living.model.Listing;

import org.androidannotations.annotations.EActivity;

import java.util.List;


@EActivity
public class FavoriteActivity extends DetailsActivity implements DatabaseHelper.FavoriteDatabaseListener {

    @Override
    protected DatabaseHelper.DatabaseState getDataBaseState() {
        return DatabaseHelper.DatabaseState.FAVORITE;
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
    public void onUpdate(List<Listing> result) {
        super.onUpdate(result);
    }
}
