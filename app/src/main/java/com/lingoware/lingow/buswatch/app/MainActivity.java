package com.lingoware.lingow.buswatch.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
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
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        View.OnClickListener, AdapterView.OnItemClickListener, ViewPager.OnPageChangeListener,
        GoogleMap.OnMarkerDragListener, RouteFetcher.RouteUpdateListener {


    public static final String LOCATION = "com.lingoware.lingow.buswatch.app.MainActivity.LOCATION";
    GoogleMap mMap;
    SlidingUpPanelLayout panel;
    Location location;
    NonSwipeableViewPager routePager;
    boolean duringcheckin = false;
    Marker marker;
    RouteFragment currentRouteFragment;
    BitmapDescriptor[] buses;
    int selectedRoutePage;
    RouteFragmentAdapter routeFragmentAdapter;
    LocationLoader loader;
    private MapFragment mMapFragment;
    private List<Route> routes;
    private List<Polyline> polylines = new ArrayList<>();

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
        loader = new LocationLoader(this);
        loadCurrentLocation(savedInstanceState);
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
        reloadSlidingPanel(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    private void reloadSlidingPanel(LatLng pos) {
        RouteFetcher routeFetcher = new RouteFetcher(routeFragmentAdapter, this);
        routeFetcher.execute(pos);
    }

    @Override
    protected void onResume() {
        super.onResume();
        trySetupMap();
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
        FloatingActionButton fabCheckout;
        FloatingActionButton fabCheckin;

        switch (v.getId()) {
            case R.id.btnAddRoute:
                Intent i = new Intent(this, NewRouteActivity.class);
                startActivity(i);
                break;
            case R.id.btnCheckin:
                fabCheckin = (FloatingActionButton) v;
                panel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                routePager.setPagingEnabled(false);
                ((TextView) currentRouteFragment.getView().findViewById(R.id.txtCheckIn))
                        .setText("CheckOut");
                ((TextView) currentRouteFragment.getView().findViewById(R.id.txtReviewTitle))
                        .setText("Please Rate the Route");
                ((TextView) currentRouteFragment.getView().findViewById(R.id.txtReviewMessage))
                        .setText("Review will be sent upon checkout");
                fabCheckin.setEnabled(false);
                fabCheckin.setVisibility(View.INVISIBLE);
                fabCheckin.invalidate();

                fabCheckout = ((FloatingActionButton) currentRouteFragment.getView()
                        .findViewById(R.id.btnCheckout));
                fabCheckout.setEnabled(true);
                fabCheckout.setVisibility(View.VISIBLE);
                fabCheckout.invalidate();

                loader.startUpdates(new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        MainActivity.this.location = location;
                        MainActivity.this.marker.setPosition(
                                new LatLng(location.getLatitude(), location.getLongitude()));
                    }
                });

                currentRouteFragment.setModificableRatingBars(currentRouteFragment.getView(), true);

                //TODO use colored bus markers

                duringcheckin = true;

                break;
            case R.id.btnCheckout:
                fabCheckout = (FloatingActionButton) v;
                panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                routePager.setPagingEnabled(true);
                ((TextView) currentRouteFragment.getView().findViewById(R.id.txtCheckIn))
                        .setText("CheckIn");
                ((TextView) currentRouteFragment.getView().findViewById(R.id.txtReviewTitle))
                        .setText("Route's Rating");
                ((TextView) currentRouteFragment.getView().findViewById(R.id.txtReviewMessage))
                        .setText("");
                fabCheckout.setEnabled(false);
                fabCheckout.setVisibility(View.INVISIBLE);
                fabCheckout.invalidate();

                fabCheckin = ((FloatingActionButton) currentRouteFragment.getView()
                        .findViewById(R.id.btnCheckin));
                fabCheckin.setEnabled(true);
                fabCheckin.setVisibility(View.VISIBLE);
                fabCheckin.invalidate();
                currentRouteFragment.setModificableRatingBars(currentRouteFragment.getView(), false);

                loader.stopUpdates();

                marker.setIcon(BitmapDescriptorFactory.defaultMarker());
                duringcheckin = false;

            default:
        }
    }

    private BitmapDescriptor coloredBusIcon(Route r) {
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_bus);
        Paint p = new Paint(r.getColor());
        return null;
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
                break;
            default:
                panel.setTouchEnabled(true);
                cleanPolylines();
                addRouteToMap(routes.get(position - 1));
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
        generateBuses();
        addPolylines();
        addBuses();
    }

    private void addBuses() {
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
            polylines.add(mMap.addPolyline(po));
        }

    }

    private void generateBuses() {
        //TODO Falta generar iconos de autobuses de distintos colores para cada ruta
    }

}
