package com.jd.living.screen.drawer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jd.living.R;

public class NavigationFragment extends Fragment {

    public static final String PREF_FILE_NAME = "DRAWER";
    public static final String PREF_KEY = "LEARNED";

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View mDrawerView;

    private RecyclerView mRecyclerView;

    private boolean mIsLearned = false;
    private boolean firstResume = false;

    private DrawerMenuListener mDrawerMenuListener;
    DrawerMenuAdapter adapter;

    private int oldItemId;

    public NavigationFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof DrawerMenuListener) {
            mDrawerMenuListener = (DrawerMenuListener) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mIsLearned = isLearned();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(getLayoutManager());
        mRecyclerView.setAdapter(getDrawerMenuAdapter());

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            firstResume = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (firstResume && !mIsLearned) {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }

        firstResume = false;
    }

    public void setUp(int drawerViewId, DrawerLayout drawerLayout, Toolbar toolbar) {
        mDrawerView = getActivity().findViewById(drawerViewId);
        mDrawerLayout = drawerLayout;

        mDrawerToggle = new Toggle(
                getActivity(),
                mDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        );

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerToggle.syncState();
    }

    private LinearLayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    private DrawerMenuAdapter getDrawerMenuAdapter() {
        if (adapter == null ) {
            adapter = new DrawerMenuAdapter(getActivity());
            adapter.setDrawerMenuListener(new DrawerMenuAdapter.DrawerMenuListener() {
                @Override
                public void onMenuItemClicked(int position, DrawerMenuAdapter.DrawerMenuItem item) {
                    onUpdate(position, item);
                }
            });
        }

        return adapter;
    }

    private boolean isLearned() {
        return getActivity().getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE).getBoolean(PREF_KEY, false);
    }

    private void setLearned(boolean learned) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_KEY, learned).apply();

        mIsLearned = learned;
    }


    public void onUpdate(int position) {
        onUpdate(position, adapter.getItem(position));
    }

    private void onUpdate(int position, DrawerMenuAdapter.DrawerMenuItem item) {
        if (mDrawerMenuListener != null) {
            mDrawerMenuListener.onMenuItemClicked(position, item);
        }
        RecyclerView.ViewHolder oldViewHolder = mRecyclerView.findViewHolderForPosition(oldItemId);
        if (oldViewHolder != null) {
            oldViewHolder.itemView.setSelected(false);
        }
        RecyclerView.ViewHolder newViewHolder = mRecyclerView.findViewHolderForPosition(position);
        if (newViewHolder != null) {
            newViewHolder.itemView.setSelected(true);
        }
        oldItemId = position;
        mDrawerLayout.closeDrawer(mDrawerView);
    }


    private class Toggle extends ActionBarDrawerToggle {
        public Toggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            if (!isLearned()) {
                setLearned(true);
            }

            getActivity().invalidateOptionsMenu();
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
            getActivity().invalidateOptionsMenu();
        }
    }

    public static interface DrawerMenuListener {
        public abstract void onMenuItemClicked(int pos, DrawerMenuAdapter.DrawerMenuItem item);
    }
}