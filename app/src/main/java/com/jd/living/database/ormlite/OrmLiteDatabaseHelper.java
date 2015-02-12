package com.jd.living.database.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.table.TableUtils;
import com.jd.living.model.ormlite.Favorite;
import com.jd.living.model.ormlite.SearchHistory;

import java.sql.SQLException;


/**
 * Database helper class used to manage the creation and upgrading of your
 * database. This class also usually provides the DAOs used by the other
 * classes.
 */
public class OrmLiteDatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "living.db";

    private static final int DATABASE_VERSION = 2;

    private RuntimeExceptionDao<SearchHistory, Integer> searchHistoryDao = null;
    private RuntimeExceptionDao<Favorite, Integer> favoriteDatabase = null;


    public OrmLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, com.j256.ormlite.support.ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, SearchHistory.class);
            TableUtils.createTable(connectionSource, Favorite.class);
        } catch (SQLException e) {
            Log.e(OrmLiteDatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, com.j256.ormlite.support.ConnectionSource connectionSource, int i, int i2) {
        try {
            TableUtils.dropTable(connectionSource, SearchHistory.class, true);
            TableUtils.dropTable(connectionSource, Favorite.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            Log.e(OrmLiteDatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    public RuntimeExceptionDao<SearchHistory, Integer> getSearchHistoryDao() {
        if (searchHistoryDao == null) {
            searchHistoryDao = getRuntimeExceptionDao(SearchHistory.class);
        }
        return searchHistoryDao;
    }


    public RuntimeExceptionDao<Favorite, Integer> getFavoriteDatabase() {
        if (favoriteDatabase == null) {
            favoriteDatabase = getRuntimeExceptionDao(Favorite.class);
        }
        return favoriteDatabase;
    }

    @Override
    public void close() {
        super.close();
        searchHistoryDao = null;
        favoriteDatabase = null;
    }
}