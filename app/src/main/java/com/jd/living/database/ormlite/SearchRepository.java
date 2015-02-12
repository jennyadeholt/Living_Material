package com.jd.living.database.ormlite;

import com.jd.living.model.ormlite.SearchHistory;

import java.util.List;


public class SearchRepository {

    private final OrmLiteDatabaseHelper helper;

    public SearchRepository(OrmLiteDatabaseHelper helper) {
        this.helper = helper;
    }

    public List<SearchHistory> getSearchHistories() {
        return helper.getSearchHistoryDao().queryForAll();
    }

    public SearchHistory getSearchHistory(int id) {
        return helper.getSearchHistoryDao().queryForId(id);
    }

    public void addSearchHistory(SearchHistory searchHistory) {
        helper.getSearchHistoryDao().createIfNotExists(searchHistory);
    }

    public void updateSearchHistory(SearchHistory searchHistory) {
        helper.getSearchHistoryDao().update(searchHistory);
    }

    public void deleteSeachHistory(SearchHistory searchHistory) {
        helper.getSearchHistoryDao().delete(searchHistory);
    }

    public void clearSearchDatabase() {
        helper.getSearchHistoryDao().delete(getSearchHistories());
    }

    public SearchHistory getLatestSearchHistory() {
        List<SearchHistory> searchHistories = getSearchHistories();
        if (searchHistories.isEmpty()) {
            return null;
        } else {
            SearchHistory history = searchHistories.get(0);
            for (SearchHistory searchHistory : searchHistories) {
                if (searchHistory.getTimestamp() > history.getTimestamp()) {
                    history = searchHistory;
                }
            }
            return history;
        }
    }
}