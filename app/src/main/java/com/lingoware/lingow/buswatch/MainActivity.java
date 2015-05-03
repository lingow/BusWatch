package com.lingoware.lingow.buswatch;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, AdapterView.OnItemClickListener {


    public static final String LOCATION = "com.lingoware.lingow.buswatch.MainActivity.LOCATION";
    GoogleMap mMap;
    SlidingUpPanelLayout panel;
    Location location;
    NonSwipeableViewPager routePager;
    boolean duringcheckin = false;
    private MapFragment mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (location == null) {
            location = getIntent().getParcelableExtra(LOCATION);
        }
        if (location == null && savedInstanceState != null) {
            location = savedInstanceState.getParcelable(LOCATION);
        }
        if (location == null) {

        }

        setupSlidingPanel();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable(LOCATION, location);
    }

    private void setupSlidingPanel() {
        panel = ((SlidingUpPanelLayout) findViewById(R.id.activity_main_sliding_layout));
        panel.setTouchEnabled(false);
        RouteFragmentAdapter routeFragmentAdapter = new RouteFragmentAdapter(getSupportFragmentManager());
        routePager = ((NonSwipeableViewPager) findViewById(R.id.route_scroller));
        routePager.setAdapter(routeFragmentAdapter);
        RouteFetcher routeFetcher = new RouteFetcher(routeFragmentAdapter);
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
        routeFetcher.execute(position);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMapFragment == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            mMapFragment.getMapAsync(this);
        }
    }

    private void setUpMap() {
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(position).title("Marker"));
        final CameraUpdate center =
                CameraUpdateFactory.newLatLng(position);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                mMap.moveCamera(center);
            }

            @Override
            public void onCancel() {
                mMap.moveCamera(center);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mMap == null)
            mMap = googleMap;
        setUpMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        /* TODO: Descomentar esto si es que tendremos Settings

        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_main, menu);

        */

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddRoute:
                Intent i = new Intent(this, NewRouteActivity.class);
                startActivity(i);
                break;
            case R.id.btnCheckin:
                FloatingActionButton fab = (FloatingActionButton) v;
                if (!duringcheckin) {
                    panel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                    panel.setTouchEnabled(true);
                    routePager.setPagingEnabled(false);
                    duringcheckin = true;
                } else {
                    panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    routePager.setPagingEnabled(true);
                    panel.setTouchEnabled(false);
                    duringcheckin = false;
                }
            default:
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.routelist_view:
                if (routePager != null) {
                    routePager.setCurrentItem(position + 1);
                    /*TODO Set this routes's poliline in the map */
                }
                break;
            default:
        }
    }
}
