package com.jd.living.screen.details.favorite;


import android.os.Bundle;

import com.jd.living.screen.details.DetailsView;
import com.jd.living.database.DatabaseHelper;

import org.androidannotations.annotations.EFragment;

@EFragment
public class FavoriteView extends DetailsView {

    public static FavoriteView_ newInstance(int num) {
        FavoriteView_ f = new FavoriteView_();
        Bundle args = new Bundle();
        args.putInt("objectIndex", num);
        f.setArguments(args);
        return f;
    }

    @Override
    protected DatabaseHelper.DatabaseState getDataBaseState() {
        return DatabaseHelper.DatabaseState.FAVORITE;
    }


}
