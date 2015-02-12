package com.jd.living.screen.drawer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jd.living.R;
import com.jd.living.help.AbstractListAdapter;

import java.util.ArrayList;
import java.util.List;


public class DrawerMenuAdapter extends AbstractListAdapter<DrawerMenuAdapter.DrawerMenuItem,
        DrawerMenuAdapter.DrawerMenuViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ICON = 1;
    private static final int TYPE_SETTING = 2;

    private LayoutInflater mInflater;
    private DrawerMenuListener mDrawerMenuListener;
    private Context mContext;

    private List<DrawerMenuItem> items;

    public DrawerMenuAdapter(Context context) {
        this.mContext = context;

        items = new ArrayList<>();
        items.add(new DrawerMenuItem(0, R.string.app_name, 1));
        items.add(new DrawerMenuItem(R.drawable.action_search, R.string.search_result, 2));
        items.add(new DrawerMenuItem(R.drawable.ic_menu_add, R.string.new_search, 3));
        //items.add(new DrawerMenuItem(R.drawable.ic_menu_mapmode, R.string.map_result));
        items.add(new DrawerMenuItem(R.drawable.ic_menu_archive, R.string.searches, 4));
        items.add(new DrawerMenuItem(R.drawable.ic_menu_star, R.string.favorites, 5));
        //items.add(new DrawerMenuItem(R.drawable.action_search, R.string.detail_view));
        //items.add(new DrawerMenuItem(R.drawable.ic_menu_camera, R.string.images));
        items.add(new DrawerMenuItem(R.string.settings, 6));
        items.add(new DrawerMenuItem(R.string.help, 7));

        setData(items);
        setHasStableIds(true);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public DrawerMenuViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new DrawerMenuViewHolder(mInflater.inflate(R.layout.drawer_menu_header, viewGroup, false), viewType);
        } else if (viewType == TYPE_ICON) {
            return new DrawerMenuViewHolder(mInflater.inflate(R.layout.drawer_menu_item_icon, viewGroup, false), viewType);
        } else {
            return new DrawerMenuViewHolder(mInflater.inflate(R.layout.drawer_menu_item, viewGroup, false), viewType);
        }
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

        public DrawerMenuItem(int labelResourceId, int itemId) {
            this(0, labelResourceId, itemId);
        }

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

    // Witht the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        if (position == 0) {
            return TYPE_HEADER;
        } else if (items.get(position).getIconResourceId() == 0) {
            return TYPE_SETTING;
        } else {
            return TYPE_ICON;
        }
    }

    protected class DrawerMenuViewHolder extends RecyclerView.ViewHolder {

        private TextView mLabel;
        private ImageView mIcon;

        private int viewType;

        private int mPosition;
        private DrawerMenuItem mMenuItem;

        public DrawerMenuViewHolder(View itemView, int viewType) {
            super(itemView);

            this.viewType = viewType;

            if (viewType == TYPE_HEADER) {

            } else {
                mLabel = (TextView) itemView.findViewById(R.id.label);
                if (viewType == TYPE_ICON) {
                    mIcon = (ImageView) itemView.findViewById(R.id.icon);
                }
            }

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

            if (mLabel != null) {
                mLabel.setText(item.getLabel());
                if (mIcon != null) {
                    mIcon.setImageResource(item.getIconResourceId());
                }
            }
        }
    }
}
