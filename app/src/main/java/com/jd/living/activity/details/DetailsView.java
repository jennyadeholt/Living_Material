package com.jd.living.activity.details;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jd.living.R;
import com.jd.living.database.DatabaseHelper;
import com.jd.living.model.Listing;
import com.jd.living.util.StringUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;

@EFragment
public abstract class DetailsView extends Fragment {

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
    protected ImageView topImage;

    @ViewById
    protected ImageView favorite;

    @Bean
    protected DatabaseHelper database;

    protected Listing listing;

    private int id;


    protected abstract DatabaseHelper.DatabaseState getDataBaseState();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.details, null);
    }

    @AfterViews
    public void init() {
        id = getArguments() != null ? getArguments().getInt("objectIndex") : 1;
        listing = database.getListingBasedOnLocation(id, getDataBaseState());

        if (listing != null) {
            update();
        }
    }

    protected Listing getListing() {
        return listing;
    }

    protected void updateFavorite(boolean onTouch) {
        boolean isFavorite = database.getFavoriteDatabase().isFavorite(listing);
        int resId = R.drawable.favorite_drawable;

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
                resId = R.drawable.favorite_drawable_selected;
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

    private void update() {
        setupDetails();
        updateFavorite(false);
        new FetchWebImages().execute();
    }

    private class FetchWebImages extends AsyncTask<Void, Void, Void> {
        Bitmap bitmap;


        @Override
        protected Void doInBackground(Void... params) {

            try {

                Document document = Jsoup.connect("http://www.booli.se/redirect/all-images?id=" + listing.getBooliId()).get();

                Elements images = document.select("img[src]");

                Log.d("", listing.getAddress() + " http://www.booli.se/redirect/all-images?id=" + listing.getBooliId());
                for (Element image : images) {
                    String url = image.attr("src");
                    if ((url.startsWith("http://") || url.startsWith("https://")) &&
                            !url.contains("logo") &&
                            !url.contains("static") &&
                            !url.contains("icons") &&
                            !url.contains("images/u") &&
                            !url.contains("facebook") &&
                            !url.contains("sigill")) {
                        Log.d("", "Valid URL: " + url);
                        InputStream input = new java.net.URL(url).openStream();
                        bitmap = BitmapFactory.decodeStream(input);
                        input.close();
                        break;
                    } else {
                        Log.d("", "Thrown URL: " + url);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (bitmap != null) {
                topImage.setImageBitmap(bitmap);
            }
        }
    }
}
