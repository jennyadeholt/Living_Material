package com.jd.living.screen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.jd.living.R;
import com.jd.living.screen.drawer.DrawerMenuAdapter;
import com.jd.living.screen.drawer.NavigationFragment;


public class MainActivity extends ActionBarActivity implements NavigationFragment.DrawerMenuListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            NavigationFragment navigationFragment = (NavigationFragment)
                    getSupportFragmentManager().findFragmentById(R.id.navigation_fragment);

            navigationFragment.setUp(R.id.navigation_fragment,
                    (DrawerLayout) findViewById(R.id.drawer_layout),
                    toolbar);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Menu item info pressed!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMenuItemClicked(int pos, DrawerMenuAdapter.DrawerMenuItem item) {
        Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putString(PlanetFragment.TEXT, item.getLabel());
        args.putInt(PlanetFragment.IMAGE_RESOURCE, item.getIconResourceId());
        fragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        setTitle(" " + pos);
    }

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public static class PlanetFragment extends Fragment {
        public static final String TEXT = "text";
        public static final String IMAGE_RESOURCE = "image_resource";

        public PlanetFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
            int imageResource = getArguments().getInt(IMAGE_RESOURCE);
            String text = getArguments().getString(TEXT);
            ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageResource);
            getActivity().setTitle(text);
            return rootView;
        }
    }

}
