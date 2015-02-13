package com.jd.living.activity.details;


import com.jd.living.database.DatabaseHelper;

import org.androidannotations.annotations.EFragment;

@EFragment
public class SearchDetailsFragment extends DetailsFragment {

    @Override
    protected DatabaseHelper.DatabaseState getDataBaseState() {
        return DatabaseHelper.DatabaseState.SEARCH;
    }
}
