package com.jd.living.activity.details.search;


import android.os.Bundle;

import com.jd.living.activity.details.DetailsView;
import com.jd.living.database.DatabaseHelper;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EFragment;

@EFragment
public class SearchView extends DetailsView {

    public static SearchView_ newInstance(int num) {
        SearchView_ f = new SearchView_();
        Bundle args = new Bundle();
        args.putInt("objectIndex", num);
        f.setArguments(args);
        return f;
    }

    @Override
    protected DatabaseHelper.DatabaseState getDataBaseState() {
        return DatabaseHelper.DatabaseState.SEARCH;
    }
}
