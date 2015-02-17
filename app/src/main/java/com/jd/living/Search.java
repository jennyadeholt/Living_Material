package com.jd.living;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.jd.living.activity.settings.SearchPreferenceKey;
import com.jd.living.model.ormlite.SearchHistory;
import com.jd.living.util.StringUtil;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.HashSet;
import java.util.Set;

@EBean(scope = EBean.Scope.Singleton)
public class Search {

    @RootContext
    Context context;

    protected SharedPreferences preferences;
    protected long timestamp;

    @AfterInject
    public void init() {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setTime(long timeStamp) {
        this.timestamp = timeStamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void updateSearch(SearchHistory searchHistory) {

        Set<String> types = new HashSet<>();
        for (String type : context.getResources().getStringArray(R.array.building_types)) {
            if (searchHistory.getTypes().contains(type)) {
                types.add(type);
            }
        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(SearchPreferenceKey.PREFERENCE_BUILDING_TYPE, types);
        editor.putString(SearchPreferenceKey.PREFERENCE_ROOM_MIN_NUMBERS, searchHistory.getMinRooms());
        editor.putString(SearchPreferenceKey.PREFERENCE_ROOM_MAX_NUMBERS, searchHistory.getMaxRooms());
        editor.putString(SearchPreferenceKey.PREFERENCE_AMOUNT_MIN, searchHistory.getMinAmount());
        editor.putString(SearchPreferenceKey.PREFERENCE_AMOUNT_MAX, searchHistory.getMaxAmount());
        editor.putString(SearchPreferenceKey.PREFERENCE_LOCATION, searchHistory.getLocation());
        editor.putString(SearchPreferenceKey.PREFERENCE_BUILD_TYPE, searchHistory.getProduction());
        editor.putString(SearchPreferenceKey.PREFERENCE_OBJECT_TYPE, searchHistory.isSold() ? "1" : "0");
        editor.apply();
    }

    public String getTypes() {
        Set<String> buildTypes = preferences.getStringSet(SearchPreferenceKey.PREFERENCE_BUILDING_TYPE, new HashSet<String>());
        String types = "";

        for (String type : buildTypes.toArray(new String[buildTypes.size()])) {
            types = types + type + ", ";
        }

        if (!TextUtils.isEmpty(types)) {
            types = types.substring(0, types.length() - 2);
        }

        return types;
    }

    public String getMinRooms() {
        return preferences.getString(SearchPreferenceKey.PREFERENCE_ROOM_MIN_NUMBERS, "1");
    }

    public String getMaxRooms(boolean modify) {
        String maxRooms = preferences.getString(SearchPreferenceKey.PREFERENCE_ROOM_MAX_NUMBERS, "");
        if (modify) {
            if (maxRooms.equals("5")) {
                maxRooms = "";
            }
        } else {
            if (maxRooms.equals("")) {
                maxRooms = "5";
            }
        }
        return maxRooms;
    }

    public String getMinAmount(boolean modify) {
        String minAmount = preferences.getString(SearchPreferenceKey.PREFERENCE_AMOUNT_MIN, "");
        if (modify) {
            minAmount = StringUtil.getStringAsNumber(minAmount);
        }
        return minAmount;
    }

    public String getMaxAmount(boolean modify) {
        String maxAmount = preferences.getString(SearchPreferenceKey.PREFERENCE_AMOUNT_MAX, "");
        if (modify) {
            maxAmount = StringUtil.getStringAsNumber(maxAmount);
        }
        return maxAmount;
    }

    public String getMaxAmount() {
        String max = getMaxAmount(true);
        return max.equals("0") ? "" : max;
    }

    public String getMinAmount() {
        String min = getMinAmount(true);
        return min.equals("0") ? "" : min;
    }

    public String getLocation() {
        return preferences.getString(SearchPreferenceKey.PREFERENCE_LOCATION, "Hörby");
    }

    public String getProduction() {
        return preferences.getString(SearchPreferenceKey.PREFERENCE_BUILD_TYPE, "null");
    }

    public boolean fetchSoldObjects() {
        return !preferences.getString(SearchPreferenceKey.PREFERENCE_OBJECT_TYPE, "0").equals("0");
    }

    public String getMaxRent(boolean modify) {
        String maxAmount = preferences.getString(SearchPreferenceKey.PREFERENCE_RENT_MAX, "");
        if (modify) {
            maxAmount = StringUtil.getStringAsNumber(maxAmount);
        }
        return maxAmount;
    }

    public String getMaxRent() {
       String rent = getMaxRent(true);
       return rent.equals("0") ? "" : rent;
    }
}
