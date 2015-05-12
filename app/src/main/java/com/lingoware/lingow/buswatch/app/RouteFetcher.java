package com.lingoware.lingow.buswatch.app;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.lingoware.lingow.buswatch.common.beans.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingow on 2/05/15.
 */
public class RouteFetcher extends AsyncTask<LatLng, Integer, List<Route>> {

    private RouteUpdateListener listeners[];

    public RouteFetcher(RouteUpdateListener... listeners) {
        this.listeners = listeners;
    }

    @Override
    protected List<Route> doInBackground(LatLng... params) {
        List<Route> routes = new ArrayList<>();
        for (LatLng l : params) {
            routes.addAll(
                    BuswatchServiceHolder.getInstance().getService().getRoutes(
                            new com.lingoware.lingow.buswatch.common.util.LatLng(
                                    l.latitude, l.longitude), 100));
        }
        int colors[] = ColorGenerator.generateColors(routes.size());
        for (int i = 0; i < colors.length; i++) {
            routes.get(i).setColor(colors[i]);
        }
        return routes;

    }

    @Override
    protected void onPostExecute(List<Route> routes) {
        super.onPostExecute(routes);
        for (RouteUpdateListener l : listeners) {
            l.routesUpdated(routes);
        }
    }

    public interface RouteUpdateListener {
        public void routesUpdated(List<Route> routes);
    }
}
