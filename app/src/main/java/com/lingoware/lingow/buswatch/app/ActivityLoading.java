package com.lingoware.lingow.buswatch.app;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.location.LocationListener;
import com.lingoware.lingow.buswatch.R;


public class ActivityLoading extends FragmentActivity implements LocationListener {


    LocationLoader locationLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        LocationLoader loader = new LocationLoader(this);
        loader.getSingle(this);
    }
    @Override
    public void onLocationChanged(Location location) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.LOCATION, location);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
