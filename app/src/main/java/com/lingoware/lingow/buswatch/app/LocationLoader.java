package com.lingoware.lingow.buswatch.app;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by lingow on 3/05/15.
 */
public class LocationLoader implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Activity context;
    boolean connected;
    LocationListener[] locationListeners;
    boolean singleUpdate = false;

    LocationRequest mLocationRequest;
    private boolean updatesStarted;

    public LocationLoader(Activity context) {
        this.context = context;
        buildGoogleApiClient();
        createLocationRequest();
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (updatesStarted) {
            updateAll(locationListeners);
        } else {
            if (singleUpdate) {
                singleUpdateAll(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient), locationListeners);
                singleUpdate = false;
                locationListeners = null;
                disconnect();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(context, "Connection Suspended: " + i, Toast.LENGTH_SHORT);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(context, "Connection Failed: " + connectionResult, Toast.LENGTH_SHORT);
    }

    private void singleUpdateAll(Location loc, LocationListener... loclis) {
        mLastLocation = loc;
        for (LocationListener l : locationListeners) {
            l.onLocationChanged(mLastLocation);
        }
    }

    public void getSingle(LocationListener... loclis) {
        if (singleUpdate)
            return;

        if (connected) {
            singleUpdateAll(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient), loclis);
        } else {
            this.locationListeners = loclis;
            singleUpdate = true;
            connect();
        }
    }

    public void startUpdates(LocationListener... loclis) {
        if (updatesStarted)
            return;

        locationListeners = loclis;

        if (connected) {
            updateAll(loclis);
        } else {
            updatesStarted = true;
            connect();
        }
    }

    private void updateAll(LocationListener... loclis) {
        for (LocationListener l : loclis) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, l);
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void stopUpdates() {
        for (LocationListener l : locationListeners) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, l);
        }
        locationListeners = null;
        if (updatesStarted) {
            disconnect();
            updatesStarted = false;
        }

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void connect() {
        mGoogleApiClient.connect();

    }

    public void disconnect() {
        mGoogleApiClient.disconnect();
    }
}
