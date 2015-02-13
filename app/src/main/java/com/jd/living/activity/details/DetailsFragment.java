package com.jd.living.activity.details;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jd.living.R;
import com.jd.living.database.BooliDatabase;
import com.jd.living.database.DatabaseHelper;
import com.jd.living.database.SearchDatabase;
import com.jd.living.model.Listing;
import com.jd.living.util.StringUtil;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.InputStream;
import java.net.URL;

@EFragment
public abstract class DetailsFragment extends Fragment {

    @ViewById
    protected TableLayout tableLayout;

    @ViewById(R.id.number_of_objects)
    protected TextView nbrOfObjects;

    @ViewById
    protected TextView address;

    @ViewById
    protected TextView area;

    @ViewById
    protected TextView type;

    @ViewById
    protected TextView price;

    @ViewById
    protected ImageView thumbnail;

    @ViewById
    protected ImageView favorite;

    @Bean
    DatabaseHelper database;

    protected Listing listing;

    private int id;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_details, container, false);
    }

    @AfterViews
    public void init() {
        listing = database.getCurrentListing(getDataBaseState());

        if (listing != null) {
            id = listing.getBooliId();
            getActivity().setTitle(listing.getAddress());
            update();
        }
    }

    protected abstract DatabaseHelper.DatabaseState getDataBaseState();

    private void update() {
        setupDetails();
        updateFavorite(false);
        getImage();
    }

    protected Listing getListing() {
        return listing;
    }

    protected void updateFavorite(boolean onTouch) {
        boolean isFavorite = database.getFavoriteDatabase().isFavorite(listing);
        int resId = R.drawable.btn_star_off_disabled_focused_holo_light;

        if (listing.isSold()) {
            favorite.setVisibility(View.GONE);
        } else {
            if (onTouch) {
                int resText = R.string.toast_removed_favorite;
                if (!isFavorite) {
                    resText = R.string.toast_added_favorite;
                    resId = R.drawable.btn_rating_star_on_normal_holo_light;
                }
                database.getFavoriteDatabase().updateFavorite(listing);


                Toast.makeText(getActivity(), resText, Toast.LENGTH_SHORT).show();
            } else if (isFavorite) {
                resId = R.drawable.btn_rating_star_on_normal_holo_light;
            }
            favorite.setVisibility(View.VISIBLE);
            favorite.setImageResource(resId);
        }
    }

    protected void setupDetails() {
        boolean isSold = listing.isSold();

        tableLayout.removeAllViews();
        address.setText(listing.getAddress());
        area.setText(listing.getArea());
        type.setText(listing.getObjectType());
        if (!listing.getListPrice().equals("0")) {
            price.setText(listing.getListPrice());
        }

        int totalSize = database.getResult(getDataBaseState()).size();
        nbrOfObjects.setText(totalSize == 1 ? "" : (id + 1) + "/" + totalSize);

        if (isSold) {
            addDetails(R.string.details_sold_price, listing.getSoldPrice());
        }

        addDetails(R.string.details_living_area, getString(R.string.details_living_area_text, listing.getLivingArea()));
        if (!listing.getPlotArea().equals("0")) {
            addDetails(R.string.details_plot_area, getString(R.string.details_plot_area_text, listing.getPlotArea()));
        }

        if (!listing.getRent().equals("0")) {
            addDetails(R.string.details_rent, listing.getRent());
        }
        if (listing.getFloor() != 0) {
            addDetails(R.string.details_floor, getString(R.string.details_floor_text, listing.getFloor()));
        }

        addDetails(R.string.details_rooms, getString(R.string.details_room_text, listing.getRoomsAsString()));

        if (isSold) {
            String after = StringUtil.getDaysSince(getActivity(), listing.getPublished(), listing.getSoldDate());
            addDetails(R.string.details_sold, getString(R.string.details_sold_after, listing.getSoldDate(), after));
        } else {
            addDetails(R.string.details_published, StringUtil.getDaysSince(getActivity(), listing.getPublished(), ""));
        }

        if (listing.getConstructionYear() != 0) {
            addDetails(R.string.details_construction_year, String.valueOf(listing.getConstructionYear()));
        }

        addDetails(R.string.details_source, listing.getSource());
    }

    protected void addDetails(int nameId, String content) {
        View row = getActivity().getLayoutInflater().inflate(R.layout.table_row, null);

        ((TextView) row.findViewById(R.id.extra_name)).setText(nameId);
        ((TextView) row.findViewById(R.id.content)).setText(content);
        tableLayout.addView(row);
    }

    @Background
    public void getImage() {
        Drawable drawable = null;
        try {
            InputStream is = (InputStream) new URL(listing.getImageUrl()).getContent();
            drawable = Drawable.createFromStream(is, "src name");
            is.close();
        } catch (Exception e) {
            System.out.println("Exc=" + e);
        }

        setImage(drawable);
    }

    @UiThread
    public void setImage(Drawable drawable) {
        if (drawable != null) {
            thumbnail.setImageDrawable(drawable);
        }
    }
}
