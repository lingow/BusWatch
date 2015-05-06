package com.lingoware.lingow.buswatch.app;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.lingoware.lingow.buswatch.app.beans.Route;

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
        /*TODO: Esta lista de rutas es provisional Aqui habrá que sustituir el codigo que pide las
          rutas del servidor */

        List<Route> rutas = new ArrayList<Route>();
        rutas.add(new Route(1, "51C"));
        rutas.add(new Route(2, "450"));
        rutas.add(new Route(3, "626"));
        return rutas;
    }

    @Override
    protected void onPostExecute(List<Route> routes) {
        super.onPostExecute(routes);
        for (RouteUpdateListener l : listeners) {
            l.routesUpdated(routes, ColorGenerator.generateColors(routes.size()));
        }
    }

    public interface RouteUpdateListener {
        public void routesUpdated(List<Route> routes, int colors[]);
    }
}
