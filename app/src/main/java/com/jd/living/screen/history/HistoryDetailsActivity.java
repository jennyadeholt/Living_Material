package com.jd.living.screen.history;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.jd.living.R;
import com.jd.living.database.SearchDatabase;
import com.jd.living.database.SearchHistoryDatabase;
import com.jd.living.model.ormlite.SearchHistory;
import com.jd.living.util.StringUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity
public class HistoryDetailsActivity extends ActionBarActivity {

    @ViewById
    protected TextView address;

    @ViewById
    protected TableLayout tableLayout;

    @Bean
    SearchHistoryDatabase historyDatabase;

    @Bean
    SearchDatabase searchDatabase;

    private SearchHistory searchHistory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_details_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_run_search:
                searchDatabase.launchListingsSearch(searchHistory);
                finish();
                return true;
            case R.id.action_watch:
                //favoriteDatabase.updateFavorite(listing);
                return true;
            case R.id.action_delete_history:
                historyDatabase.deleteSearchHistory(searchHistory);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @AfterViews
    public void init() {
        int id = getIntent().getIntExtra("id", -1);

        if (id != -1) {
            searchHistory = historyDatabase.getSearchHistory(id);
            if (searchHistory != null) {
                setupDetails();
            }
        }
    }

    protected void setupDetails() {
        tableLayout.removeAllViews();
        address.setText(searchHistory.getLocation());

        addDetails(R.string.preferences_area, searchHistory.getLocation());
        addDetails(R.string.preferences_amount_min, searchHistory.getMinAmount());
        addDetails(R.string.preferences_amount_max, searchHistory.getMaxAmount());
        addDetails(R.string.preferences_rent_max, searchHistory.getMaxRent());
        addDetails(R.string.preferences_nbr_of_rooms_min, getString(R.string.details_room_text, searchHistory.getMinRooms()));
        addDetails(R.string.preferences_nbr_of_rooms_max, getString(R.string.details_room_text, searchHistory.getMaxRooms()));

        addDetails(R.string.preferences_type_of_building, StringUtil.getBuildingTypes(this, searchHistory.getTypes().split(", ")));

        String[] names = getResources().getStringArray(R.array.build_types_strings);
        String[] types = getResources().getStringArray(R.array.build_types);

        String production = StringUtil.getText(searchHistory.getProduction(), names, types);
        addDetails(R.string.preferences_type_of_production, production);
    }

    protected void addDetails(int nameId, String content) {
        View row = getLayoutInflater().inflate(R.layout.table_row, null);

        ((TextView) row.findViewById(R.id.extra_name)).setText(nameId);
        ((TextView) row.findViewById(R.id.content)).setText(content);
        tableLayout.addView(row);
    }
}
