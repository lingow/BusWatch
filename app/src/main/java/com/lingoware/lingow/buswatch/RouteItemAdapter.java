package com.lingoware.lingow.buswatch;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by lingow on 2/05/15.
 */
public class RouteItemAdapter extends ArrayAdapter<Route> implements RouteFetcher.RouteUpdateListener {
    public RouteItemAdapter(Context context, int resource) {
        super(context, resource);
    }

    public RouteItemAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public RouteItemAdapter(Context context, int resource, Route[] objects) {
        super(context, resource, objects);
    }

    public RouteItemAdapter(Context context, int resource, int textViewResourceId, Route[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public RouteItemAdapter(Context context, int resource, List<Route> objects) {
        super(context, resource, objects);
    }

    public RouteItemAdapter(Context context, int resource, int textViewResourceId, List<Route> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public void routesUpdated(List<Route> routes) {
        this.addAll(routes);
        this.notifyDataSetChanged();
    }
}
