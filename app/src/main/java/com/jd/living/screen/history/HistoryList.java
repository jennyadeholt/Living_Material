package com.jd.living.screen.history;

import android.content.Intent;
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

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchHistory history = historyListAdapter.getItem(position);
                if (history != null) {
                    Intent intent = new Intent(getActivity(), HistoryDetailsActivity_.class);
                    intent.putExtra("id", history.getId());
                    getActivity().startActivity(intent);
                }
            }
        });
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_all_history:
                database.clearSearchHistoryDatabase();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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

