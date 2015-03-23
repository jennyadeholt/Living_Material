package com.jd.living.screen.details;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.jd.living.R;
import com.jd.living.screen.details.favorite.FavoriteView_;
import com.jd.living.screen.details.search.SearchView_;
import com.jd.living.database.DatabaseHelper;
import com.jd.living.model.Listing;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity
public abstract class DetailsActivity extends ActionBarActivity {

    private static int NUM_ITEMS = 0;

    @ViewById
    protected ViewPager pager;

    @Bean
    protected DatabaseHelper database;

    protected List<Listing> result;

    private FragmentStatePagerAdapter mPagerAdapter;

    protected abstract DatabaseHelper.DatabaseState getDataBaseState();

    @AfterViews
    public void init() {
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int position) {
                Listing listing = database.getListingBasedOnLocation(position, getDataBaseState());
                database.setCurrentId(listing.getBooliId(), getDataBaseState());
                pager.setCurrentItem(position, false);
                setTitle(mPagerAdapter.getPageTitle(position));
            }

            @Override
            public void onPageScrollStateChanged(int currentState) {

            }
        });
        pager.setOffscreenPageLimit(4);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_pager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @UiThread
    public void onUpdate(List<Listing> result) {
        NUM_ITEMS = result.size();

        mPagerAdapter = new MyAdapter(getSupportFragmentManager());
        pager.setAdapter(mPagerAdapter);

        Listing listing = database.getCurrentListing(getDataBaseState());
        pager.setCurrentItem(database.getListIndex(listing.getBooliId(), getDataBaseState()), false);
        setTitle("");
    }

    public class MyAdapter extends FragmentStatePagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            if (getDataBaseState() == DatabaseHelper.DatabaseState.FAVORITE) {
                return FavoriteView_.newInstance(position);
            } else {
                return SearchView_.newInstance(position);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //Listing listing = database.getListingBasedOnLocation(position, getDataBaseState());
            return "";
        }
    }
}
