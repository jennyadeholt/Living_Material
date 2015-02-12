package com.jd.living.help;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractListAdapter<V, K extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<K> {

    protected List<V> mData = new ArrayList<V>();

    @Override
    public abstract K onCreateViewHolder(ViewGroup viewGroup, int viewType);

    @Override
    public abstract void onBindViewHolder(K k, int position);

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public V getItem(int position) {
        return mData.get(0);
    }

    public void setData(final List<V> data) {
        for (int i = mData.size() - 1; i >= 0; --i) {
            if (getLocation(data, mData.get(i)) < 0) {
                deleteEntity(i);
            }
        }

        for (int i = 0; i < data.size(); ++i) {
            V entity = data.get(i);
            int loc = getLocation(mData, entity);
            if (loc < 0) {
                addEntity(i, entity);
            } else if (loc != i) {
                moveEntity(i, loc);

            }
        }
    }

    private int getLocation(List<V> data, V entity) {
        for (int j = 0; j < data.size(); ++j) {
            V newEntity = data.get(j);
            if (entity.equals(newEntity)) {
                return j;
            }
        }

        return -1;
    }

    public void addEntity(int i, V entity) {
        mData.add(i, entity);
        notifyItemInserted(i);
    }

    public void deleteEntity(int i) {
        mData.remove(i);
        notifyItemRemoved(i);
    }

    public void moveEntity(int i, int loc) {
        move(mData, i, loc);
        notifyItemMoved(i, loc);
    }

    private void move(List<V> data, int a, int b) {
        V temp = data.remove(a);
        data.add(b, temp);
    }

}