package com.jd.living.screen.drawer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jd.living.help.AbstractListAdapter;
import com.jd.living.R;

import java.util.ArrayList;
import java.util.List;

public class DrawerMenuAdapter extends AbstractListAdapter<DrawerMenuAdapter.DrawerMenuItem, DrawerMenuAdapter.DrawerMenuViewHolder> {

    private LayoutInflater mInflater;
    private DrawerMenuListener mDrawerMenuListener;
    private Context mContext;

    public DrawerMenuAdapter(Context context) {

        this.mContext = context;

        List<DrawerMenuItem> items = new ArrayList<>();
        items.add(new DrawerMenuItem(R.drawable.ic_menu_add, R.string.new_search, 1));
        items.add(new DrawerMenuItem(R.drawable.action_search, R.string.search_result, 2));
        //items.add(new DrawerMenuItem(R.drawable.ic_menu_mapmode, R.string.map_result));
        items.add(new DrawerMenuItem(R.drawable.ic_menu_archive, R.string.searches, 3));
        items.add(new DrawerMenuItem(R.drawable.ic_menu_star, R.string.favorites, 4));
        //items.add(new DrawerMenuItem(R.drawable.action_search, R.string.detail_view));
        //items.add(new DrawerMenuItem(R.drawable.ic_menu_camera, R.string.images));
        items.add(new DrawerMenuItem(R.drawable.ic_menu_preferences, R.string.settings, 5));
        items.add(new DrawerMenuItem(R.drawable.ic_menu_help, R.string.help, 6));
        items.add(new DrawerMenuItem(R.drawable.ic_menu_attachment, R.string.about, 7));


        setData(items);
        setHasStableIds(true);

        mInflater = LayoutInflater.from(context);
    }

    @Override
    public DrawerMenuViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new DrawerMenuViewHolder(mInflater.inflate(R.layout.drawer_menu_item, viewGroup, false)
        );
    }

    @Override
    public void onBindViewHolder(DrawerMenuViewHolder drawerMenuViewHolder, int position) {
        DrawerMenuItem item = mData.get(position);
        drawerMenuViewHolder.bind(position, item);
    }

    public void setDrawerMenuListener(DrawerMenuListener listener) {
        mDrawerMenuListener = listener;
    }

    public static interface DrawerMenuListener {
        public abstract void onMenuItemClicked(int position, DrawerMenuItem item);
    }

    public class DrawerMenuItem {
        private final int mIconResourceId;
        private final String mLabel;

        public DrawerMenuItem(int iconResourceId, int labelResourceId, int itemId) {
            mIconResourceId = iconResourceId;
            mLabel = mContext.getResources().getString(labelResourceId);
        }

        public int getIconResourceId() {
            return mIconResourceId;
        }

        public String getLabel() {
            return mLabel;
        }

        public int getItemId() {
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DrawerMenuItem that = (DrawerMenuItem) o;

            if (mIconResourceId != that.mIconResourceId) return false;
            if (mLabel != null ? !mLabel.equals(that.mLabel) : that.mLabel != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = mIconResourceId;
            result = 31 * result + (mLabel != null ? mLabel.hashCode() : 0);
            return result;
        }
    }

    public class DrawerMenuViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mIcon;
        private final TextView mLabel;

        private int mPosition;
        private DrawerMenuItem mMenuItem;

        public DrawerMenuViewHolder(View itemView) {
            super(itemView);
            mIcon = (ImageView) itemView.findViewById(R.id.icon);
            mLabel = (TextView) itemView.findViewById(R.id.label);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDrawerMenuListener != null) {
                        mDrawerMenuListener.onMenuItemClicked(mPosition, mMenuItem);
                    }
                }
            });
        }

        public void bind(int position, DrawerMenuItem item) {
            mPosition = position;
            mMenuItem = item;
            mIcon.setImageResource(item.getIconResourceId());
            mLabel.setText(item.getLabel());
        }
    }
}
