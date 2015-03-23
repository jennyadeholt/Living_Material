package com.jd.living.screen.search;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.jd.living.R;
import com.jd.living.screen.settings.SearchPreferenceKey;
import com.jd.living.database.DatabaseHelper;
import com.jd.living.model.Listing;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@EBean
public class SearchListAdapter extends ArrayAdapter<Listing> {

    @Bean
    DatabaseHelper database;

    private List<Listing> listings = new ArrayList<>();
    private static SharedPreferences preferences;

    public SearchListAdapter(Context context) {
        super(context, R.layout.list_item);
    }

    @AfterInject
    public void init(){
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SearchListItem searchListItem;
        if (convertView == null) {
            searchListItem = SearchListItem_.build(getContext());
        } else {
            searchListItem = (SearchListItem) convertView;
        }

        searchListItem.bind(getItem(position));

        return searchListItem;
    }

    @UiThread
    public void update() {
        notifyDataSetChanged();
    }

    @UiThread
    public void updateInBackground() {
        notifyDataSetInvalidated();
    }

    @Override
    public int getCount() {
        return listings.size();
    }

    @Override
    public Listing getItem(int position) {
        return listings.isEmpty() ? null : listings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void onUpdate(List<Listing> result) {
        Collections.sort(result, COMPARE);
        this.listings = result;
        update();
    }

    private static Comparator<Listing> COMPARE = new Comparator<Listing>() {
        public int compare(Listing one, Listing two) {
            String sort = preferences.getString(SearchPreferenceKey.PREFERENCE_SORT_LIST_ON, "0");
            int order = Integer.valueOf(preferences.getString(SearchPreferenceKey.PREFERENCE_SORT_ORDER, "0"));

            if (order != 0) {
                Listing temp = one;
                one = two;
                two = temp;
            }

            switch (Integer.valueOf(sort)) {
                case 0:
                    return one.getPublished().compareToIgnoreCase(two.getPublished());
                case 1:
                    return one.getAddress().compareToIgnoreCase(two.getAddress());
                case 2:
                    return one.getPrice() < two.getPrice() ? -1 : 1;
                case 3:
                    return one.getLivingArea() < two.getLivingArea() ? -1 : 1;
                case 4:
                    return one.getArea().compareToIgnoreCase(two.getArea());
                default:
                    return one.getAddress().compareToIgnoreCase(two.getAddress());
            }
        }
    };
}
