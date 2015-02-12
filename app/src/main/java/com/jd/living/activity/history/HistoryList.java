package com.jd.living.activity.history;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jd.living.R;
import com.jd.living.database.DatabaseHelper;
import com.jd.living.database.SearchHistoryDatabase;
import com.jd.living.model.ormlite.SearchHistory;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;


@EFragment
public class HistoryList extends ListFragment implements SearchHistoryDatabase.SearchHistoryDatabaseListener {

    @ViewById
    ListView list;

    @ViewById
    TextView info;

    @Bean
    SearchHistoryDatabase database;

    @Bean
    DatabaseHelper databaseHelper;

    @Bean
    HistoryListAdapter historyListAdapter;


    @AfterViews
    public void init() {
        setListAdapter(historyListAdapter);
        registerForContextMenu(getListView());
    }

    @Override
    public void onStart() {
        super.onStart();
        database.registerSearchHistoryDatabaseListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        database.unregisterSearchHistoryDatabaseListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.list, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_history, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == android.R.id.list) {
            SearchHistory history = getSearchHistory(menuInfo);
            getActivity().getMenuInflater().inflate(R.menu.history_list_menu, menu);
            menu.setHeaderTitle(history.getLocation());
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        SearchHistory history = getSearchHistory(item.getMenuInfo());
        switch (item.getItemId()) {
            case R.id.action_remove:
                database.deleteSearchHistory(history);
                return true;
            case R.id.action_view:
                // database.setCurrentId(listing.getBooliId());
                return true;
            case R.id.action_run_search:
                databaseHelper.launchSearch(history);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_all_history:
                database.clearSearchHistoryDatabase();
                return true;
            case R.id.action_delete_history:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
    @ItemClick
    void listItemClicked(final SearchHistory searchHistory) {

        VerificationDialogFragment dialogFragment = new VerificationDialogFragment(new VerificationDialogFragment.NoticeDialogListener() {
            @Override
            public void onDialogPositiveClick(DialogFragment dialog) {
                databaseHelper.launchSearch(searchHistory);
            }

            @Override
            public void onDialogNegativeClick(DialogFragment dialog) {

            }
        }, R.string.dialog_update_search_title, R.string.dialog_update_search_text, searchHistory);
        dialogFragment.show(getChildFragmentManager(), "VerificationDialogFragment");
    }
    */

    @UiThread
    public void update(List<SearchHistory> result) {
        if (isAdded()) {
            info.setText(getString(R.string.number_of_searches, result.size()));
        }
    }

    @Override
    public void onUpdate(List<SearchHistory> searchHistories) {
        update(searchHistories);
    }

    private SearchHistory getSearchHistory(ContextMenu.ContextMenuInfo item) {
        int position = ((AdapterView.AdapterContextMenuInfo) item).position;
        return historyListAdapter.getItem(position);
    }
}

