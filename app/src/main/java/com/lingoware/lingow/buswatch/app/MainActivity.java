package com.lingoware.lingow.buswatch.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.lingoware.lingow.buswatch.R;
import com.lingoware.lingow.buswatch.common.beans.Route;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        View.OnClickListener, AdapterView.OnItemClickListener, ViewPager.OnPageChangeListener,
        GoogleMap.OnMarkerDragListener, RouteFetcher.RouteUpdateListener,
        SharedPreferences.OnSharedPreferenceChangeListener, RouteFragmentAdapter.DataSetChangesListener {


    public static final String LOCATION = "com.lingoware.lingow.buswatch.app.MainActivity.LOCATION";
    private static final String CHECKINID = "com.lingoware.lingow.buswatch.app.MainActivity.CHECKINID";
    private static final String SELECTEDROUTE = "com.lingoware.lingow.buswatch.app.MainActivity.SELECTEDROUTE";

    GoogleMap mMap;
    SlidingUpPanelLayout panel;
    Location location;
    NonSwipeableViewPager routePager;
    int checkedInId = -1;
    Marker marker;
    RouteFragment currentRouteFragment;
    BitmapDescriptor[] buses;
    int selectedRoutePage;
    RouteFragmentAdapter routeFragmentAdapter;
    LocationLoader loader;
    Map<Integer, List<Marker>> busMarkers = new HashMap<>();
    private MapFragment mMapFragment;
    private List<Route> routes = new ArrayList<>();
    private List<Polyline> polylines = new ArrayList<>();
    private int checkinFrequency;
    private int syncFrequency;
    private int routeSeekRange;
    private Handler mHandler;
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            updateUnitPositions();
            mHandler.postDelayed(mStatusChecker, syncFrequency);
        }
    };
    private int selectedRouteid = -1;

    boolean mapset() {
        return marker != null;
    }

    boolean mapready() {
        return mMap != null;
    }

    boolean locationready() {
        return location != null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadSettings();
        loader = new LocationLoader(this);
        loadCurrentLocation(savedInstanceState);
        mHandler = new Handler();
    }

    private void loadSettings() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        onSharedPreferenceChanged(sp, getResources().getString(R.string.pref_checkin_frequency));
        onSharedPreferenceChanged(sp, getResources().getString(R.string.pref_sync_frequency));
        onSharedPreferenceChanged(sp, getResources().getString(R.string.pref_route_seek_range));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        selectedRouteid = savedInstanceState.getInt(SELECTEDROUTE, -1);
        checkedInId = savedInstanceState.getInt(CHECKINID, -1);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CHECKINID, checkedInId);
        outState.putInt(SELECTEDROUTE, selectedRouteid);

    }

    private void loadCurrentLocation(Bundle savedInstanceState) {
        location = getIntent().getParcelableExtra(LOCATION);
        if (location != null) {
            setupSlidingPanel();
            return;
        }

        if (savedInstanceState != null) {
            location = savedInstanceState.getParcelable(LOCATION);
        }

        if (location != null) {
            setupSlidingPanel();
            return;
        }

        loader.getSingle(new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                MainActivity.this.location = location;
                MainActivity.this.setupSlidingPanel();
                MainActivity.this.trySetupMap();
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable(LOCATION, location);
    }

    private void setupSlidingPanel() {
        panel = ((SlidingUpPanelLayout) findViewById(R.id.activity_main_sliding_layout));
        panel.setTouchEnabled(false);
        routeFragmentAdapter = new RouteFragmentAdapter(getSupportFragmentManager());
        routePager = ((NonSwipeableViewPager) findViewById(R.id.route_scroller));
        routePager.setAdapter(routeFragmentAdapter);
        routePager.setOnPageChangeListener(this);
        routeFragmentAdapter.addDataSetChangesListener(this);
        reloadSlidingPanel(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    private void reloadSlidingPanel(LatLng pos) {
        RouteFetcher routeFetcher = new RouteFetcher(routeSeekRange, routeFragmentAdapter, this);
        routeFetcher.execute(pos);
    }

    @Override
    protected void onResume() {
        super.onResume();
        trySetupMap();
        startRepeatingTask();
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
    private void trySetupMap() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (!mapready()) {
            // Try to obtain the map from the SupportMapFragment.
            mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            mMapFragment.getMapAsync(this);
        }
        if (!mapset() && locationready() && mapready()) {
            setUpMap();
        }
    }

    private void setUpMap() {
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
        marker = mMap.addMarker(new MarkerOptions().position(position).title("Check Routes Here").draggable(true));
        mMap.setOnMarkerDragListener(this);
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
        trySetupMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddRoute:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Create New Route");

                // Set up the input
                final EditText input = new EditText(this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BuswatchServiceHolder.getInstance().getService().addRoute(input.getText().toString(),
                                new com.lingoware.lingow.buswatch.common.util.LatLng(
                                        marker.getPosition().latitude,
                                        marker.getPosition().longitude
                                ));
                        MainActivity.this.reloadSlidingPanel(marker.getPosition());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                break;
            case R.id.btnCheckin:
                setCheckedInMode();

                loader.startUpdates(new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        MainActivity.this.location = location;
                        MainActivity.this.marker.setPosition(
                                new LatLng(location.getLatitude(), location.getLongitude()));
                        BuswatchServiceHolder.getInstance().getService().receiveUpdate(checkedInId,
                                new com.lingoware.lingow.buswatch.common.util.LatLng(
                                        location.getLatitude(), location.getLongitude()
                                ));
                    }
                });

                checkedInId = BuswatchServiceHolder.getInstance().getService().
                        checkin(currentRouteFragment.route.getId(),
                                new com.lingoware.lingow.buswatch.common.util.LatLng(
                                        marker.getPosition().latitude, marker.getPosition().longitude));
                break;
            case R.id.btnCheckout:
                Map<Integer, Float> ratings = currentRouteFragment.getRatings();
                setCheckedOutMode();

                loader.stopUpdates();
                BuswatchServiceHolder.getInstance().getService().checkout(checkedInId,
                        ratings.get(R.id.SecurityStars),
                        ratings.get(R.id.serviceStars),
                        ratings.get(R.id.comfortStars),
                        ratings.get(R.id.overallStars),
                        ratings.get(R.id.conditionStars)
                );
                checkedInId = -1;
                reloadSlidingPanel(marker.getPosition());
            default:
        }
    }

    private void setCheckedOutMode() {
        panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        routePager.setPagingEnabled(true);

        currentRouteFragment.setCheckedOutMode();
    }

    private void setCheckedInMode() {
        panel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        routePager.setPagingEnabled(false);
        currentRouteFragment.setCheckedInMode();
    }

    private BitmapDescriptor coloredBusIcon(Route r) {
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_bus);
        /*
        int[] pixels = new int[b.getHeight() * b.getWidth()];

        b.getPixels(pixels, 0, b.getWidth(), 0, 0, b.getWidth(), b.getHeight());

        for (int i = 0; i < b.getHeight() * b.getWidth(); i++) {

            if (pixels[i] == Color.BLACK)
                pixels[i] = r.getColor();
        }

        b.setPixels(pixels, 0, b.getWidth(), 0, 0, b.getWidth(), b.getHeight());

        */
        return BitmapDescriptorFactory.fromBitmap(b);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.routelist_view:
                if (routePager != null) {
                    routePager.setCurrentItem(position + 1);
                }
                break;
            default:
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        selectedRoutePage = position;
        currentRouteFragment = routeFragmentAdapter.getRegisteredFragment(position);
        switch (position) {
            case 0:
                panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                panel.setTouchEnabled(false);
                addPolylines();
                hideBuses();
                for (Route r : routes) {
                    showBuses(r);
                }
                break;
            default:
                panel.setTouchEnabled(true);
                cleanPolylines();
                hideBuses();
                addRouteToMap(currentRouteFragment.route);
                if (checkedInId != -1) {
                    setCheckedInMode();
                }

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        LatLng p = marker.getPosition();
        mMap.animateCamera(CameraUpdateFactory.newLatLng(p));
        location.setLatitude(p.latitude);
        location.setLongitude(p.longitude);
        reloadSlidingPanel(p);
    }

    @Override
    public void routesUpdated(List<Route> routes) {
        this.routes = routes;
        addPolylines();
    }

    private void addPolylines() {
        cleanPolylines();
        for (Route r : routes) {
            addRouteToMap(r);
        }
    }

    private void cleanPolylines() {
        for (Polyline p : polylines) {
            p.remove();
        }
    }

    private void addRouteToMap(Route r) {
        for (List<com.lingoware.lingow.buswatch.common.util.LatLng> latLngList : r.getRoutePaths()) {
            PolylineOptions po = new PolylineOptions();
            for (com.lingoware.lingow.buswatch.common.util.LatLng latLng : latLngList) {
                po.add(new LatLng(latLng.latitude, latLng.longitude));
            }
            po.color(r.getColor());
            if (mMap != null) {
                polylines.add(mMap.addPolyline(po));
            } else {

            }
        }
        showBuses(r);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(getResources().getString(R.string.pref_checkin_frequency))) {
            this.checkinFrequency = Integer.parseInt(sharedPreferences.getString(key,
                    getResources().getString(R.string.pref_checkin_frequency_default)));
        } else if (key.equals(getResources().getString(R.string.pref_sync_frequency))) {
            this.syncFrequency = 1000 * Integer.parseInt(sharedPreferences.getString(key,
                    getResources().getString(R.string.pref_sync_frequency_default)));
        } else if (key.equals(getResources().getString(R.string.pref_route_seek_range))) {
            this.routeSeekRange = Integer.parseInt(sharedPreferences.getString(key,
                    getResources().getString(R.string.pref_route_seek_range_default)));
        }
    }

    private void showBuses(Route r) {
        if (busMarkers.containsKey(r.getId())) {
            for (Marker m : busMarkers.get(r.getId())) {
                m.setVisible(true);
            }
        }
    }

    private void hideBuses() {
        for (List<Marker> markers : busMarkers.values()) {
            for (Marker m : markers) {
                m.setVisible(false);
            }
        }
    }

    private void updateUnitPositions() {
        if (routes != null) {
            for (List<Marker> markers : busMarkers.values()) {
                for (Marker m : markers) {
                    m.remove();
                }
            }
            for (Route r : routes) {
                generateBuses(r);
            }
        }
    }

    private void generateBuses(Route r) {
        busMarkers.put(r.getId(), new ArrayList<Marker>());
        List<com.lingoware.lingow.buswatch.common.util.LatLng> routePoints =
                BuswatchServiceHolder.getInstance().getService().getUnitPoints(r.getId());
        for (com.lingoware.lingow.buswatch.common.util.LatLng latLng : routePoints) {
            busMarkers.get(r.getId()).add(mMap.addMarker(
                    new MarkerOptions()
                            .icon(coloredBusIcon(r))
                            .position(new LatLng(latLng.latitude, latLng.longitude))));
        }
    }

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopRepeatingTask();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loader.updatesStarted) {
            loader.stopUpdates();
        }
    }

    @Override
    public void onDataSetChanged() {
        for (int i = 0; i < routeFragmentAdapter.getCount(); i++) {
            if (selectedRouteid == routeFragmentAdapter.getItem(i).getId()) {
                routePager.setCurrentItem(i + 1);
                return;
            }
        }
        routePager.setCurrentItem(0);
    }
}
